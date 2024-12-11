package pe.idat.dsfb.dcn.library.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.idat.dsfb.dcn.library.dtos.AuthorValidation;
import pe.idat.dsfb.dcn.library.dtos.AuthorInformation;
import pe.idat.dsfb.dcn.library.dtos.BookInformation;
import pe.idat.dsfb.dcn.library.models.Author;
import pe.idat.dsfb.dcn.library.repositories.AuthorRepository;


@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public Author create(AuthorValidation authorValidation) {

        Author author = new Author();
        author.setName(authorValidation.getName());
        author.setNationality(authorValidation.getNationality());
        author.setBirthDate(authorValidation.getBirthDate());
        author.setBiography(authorValidation.getBiography());
        author.setEmail(authorValidation.getEmail());

        return authorRepository.saveAndFlush(author);
    }

    @Transactional
    public Author update(long id, AuthorValidation authorValidation){
        Optional<Author> existingAuthor = authorRepository.findById(id);

        if(!existingAuthor.isPresent()){
            return null;
        }

        Author author = existingAuthor.get();
        author.setName(authorValidation.getName());
        author.setNationality(authorValidation.getNationality());
        author.setBirthDate(authorValidation.getBirthDate());
        author.setBiography(authorValidation.getBiography());
        author.setEmail(authorValidation.getEmail());

        return authorRepository.saveAndFlush(author);
    }

    public Author getByName(String name){
        Optional<Author> result = authorRepository.findByName(name);

        if(!result.isPresent()){
            return null;
        }

        return result.get();
    }

    @Transactional
    public boolean delete(long id) {
        Optional<Author> result = authorRepository.findById(id);

        if(!result.isPresent()){
            return false;
        }

        authorRepository.deleteById(id);

        return true;
    }

    public AuthorInformation getAuthorWithBooks(Long id) {
        Optional<Author> result = authorRepository.findById(id);

        if(!result.isPresent()){
            return null;
        }

        Author author = result.get();

        List<BookInformation> booksInformation = author.getBooks().stream()
                                    .map(book -> new BookInformation(
                                            book.getTitle(),
                                            book.getPageCount(),
                                            book.getLanguage(),
                                            book.getPublicationDate()))
                                    .collect(Collectors.toList());

        return new AuthorInformation(
                author.getId(),
                author.getName(),
                author.getNationality(),
                author.getBirthDate(),
                author.getBiography(),
                author.getEmail(),
                booksInformation
        );
    }

    public List<AuthorInformation> getAllAuthorsWithBooks() {
        List<Author> authors = authorRepository.findAll();

        return authors.stream().map(author -> {
            List<BookInformation> booksInformation = author.getBooks().stream()
                    .map(book -> new BookInformation(
                            book.getTitle(),
                            book.getPageCount(),
                            book.getLanguage(),
                            book.getPublicationDate()))
                    .collect(Collectors.toList());

            return new AuthorInformation(
                    author.getId(),
                    author.getName(),
                    author.getNationality(),
                    author.getBirthDate(),
                    author.getBiography(),
                    author.getEmail(),
                    booksInformation
            );
        }).collect(Collectors.toList());
    }

    public Page<AuthorInformation> getAllAuthorsWithFilters(String name, String nationality, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Author> authorsPage = authorRepository.findAllWithFilters(name, nationality, pageable);

        return authorsPage.map(author -> {
            List<BookInformation> booksInformation = author.getBooks().stream()
                    .map(book -> new BookInformation(
                            book.getTitle(),
                            book.getPageCount(),
                            book.getLanguage(),
                            book.getPublicationDate()))
                    .collect(Collectors.toList());

            return new AuthorInformation(
                    author.getId(),
                    author.getName(),
                    author.getNationality(),
                    author.getBirthDate(),
                    author.getBiography(),
                    author.getEmail(),
                    booksInformation
            );
        });
    }

    public List<Author> findByNames(List<String> names) {
        return authorRepository.findByNameIn(names);
    }

}
