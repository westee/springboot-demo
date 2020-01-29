package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService = new UserService(bCryptPasswordEncoder, userMapper);

    @Test
    public void testSave() {
        Mockito.when(bCryptPasswordEncoder.encode("password")).thenReturn("encodePassword");
        userService.save("user", "password");
        Mockito.verify(userMapper).save("user", "encodePassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUsername("user");

        Mockito.verify(userMapper).findUserByUsername("user");
    }

    @Test
    public void testLoadUserByUsernameFailed() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("user"));
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        Mockito.when(userMapper.findUserByUsername("user"))
                .thenReturn(new User(123, "user","encodePassword"));

        UserDetails userDetails = userService.loadUserByUsername("user");

        Assertions.assertEquals("user", userDetails.getUsername());
        Assertions.assertEquals("encodePassword", userDetails.getPassword());
    }
}