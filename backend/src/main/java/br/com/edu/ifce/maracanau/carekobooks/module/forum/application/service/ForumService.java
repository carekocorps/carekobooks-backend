package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.shared.AuthenticatedUser;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query.ForumSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.NotFoundException;
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
    private final ForumMapper forumMapper;
    private final ForumValidator forumValidator;

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
        if (!AuthenticatedUser.isAuthorOrAdmin(request.getUserId())) {
            throw new ForbiddenException("Forbidden");
        }

        var forum = forumMapper.toModel(request);
        forumValidator.validate(forum);
        return forumMapper.toDTO(forumRepository.save(forum));
    }

    @Transactional
    public void update(Long id, ForumRequest request) {
        if (!AuthenticatedUser.isAuthorOrAdmin(request.getUserId())) {
            throw new ForbiddenException("Forbidden");
        }

        var forum = forumRepository.findById(id).orElse(null);
        if (forum == null){
            throw new NotFoundException("Forum not found");
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

        if (!AuthenticatedUser.isAuthorOrAdmin(forum.getUser().getId())) {
            throw new ForbiddenException("Forbidden");
        }

        forumRepository.deleteById(id);
    }

}
