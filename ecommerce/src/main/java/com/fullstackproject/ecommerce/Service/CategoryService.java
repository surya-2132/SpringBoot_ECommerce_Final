package com.fullstackproject.ecommerce.Service;

import com.fullstackproject.ecommerce.Exception.CategoryException;
import com.fullstackproject.ecommerce.Model.Category;
import com.fullstackproject.ecommerce.Repository.CategoryRepository;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category getCategoryByID(ObjectId objectId) throws CategoryException {
        Optional<Category> category = categoryRepository.findById(objectId);
        if(category.isEmpty()){
            throw new CategoryException("Category Not Found");
        }
        return category.orElseGet(category::get);
    }

    public List<Category> getCategoryByField(String field, String value) throws CategoryException {
        List<Category> listCategory;
        if ("id".equals(field)) {
            listCategory = categoryRepository.findByID(new ObjectId(value));
        } else if ("name".equals(field)) {
            listCategory = categoryRepository.findByName(value);
        } else if ("description".equals(field)) {
            listCategory = categoryRepository.findByDesc(value);
        } else {
            throw new CategoryException("incorrect field");
        }
        if (listCategory.isEmpty()) {
            throw new CategoryException("Category doesn't exist");
        }
        return listCategory;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public String updateCategory(String id, Category category) {
        ObjectId objectId = new ObjectId(id);
        Optional<Category> categoryList = categoryRepository.findById(objectId);
        if(categoryList.isEmpty()) {
            throw new RuntimeException("given category doesn't exist");
        }
        Category updateCategory = categoryList.get();
        if (category.getName() != null)
            updateCategory.setName(category.getName());
        if (category.getDescription() != null)
            updateCategory.setDescription(category.getDescription());

        categoryRepository.save(updateCategory);
        return "{" +
                "\"message\":"+"Successfully updated category\",\n"+
                "\"data\":"+updateCategory+",\n"+
                "}";
    }

    public String deleteCategoryById(ObjectId id) throws CategoryException {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new CategoryException("Category NOT found");
        }
        categoryRepository.deleteById(id);
        return "{"+
                "\"message: \":"+"Successfully updated category\",\n"+
                "\"data\":"+id+",\n"+
                "}";
    }
}
