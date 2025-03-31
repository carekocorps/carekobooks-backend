package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.dto.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query.ForumSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
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

    public ApplicationPage<ForumDTO> search(ForumSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(forumRepository.findAll(specification, pageRequest).map(forumMapper::toDTO));
    }

    public Optional<ForumDTO> findById(Long id) {
        return forumRepository.findById(id).map(forumMapper::toDTO);
    }

    @Transactional
    public ForumDTO create(ForumRequest request) {
        var forum = forumMapper.toModel(request);
        forumValidator.validate(forum);
        return forumMapper.toDTO(forumRepository.save(forum));
    }

    @Transactional
    public void update(Long id, ForumRequest request) {
        var forum = forumRepository.findById(id).orElse(null);
        if (forum == null){
            throw new NotFoundException("Forum not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(forum.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to update this forum");
        }

        forumMapper.updateEntity(forum, request);
        forumValidator.validate(forum);
        forumRepository.save(forum);
    }

    @Transactional
    public void deleteById(Long id) {
        var forum = forumRepository.findById(id).orElse(null);
        if (forum == null) {
            throw new NotFoundException("Forum not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(forum.getUser().getUsername())) {
            throw new ForbiddenException("You are not allowed to delete this forum");
        }

        forumRepository.deleteById(id);
    }

}
