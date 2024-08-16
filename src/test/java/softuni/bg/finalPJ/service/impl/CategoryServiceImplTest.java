package softuni.bg.finalPJ.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import softuni.bg.finalPJ.models.entities.Category;
import softuni.bg.finalPJ.models.enums.CategoryEnum;
import softuni.bg.finalPJ.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl serviceToTest;

    @Mock
    private CategoryRepository mockCategoryRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new CategoryServiceImpl(mockCategoryRepository);
    }

    @Test
    void testFindAll() {
        List<Category> testCategories = new ArrayList<>();
        testCategories.add(createTestCategory(1L, CategoryEnum.TRADE));
        testCategories.add(createTestCategory(2L, CategoryEnum.SALES));

        when(mockCategoryRepository.findAll()).thenReturn(testCategories);

        List<Category> result = serviceToTest.findAll();

        Assertions.assertNotNull(result, "The result should not be null");
        Assertions.assertEquals(2, result.size(), "The result size should be 2");
        Assertions.assertEquals(CategoryEnum.TRADE, result.get(0).getCategory(), "First category should match");
        Assertions.assertEquals(CategoryEnum.SALES, result.get(1).getCategory(), "Second category should match");
    }

    @Test
    void testSave() {
        Category testCategory = createTestCategory(1L, CategoryEnum.TRADE);

        serviceToTest.save(testCategory);

        verify(mockCategoryRepository, times(1)).save(testCategory);
    }

    @Test
    void testFindByIdFound() {
        Category testCategory = createTestCategory(1L, CategoryEnum.TRADE);

        when(mockCategoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        Category result = serviceToTest.findById(1L);

        Assertions.assertNotNull(result, "The result should not be null");
        Assertions.assertEquals(CategoryEnum.TRADE, result.getCategory(), "The category should match");
    }

    @Test
    void testFindByIdNotFound() {
        when(mockCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceToTest.findById(1L);
        });

        String expectedMessage = "Invalid category Id:1";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage), "The exception message should match");
    }

    @Test
    void testDelete() {
        Category testCategory = createTestCategory(1L, CategoryEnum.TRADE);

        serviceToTest.delete(testCategory);

        verify(mockCategoryRepository, times(1)).delete(testCategory);
    }

    private Category createTestCategory(Long id, CategoryEnum categoryEnum) {
        return new Category()
                .setId(id)
                .setCategory(categoryEnum);
    }
}
