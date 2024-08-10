package softuni.bg.finalPJ.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.entities.Category;
import softuni.bg.finalPJ.repositories.CategoryRepository;
import softuni.bg.finalPJ.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category category) {

        categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) {
       return categoryRepository.findById(id)
               .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
    }

    @Override
    public void delete(Category category) {

        categoryRepository.delete(category);
    }
}
