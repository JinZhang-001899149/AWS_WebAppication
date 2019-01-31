package com.cloud.assignment.assignment01;

import assignment_01.UserService;
import assignment_01.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.core.JsonProcessingException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = UserService.class, secure = false)
@SpringBootTest
public class Assignment01ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;



    @Test
    public void register() throws Exception{
            User mockUser = new User();
            mockUser.setEmail("xinxingmail.com");
            mockUser.setPassword("xXwooop796");

            String exampleUserJson = this.mapToJson(mockUser);

     /*   Mockito.when(
                userService.register(Mockito.any(User.class))).thenReturn(mockUser.getEmail());*/
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/api/user/register")
                    .accept(MediaType.APPLICATION_JSON).content(exampleUserJson)
                    .contentType(MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            MockHttpServletResponse response = result.getResponse();

            String expectedResult = "{\"Sucessfully Registered\"}";
            assertThat(expectedResult).isEqualTo(result);

    }

    private String mapToJson(Object object) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}

