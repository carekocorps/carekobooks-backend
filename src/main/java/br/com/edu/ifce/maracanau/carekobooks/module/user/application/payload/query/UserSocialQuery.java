package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.specification.UserSocialSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.BaseApplicationQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class UserSocialQuery extends BaseApplicationQuery<User> {

    @JsonIgnore
    private String username;

    @JsonIgnore
    private UserRelationship relationship;

    private String targetUsername;
    private String targetDisplayName;

    @Override
    public Specification<User> getSpecification() {
        var specs = super.getSpecification();
        specs = specs.and(relationshipUsernameEqual(username, relationship));
        if (StringUtils.isNotBlank(targetUsername)) specs = specs.and(targetUsernameContains(targetUsername));
        if (StringUtils.isNotBlank(targetDisplayName)) specs = specs.and(targetDisplayNameContains(targetDisplayName));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "username", "username",
                "createdAt", "createdAt",
                "updatedAt", "updatedAt"
        ));
    }

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "username",
                    "createdAt",
                    "updatedAt"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
