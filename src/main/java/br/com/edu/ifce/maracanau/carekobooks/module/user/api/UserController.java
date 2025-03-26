package br.com.edu.ifce.maracanau.carekobooks.module.user.api;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.docs.UserControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.UserDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSocialSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.shared.api.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController implements BaseController, UserControllerDocs {

    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<UserDTO>> search(@ParameterObject UserSearchQuery query) {
        var userDTOs = userService.search(query);
        return ResponseEntity.ok(userDTOs);
    }

    @Override
    @GetMapping("/{username}/followers")
    public ResponseEntity<ApplicationPage<UserDTO>> searchFollowers(@PathVariable String username, @ParameterObject UserSocialSearchQuery query) {
        query.setUsername(username);
        query.setUserRelationship(UserRelationship.FOLLOWERS);
        var userDTOs = userService.search(query);
        return ResponseEntity.ok(userDTOs);
    }

    @Override
    @GetMapping("/{username}/following")
    public ResponseEntity<ApplicationPage<UserDTO>> searchFollowing(@PathVariable String username, @ParameterObject UserSocialSearchQuery query) {
        query.setUsername(username);
        query.setUserRelationship(UserRelationship.FOLLOWING);
        var userDTOs = userService.search(query);
        return ResponseEntity.ok(userDTOs);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username) {
        var userDTO = userService.findByUsername(username);
        return userDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PutMapping("/{username}")
    public ResponseEntity<Void> update(@PathVariable String username, @RequestBody UserRegisterRequest request) {
        userService.update(username, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping("/{username}/following/{targetUsername}")
    public ResponseEntity<Void> followByUsernameAndTargetUsername(@PathVariable String username, @PathVariable String targetUsername) {
        userService.updateFollowingByUsernameAndTargetUsername(username, targetUsername, true);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{username}/following/{targetUsername}")
    public ResponseEntity<Void> unfollowByUsernameAndTargetUsername(@PathVariable String username, @PathVariable String targetUsername) {
        userService.updateFollowingByUsernameAndTargetUsername(username, targetUsername, false);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }

}
