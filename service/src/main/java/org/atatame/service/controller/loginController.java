package org.atatame.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.atatame.common.enums.ResultEnum;
import org.atatame.common.result.Result;
import org.atatame.service.pojo.request.LoginRequest;
import org.atatame.service.pojo.vo.UserVo;
import org.atatame.service.service.UserService;
import org.atatame.service.userIml.UserDetailsIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录相关操作")
@RestController
@RequestMapping("/api/authenticate")
public class loginController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<Object> login(@RequestBody LoginRequest loginVo, HttpServletRequest request, HttpServletResponse response){
        try{
            UsernamePasswordAuthenticationToken authToken=
                    new UsernamePasswordAuthenticationToken(
                            loginVo.getUsername(),
                            loginVo.getPassword()
                    );

            //authenticationManager 会寻找到合适的认证器调用认证函数authenticate（因为根据需求不同可能需要不同的认证方式）
            Authentication authentication=authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 手动创建session并保存安全上下文
//            HttpSession session = request.getSession(true);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            UserDetailsIml user=(UserDetailsIml)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return Result.ok(new UserVo(user.getId(), user.getUsername()));
        }catch (BadCredentialsException e){
            return Result.error(ResultEnum.ACCOUNT_PASSWORD_ERROR);
        }catch (DisabledException e){
            return Result.error(ResultEnum.USER_IS_BANED);
        }catch (Exception e){
            System.err.println(e);
            return Result.error(e);
        }
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public Result<Object> register(@RequestBody LoginRequest registerVo){
        if(userService.isUserNameExist(registerVo.getUsername())){
            return Result.error(ResultEnum.USER_EXISTED);
        }
        if(userService.register(registerVo.getUsername(),registerVo.getPassword())){
            return Result.ok();
        }
        return Result.error();
    }
}
