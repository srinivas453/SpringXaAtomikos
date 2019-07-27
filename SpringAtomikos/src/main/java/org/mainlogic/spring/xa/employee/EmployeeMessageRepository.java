package org.mainlogic.spring.xa.employee;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class EmployeeMessageRepository {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void send(Employee emp) throws JsonProcessingException {

		String json = new ObjectMapper().writeValueAsString(emp);
		jmsTemplate.convertAndSend("EmpQueue", json);
	}

	public Employee receive() throws IOException {

		return new ObjectMapper().readerFor(Employee.class).readValue((String) jmsTemplate.receiveAndConvert("EmpQueue"));
	}
}
