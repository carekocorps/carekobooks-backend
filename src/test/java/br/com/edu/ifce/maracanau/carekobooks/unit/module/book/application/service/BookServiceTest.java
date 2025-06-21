package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.MultipartFileFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookGenreService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookAlreadyContainingGenreException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotContainingGenreException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreCountMismatchException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private ImageService imageService;

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private BookGenreService bookGenreService;

    @Mock
    private BookGenreMapper bookGenreMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookValidator bookValidator;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void find_withNonExistingBook_shouldReturnEmptyBook() {
        // Arrange
        var id = Math.abs(new Random().nextLong()) + 1;

        when(bookRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookService.find(id);

        // Assert
        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findById(id);
        verify(bookMapper, never()).toResponse(any(Book.class));
    }

    @Test
    void find_withExistingBook_shouldReturnBookResponse() {
        // Arrange
        var book = BookFactory.validBook();
        var response = BookResponseFactory.validResponse(book);

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        when(bookMapper.toResponse(book))
                .thenReturn(response);

        // Act
        var result = bookService.find(book.getId());

        // Assert
        assertThat(result)
                .isPresent()
                .contains(response);

        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookMapper, times(1)).toResponse(book);
    }

    @Test
    void create_withBookGenreCountMismatch_shouldThrowGenreCountMismatchException() {
        // Arrange
        MultipartFile multipartFile = null;
        var request = BookRequestFactory.invalidRequestByRepeatingGenres();
        var book = BookFactory.validBook(request);

        when(bookMapper.toEntity(request))
                .thenReturn(book);

        doNothing()
                .when(bookValidator)
                .validate(book);

        // Act && Assert
        assertThatThrownBy(() -> bookService.create(request, multipartFile)).isInstanceOf(BookGenreCountMismatchException.class);
        verify(bookMapper, times(1)).toEntity(request);
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookValidator, times(1)).validate(book);
        verify(bookRepository, never()).save(any(Book.class));
        verify(bookMapper, never()).toResponse(any(Book.class));
    }

    @Test
    void create_withValidBookRequestAndNonNullImage_shouldReturnBookResponse() {
        // Arrange
        var request = BookRequestFactory.validRequest();
        var book = BookFactory.validBook(request);
        var bookResponse = BookResponseFactory.validResponse(book);

        var multipartFile = MultipartFileFactory.validFile();
        var image = ImageFactory.validImage(multipartFile);
        var imageResponse = ImageResponseFactory.validResponse(image);

        when(bookMapper.toEntity(request))
                .thenReturn(book);

        when(imageService.create(multipartFile))
                .thenReturn(imageResponse);

        when(imageMapper.toEntity(imageResponse))
                .thenReturn(image);

        doNothing()
                .when(bookValidator)
                .validate(book);

        when(bookRepository.save(book))
                .thenReturn(book);

        when(bookMapper.toResponse(book))
                .thenReturn(bookResponse);

        // Act
        var result = bookService.create(request, multipartFile);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(bookResponse);

        verify(bookMapper, times(1)).toEntity(request);
        verify(imageService, times(1)).create(multipartFile);
        verify(imageMapper, times(1)).toEntity(imageResponse);
        verify(bookValidator, times(1)).validate(book);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toResponse(book);
    }

    @Test
    void create_withValidBookRequestAndNullImage_shouldReturnBookResponse() {
        // Arrange
        MultipartFile multipartFile = null;
        var request = BookRequestFactory.validRequest();
        var book = BookFactory.validBook(request);
        var bookResponse = BookResponseFactory.validResponse(book);

        when(bookMapper.toEntity(request))
                .thenReturn(book);

        doNothing()
                .when(bookValidator)
                .validate(book);

        when(bookRepository.save(book))
                .thenReturn(book);

        when(bookMapper.toResponse(book))
                .thenReturn(bookResponse);

        // Act
        var result = bookService.create(request, multipartFile);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(bookResponse);

        verify(bookMapper, times(1)).toEntity(request);
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookValidator, times(1)).validate(book);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toResponse(book);
    }

    @Test
    void update_withNonExistingBook_shouldThrowNotFoundException() {
        // Arrange
        var id = Math.abs(new Random().nextLong()) + 1;
        var request = BookRequestFactory.validRequest();
        var multipartFile = MultipartFileFactory.validFile();

        when(bookRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> bookService.update(id, request, multipartFile)).isInstanceOf(BookNotFoundException.class);
        verify(bookRepository, times(1)).findById(id);
        verify(imageService, never()).delete(any(Long.class));
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookMapper, never()).updateEntity(any(Book.class), any(BookRequest.class));
        verify(bookValidator, never()).validate(any(Book.class));
        verify(bookRepository, never()).save(any(Book.class));
        verify(bookMapper, never()).toResponse(any(Book.class));
    }

    @Test
    void update_withExistingBookAndValidBookRequestAndNullImage_shouldSucceed() {
        // Arrange
        MultipartFile multipartFile = null;
        var request = BookRequestFactory.validRequest();
        var book = BookFactory.validBook(request);
        var updatedBook = BookFactory.updatedBook(book, request);
        var updatedBookResponse = BookResponseFactory.validResponse(updatedBook);

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        doNothing()
                .when(bookMapper)
                .updateEntity(book, request);

        doNothing()
                .when(bookValidator)
                .validate(book);

        when(bookRepository.save(book))
                .thenReturn(updatedBook);

        when(bookMapper.toResponse(updatedBook))
                .thenReturn(updatedBookResponse);

        // Act
        var result = bookService.update(updatedBook.getId(), request, multipartFile);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(updatedBookResponse);

        verify(bookRepository, times(1)).findById(book.getId());
        verify(imageService, never()).delete(any(Long.class));
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookMapper, times(1)).updateEntity(book, request);
        verify(bookValidator, times(1)).validate(book);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toResponse(updatedBook);
    }

    @Test
    void update_withExistingBookAndInvalidBookRequestByGenreCountMismatchAndNullImage_shouldThrowGenreCountMismatchException() {
        // Arrange
        MultipartFile multipartFile = null;
        var request = BookRequestFactory.invalidRequestByRepeatingGenres();
        var book = BookFactory.validBook(request);
        var bookId = book.getId();

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        doNothing()
                .when(bookMapper)
                .updateEntity(book, request);

        doNothing()
                .when(bookValidator)
                .validate(book);

        // Act && Assert
        assertThatThrownBy(() -> bookService.update(bookId, request, multipartFile)).isInstanceOf(BookGenreCountMismatchException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(imageService, never()).delete(any(Long.class));
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookMapper, times(1)).updateEntity(book, request);
        verify(bookValidator, times(1)).validate(book);
        verify(bookRepository, never()).save(any(Book.class));
        verify(bookMapper, never()).toResponse(any(Book.class));
    }

    @Test
    void update_withExistingBookAndValidBookRequestAndNonNullImage_shouldSucceed() {
        // Arrange
        var request = BookRequestFactory.validRequest();
        var book = BookFactory.validBookWithImage(request);
        var bookImageId = book.getImage().getId();
        var updatedBook = BookFactory.updatedBook(book, request);
        var updatedBookResponse = BookResponseFactory.validResponse(updatedBook);

        var multipartFile = MultipartFileFactory.validFile();
        var image = ImageFactory.validImage(multipartFile);
        var imageResponse = ImageResponseFactory.validResponse(image);

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        doNothing()
                .when(imageService)
                .delete(bookImageId);

        when(imageService.create(multipartFile))
                .thenReturn(imageResponse);

        when(imageMapper.toEntity(imageResponse))
                .thenReturn(image);

        doNothing()
                .when(bookMapper)
                .updateEntity(book, request);

        doNothing()
                .when(bookValidator)
                .validate(book);

        when(bookRepository.save(book))
                .thenReturn(updatedBook);

        when(bookMapper.toResponse(updatedBook))
                .thenReturn(updatedBookResponse);

        // Act
        var result = bookService.update(book.getId(), request, multipartFile);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(updatedBookResponse);

        verify(bookRepository, times(1)).findById(book.getId());
        verify(imageService, times(1)).delete(bookImageId);
        verify(imageService, times(1)).create(multipartFile);
        verify(imageMapper, times(1)).toEntity(imageResponse);
        verify(bookMapper, times(1)).updateEntity(book, request);
        verify(bookValidator, times(1)).validate(book);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toResponse(updatedBook);
    }

    @Test
    void changeGenre_withNonExistingBook_shouldFail() {
        // Arrange
        var bookId = Math.abs(new Random().nextLong()) + 1;
        var genreName = "genre";
        var isAdditionRequired = new Random().nextBoolean();

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> bookService.changeGenre(bookId, genreName, isAdditionRequired)).isInstanceOf(BookNotFoundException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookGenreService, never()).find(any(String.class));
        verify(bookRepository, never()).addGenre(any(Long.class), any(Long.class));
        verify(bookRepository, never()).removeGenre(any(Long.class), any(Long.class));
        verify(bookValidator, never()).validate(any(Book.class));
    }

    @Test
    void changeGenre_withExistingBookAndNonExistingGenre_shouldFail() {
        // Arrange
        var book = BookFactory.validBook();
        var bookId = book.getId();

        var genreName = book.getGenres().getFirst().getName();
        var isAdditionRequired = new Random().nextBoolean();

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(bookGenreService.find(genreName))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> bookService.changeGenre(bookId, genreName, isAdditionRequired)).isInstanceOf(BookGenreNotFoundException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookGenreService, times(1)).find(genreName);
        verify(bookRepository, never()).addGenre(any(Long.class), any(Long.class));
        verify(bookRepository, never()).removeGenre(any(Long.class), any(Long.class));
        verify(bookValidator, never()).validate(any(Book.class));
    }

    @Test
    void changeGenre_withExistingBookAndExistingGenreAndBookAlreadyContainsGenreAndIsAdditionRequired_shouldThrowAlreadyContainingGenreException() {
        // Arrange
        var book = BookFactory.validBook();
        var bookId = book.getId();

        var genre = book.getGenres().getFirst();
        var genreName = genre.getName();
        var genreResponse = BookGenreResponseFactory.validResponse(genre);
        var isAdditionRequired = true;

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(bookGenreService.find(genreName))
                .thenReturn(Optional.of(genreResponse));

        when(bookGenreMapper.toEntity(genreResponse))
                .thenReturn(genre);

        // Act && Assert
        assertThatThrownBy(() -> bookService.changeGenre(bookId, genreName, isAdditionRequired)).isInstanceOf(BookAlreadyContainingGenreException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookGenreService, times(1)).find(genreName);
        verify(bookGenreMapper, times(1)).toEntity(genreResponse);
        verify(bookRepository, never()).addGenre(any(Long.class), any(Long.class));
        verify(bookRepository, never()).removeGenre(any(Long.class), any(Long.class));
        verify(bookValidator, never()).validate(any(Book.class));
    }

    @Test
    void changeGenre_withExistingBookAndNonExistingGenreAndIsAdditionNotRequired_shouldThrowNotContainingGenreException() {
        // Arrange
        var book = BookFactory.validBook();
        var bookId = book.getId();

        var genre = BookGenreFactory.validGenre();
        var genreName = genre.getName();
        var genreResponse = BookGenreResponseFactory.validResponse(genre);
        var isAdditionRequired = false;

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(bookGenreService.find(genre.getName()))
                .thenReturn(Optional.of(genreResponse));

        when(bookGenreMapper.toEntity(genreResponse))
                .thenReturn(genre);

        // Act && Assert
        assertThatThrownBy(() -> bookService.changeGenre(bookId, genreName, isAdditionRequired)).isInstanceOf(BookNotContainingGenreException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookGenreService, times(1)).find(genreName);
        verify(bookGenreMapper, times(1)).toEntity(genreResponse);
        verify(bookRepository, never()).addGenre(any(Long.class), any(Long.class));
        verify(bookRepository, never()).removeGenre(any(Long.class), any(Long.class));
        verify(bookValidator, never()).validate(any(Book.class));
    }

    @Test
    void changeGenre_withExistingBookAndExistingGenreAndIsAdditionNotRequired_shouldSucceed() {
        // Arrange
        var book = BookFactory.validBook();
        var bookId = book.getId();

        var genre = book.getGenres().getFirst();
        var genreName = genre.getName();
        var genreResponse = BookGenreResponseFactory.validResponse(genre);
        var isAdditionRequired = false;

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(bookGenreService.find(genre.getName()))
                .thenReturn(Optional.of(genreResponse));

        when(bookGenreMapper.toEntity(genreResponse))
                .thenReturn(genre);

        doNothing()
                .when(bookRepository)
                .removeGenre(bookId, genre.getId());

        doNothing()
                .when(bookValidator)
                .validate(book);

        // Act && Assert
        assertThatCode(() -> bookService.changeGenre(bookId, genreName, isAdditionRequired)).doesNotThrowAnyException();
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookGenreService, times(1)).find(genreName);
        verify(bookGenreMapper, times(1)).toEntity(genreResponse);
        verify(bookRepository, never()).addGenre(any(Long.class), any(Long.class));
        verify(bookRepository, times(1)).removeGenre(bookId, genre.getId());
        verify(bookValidator, times(1)).validate(book);
    }

    @Test
    void changeGenre_withExistingBookAndExistingGenreAndIsAdditionRequired_shouldSucceed() {
        // Arrange
        var book = BookFactory.validBook();
        var genre = BookGenreFactory.validGenre();
        var genreResponse = BookGenreResponseFactory.validResponse(genre);
        var isAdditionRequired = true;

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        when(bookGenreService.find(genre.getName()))
                .thenReturn(Optional.of(genreResponse));

        when(bookGenreMapper.toEntity(genreResponse))
                .thenReturn(genre);

        doNothing()
                .when(bookRepository)
                .addGenre(book.getId(), genre.getId());

        doNothing()
                .when(bookValidator)
                .validate(book);

        // Act && Assert
        assertThatCode(() -> bookService.changeGenre(book.getId(), genre.getName(), isAdditionRequired)).doesNotThrowAnyException();
        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookGenreService, times(1)).find(genre.getName());
        verify(bookGenreMapper, times(1)).toEntity(genreResponse);
        verify(bookRepository, times(1)).addGenre(book.getId(), genre.getId());
        verify(bookRepository, never()).removeGenre(any(Long.class), any(Long.class));
        verify(bookValidator, times(1)).validate(book);
    }

    @Test
    void changeImage_withNonExistingBook_shouldThrowNotFoundException() {
        // Arrange
        var bookId = Math.abs(new Random().nextLong()) + 1;
        var multipartFile = MultipartFileFactory.validFile();

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> bookService.changeImage(bookId, multipartFile)).isInstanceOf(BookNotFoundException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(imageService, never()).delete(any(Long.class));
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void changeImage_withExistingBookAndNullImageAndNullBookImage_shouldThrowImageNotFoundException() {
        // Arrange
        MultipartFile multipartFile = null;
        var book = BookFactory.validBook();
        var bookId = book.getId();

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        // Act && Assert
        assertThatThrownBy(() -> bookService.changeImage(bookId, multipartFile)).isInstanceOf(ImageNotFoundException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(imageService, never()).delete(any(Long.class));
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void changeImage_withExistingBookAndNullImageAndValidBookImage_shouldSucceed() {
        // Arrange
        MultipartFile multipartFile = null;
        var book = BookFactory.validBookWithImage();
        var bookImageId = book.getImage().getId();

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        doNothing()
                .when(imageService)
                .delete(bookImageId);

        when(bookRepository.save(book))
                .thenReturn(book);

        // Act && Assert
        assertThatCode(() -> bookService.changeImage(book.getId(), multipartFile)).doesNotThrowAnyException();
        verify(bookRepository, times(1)).findById(book.getId());
        verify(imageService, times(1)).delete(bookImageId);
        verify(imageService, never()).create(any(MultipartFile.class));
        verify(imageMapper, never()).toEntity(any(ImageResponse.class));
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void changeImage_withExistingBookAndNullBookImageAndValidImage_shouldSucceed() {
        // Arrange
        var book = BookFactory.validBook();
        var multipartFile = MultipartFileFactory.validFile();
        var image = ImageFactory.validImage(multipartFile);
        var imageResponse = ImageResponseFactory.validResponse(image);

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        when(imageService.create(multipartFile))
                .thenReturn(imageResponse);

        when(imageMapper.toEntity(imageResponse))
                .thenReturn(image);

        when(bookRepository.save(book))
                .thenReturn(book);

        // Act && Assert
        assertThatCode(() -> bookService.changeImage(book.getId(), multipartFile)).doesNotThrowAnyException();
        verify(bookRepository, times(1)).findById(book.getId());
        verify(imageService, never()).delete(any(Long.class));
        verify(imageService, times(1)).create(multipartFile);
        verify(imageMapper, times(1)).toEntity(imageResponse);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void changeImage_withExistingBookAndValidImage_shouldSucceed() {
        // Arrange
        var book = BookFactory.validBookWithImage();
        var bookImageId = book.getImage().getId();

        var multipartFile = MultipartFileFactory.validFile();
        var image = ImageFactory.validImage(multipartFile);
        var imageResponse = ImageResponseFactory.validResponse(image);

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        doNothing()
                .when(imageService)
                .delete(bookImageId);

        when(imageService.create(multipartFile))
                .thenReturn(imageResponse);

        when(imageMapper.toEntity(imageResponse))
                .thenReturn(image);

        when(bookRepository.save(book))
                .thenReturn(book);

        // Act && Assert
        assertThatCode(() -> bookService.changeImage(book.getId(), multipartFile)).doesNotThrowAnyException();
        verify(bookRepository, times(1)).findById(book.getId());
        verify(imageService, times(1)).delete(bookImageId);
        verify(imageService, times(1)).create(multipartFile);
        verify(imageMapper, times(1)).toEntity(imageResponse);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void delete_withNonExistingBook_shouldThrowNotFoundException() {
        // Arrange
        var bookId = Math.abs(new Random().nextLong()) + 1;

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> bookService.delete(bookId)).isInstanceOf(BookNotFoundException.class);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).delete(any(Book.class));
    }

    @Test
    void delete_withExistingBook_shouldSucceed() {
        // Arrange
        var book = BookFactory.validBook();

        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));

        // Act && Assert
        assertThatCode(() -> bookService.delete(book.getId())).doesNotThrowAnyException();
        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookRepository, times(1)).delete(book);
    }

}
