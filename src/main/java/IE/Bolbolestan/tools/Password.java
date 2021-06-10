package IE.Bolbolestan.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class Password {
    private static byte[] salt;

    static {
        try {
            Password.generateSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void generateSalt() throws NoSuchAlgorithmException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        Password.salt = salt;
    }

    public static String hash(String passwordToHash) {
        String generatedPassword = null;

        try {
            //Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static boolean verifyUserPassword(String providedPassword, String securedPassword) {
        return providedPassword.equals(securedPassword);
    }
}
