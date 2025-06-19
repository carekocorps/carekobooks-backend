package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import br.com.edu.ifce.maracanau.carekobooks.integration.common.contributor.BasePropertyContributor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;

import java.util.List;

@TestConfiguration(proxyBeanMethods = false)
public class DynamicPropertyRegistrarConfig {

    @Bean
    public DynamicPropertyRegistrar properties(List<BasePropertyContributor> contributors) {
        return registry -> contributors.forEach(x -> x.contribute(registry));
    }

}
