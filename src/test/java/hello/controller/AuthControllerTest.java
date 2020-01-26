package hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc;

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(authenticationManager, userService)).build();
        /**
         * 使用 @BeforeEach 注解实际调用情况如下
         *     AuthControllerTest testInstance1 = new AuthControllerTest();
         *     testInstance1.setUp();
         *     testInstance1.test1();
         *
         *     AuthControllerTest testInstance2 = new AuthControllerTest();
         *     testInstance2.setUp();
         *     testInstance2.test2();
         *  避免状态共享
         *
         */
    }

    @Test
    public void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户没有登录")));
    }

    @Test
    void testLogin() throws Exception {
        // 未登录
        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户没有登录")));

        // 登录
        Map<String, String> usernamePassword = new HashMap<>();
        usernamePassword.put("username", "张三");
        usernamePassword.put("password", "123");

        Mockito.when(userService.loadUserByUsername("张三"))
                .thenReturn(new User("张三", bCryptPasswordEncoder.encode("123"), Collections.emptyList()));
        Mockito.when(userService.getUserByUsername("张三"))
                .thenReturn(new hello.entity.User(123,"张三", bCryptPasswordEncoder.encode("123")));

        MvcResult response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(usernamePassword)))
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("登录成功"));
                    }
                })
                .andReturn();

        HttpSession session = response.getRequest().getSession();

        mvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("张三")));
    }

    @Test
    void auth() {

    }

    @Test
    void register() {
    }

    @Test
    void logout() {
    }


}