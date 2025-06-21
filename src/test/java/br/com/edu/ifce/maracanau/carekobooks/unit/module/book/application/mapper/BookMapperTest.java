package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ImageMapper imageMapper = Mappers.getMapper(ImageMapper.class);

    @Mock
    private BookGenreMapper bookGenreMapper = Mappers.getMapper(BookGenreMapper.class);

    @InjectMocks
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @Test
    void toEntity_withNullBookRequest_shouldReturnNullBook() {
        // Arrange
        BookRequest bookRequest = null;

        // Act
        var result = bookMapper.toEntity(bookRequest);

        // Assert
        assertThat(result).isNull();
        verify(bookGenreMapper, never()).toEntity(ArgumentMatchers.<List<String>>any());
    }

    @Test
    void toEntity_withValidBookRequest_shouldReturnBook() {
        // Arrange
        var bookRequest = BookRequestFactory.validRequest();
        var book = BookFactory.validBook(bookRequest);

        when(bookGenreMapper.toEntity(bookRequest.getGenres()))
                .thenReturn(book.getGenres());

        // Act
        var result = bookMapper.toEntity(bookRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(book.getTitle());
        assertThat(result.getSynopsis()).isEqualTo(book.getSynopsis());
        assertThat(result.getAuthorName()).isEqualTo(book.getAuthorName());
        assertThat(result.getPublisherName()).isEqualTo(book.getPublisherName());
        assertThat(result.getPublishedAt()).isEqualTo(book.getPublishedAt());
        assertThat(result.getPageCount()).isEqualTo(book.getPageCount());
        assertThat(result.getGenres()).isEqualTo(book.getGenres());
        verify(bookGenreMapper, times(1)).toEntity(bookRequest.getGenres());
    }

    @Test
    void toEntity_withNonExistingBookId_shouldReturnNullBook() {
        // Arrange
        var id = Math.abs(new Random().nextLong()) + 1;

        when(bookRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookMapper.toEntity(id);

        // Assert
        assertThat(result).isNull();
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void toEntity_withExistingBookId_shouldReturnBook() {
        // Arrange
        var book = BookFactory.validBook();

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        // Act
        var result = bookMapper.toEntity(book.getId());

        // Assert
        assertThat(result).isNotNull();
        verify(bookRepository, times(1)).findById(book.getId());
    }

    @Test
    void toResponse_withNullBook_shouldReturnNullBookResponse() {
        // Arrange
        Book book = null;

        // Act
        var result = bookMapper.toResponse(book);

        // Assert
        assertThat(result).isNull();
        verify(bookGenreMapper, never()).toResponse(any(BookGenre.class));
        verify(imageMapper, never()).toResponse(any(Image.class));
    }

    @Test
    void toResponse_withValidBook_shouldReturnBookResponse() {
        // Arrange
        var book = BookFactory.validBookWithImage();
        var bookResponse = BookResponseFactory.validResponse(book);

        for (var genre : book.getGenres()) {
            when(bookGenreMapper.toResponse(genre))
                    .thenReturn(BookGenreResponseFactory.validResponse(genre));
        }

        when(imageMapper.toResponse(book.getImage()))
                .thenReturn(ImageResponseFactory.validResponse(book.getImage()));

        // Act
        var result = bookMapper.toResponse(book);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookResponse.getId());
        assertThat(result.getTitle()).isEqualTo(bookResponse.getTitle());
        assertThat(result.getSynopsis()).isEqualTo(bookResponse.getSynopsis());
        assertThat(result.getAuthorName()).isEqualTo(bookResponse.getAuthorName());
        assertThat(result.getPublisherName()).isEqualTo(bookResponse.getPublisherName());
        assertThat(result.getPublishedAt()).isEqualTo(bookResponse.getPublishedAt());
        assertThat(result.getPageCount()).isEqualTo(bookResponse.getPageCount());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(bookResponse.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(bookResponse.getUpdatedAt());
        assertThat(result.getGenres()).extracting(BookGenreResponse::getName).containsExactlyElementsOf(bookResponse.getGenres().stream().map(BookGenreResponse::getName).toList());
        verify(bookGenreMapper, times(book.getGenres().size())).toResponse(any(BookGenre.class));
        verify(imageMapper, times(1)).toResponse(book.getImage());
    }

    @Test
    void toSimplifiedResponse_withNullBook_shouldReturnNullBookResponse() {
        // Arrange
        Book book = null;

        // Act
        var result = bookMapper.toSimplifiedResponse(book);

        // Assert
        assertThat(result).isNull();
        verify(bookGenreMapper, never()).toResponse(any(BookGenre.class));
        verify(imageMapper, never()).toResponse(any(Image.class));
    }

    @Test
    void toSimplifiedResponse_withValidBook_shouldReturnBookResponse() {
        // Arrange
        var book = BookFactory.validBookWithImage();
        var bookResponse = SimplifiedBookResponseFactory.validResponse(book);

        for (var genre : book.getGenres()) {
            when(bookGenreMapper.toResponse(genre))
                    .thenReturn(BookGenreResponseFactory.validResponse(genre));
        }

        when(imageMapper.toResponse(book.getImage()))
                .thenReturn(ImageResponseFactory.validResponse(book.getImage()));

        // Act
        var result = bookMapper.toResponse(book);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookResponse.getId());
        assertThat(result.getTitle()).isEqualTo(bookResponse.getTitle());
        assertThat(result.getAuthorName()).isEqualTo(bookResponse.getAuthorName());
        assertThat(result.getPublisherName()).isEqualTo(bookResponse.getPublisherName());
        assertThat(result.getPublishedAt()).isEqualTo(bookResponse.getPublishedAt());
        assertThat(result.getPageCount()).isEqualTo(bookResponse.getPageCount());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(bookResponse.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(bookResponse.getUpdatedAt());
        assertThat(result.getGenres()).extracting(BookGenreResponse::getName).containsExactlyElementsOf(bookResponse.getGenres().stream().map(BookGenreResponse::getName).toList());
        verify(bookGenreMapper, times(book.getGenres().size())).toResponse(any(BookGenre.class));
        verify(imageMapper, times(1)).toResponse(book.getImage());
    }

    @Test
    void updateEntity_withValidBookAndNullBookRequest_shouldPreserveBook() {
        // Arrange
        BookRequest bookRequest = null;
        var book = BookFactory.validBook();
        var newBook = SerializationUtils.clone(book);

        // Act
        bookMapper.updateEntity(newBook, bookRequest);

        // Assert
        assertThat(newBook.getId()).isEqualTo(book.getId());
        assertThat(newBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(newBook.getSynopsis()).isEqualTo(book.getSynopsis());
        assertThat(newBook.getAuthorName()).isEqualTo(book.getAuthorName());
        assertThat(newBook.getPublisherName()).isEqualTo(book.getPublisherName());
        assertThat(newBook.getPublishedAt()).isEqualTo(book.getPublishedAt());
        assertThat(newBook.getPageCount()).isEqualTo(book.getPageCount());
        assertThat(newBook.getCreatedAt()).isEqualToIgnoringNanos(book.getCreatedAt());
        assertThat(newBook.getGenres()).extracting(BookGenre::getName).containsExactlyElementsOf(book.getGenres().stream().map(BookGenre::getName).toList());
        verify(bookGenreMapper, never()).toEntity(ArgumentMatchers.<List<String>>any());
    }

    @Test
    void updateEntity_withValidBookAndValidBookRequest_shouldUpdateBook() {
        // Arrange
        var bookRequest = BookRequestFactory.validRequest();
        var book = BookFactory.validBook(bookRequest);
        var newBook = SerializationUtils.clone(book);
        newBook.setGenres(new ArrayList<>(newBook.getGenres()));

        when(bookGenreMapper.toEntity(bookRequest.getGenres()))
                .thenReturn(bookRequest.getGenres().stream().map(BookGenreFactory::validGenre).toList());

        // Act
        bookMapper.updateEntity(newBook, bookRequest);

        // Assert
        assertThat(newBook.getId()).isEqualTo(book.getId());
        assertThat(newBook.getTitle()).isEqualTo(bookRequest.getTitle());
        assertThat(newBook.getSynopsis()).isEqualTo(bookRequest.getSynopsis());
        assertThat(newBook.getAuthorName()).isEqualTo(bookRequest.getAuthorName());
        assertThat(newBook.getPublisherName()).isEqualTo(bookRequest.getPublisherName());
        assertThat(newBook.getPublishedAt()).isEqualTo(bookRequest.getPublishedAt());
        assertThat(newBook.getPageCount()).isEqualTo(bookRequest.getPageCount());
        assertThat(newBook.getCreatedAt()).isEqualToIgnoringNanos(book.getCreatedAt());
        assertThat(newBook.getGenres()).extracting(BookGenre::getName).containsExactlyElementsOf(bookRequest.getGenres());
        verify(bookGenreMapper, times(1)).toEntity(bookRequest.getGenres());
    }

}
