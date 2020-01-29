package hello.service;

import hello.dao.BLogDAO;
import hello.entity.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BLogDAO bLogDAO;
    @InjectMocks
    BlogService blogService;

    @Test
    public void getBlogFromDB(){
        blogService.getBlogs(1,10, null);
        verify(bLogDAO).getBlogs(1,10,null);
    }

    @Test
    public  void returnFailureWhenExceptionThrown(){
        when(bLogDAO.getBlogs(anyInt(), anyInt(), any())).thenThrow(new RuntimeException());
         Result result = blogService.getBlogs(1, 10 , null);

        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("系统异常", result.getMessage());
    }
}
