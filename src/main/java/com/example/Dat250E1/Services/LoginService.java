package com.example.Dat250E1.Services;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import io.valkey.Jedis;
import io.valkey.JedisPool;

import java.util.Objects;
import java.util.Set;

@Service
public class LoginService {
    private static final String key = "logged_in_users";
    private final io.valkey.JedisPool jedisPool;
    private final io.valkey.Jedis jedis;

    public LoginService() {
        this.jedisPool = new io.valkey.JedisPool("localhost", 6379);
        this.jedis = jedisPool.getResource();
    }

    public void userLogin(String username) {
        jedis.set(key, username);
    }

    public void userLogout() {
        jedis.set(key, "nil");
    }

    public boolean isUserLoggedIn() {
        return !Objects.equals(jedis.get(key), "nil");
    }

    @PreDestroy
    public void cleanup() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
