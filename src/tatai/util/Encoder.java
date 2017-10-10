package tatai.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Encoder {
    private static final byte KEY = 13;
    public static void encode(File file) {
        try {
            Path path = file.toPath();
            byte[] data = Files.readAllBytes(path);
            for (int i = 0; i < data.length; ++i) {
                data[i] ^= KEY;
            }
            Files.write(path, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
