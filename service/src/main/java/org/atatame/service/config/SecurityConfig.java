package org.atatame.service.config;

import org.atatame.common.result.Result;
import org.atatame.service.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/*
spring security拦截器的流程
客户端请求
    ↓
SecurityContextPersistenceFilter
会尝试根据cookieId获取session并存入上下文若无则先放行 在finally部分会存一次当前上下文到session中
    ↓
HeaderWriterFilter (添加安全头部)
    ↓
JwtAuthenticationFilter (验证JWT Token)
    ↓
LogoutFilter (检查是否为注销请求)
    ↓
RequestCacheAwareFilter (处理请求缓存)
    ↓
SecurityContextHolderAwareRequestFilter (包装请求)
    ↓
AnonymousAuthenticationFilter (如果没有认证则设为匿名用户)
    ↓
SessionManagementFilter (会话管理)
    ↓
ExceptionTranslationFilter (异常转换)
    ↓
FilterSecurityInterceptor (最终授权决策)
会判断请求是否认证（是否携带合法认证信息），及是否有访问对应控制器的权限
    ↓
MessagesController (执行控制器方法)
*/
@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();//管理器内部包含所有的认证提供者实例
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //跨域请求保护开关，即服务端给客户端一个token，每次请求时携带以认证，防止恶意网站盗用cookie
            .csrf(AbstractHttpConfigurer::disable)
                //设置会话创建为无状态
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 公开接口，无需认证
                .requestMatchers( "/api/authenticate/**", "/webjars/**","/doc.html", "/v3/api-docs/**").permitAll()
                    // WebSocket连接端点
                .requestMatchers("/chat/**").permitAll()
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
//                通过配置实现login接口 会默认调用UsernamePasswordAuthenticationFilter完成认证
//            .formLogin(form->form.
//                    loginProcessingUrl("/api/login")
//                    .successHandler((request, response, authentication) -> {
//                        response.setContentType("application/json;charset=UTF-8");
//                        response.getWriter().write(Result.ok().toString());
//                    })
//            )
            .httpBasic(AbstractHttpConfigurer::disable);

//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("https://*.example.com"));//指定请求来源
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));//指定请求方法
        configuration.setAllowedHeaders(List.of("*"));//允许哪些认证信息
        configuration.setAllowCredentials(true);//是否允许携带认证信息

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("*", configuration);//表示对何种控制器路径应用该cors配置
        return source;
    }
}
