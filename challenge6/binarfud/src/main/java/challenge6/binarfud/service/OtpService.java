package challenge6.binarfud.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import challenge6.binarfud.security.jwt.JwtUtils;


@Service
public class OtpService {
    private static final Integer EXPIRE_MINS = 5;
    LoadingCache<String, String> cache;

    @Autowired
    JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    public OtpService() {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(loader);
    }

    public String generateOTP(String username) {
        Random random = new Random();
        Integer otp = 100000 + random.nextInt(900000);

        String jwtOtp = "";
        try {
            jwtOtp = jwtUtils.generateToken(username, jwtOtp);
        } catch (JacksonException e) {
            logger.error(e.getMessage(), e);
        }

        cache.getUnchecked(jwtOtp);
        return jwtOtp;
    }

    public Optional<String> getOtp(String key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    public boolean validateOtp(String otp) {
        return getOtp(otp).isPresent();
    }

    public void clearOTP(String key) {
        cache.invalidate(key);
    }
}
