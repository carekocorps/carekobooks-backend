package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.specification.UserSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.BaseApplicationPageSearchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class UserSearchQuery extends BaseApplicationPageSearchQuery<User> {

    private String username;

    @Schema(description = "Only applies if 'username' is provided")
    private UserRelationship userRelationship;

    @Override
    public Specification<User> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(username)) {
            specs = switch(userRelationship) {
                case FOLLOWING -> specs.and(followingUsernameEqual(username));
                case FOLLOWERS -> specs.and(followersUsernameEqual(username));
                case null -> specs.and(usernameContains(username));
            };
        }

        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "username", "username",
                "created-at", "createdAt",
                "updated-at", "updatedAt"
        ));
    }

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "username",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
