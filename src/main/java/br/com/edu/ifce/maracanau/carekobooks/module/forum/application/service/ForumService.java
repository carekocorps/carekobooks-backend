package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.response.ForumResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query.ForumQuery;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper.ForumMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.ForumRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.validator.ForumValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ForumService {

    private final ForumRepository forumRepository;
    private final ForumValidator forumValidator;
    private final ForumMapper forumMapper;

    public ApplicationPage<ForumResponse> search(ForumQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(forumRepository.findAll(specification, pageRequest).map(forumMapper::toResponse));
    }

    public Optional<ForumResponse> find(Long id) {
        return forumRepository.findById(id).map(forumMapper::toResponse);
    }

    @Transactional
    public ForumResponse create(ForumRequest request) {
        var forum = forumMapper.toModel(request);
        forumValidator.validate(forum);
        return forumMapper.toResponse(forumRepository.save(forum));
    }

    @Transactional
    public ForumResponse update(Long id, ForumRequest request) {
        var forum = forumRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Forum not found"));

        if (UserContextProvider.isUserUnauthorized(forum.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this forum");
        }

        forumMapper.updateModel(forum, request);
        forumValidator.validate(forum);
        forumRepository.save(forum);
        return forumMapper.toResponse(forum);
    }

    @Transactional
    public void delete(Long id) {
        var forum = forumRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Forum not found"));

        if (UserContextProvider.isUserUnauthorized(forum.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this forum");
        }

        forumRepository.deleteById(id);
    }

}
