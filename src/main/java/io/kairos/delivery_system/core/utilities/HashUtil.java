package io.kairos.delivery_system.core.utilities;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class HashUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int ITERATIONS = 600_000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    private HashUtil() {
        // Utility class
    }

    public static String make(String password) {

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);

        byte[] hash = derive(password, salt, ITERATIONS);

        return "$pbkdf2-sha256$"
                + ITERATIONS
                + "$"
                + Base64.getEncoder().encodeToString(salt)
                + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean check(String password, String storedHash) {

        String[] parts = storedHash.split("\\$");

        if (parts.length != 5) {
            return false;
        }

        int iterations = Integer.parseInt(parts[2]);

        byte[] salt =
                Base64.getDecoder().decode(parts[3]);

        byte[] expectedHash =
                Base64.getDecoder().decode(parts[4]);

        byte[] actualHash =
                derive(password, salt, iterations);

        return MessageDigest.isEqual(
                expectedHash,
                actualHash
        );
    }

    private static byte[] derive(
            String password,
            byte[] salt,
            int iterations
    ) {

        try {

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    iterations,
                    KEY_LENGTH
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHM);

            return factory
                    .generateSecret(spec)
                    .getEncoded();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to hash password",
                    e
            );
        }
    }
}