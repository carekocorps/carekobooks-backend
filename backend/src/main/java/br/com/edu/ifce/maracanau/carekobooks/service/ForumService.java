package br.com.edu.ifce.maracanau.carekobooks.service;

import br.com.edu.ifce.maracanau.carekobooks.core.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.dto.forum.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.forum.ForumPageQueryDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.forum.BookPageQueryDTO;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.mapper.ForumMapper;
import br.com.edu.ifce.maracanau.carekobooks.repository.ForumRepository;
import br.com.edu.ifce.maracanau.carekobooks.validator.ForumValidator;
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

    public ApplicationPage<ForumDTO> search(BookPageQueryDTO forumSearchDTO) {
        var specification = forumSearchDTO.getSpecification();
        var sort = forumSearchDTO.getSort();
        var pageRequest = PageRequest.of(forumSearchDTO.getPageNumber(), forumSearchDTO.getPageSize(), sort);
        return new ApplicationPage<>(forumRepository.findAll(specification, pageRequest).map(forumMapper::toDTO));
    }

    public Optional<ForumDTO> findById(Long id) {
        return forumRepository.findById(id).map(forumMapper::toDTO);
    }

    public ForumDTO create(ForumPageQueryDTO forumPageQueryDTO){
        var forum = forumMapper.toEntity(forumPageQueryDTO);
        forumValidator.validate(forum);
        return forumMapper.toDTO(forumRepository.save(forum));
    }

    public void update(Long id, ForumPageQueryDTO forumPageQueryDTO) {
       var forum = forumRepository.findById(id).orElse(null);
        if(forum == null){
            throw new NotFoundException("Forum not found");
        }

        forumMapper.updateEntity(forum, forumPageQueryDTO);
        forumValidator.validate(forum);
        forumRepository.save(forum);
    }

    public void deleteById(Long id) {
        if (!forumRepository.existsById(id)) {
            throw new NotFoundException("Forum not found");
        }

        forumRepository.deleteById(id);
    }

}
