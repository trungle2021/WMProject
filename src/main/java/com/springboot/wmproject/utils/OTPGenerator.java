package com.springboot.wmproject.utils;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OTPGenerator {
    public static String generateOTP() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] randomBytes = new byte[4];
            sr.nextBytes(randomBytes);
            int otpValue = Math.abs(ByteBuffer.wrap(randomBytes).getInt()) % 1000000;
            return String.format("%06d", otpValue);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate OTP", e);
        }
    }
}
