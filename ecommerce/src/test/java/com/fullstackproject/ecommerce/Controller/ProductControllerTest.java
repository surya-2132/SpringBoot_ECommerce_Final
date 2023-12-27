package com.fullstackproject.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstackproject.ecommerce.EcommerceApplicationTests;
import com.fullstackproject.ecommerce.Exception.IdException;
import com.fullstackproject.ecommerce.Exception.ProductException;
import com.fullstackproject.ecommerce.Model.PayLoadValidation;
import com.fullstackproject.ecommerce.Model.Product;
import com.fullstackproject.ecommerce.Service.ProductService;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EcommerceApplicationTests.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductController productController;
    @Autowired
    private WebApplicationContext productContext;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(productContext).build();
    }

    public static String asJson(final Object object) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonContent = objectMapper.writeValueAsString(object);
            System.out.println(jsonContent);
            return jsonContent;
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void getProductById_Success() throws ProductException {
        String productId = "658ad8f95bc2e20faf08762e";

        // Mocking the behavior of productService.getProductById
        Product mockProduct = new Product();
        Mockito.when(productService.getProductById(new ObjectId(productId))).thenReturn(mockProduct);

        // Call the method to be tested
        Product result = productController.getProductById(productId);

        // Assertions
        Assert.assertEquals(mockProduct, result);
    }

    @Test
    public void getProductById_ProductNotFound() throws ProductException {
        String productId = "658ad8f95bc2e20faf08762e";

        // Mocking the behavior to throw ProductException when productService.getProductById is called
        Mockito.when(productService.getProductById(new ObjectId(productId)))
                .thenThrow(new ProductException("Product not found"));

        // Assertions
        Assert.assertThrows(ProductException.class, () -> productController.getProductById(productId));
    }



    @Test
    public void verfiySaveProduct_EXCEPTION() throws Exception{
        Product product = new Product(new ObjectId("65843104465bd3596af87af9"), "test", "testing",100);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/create")
                        .content(asJson(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.message").value("PAYLOAD MALFORMED. OBJECT ID MUST NOT BE DEFINED"))
                .andDo(print());
    }



    @Test
    public void verifyUpdateProduct() throws Exception{
        Product product = new Product(new ObjectId("658ad8935bc2e20faf08762b"), "iphone", "premium",120000);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/update/658ad8935bc2e20faf08762b")
                        .content(asJson(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.id").value("658ad8935bc2e20faf08762b"))
//                .andExpect(jsonPath("$.name").value("test"))
//                .andExpect(jsonPath("$.description").value("testing"))
//                .andExpect(jsonPath("$.price").value(100))
                .andDo(print());
    }

    @Test
    public void verifyUpdateProduct_EXCEPTION() throws Exception{
        Product product = new Product(new ObjectId("65843104465bd3596af87af9"), "test", "testing",20);

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/update")
                        .content(asJson(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("Product DOESN'T EXISTS"))
                .andDo(print());
    }



    @Test
    public void deleteProductById_Success() {
        // Input ObjectId
        ObjectId inputId = new ObjectId("658ad8f95bc2e20faf08762e");

        // Mocking the behavior of productService.deleteProductById
        Mockito.when(productService.deleteProductById(inputId)).thenReturn("Successfully deleted product");

        // Call the method to be tested
        String result = productController.deleteProductById(inputId);

        // Assertions
        Assert.assertEquals("Successfully deleted product", result);
    }

    @Test
    public void deleteProductById_ProductNotFound() {
        // Input ObjectId
        ObjectId inputId = new ObjectId("658ad8f95bc2e20faf08762e");

        // Mocking the behavior to throw RuntimeException when productService.deleteProductById is called
        Mockito.when(productService.deleteProductById(inputId)).thenThrow(new RuntimeException("Product id " + inputId + " doesn't exist"));

        // Assertions
        RuntimeException exception = Assert.assertThrows(RuntimeException.class, () -> productController.deleteProductById(inputId));
        Assert.assertEquals("Product id " + inputId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void createProduct_Success() throws IdException {
        Product product = new Product();
        ObjectId objectId = new ObjectId("658ad8f95bc2e20faf08762e");
        product.setId(objectId);
        // Mocking payload validation to return true
        PayLoadValidation mock = org.mockito.Mockito.mock(PayLoadValidation.class);
        Mockito.when(mock.payLoadProductValidation(product)).thenReturn(true);

        // Mocking productService.createProduct
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(new Product());

        // Call the method to be tested
        Product result = productController.createProduct(new Product());

        // Assertions
        Assert.assertNotNull(result);
    }

    @Test
    public void createProduct_Failure_IdException() {
        // Mocking payload validation to return false
        Mockito.when(PayLoadValidation.payLoadProductValidation(Mockito.any())).thenReturn(false);

        // Assertions
        IdException exception = Assert.assertThrows(IdException.class, () -> productController.createProduct(new Product()));
        Assert.assertEquals("ObjectId Can't detected, Product can't create", exception.getMessage());
    }

    @Test
    public void payLoadProductValidation_True() {
        // Call the method to be tested
        boolean result = PayLoadValidation.payLoadProductValidation(new Product());

        // Assertions
        Assert.assertTrue(result);
    }
    @Test
    public void payLoadProductValidation_False() {
        // Setting an ObjectId to trigger validation failure
        Product product = new Product();
        ObjectId objectId = new ObjectId("658ad8f95bc2e20faf08762e");
        product.setId(objectId);

        // Call the method to be tested
        boolean result = PayLoadValidation.payLoadProductValidation(product);

        // Assertions
        Assert.assertFalse(result);
    }
//
//    @Test
//    public void verifyDeleteById() throws Exception{
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/products/delete/65843104465bd3596af87af9")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("Successfully Deleted !!"))
//                .andDo(print());
//    }
//
//    @Test
//    public void verifyDeleteById_EXCEPTION() throws Exception{
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/products/delete/65843104465bd3596af87af9")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.errorCode").value(404))
//                .andExpect(jsonPath("$.message").value("Product DOESN'T Exists"))
//                .andDo(print());
//    }
}
