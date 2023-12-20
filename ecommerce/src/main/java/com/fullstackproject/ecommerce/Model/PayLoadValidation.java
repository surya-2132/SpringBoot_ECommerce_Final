package com.fullstackproject.ecommerce.Model;

public class PayLoadValidation {

    public static boolean payLoadCategoryValidation(Category category){
        return category.getId() == null;
    }

    public static boolean payLoadProductValidation(Product product){
        return product.getId() == null;
    }
}
