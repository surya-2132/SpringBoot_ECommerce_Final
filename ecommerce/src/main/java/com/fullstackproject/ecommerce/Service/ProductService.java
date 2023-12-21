package com.fullstackproject.ecommerce.Service;

import com.fullstackproject.ecommerce.Exception.CategoryException;
import com.fullstackproject.ecommerce.Exception.ProductException;
import com.fullstackproject.ecommerce.Model.Category;
import com.fullstackproject.ecommerce.Model.Product;
import com.fullstackproject.ecommerce.Repository.CategoryRepository;
import com.fullstackproject.ecommerce.Repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //create and add new product
    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product getProductById(ObjectId id) throws ProductException {
        Product product = (Product) productRepository.findByID(id);
        if (product.getId() ==  null){
            throw new ProductException("Product Not Found");
        }
        return product;
    }

    //get the product by any field entered by the user
    public List<Product> getProductByField(String field, String value) throws ProductException {
        List<Product> productList;
        if("id".equals(field)){
            productList = productRepository.findByID(new ObjectId(value));
        } else if ("name".equals(field)) {
            productList = productRepository.findByName(value);
        } else if ("description".equals(field)) {
            productList = productRepository.findByDesc(value);
        }else if ("price".equals(field)){
            productList = productRepository.findByPrice(Integer.parseInt(value));
        }else {
            throw new ProductException("Product Not found BY FIELD");
        }

        if (productList.isEmpty()){
            throw new ProductException("Product Not Found");
        }
        return productList;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public String updateProduct(String id, Product product) throws ProductException {
        ObjectId objectId = new ObjectId();
        Product existingProduct = (Product) productRepository.findByID(objectId);

        //if product didn't exist
        if(existingProduct.getId() == null){
            throw new ProductException("Product ID not found, couldn't update");
        }

        //if product exist
        if(product.getName() != null){
            existingProduct.setName(product.getName());
        }if(product.getDescription() != null){
            existingProduct.setDescription(product.getDescription());
        }if(product.getPrice() != 0){
            existingProduct.setPrice(product.getPrice());
        }if(product.getCategory() != null){
            existingProduct.setCategory(product.getCategory());
        }

        productRepository.save(existingProduct);
        return "{" +
                "\"message\":"+"Successfully updated product\",\n"+
                "\"data\":"+ existingProduct +",\n"+
                "}";
    }


    public String deleteProductById(ObjectId id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if(optProduct.isEmpty()) {
            throw new RuntimeException("Product id " + id + " doesn't exist");
        }
        productRepository.deleteById(id);
        return "{" +
                "\"message\":"+"Successfully deleted product\",\n"+
                "\"id\":"+id+",\n"+
                "}";
    }

    public String linkProductWithCategory(String productIdStr, String categoryIdStr) throws ProductException, CategoryException {
        Optional<Product> optProduct = productRepository.findById(new ObjectId(productIdStr));
        if(optProduct.isEmpty()) {
            throw new ProductException("Product id: " + productIdStr + "doesn't exist");
        }
        Optional<Category> optCategory = categoryRepository.findById(new ObjectId(categoryIdStr));
        if(optCategory.isEmpty()) {
            throw new CategoryException("Category id: " + categoryIdStr + "doesn't exist");
        }

        Product productRec = optProduct.get();
        productRec.setCategory(optCategory.get());
        productRepository.save(productRec);
        return "{" +
                "\"message\":"+"Successfully linked product with category id"+
                "\"data\": "+productRec+
                "}";
    }
}
