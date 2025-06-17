package br.com.edu.ifce.maracanau.carekobooks.integration.common.auth.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakToken {

    @JsonProperty("access_token")
    private String accessToken;

}
