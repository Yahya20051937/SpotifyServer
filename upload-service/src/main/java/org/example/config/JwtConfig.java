package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {


    public PublicKey publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnihgSSO+S9kSuLlcQcA2yC1wVxqmXLn66IBW6HLbqLk0nYzCExvPMFvmIvwG4EHHbwAcIyS5rrDOk4Wx8CMfsEXhh9nAJ8s/DLT/63+JZZZhm7XhF/VFMfFuWjXUVcjH/W7vCYebzr+g5mKnUBiT7qWiYT3AcSoJrNaXHuqY4NpbVH6lWWNxnYxZdxzI6AtW8MaVIt8DpESVxImRbTPTzqfSfgWm4QApxXQyeiW6ox5Wun+48QdY18xB81YOr2S65t9t2eDZ631GrCbgaffAlTsXs/jtBY4gY09PbS3bPrEVet7G78SkZXRkypq7/yg//44n+Gr8o4CSZkQrONZW8QIDAQAB";
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) this.publicKey()).build();
    }


}
