package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.image.application.payload.response.ImageResponseFactory;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        assertNull(result);
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
        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getSynopsis(), result.getSynopsis());
        assertEquals(book.getAuthorName(), result.getAuthorName());
        assertEquals(book.getPublisherName(), result.getPublisherName());
        assertEquals(book.getPublishedAt(), result.getPublishedAt());
        assertEquals(book.getPageCount(), result.getPageCount());
        assertEquals(book.getGenres(), result.getGenres());
        verify(bookGenreMapper, times(1)).toEntity(bookRequest.getGenres());
    }

    @Test
    void toEntity_withNonExistingBookId_shouldReturnNull() {
        // Arrange
        var id = Math.abs(new Random().nextLong()) + 1;

        when(bookRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookMapper.toEntity(id);

        // Assert
        assertNull(result);
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
        assertNotNull(result);
        verify(bookRepository, times(1)).findById(book.getId());
    }

    @Test
    void toResponse_withNullBook_shouldReturnNullBookResponse() {
        // Arrange
        Book book = null;

        // Act
        var result = bookMapper.toResponse(book);

        // Assert
        assertNull(result);
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
        assertNotNull(result);
        assertEquals(bookResponse.getId(), result.getId());
        assertEquals(bookResponse.getTitle(), result.getTitle());
        assertEquals(bookResponse.getSynopsis(), result.getSynopsis());
        assertEquals(bookResponse.getAuthorName(), result.getAuthorName());
        assertEquals(bookResponse.getPublisherName(), result.getPublisherName());
        assertEquals(bookResponse.getPublishedAt(), result.getPublishedAt());
        assertEquals(bookResponse.getPageCount(), result.getPageCount());
        assertEquals(bookResponse.getCreatedAt(), result.getCreatedAt());
        assertEquals(bookResponse.getUpdatedAt(), result.getUpdatedAt());
        assertIterableEquals(bookResponse.getGenres().stream().map(BookGenreResponse::getName).toList(), result.getGenres().stream().map(BookGenreResponse::getName).toList());
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
        assertNull(result);
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
        assertNotNull(result);
        assertEquals(bookResponse.getId(), result.getId());
        assertEquals(bookResponse.getTitle(), result.getTitle());
        assertEquals(bookResponse.getAuthorName(), result.getAuthorName());
        assertEquals(bookResponse.getPublisherName(), result.getPublisherName());
        assertEquals(bookResponse.getPublishedAt(), result.getPublishedAt());
        assertEquals(bookResponse.getPageCount(), result.getPageCount());
        assertEquals(bookResponse.getCreatedAt(), result.getCreatedAt());
        assertEquals(bookResponse.getUpdatedAt(), result.getUpdatedAt());
        assertIterableEquals(bookResponse.getGenres().stream().map(BookGenreResponse::getName).toList(), result.getGenres().stream().map(BookGenreResponse::getName).toList());
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
        assertEquals(book.getId(), newBook.getId());
        assertEquals(book.getTitle(), newBook.getTitle());
        assertEquals(book.getSynopsis(), newBook.getSynopsis());
        assertEquals(book.getAuthorName(), newBook.getAuthorName());
        assertEquals(book.getPublisherName(), newBook.getPublisherName());
        assertEquals(book.getPublishedAt(), newBook.getPublishedAt());
        assertEquals(book.getPageCount(), newBook.getPageCount());
        assertEquals(book.getCreatedAt(), newBook.getCreatedAt());
        assertIterableEquals(book.getGenres().stream().map(BookGenre::getName).toList(), newBook.getGenres().stream().map(BookGenre::getName).toList());
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
        assertEquals(book.getId(), newBook.getId());
        assertEquals(bookRequest.getTitle(), newBook.getTitle());
        assertEquals(bookRequest.getSynopsis(), newBook.getSynopsis());
        assertEquals(bookRequest.getAuthorName(), newBook.getAuthorName());
        assertEquals(bookRequest.getPublisherName(), newBook.getPublisherName());
        assertEquals(bookRequest.getPublishedAt(), newBook.getPublishedAt());
        assertEquals(bookRequest.getPageCount(), newBook.getPageCount());
        assertEquals(book.getCreatedAt(), newBook.getCreatedAt());
        assertIterableEquals(bookRequest.getGenres(), newBook.getGenres().stream().map(BookGenre::getName).toList());
        verify(bookGenreMapper, times(1)).toEntity(bookRequest.getGenres());
    }

}
