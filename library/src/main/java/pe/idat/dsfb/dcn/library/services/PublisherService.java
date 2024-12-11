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

import pe.idat.dsfb.dcn.library.dtos.BookInformation;
import pe.idat.dsfb.dcn.library.dtos.PublisherInformation;
import pe.idat.dsfb.dcn.library.dtos.PublisherValidation;

import pe.idat.dsfb.dcn.library.models.Publisher;
import pe.idat.dsfb.dcn.library.repositories.PublisherRepository;

@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    @Transactional
    public Publisher create(PublisherValidation publisherValidation) {

        Publisher publisher = new Publisher();
        publisher.setName(publisherValidation.getName());
        publisher.setContactNumber(publisherValidation.getContactNumber());
        publisher.setEmail(publisherValidation.getEmail());
        publisher.setType(publisherValidation.getType());
        publisher.setCountry(publisherValidation.getCountry());
        publisher.setWebsite(publisherValidation.getWebsite());

        return publisherRepository.saveAndFlush(publisher);
    }

    @Transactional
    public Publisher update(long id, PublisherValidation publisherValidation){
        Optional<Publisher> existingPublisher = publisherRepository.findById(id);

        if(!existingPublisher.isPresent()){
            return null;
        }

        Publisher publisher = existingPublisher.get();
        publisher.setName(publisherValidation.getName());
        publisher.setContactNumber(publisherValidation.getContactNumber());
        publisher.setEmail(publisherValidation.getEmail());
        publisher.setType(publisherValidation.getType());
        publisher.setCountry(publisherValidation.getCountry());
        publisher.setWebsite(publisherValidation.getWebsite());

        return publisherRepository.saveAndFlush(publisher);
    }

    @Transactional
    public boolean delete(long id) {
        Optional<Publisher> result = publisherRepository.findById(id);

        if(!result.isPresent()){
            return false;
        }

        publisherRepository.deleteById(id);

        return true;
    }

    public PublisherInformation getPublisherWithBooks(Long id) {
        Optional<Publisher> result = publisherRepository.findById(id);

        if(!result.isPresent()){
            return null;
        }

        Publisher publisher = result.get();

        List<BookInformation> booksInformation = publisher.getBooks().stream()
                                    .map(book -> new BookInformation(
                                            book.getTitle(),
                                            book.getPageCount(),
                                            book.getLanguage(),
                                            book.getPublicationDate()))
                                    .collect(Collectors.toList());

        return new PublisherInformation(
                publisher.getId(),
                publisher.getName(),
                publisher.getContactNumber(),
                publisher.getEmail(),
                publisher.getType(),
                publisher.getCountry(),
                publisher.getWebsite(),
                booksInformation
        );
    }

    public List<PublisherInformation> getAllPublishersWithBooks() {
        List<Publisher> publishers = publisherRepository.findAll();

        return publishers.stream().map(publisher -> {
            List<BookInformation> booksInformation = publisher.getBooks().stream()
                    .map(book -> new BookInformation(
                            book.getTitle(),
                            book.getPageCount(),
                            book.getLanguage(),
                            book.getPublicationDate()))
                    .collect(Collectors.toList());

            return new PublisherInformation(
                    publisher.getId(),
                    publisher.getName(),
                    publisher.getContactNumber(),
                    publisher.getEmail(),
                    publisher.getType(),
                    publisher.getCountry(),
                    publisher.getWebsite(),
                    booksInformation
            );
        }).collect(Collectors.toList());
    }

    public List<Publisher> findByNames(List<String> names) {
        return publisherRepository.findByNameIn(names);
    }

    public Page<PublisherInformation> getAllPublishersWithFilters(String country, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Publisher> publishersPage = publisherRepository.findAllWithFilters(country, type, pageable);

        return publishersPage.map(publisher -> {
            List<BookInformation> booksInformation = publisher.getBooks().stream()
                    .map(book -> new BookInformation(
                            book.getTitle(),
                            book.getPageCount(),
                            book.getLanguage(),
                            book.getPublicationDate()))
                    .collect(Collectors.toList());

            return new PublisherInformation(
                    publisher.getId(),
                    publisher.getName(),
                    publisher.getContactNumber(),
                    publisher.getEmail(),
                    publisher.getType(),
                    publisher.getCountry(),
                    publisher.getWebsite(),
                    booksInformation
            );
        });
    }

    public Publisher getByName(String name){
        Optional<Publisher> result = publisherRepository.findByName(name);

        if(!result.isPresent()){
            return null;
        }

        return result.get();
    }
}

