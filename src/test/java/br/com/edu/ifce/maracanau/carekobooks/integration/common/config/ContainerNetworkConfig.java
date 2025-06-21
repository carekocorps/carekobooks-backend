package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.Network;

@TestConfiguration(proxyBeanMethods = false)
public class ContainerNetworkConfig {

    @Bean
    public Network containerNetwork() {
        return Network.newNetwork();
    }

}
