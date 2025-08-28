package org.atatame.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_KEY = "your-256-bit-secret-key-your-256-bit-secret-key";

    private static final Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private static final int expireTime = 30;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * 生成 JWT Token
     * @param claim 自定义声明（如用户ID、角色等）
     * @return JWT Token 字符串
     */
    static public String generateToken(Map<String, Object>claim){//生成令牌
        return Jwts.builder()
                .setIssuer("atatame")
                .setClaims(claim)
                .signWith(secretKey)
                .setExpiration(new Date(System.currentTimeMillis()+expireTime*MILLIS_MINUTE))
                .compact();
    }


    static public Claims parseToken(String token){//获得令牌的内容
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public static <T> T getParam(String paramName, String token, Class<T> clazz) {
        Claims claims = parseToken(token);
        return claims.get(paramName, clazz);
    }


    /**
     * 验证 Token 是否有效（未过期且签名正确）
     * @param token JWT Token
     * @return true=有效，false=无效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            System.err.println("Token 已过期");
            return false;
        } catch (Exception e) {
            System.err.println("无效的 Token: " +token+": "+ e.getMessage());
            return false;
        }
    }

}
