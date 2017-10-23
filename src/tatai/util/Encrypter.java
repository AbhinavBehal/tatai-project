package tatai.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class that provides static methods to encrypt and decrypt files.
 * Calling the decrypt method on a file that was not encrypted using this class will have undefined consequences,
 * and may result in the loss of data.
 */
public class Encrypter {
    private static final byte KEY = 33;

    /**
     * Encrypt the provided file.
     * @param file the file to encrypt.
     */
    public static void encrypt(File file) {
        try {
            Path path = file.toPath();
            byte[] decrypted = Files.readAllBytes(path);
            byte[] encrypted = new byte[decrypted.length * 2];
            for (int i = 0; i < decrypted.length; i++) {
                encrypted[2*i] = (byte) (decrypted[i] & KEY);
                encrypted[2*i + 1] = (byte) (decrypted[i] & ~KEY);
            }
            Files.write(path, encrypted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Decrypt the provided file.
     * @param file the file to decrypt.
     */
    public static void decrypt(File file) {
        try {
            Path path = file.toPath();
            byte[] encrypted = Files.readAllBytes(path);
            byte[] decrypted = new byte[encrypted.length / 2];
            for (int i = 0; i < decrypted.length; ++i) {
                decrypted[i] = (byte) (encrypted[2*i] | encrypted[2*i + 1]);
            }
            Files.write(path, decrypted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
