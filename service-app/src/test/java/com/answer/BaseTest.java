package com.answer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc

public abstract class BaseTest {

    @Autowired
    private MockMvc mockMvc;

    protected ResultActions checkOkResponse(String URI) throws Exception {
        return this.mockMvc.perform(get(URI))
                .andExpect(status().isOk());
    }

    protected ResultActions validateResponseData(String URI, String value) throws Exception {
        return this.mockMvc.perform(get(URI + value))
                .andExpect(content().json("{\"id\":1,\"content\":\"you asked " + value +
                        ", but our programmers still working on it\"}"));
    }
}
