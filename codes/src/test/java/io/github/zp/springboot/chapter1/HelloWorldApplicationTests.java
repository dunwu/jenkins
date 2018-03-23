package io.github.zp.springboot.chapter1;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.github.zp.springboot.chapter1.web.HelloController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldApplicationTests {

    private MockMvc mock;

    @Before
    public void setUp() throws Exception {
        mock = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    public void visitHello() throws Exception {
        mock.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello World")));
    }

}
