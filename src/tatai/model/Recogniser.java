package tatai.model;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import tatai.util.Promise;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Recogniser {

    public static Promise<String> recognise(File audioFile) {
        Promise<String> promise = new Promise<>();
        RecogniserTask task = new RecogniserTask(audioFile, promise);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return promise;
    }

    private static class RecogniserTask extends Task<Void> {
        private File _audioFile;
        private Promise<String> _promise;

        RecogniserTask(File audioFile, Promise<String> promise) {
            _audioFile = audioFile;
            _promise = promise;
        }

        @Override
        protected Void call() throws Exception {
            // Might need to change the path to the HMMs if we're packaging it in the jar
            String cmd = "HVite -H ~/Documents/HTK/MaoriNumbers/HMMs/hmm15/macros -H ~/Documents/HTK/MaoriNumbers/HMMs/hmm15/hmmdefs" +
                    " -C ~/Documents/HTK/MaoriNumbers/user/configLR -w ~/Documents/HTK/MaoriNumbers/user/wordNetworkNum -o SWT -l '*' -i out.mlf" +
                    " -p 0.0 -s 5.0 ~/Documents/HTK/MaoriNumbers/user/dictionaryD ~/Documents/HTK/MaoriNumbers/user/tiedList " +
                    _audioFile.getName() +
                    "; cat out.mlf";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            try {
                Process process = builder.start();
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (!line.equals("sil")) continue;
                    while (!(line = reader.readLine()).equals("sil")) {
                        output.append(line).append(" ");
                    }
                }
                _promise.resolve(output.toString().trim().replaceAll("aa", "\u0101"));
            } catch (IOException | InterruptedException e) {
                _promise.reject(e);
            }
            return null;
        }
    }
}
