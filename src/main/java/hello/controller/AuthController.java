package hello.controller;

import hello.entity.User;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserService userService;

    @Inject
    public AuthController( AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

//    @RequestMapping("/")
//    @ResponseBody
//    public User index(@RequestParam("id") Integer id) {
////        return this.userService.getUserById(id);
//    }

    @GetMapping("/auth")
    @ResponseBody // 解决Spring MVC的遗留问题。这个注释指明方法的返回值应该被限定在web的响应体中。
    public Object auth() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser =  userService.getUserByUsername(userName);

        if(loggedInUser == null){
            return new Result("ok", "用户未登录", false);
        } else {
            return new Result("ok", null, true, loggedInUser);
        }

    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson){
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails;
        try{
             userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e){
            return new Result("fail","用户不存在", false);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);

            return new Result("ok", "登录成功", true, userService.getUserByUsername(username));
        } catch (BadCredentialsException e){
            return new Result("fail", "密码不正确", false);
        }
    }

    // JSON序列化的结果取决于getter方法,和字段无关.
    private static class Result {
        String status;
        String message;
        boolean isLogin;
        Object data;

        public Object getData() {
            return data;
        }

        public Result(String status, String message, boolean isLogin) {
            this(status, message, isLogin, null);
        }

        public Result(String status, String message, boolean isLogin, Object data) {
            this.status = status;
            this.message = message;
            this.isLogin = isLogin;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public boolean isLogin() {
            return isLogin;
        }

    }
}
