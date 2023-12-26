package com.fullstackproject.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstackproject.ecommerce.EcommerceApplication;
import com.fullstackproject.ecommerce.Exception.IdException;
import com.fullstackproject.ecommerce.Model.Category;
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
@ContextConfiguration(classes = EcommerceApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext categoryContext;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(categoryContext).build();
    }

    //658437c389407d3fe6f26c28
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
    public void verifyGetCategoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/get-category/658437c389407d3fe6f26c28")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("658437c389407d3fe6f26c28"))
                .andDo(print());
    }

    @Test
    public void verfiySaveCategory_EXCEPTION() throws Exception, IdException {
        Category category = new Category(new ObjectId("658437c389407d3fe6f26c28"), "category1", "testing");

        mockMvc.perform(MockMvcRequestBuilders.post("/category/add-category")
                        .content(asJson(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
//                .andExpect(jsonPath("$.message").value("Category did not created"))
//                .andExpect(jsonPath("$.errorCode").value(400))
                .andDo(print());
    }

    @Test
    public void verifyUpdateCategory() throws Exception{
        Category category = new Category(new ObjectId("6584380f3c44b0778fc13e9c"), "category2", "testing");

        mockMvc.perform(MockMvcRequestBuilders.post("/category/update")
                        .content(asJson(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value("6584380f3c44b0778fc13e9c"))
                .andExpect(jsonPath("$.name").value("category2"))
                .andExpect(jsonPath("$.description").value("testing"))
                .andDo(print());
    }


}
