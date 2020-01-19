package hello.controller;

import hello.entity.Result;
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
    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/auth")
    @ResponseBody // 解决Spring MVC的遗留问题。这个注释指明方法的返回值应该被限定在web的响应体中。
    public Object auth() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser = userService.getUserByUsername(userName);

        if (loggedInUser == null) {
            return new Result("ok", "用户未登录", false);
        } else {
            return new Result("ok", "用户已登录", true, loggedInUser);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return Result.failResult( "用户名或密码不能为空");
        }

        if (username.length() < 6 || username.length() > 15) {
            return Result.failResult("用户名不合法");
        }

        if (password.length() < 6 || password.length() > 15) {
            return Result.failResult("密码不合法");
        }

        try {
            userService.save(username, password);
        } catch (Exception e){
            e.printStackTrace();
            return  Result.failResult("用户已存在");
        }
        return new Result("ok", "注册成功", false);
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout(){
        SecurityContextHolder.clearContext();
        return Result.successResult("注销成功");
//        if(loggedInUser == null){
//            return new Result("fail","用户没有登陆",false);
//        } else {
//            return new Result("ok", "注销成功", false);
//        }
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return  Result.failResult("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);

            return new Result("ok", "登录成功", true, userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return Result.failResult("密码不正确");
        }
    }

   }
