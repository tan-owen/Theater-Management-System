package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for secure password handling using SHA-256 hashing with salt.
 * This implements industry-standard practices to prevent password cracking attacks.
 * 
 * How it works:
 * 1. Generate a random salt (noise) for each password
 * 2. Combine password + salt and apply SHA-256 hashing multiple times
 * 3. Store both hash and salt in "hash:salt" format in the database
 * 4. When verifying, repeat the hash with the stored salt and compare results
 * 
 * This approach prevents rainbow table attacks because each password has a unique salt.
 */
public class PasswordHasher {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;  // 16 bytes = 128 bits of randomness
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERATIONS = 1000;  // Hash the password 1000 times for extra security

    /**
     * Generates a random salt (random data added to password before hashing).
     * Each salt is unique, ensuring identical passwords produce different hashes.
     * 
     * @return Base64-encoded salt string (base64 makes it safe to store in text files)
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);  // Fill array with random bytes
        return Base64.getEncoder().encodeToString(salt);  // Encode to text format
    }

    /**
     * Hashes a password combined with its salt using SHA-256.
     * The password is hashed 1000 times (iterations) to slow down brute-force attacks.
     * 
     * Process:
     * 1. Append salt to password (saltedPassword = password + salt)
     * 2. Apply SHA-256 hash algorithm
     * 3. Take the output and hash it again, repeat 1000 times
     * 4. Return the final result encoded in Base64 (text format)
     * 
     * @param password The original plaintext password
     * @param salt The unique salt for this password
     * @return The hashed result in Base64 format
     */
    public static String hashPassword(String password, String salt) {
        try {
            String saltedPassword = password + salt;
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            
            // Hash the result multiple times to make brute-force attacks slower
            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hashedBytes = md.digest(hashedBytes);
            }
            
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Checks if a plaintext password matches a previously hashed and salted password.
     * This is used during login to verify user credentials.
     * 
     * Method: Hash the input password with the stored salt and compare the results.
     * 
     * @param password The plaintext password to verify
     * @param hash The stored hashed password
     * @param salt The stored salt for this password
     * @return true if the password is correct, false otherwise
     */
    public static boolean verifyPassword(String password, String hash, String salt) {
        String hashedInput = hashPassword(password, salt);
        return hashedInput.equals(hash);
    }

    /**
     * Generates a complete hashed password entry for a new user.
     * This combines salt generation and hashing into one convenient method.
     * Used during user registration to securely store the new password.
     * 
     * @param password The plaintext password from user registration
     * @return String in format "hash:salt" ready to store in database
     */
    public static String createHashWithSalt(String password) {
        String salt = generateSalt();
        String hash = hashPassword(password, salt);
        return hash + ":" + salt;
    }

    /**
     * Extracts the hash portion from a combined "hash:salt" string.
     * Since passwords are stored as "hash:salt", we need to split them to get individual parts.
     * 
     * @param combined The combined hash:salt string from database
     * @return Just the hash part (before the colon)
     */
    public static String extractHash(String combined) {
        return combined.split(":")[0];
    }

    /**
     * Extracts the salt portion from a combined "hash:salt" string.
     * The salt is needed to re-hash the user's password during login verification.
     * 
     * @param combined The combined hash:salt string from database
     * @return Just the salt part (after the colon)
     */
    public static String extractSalt(String combined) {
        return combined.split(":")[1];
    }

    /**
     * Verifies a password against combined hash:salt format.
     * All passwords must be stored in the format "hash:salt".
     * 
     * @param password The plaintext password to verify
     * @param combined The combined hash:salt string
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPasswordWithCombined(String password, String combined) {
        // Validate that combined hash has the correct format
        if (combined == null || combined.isEmpty() || !combined.contains(":")) {
            return false;
        }
        
        String hash = extractHash(combined);
        String salt = extractSalt(combined);
        return verifyPassword(password, hash, salt);
    }
}
