package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    void save(Category category);

    Category findById(Long id);

    void delete(Category category);
}
