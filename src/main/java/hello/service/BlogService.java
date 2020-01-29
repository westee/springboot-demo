package hello.service;

import hello.dao.BLogDAO;
import hello.entity.Blog;
import hello.entity.BlogResult;
import hello.entity.Result;
import hello.entity.User;
import org.mockito.InjectMocks;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private BLogDAO bLogDAO;
    private UserService userService;
    @Inject
    public BlogService(BLogDAO bLogDAO, UserService userService) {
        this.bLogDAO = bLogDAO;
        this.userService = userService;
    }

    public Result getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            List<Blog> blogs = bLogDAO.getBlogs(page, pageSize, userId);

            blogs.forEach(blog -> {
                blog.setUser(userService.getUserById(blog.getUserId()));
            });

            int count = bLogDAO.count(userId);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogResult.newResults(blogs, count, page, pageCount);
        } catch (Exception e) {
            return BlogResult.fail("系统异常");
        }

    }
}
