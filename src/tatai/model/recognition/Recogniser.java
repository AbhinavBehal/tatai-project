package tatai.model.recognition;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import tatai.util.Promise;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Class that provides static methods to recognise speech using HTK.
 */
public class Recogniser {

    /**
     * Recognise speech using HTK from the provided audio file.
     * @param audioFile The audio file to recognise the speech from.
     * @return A promise holding the recognised speech (as a string).
     */
    public static Promise<String> recognise(File audioFile) {
        Promise<String> promise = new Promise<>();
        RecogniserTask task = new RecogniserTask(audioFile);

        task.setOnSucceeded(wse -> onRecognitionFinished(wse, promise));
        task.setOnCancelled(wse -> onRecognitionFinished(wse, promise));
        task.setOnFailed(wse -> onRecognitionFinished(wse, promise));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return promise;
    }

    /**
     * Private method used to reject/resolve the recognition promise.
     * The promise is reject if an exception was thrown during the recognition, and resolved otherwise.
     */
    private static void onRecognitionFinished(WorkerStateEvent e, Promise<String> promise) {
        if (e.getSource().getException() != null) {
            promise.reject(e.getSource().getException());
        } else {
            promise.resolve((String) e.getSource().getValue());
        }
    }

    /**
     * Private class that handles the recognition of speech.
     */
    private static class RecogniserTask extends Task<String> {
        private File _audioFile;

        RecogniserTask(File audioFile) {
            _audioFile = audioFile;
        }

        @Override
        protected String call() throws Exception {
            // Set up the HTK command and call it in a new process
            String cmd =
                    "HVite"
                    + " -H " + "MaoriNumbers/HMMs/hmm15/macros"
                    + " -H " + "MaoriNumbers/HMMs/hmm15/hmmdefs"
                    + " -C " + "MaoriNumbers/user/configLR"
                    + " -w " + "MaoriNumbers/user/wordNetworkNum"
                    + " -o SWT -l '*' -i out.mlf"
                    + " -p 0.0 -s 5.0 " + "MaoriNumbers/user/dictionaryD"
                    + " " + "MaoriNumbers/user/tiedList"
                    + " " + _audioFile.getName()
                    + "; cat out.mlf";

            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            Process process = builder.start();
            process.waitFor();

            // Read the HTK output and find the recognised speech
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (!line.equals("sil")) continue;
                    while (!(line = reader.readLine()).equals("sil")) {
                        output.append(line).append(" ");
                    }
                }
                // Return the recognised speech, with macrons in appropriate places
                return output.toString().trim().replaceAll("aa", "\u0101");
            }
        }
    }
}
