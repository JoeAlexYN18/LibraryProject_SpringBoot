package pe.idat.dsfb.dcn.library.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import pe.idat.dsfb.dcn.library.dtos.AuthorInformationForBook;
import pe.idat.dsfb.dcn.library.dtos.BookDetails;

import pe.idat.dsfb.dcn.library.dtos.BookValidation;
import pe.idat.dsfb.dcn.library.dtos.CategoryInformationForBook;
import pe.idat.dsfb.dcn.library.dtos.PublisherInformationForBook;
import pe.idat.dsfb.dcn.library.models.Author;
import pe.idat.dsfb.dcn.library.models.Book;
import pe.idat.dsfb.dcn.library.models.Category;
import pe.idat.dsfb.dcn.library.models.Publisher;
import pe.idat.dsfb.dcn.library.repositories.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Book create(BookValidation bookValidation, List<Author> authors, List<Publisher> publishers, List<Category> categories) {

        Book book = new Book();
        book.setTitle(bookValidation.getTitle());
        book.setISBN(bookValidation.getISBN());
        book.setPageCount(bookValidation.getPageCount());
        book.setLanguage(bookValidation.getLanguage());
        book.setPrice(bookValidation.getPrice());
        book.setPublicationDate(bookValidation.getPublicationDate());
        book.setFormat(bookValidation.getFormat());
        book.setAuthors(authors);
        book.setPublishers(publishers);
        book.setCategories(categories);

        return bookRepository.saveAndFlush(book);
    }

    @Transactional
    public Book update(Book existingbook, BookValidation bookValidation, List<Author> authors, List<Publisher> publishers, List<Category> categories) {

        existingbook.setTitle(bookValidation.getTitle());
        existingbook.setISBN(bookValidation.getISBN());
        existingbook.setPageCount(bookValidation.getPageCount());
        existingbook.setLanguage(bookValidation.getLanguage());
        existingbook.setPrice(bookValidation.getPrice());
        existingbook.setPublicationDate(bookValidation.getPublicationDate());
        existingbook.setFormat(bookValidation.getFormat());
        existingbook.setAuthors(authors);
        existingbook.setPublishers(publishers);
        existingbook.setCategories(categories);

        return bookRepository.saveAndFlush(existingbook);
    }

    public Book getByISBN(String ISBN){
        Optional<Book> result = bookRepository.findByISBN(ISBN);

        if(!result.isPresent()){
            return null;
        }

        return result.get();
    }

    public Book getById(long id){
        Optional<Book> result = bookRepository.findById(id);

        if(!result.isPresent()){
            return null;
        }

        return result.get();
    }

    @Transactional
    public boolean delete(long id) {
        Optional<Book> result = bookRepository.findById(id);

        if(!result.isPresent()){
            return false;
        }

        bookRepository.deleteById(id);

        return true;
    }

    public BookDetails getBookDetails(Long id) {
        Optional<Book> result = bookRepository.findById(id);

        if(!result.isPresent()){
            return null;
        }

        Book book = result.get();

        List<AuthorInformationForBook> authorsInformation = book.getAuthors().stream()
                                    .map(author -> new AuthorInformationForBook(
                                            author.getName(),
                                            author.getNationality(),
                                            author.getBirthDate(),
                                            author.getBiography(),
                                            author.getEmail()))
                                    .collect(Collectors.toList());

        List<PublisherInformationForBook> publishersInformation = book.getPublishers().stream()
                                    .map(publisher -> new PublisherInformationForBook(
                                            publisher.getName(),
                                            publisher.getType(),
                                            publisher.getWebsite()))
                                    .collect(Collectors.toList());
                                    
        List<CategoryInformationForBook> categoriesInformation = book.getCategories().stream()
                                    .map(category -> new CategoryInformationForBook(
                                            category.getName(),
                                            category.getDescription()))
                                    .collect(Collectors.toList());

        return new BookDetails(
                book.getId(),
                book.getTitle(),
                book.getISBN(),
                book.getPageCount(),
                book.getLanguage(),
                book.getPrice(),
                book.getPublicationDate(),
                book.getFormat(),
                authorsInformation,
                publishersInformation,
                categoriesInformation
        );
    }

    public List<BookDetails> getAllBookDetails() {
        List<Book> books = bookRepository.findAll();

        return books.stream().map(book -> {
            List<AuthorInformationForBook> authorsInformation = book.getAuthors().stream()
                    .map(author -> new AuthorInformationForBook(
                        author.getName(),
                        author.getNationality(),
                        author.getBirthDate(),
                        author.getBiography(),
                        author.getEmail()))
                    .collect(Collectors.toList());
            
            List<PublisherInformationForBook> publishersInformation = book.getPublishers().stream()
                    .map(publisher -> new PublisherInformationForBook(
                        publisher.getName(),
                        publisher.getType(),
                        publisher.getWebsite()))
                    .collect(Collectors.toList());

            List<CategoryInformationForBook> categoriesInformation = book.getCategories().stream()
                    .map(category -> new CategoryInformationForBook(
                            category.getName(),
                            category.getDescription()))
                    .collect(Collectors.toList());

            return new BookDetails(
                    book.getId(),
                    book.getTitle(),
                    book.getISBN(),
                    book.getPageCount(),
                    book.getLanguage(),
                    book.getPrice(),
                    book.getPublicationDate(),
                    book.getFormat(),
                    authorsInformation,
                    publishersInformation,
                    categoriesInformation
            );
        }).collect(Collectors.toList());
    }

    public Page<BookDetails> getAllBookDetailsWithFilters(String language, String format, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Book> booksPage = bookRepository.findAllWithFilters(language, format, pageable);

        return booksPage.map(book -> {
            List<AuthorInformationForBook> authorsInformation = book.getAuthors().stream()
                    .map(author -> new AuthorInformationForBook(
                            author.getName(),
                            author.getNationality(),
                            author.getBirthDate(),
                            author.getBiography(),
                            author.getEmail()))
                    .collect(Collectors.toList());

            List<PublisherInformationForBook> publishersInformation = book.getPublishers().stream()
                    .map(publisher -> new PublisherInformationForBook(
                        publisher.getName(),
                        publisher.getType(),
                        publisher.getWebsite()))
                    .collect(Collectors.toList());

            List<CategoryInformationForBook> categoriesInformation = book.getCategories().stream()
                    .map(category -> new CategoryInformationForBook(
                            category.getName(),
                            category.getDescription()))
                    .collect(Collectors.toList());

            return new BookDetails(
                book.getId(),
                book.getTitle(),
                book.getISBN(),
                book.getPageCount(),
                book.getLanguage(),
                book.getPrice(),
                book.getPublicationDate(),
                book.getFormat(),
                authorsInformation,
                publishersInformation,
                categoriesInformation
            );
        });
    }
}

