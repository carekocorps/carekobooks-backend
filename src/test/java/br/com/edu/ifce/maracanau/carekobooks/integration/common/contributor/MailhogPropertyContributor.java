package br.com.edu.ifce.maracanau.carekobooks.integration.common.contributor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;

@Component
public class MailhogPropertyContributor implements BasePropertyContributor {

    @Value("${mailhog.port.http}")
    private Integer httpPort;

    @Value("${mailhog.port.smtp}")
    private Integer smtpPort;

    @Autowired(required = false)
    @Qualifier("mailhogContainer")
    private GenericContainer<?> mailhogContainer;

    @Override
    public void contribute(DynamicPropertyRegistry registry) {
        if (mailhogContainer != null && mailhogContainer.isRunning()) {
            registry.add("mailhog.host", mailhogContainer::getHost);
            registry.add("mailhog.port.http", () -> mailhogContainer.getMappedPort(httpPort));
            registry.add("mailhog.port.smtp", () -> mailhogContainer.getMappedPort(smtpPort));
        }
    }

}
