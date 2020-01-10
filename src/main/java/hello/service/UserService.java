package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
// 以下两个注解用来实现bean的声明。
@Component
@Service
public class UserService {
    private UserMapper userMapper;

    @Inject
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserById(Integer id){
        return  userMapper.findUserById(id);
    }
}
