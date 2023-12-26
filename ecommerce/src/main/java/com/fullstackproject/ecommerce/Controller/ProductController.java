package com.fullstackproject.ecommerce.Controller;

import com.fullstackproject.ecommerce.Exception.CategoryException;
import com.fullstackproject.ecommerce.Exception.IdException;
import com.fullstackproject.ecommerce.Exception.ProductException;
import com.fullstackproject.ecommerce.Model.PayLoadValidation;
import com.fullstackproject.ecommerce.Model.Product;
import com.fullstackproject.ecommerce.Service.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) throws IdException {
        if(!PayLoadValidation.payLoadProductValidation(product)) {
            throw new IdException("ObjectId Can't detected, Product can't create");
        }
        return productService.createProduct(product);
    }

    @GetMapping("/get-productby-id/{id}")
    public Product getProductById(@PathVariable("id") String id) throws ProductException{
        return productService.getProductById(new ObjectId(id));
    }

    @PostMapping("/get")
    public List<Product> getProductByField(@RequestBody Map<String, ObjectId> map) throws ProductException{
        return productService.getProductByField(map.get("field").toString(),map.get("value").toString());
    }

    @GetMapping("/get-allproducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/update/{id}") //@PutMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") String id,@RequestBody Product product) throws ProductException{
        return productService.updateProduct(id, product);
    }

    @PostMapping("/product-with-category")
    public String linkProdWithCategory(@RequestBody Map<String,Object> map) throws CategoryException, ProductException {
        return productService.linkProductWithCategory(map.get("ProductId").toString(),map.get("CategoryId").toString());
    }

    @PostMapping("/delete/{id}") //@DeleteMapping("/delete/{id}")
    public String deleteProductById(@PathVariable("id") ObjectId id){
        return productService.deleteProductById(id);
    }
}
