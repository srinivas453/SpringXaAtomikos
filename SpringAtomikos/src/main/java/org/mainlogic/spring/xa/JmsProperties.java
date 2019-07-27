package org.mainlogic.spring.xa;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("jms")
public class JmsProperties {

}