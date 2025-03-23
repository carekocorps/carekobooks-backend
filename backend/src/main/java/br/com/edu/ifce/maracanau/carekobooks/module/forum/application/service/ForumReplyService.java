package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.shared.AuthenticatedUser;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumReplyDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.mapper.ForumReplyMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query.ForumReplySearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.validator.ForumReplyValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.ForumReplyRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ForumReplyService {

    private final ForumReplyRepository forumReplyRepository;
    private final ForumReplyMapper forumReplyMapper;
    private final ForumReplyValidator forumReplyValidator;

    public ApplicationPage<ForumReplyDTO> search(ForumReplySearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(forumReplyRepository.findAll(specification, pageRequest).map(forumReplyMapper::toDTO));
    }

    public Optional<ForumReplyDTO> findById(Long id) {
        return forumReplyRepository.findById(id).map(forumReplyMapper::toDTO);
    }

    @Transactional
    public ForumReplyDTO create(ForumReplyRequest request) {
        if (!AuthenticatedUser.isAuthorOrAdmin(request.getUserId())) {
            throw new ForbiddenException("Forbidden");
        }

        var forumReply = forumReplyMapper.toModel(request);
        forumReplyValidator.validate(forumReply);
        return forumReplyMapper.toDTO(forumReplyRepository.save(forumReply));
    }

    @Transactional
    public void update(Long id, ForumReplyRequest request) {
        if (!AuthenticatedUser.isAuthorOrAdmin(request.getUserId())) {
            throw new ForbiddenException("Forbidden");
        }

        var forumReply = forumReplyRepository.findById(id).orElse(null);
        if (forumReply == null) {
            throw new NotFoundException("Forum Reply not found");
        }

        forumReplyMapper.updateEntity(forumReply, request);
        forumReplyValidator.validate(forumReply);
        forumReplyRepository.save(forumReply);
    }

    @Transactional
    public void deleteById(Long id) {
        var forumReply = forumReplyRepository.findById(id).orElse(null);
        if (forumReply == null) {
            throw new NotFoundException("Forum Reply not found");
        }

        if (!AuthenticatedUser.isAuthorOrAdmin(forumReply.getUser().getId())) {
            throw new ForbiddenException("Forbidden");
        }

        forumReplyRepository.deleteById(id);
    }

}
