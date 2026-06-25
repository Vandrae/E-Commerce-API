package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;


@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryService categoryService;
    private ProductService productService;


    //@Autowired on a constructor tells spring to inject everything
    @Autowired
    public CategoriesController(CategoryService categoryService, ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
    }


    //endpoint for showing all categories in the DB
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Category> getAll()
    {
        return categoryService.getAllCategories();
    }

    //endpoint for showing a specific category in the DB based on the ID
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id)
    {
        Category category = categoryService.getById(id);

        if (category == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return category;
    }

    //endpoint for showing all products connected to specific category
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        return productService.listByCategoryId(categoryId);
    }

    @PostMapping
    // only a user with the role admin can execute this method
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        return ResponseEntity.status(201).body(categoryService.create(category));
    }

    //allows an admin to update a specific category
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        if (categoryService.getById(id) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return categoryService.update(id, category);
    }

    //allows an admin to delete a specific category
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        if (categoryService.getById(id) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
