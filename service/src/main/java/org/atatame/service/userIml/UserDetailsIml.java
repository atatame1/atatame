package org.atatame.service.userIml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Getter
@JsonDeserialize(using = UserDetailsIml.UserDetailsImlDeserializer.class)
public class UserDetailsIml extends User {
    private final Long id;

    public UserDetailsIml(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public UserDetailsIml(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    // 用于JSON序列化的构造函数
    public UserDetailsIml() {
        super("", "", true, true, true, true, Collections.emptyList());
        this.id = 0L;
    }

    public static class UserDetailsImlDeserializer extends JsonDeserializer<UserDetailsIml> {
        @Override
        public UserDetailsIml deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);

            Long id = node.get("id").asLong();
            String username = node.get("username").asText();
            String password = node.get("password").asText();

            boolean enabled = node.get("enabled").asBoolean(true);
            boolean accountNonExpired = node.get("accountNonExpired").asBoolean(true);
            boolean credentialsNonExpired = node.get("credentialsNonExpired").asBoolean(true);
            boolean accountNonLocked = node.get("accountNonLocked").asBoolean(true);

            Collection<GrantedAuthority> authorities = new ArrayList<>();
            if (node.has("authorities")) {
                for (JsonNode authNode : node.get("authorities")) {
                    String authority = authNode.get("authority").asText();
                    authorities.add(new SimpleGrantedAuthority(authority));
                }
            }

            return new UserDetailsIml(
                    id,
                    username,
                    password,
                    enabled,
                    accountNonExpired,
                    credentialsNonExpired,
                    accountNonLocked,
                    authorities
            );
        }
    }

}
