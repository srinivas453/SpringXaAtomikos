package org.mainlogic.spring.xa;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("database")
public class JdbcDatasourceProperties {

	private String username;
	private String password;
	private String url;
	private String driverclass;
	private String dataSourceClass;
}