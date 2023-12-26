package com.fullstackproject.ecommerce.Service;

import com.fullstackproject.ecommerce.Exception.ProductException;
import com.fullstackproject.ecommerce.Model.Product;
import com.fullstackproject.ecommerce.Repository.CategoryRepository;
import com.fullstackproject.ecommerce.Repository.ProductRepository;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepo;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        productList.add(new Product(new ObjectId("658035a8f431076e8e80b08a"), "Heritage", "milk", 20));
        productList.add(new Product(new ObjectId("6580241b9c0efe21967a4917"), "Ariel", "detergent", 30));
        productList.add(new Product(new ObjectId("6580189a55cec04f3cdf3d4d"), "Vim", "dishwasher", 40));

        //mocks
        when(productRepo.findAll()).thenReturn(productList);
        List<Product> productResult = productService.getAllProducts();

        assertEquals(3, productResult.size());
    }

    @Test
    public void getProductById() throws ProductException {
        List<Product> productList = new ArrayList<Product>();
        productList.add(new Product(new ObjectId("65841d8fc5c08493065f9217"), "Heritage", "milk", 20));
        productList.add(new Product(new ObjectId("65841e3bc5c08493065f9218"), "Ariel", "detergent", 30));
        productList.add(new Product(new ObjectId("65841e77c5c08493065f9219"), "Vim", "dishwasher", 40));

        ObjectId id = new ObjectId("65841d8fc5c08493065f9217");

        //mocks
        when(productRepo.findById(id)).thenReturn(Optional.of(productList.get(0)));
        Product productResult = productService.getProductById(id);

        // assertions
        assertEquals(id, productResult.getId());
        assertEquals("Heritage", productResult.getName());
        assertEquals("milk", productResult.getDescription());
        assertEquals(20, productResult.getPrice());
    }

    @Test
    public void saveProduct() {

        Product product = new Product(new ObjectId("6580189a55cec04f3cdf3d4d"), "Perk", "chocolate", 20);

        when(productRepo.save(product)).thenReturn(product);

        Product savedProduct = productService.createProduct(product);

        assertEquals(new ObjectId("6580189a55cec04f3cdf3d4d"), savedProduct.getId());
        assertEquals("Perk", savedProduct.getName());
        assertEquals("chocolate", savedProduct.getDescription());
        assertEquals(20, savedProduct.getPrice());
    }


    @Test
    public void deleteProductById() {

        Product product = new Product(new ObjectId("658016ed4df7bb26e3dc43a3"), "Lays", "snack", 10);
        when(productRepo.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        productService.deleteProductById(product.getId());
        verify(productRepo, times(1)).deleteById(product.getId());
    }
}
