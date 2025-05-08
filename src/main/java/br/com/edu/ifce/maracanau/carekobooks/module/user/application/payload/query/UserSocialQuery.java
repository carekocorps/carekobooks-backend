package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.specification.UserSocialSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.BaseApplicationQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
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

    @Override
    public Specification<User> getSpecification() {
        var specs = super.getSpecification();
        return relationship == UserRelationship.FOLLOWING
                ? specs.and(followingUsernameEqual(username))
                : specs.and(followersUsernameEqual(username));
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
