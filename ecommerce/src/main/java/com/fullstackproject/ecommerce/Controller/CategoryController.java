package com.fullstackproject.ecommerce.Controller;

import com.fullstackproject.ecommerce.Exception.CategoryException;
import com.fullstackproject.ecommerce.Exception.IdException;
import com.fullstackproject.ecommerce.Model.Category;
import com.fullstackproject.ecommerce.Model.PayLoadValidation;
import com.fullstackproject.ecommerce.Service.CategoryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add-category")
    public Category saveCategory(@RequestBody Category category) throws IdException {
        if(!PayLoadValidation.payLoadCategoryValidation(category)) {
            throw new IdException("Category did not created");
        }
        return categoryService.createCategory(category);
    }

    @GetMapping("/get-category/{id}")
    public Category getCategoryByID(@PathVariable("id") ObjectId id) throws CategoryException {
        return categoryService.getCategoryByID(id);
    }

    @PostMapping("/get")  //@GetMapping("/get-category/{field}")
    public List<Category> getCategoryByField(@RequestBody Map<String, Object> map)throws CategoryException{
        return categoryService.getCategoryByField(map.get("field").toString(),map.get("value").toString());
    }

    @GetMapping("/get-all-categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") String id,@RequestBody Category category){
        return categoryService.updateCategory(id, category);
    }

    @PostMapping("/delete-id/{id}")
    public String deleteCategoryId(@PathVariable("id") ObjectId id) throws CategoryException{
        return categoryService.deleteCategoryById(id);
    }
}