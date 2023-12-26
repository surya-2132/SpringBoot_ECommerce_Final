package com.fullstackproject.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstackproject.ecommerce.EcommerceApplicationTests;
import com.fullstackproject.ecommerce.Model.Product;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
    public void verifyGetProductById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/products/get-productby-id/65843104465bd3596af87af9")//diff object id
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("65843104465bd3596af87af9"))
                .andDo(print());
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
        Product product = new Product(new ObjectId("65843104465bd3596af87af9"), "test", "testing",100);

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/update")
                        .content(asJson(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value("65843104465bd3596af87af9"))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.description").value("testing"))
                .andExpect(jsonPath("$.price").value(100))
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
    public void verifyDeleteById() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/delete/65843104465bd3596af87af9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Successfully Deleted !!"))
                .andDo(print());
    }

    @Test
    public void verifyDeleteById_EXCEPTION() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/delete/65843104465bd3596af87af9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.message").value("Product DOESN'T Exists"))
                .andDo(print());
    }
}
