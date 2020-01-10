package hello.controller;

import hello.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody // 解决Spring MVC的遗留问题。这个注释指明方法的返回值应该被限定在web的响应体中。
    public Object auth() {
        return new Result("ok", "登录成功", true);
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson){
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails = null;
        try{
             userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e){
            return new Result("fail","用户不存在", false);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);

            User loggedInUser = new User(1, "里斯");
            return new Result("ok", "登录成功", true, loggedInUser);
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
