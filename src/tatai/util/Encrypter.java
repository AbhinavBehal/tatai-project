package tatai.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Encrypter {
    private static final byte KEY = 33;

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
