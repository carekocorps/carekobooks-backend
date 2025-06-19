package br.com.edu.ifce.maracanau.carekobooks.integration.common.contributor;

import org.springframework.test.context.DynamicPropertyRegistry;

public interface PropertyContributor {

    void contribute(DynamicPropertyRegistry registry);

}
