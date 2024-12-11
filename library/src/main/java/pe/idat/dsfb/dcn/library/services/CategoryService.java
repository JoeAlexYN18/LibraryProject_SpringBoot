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
import pe.idat.dsfb.dcn.library.dtos.CategoryInformation;
import pe.idat.dsfb.dcn.library.dtos.CategoryValidation;
import pe.idat.dsfb.dcn.library.models.Category;
import pe.idat.dsfb.dcn.library.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Category create(CategoryValidation categoryValidation) {

        Category category = new Category();
        category.setName(categoryValidation.getName());
        category.setDescription(categoryValidation.getDescription());

        return categoryRepository.saveAndFlush(category);
    }

    @Transactional
    public Category update(long id, CategoryValidation categoryValidation){
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if(!existingCategory.isPresent()){
            return null;
        }

        Category category = existingCategory.get();
        category.setName(categoryValidation.getName());
        category.setDescription(categoryValidation.getDescription());

        return categoryRepository.saveAndFlush(category);
    }

    @Transactional
    public boolean delete(long id) {
        Optional<Category> result = categoryRepository.findById(id);

        if(!result.isPresent()){
            return false;
        }

        categoryRepository.deleteById(id);

        return true;
    }

    public CategoryInformation getCategoryWithBooks(Long id) {
        Optional<Category> result = categoryRepository.findById(id);

        if(!result.isPresent()){
            return null;
        }

        Category category = result.get();

        List<BookInformation> booksInformation = category.getBooks().stream()
                                    .map(book -> new BookInformation(
                                            book.getTitle(),
                                            book.getPageCount(),
                                            book.getLanguage(),
                                            book.getPublicationDate()))
                                    .collect(Collectors.toList());

        return new CategoryInformation(
                category.getId(),
                category.getName(),
                category.getDescription(),
                booksInformation
        );
    }

    public List<CategoryInformation> getAllCategoriesWithBooks() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category -> {
            List<BookInformation> booksInformation = category.getBooks().stream()
                    .map(book -> new BookInformation(
                            book.getTitle(),
                            book.getPageCount(),
                            book.getLanguage(),
                            book.getPublicationDate()))
                    .collect(Collectors.toList());

            return new CategoryInformation(
                category.getId(),
                category.getName(),
                category.getDescription(),
                booksInformation
            );
        }).collect(Collectors.toList());
    }

    public Page<CategoryInformation> getAllCategoriesWithFilters(String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Category> categoriesPage = categoryRepository.findAllWithFilters(description, pageable);

        return categoriesPage.map(category -> {
            List<BookInformation> booksInformation = category.getBooks().stream()
                    .map(book -> new BookInformation(
                            book.getTitle(),
                            book.getPageCount(),
                            book.getLanguage(),
                            book.getPublicationDate()))
                    .collect(Collectors.toList());

            return new CategoryInformation(
                category.getId(),
                category.getName(),
                category.getDescription(),
                booksInformation
            );
        });
    }

    public List<Category> findByNames(List<String> names) {
        return categoryRepository.findByNameIn(names);
    }

    public Category getByName(String name){
        Optional<Category> result = categoryRepository.findByName(name);

        if(!result.isPresent()){
            return null;
        }

        return result.get();
    }
}



