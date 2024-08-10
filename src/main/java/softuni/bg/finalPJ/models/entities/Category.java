package softuni.bg.finalPJ.models.entities;

import jakarta.persistence.*;
import softuni.bg.finalPJ.models.enums.CategoryEnum;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;


    public Long getId() {
        return id;
    }

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public Category setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }
}
