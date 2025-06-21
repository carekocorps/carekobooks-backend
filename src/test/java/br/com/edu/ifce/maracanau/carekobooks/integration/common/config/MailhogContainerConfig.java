package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

@Import({
        ContainerNetworkConfig.class,
        DynamicPropertyRegistrarConfig.class
})
@TestConfiguration(proxyBeanMethods = false)
public class MailhogContainerConfig {

    private static final String DOCKER_IMAGE = "mailhog/mailhog:v1.0.1";

    @Value("${mailhog.port.http}")
    private Integer httpPort;

    @Value("${mailhog.port.smtp}")
    private Integer smtpPort;

    @Bean
    public GenericContainer<?> mailhogContainer(Network network) {
        return new GenericContainer<>(DOCKER_IMAGE)
                .withExposedPorts(smtpPort, httpPort)
                .waitingFor(Wait.forHttp("/").forPort(httpPort))
                .withNetwork(network)
                .withNetworkAliases("mailhog");
    }

}
