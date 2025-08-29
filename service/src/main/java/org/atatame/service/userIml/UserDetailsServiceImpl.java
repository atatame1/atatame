package org.atatame.service.userIml;

import org.atatame.service.mapper.UserMapper;
import org.atatame.service.pojo.entity.user;
import org.atatame.common.enums.SecurityRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String,Object>redisTemplate;

    private static final String USER_CACHE_PREFIX = "user:details:";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String cacheKy=USER_CACHE_PREFIX+username;
        UserDetails cacheUserDetails=(UserDetails)redisTemplate.opsForValue().get(cacheKy);
        if(cacheUserDetails!=null){
            return cacheUserDetails;
        }

        return loadUserByUsernameFromDB(username);
    }

    public UserDetails loadUserByUsernameFromDB(String username){
        // 根据用户名查找用户
        user user = userMapper.selectByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
//        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
//                .username(user.getName())
//                .password(user.getPassword())
//                .authorities(SecurityRoleEnum.USER)
//                .disabled(!user.getEnable())
//                .build();

        UserDetails userDetails = new UserDetailsIml(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getIsEnabled(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(SecurityRoleEnum.USER))
        );
        // 存入Redis缓存
        String cacheKey = USER_CACHE_PREFIX + username;
        redisTemplate.opsForValue().set(cacheKey, userDetails, 30, TimeUnit.MINUTES);
        return userDetails;
    }
}
