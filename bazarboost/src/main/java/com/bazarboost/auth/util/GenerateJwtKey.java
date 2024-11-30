package com.bazarboost.auth.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;

import javax.crypto.SecretKey;

public class GenerateJwtKey {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Llave secreta JWT: " + secretString);
    }
}