package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 以下两个注解用来实现bean的声明。
//@Component // 不需要JavaConfiguration中的配置了，等价于用@Bean现实的声明。
@Service
public class UserService implements UserDetailsService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserMapper userMapper;
//    private Map<String, User> users = new ConcurrentHashMap<>(); // HashMap是线程不安全的

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
//        this.save("zhangsan", "123");
    }

    public void save(String username, String password){
        userMapper.save(username, bCryptPasswordEncoder.encode(password));
    }

    public User getUserByUsername(String username){
        return  userMapper.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username + "不存在");
        }

        //  User user = users.get(username);
//        User user = getUserByUsername(username);
        return  new org.springframework.security.core.userdetails.User(
                username, user.getEncryptedPassword() , Collections.emptyList());
    }
}
