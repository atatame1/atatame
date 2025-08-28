package org.atatame.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.atatame.common.enums.ResultEnum;
import org.atatame.common.result.Result;
import org.atatame.service.pojo.vo.LoginVo;
import org.atatame.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录相关操作")
@RestController
@RequestMapping("/api")
public class loginController {
    @Autowired
    private UserService userService;

    private final AuthenticationManager authenticationManager=new ProviderManager();

    @PostMapping("/login")
    public Result<Object> login(@RequestBody LoginVo loginVo){
        try{
            UsernamePasswordAuthenticationToken authToken=
                    new UsernamePasswordAuthenticationToken(
                            loginVo.getUsername(),
                            loginVo.getPassword()
                    );

            //authenticationManager 会寻找到合适的认证器调用认证函数authenticate（因为根据需求不同可能需要不同的认证方式）
            Authentication authentication=authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return Result.ok();
        }catch (BadCredentialsException e){
            return Result.error(ResultEnum.ACCOUNT_PASSWORD_ERROR);
        }catch (DisabledException e){
            return Result.error(ResultEnum.USER_IS_BANED);
        }
    }
}
