package org.mainlogic.spring.xa;

import java.sql.SQLException;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jms.AtomikosConnectionFactoryBean;

@Configuration
@ComponentScan
@EnableTransactionManagement
public class ApplicationConfig {
	
	@Autowired
	private JdbcDatasourceProperties databaseProperties;

	@Bean(name = "atomikosUserTransaction")
	public UserTransaction userTransaction() throws SystemException {

		UserTransaction userTransactionImp = new UserTransactionImp();
		userTransactionImp.setTransactionTimeout(240);

		return userTransactionImp;
	}

	@Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
	public TransactionManager atomikosTransactionManager() {

		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.setForceShutdown(false);

		return userTransactionManager;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() throws SystemException {

		JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction(), atomikosTransactionManager());
		jtaTransactionManager.setAllowCustomIsolationLevels(true);

		return jtaTransactionManager;
	}

	@Bean(name = "atomikosDataSource", initMethod = "init", destroyMethod = "close")
	public DataSource getDataSource() throws SQLException {
		
		Properties properties = new Properties();
		properties.put("userName", databaseProperties.getUsername());
		properties.put("password", databaseProperties.getPassword());
		properties.put("url", databaseProperties.getUrl());

		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();

		xaDataSource.setUniqueResourceName("JdbcDS");
		xaDataSource.setPoolSize(3);
		xaDataSource.setXaDataSourceClassName(databaseProperties.getDataSourceClass());
		xaDataSource.setXaProperties(properties);

		return xaDataSource;
	}

	@Bean(name = "atomikosJmsConnectionFactory", initMethod = "init")
	public ConnectionFactory getJmsConnectionFactory() {

		AtomikosConnectionFactoryBean jmsConnectionFactory = new AtomikosConnectionFactoryBean();

		jmsConnectionFactory.setUniqueResourceName("JmsDS");
		jmsConnectionFactory.setPoolSize(3);

		ActiveMQXAConnectionFactory xaConnectionFactory = new ActiveMQXAConnectionFactory();
		xaConnectionFactory.setBrokerURL("vm://localhost?broker.persistent=false");

		jmsConnectionFactory.setXaConnectionFactory(xaConnectionFactory);

		return jmsConnectionFactory;
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJdbcTemplate() throws Exception {

		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(getDataSource());

		return jdbcTemplate;
	}

	@Bean(name = "jmsTemplate")
	public JmsTemplate getJmsTemplate() throws Throwable {

		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(getJmsConnectionFactory());
		jmsTemplate.setReceiveTimeout(2000);
		jmsTemplate.setSessionTransacted(true);
		jmsTemplate.setSessionAcknowledgeMode(0);

		return jmsTemplate;
	}
}
