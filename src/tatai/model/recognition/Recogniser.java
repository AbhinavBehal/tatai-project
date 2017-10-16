package tatai.model.recognition;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import tatai.util.Promise;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Recogniser {

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

    private static void onRecognitionFinished(WorkerStateEvent e, Promise<String> promise) {
        if (e.getSource().getException() != null) {
            promise.reject(e.getSource().getException());
        } else {
            promise.resolve((String) e.getSource().getValue());
        }
    }

    private static class RecogniserTask extends Task<String> {
        private File _audioFile;

        RecogniserTask(File audioFile) {
            _audioFile = audioFile;
        }

        @Override
        protected String call() throws Exception {
            // Might need to change the path to the HMMs if we're packaging it in the jar
            String cmd = "HVite -H /HMMs/hmm15/macros -H /HMMs/hmm15/hmmdefs" +
                    " -C /user/configLR -w /user/wordNetworkNum -o SWT -l '*' -i out.mlf" +
                    " -p 0.0 -s 5.0 /user/dictionaryD /user/tiedList " +
                    _audioFile.getName() +
                    "; cat out.mlf";

            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            Process process = builder.start();
            process.waitFor();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (!line.equals("sil")) continue;
                    while (!(line = reader.readLine()).equals("sil")) {
                        output.append(line).append(" ");
                    }
                }
                return output.toString().trim().replaceAll("aa", "\u0101");
            }
        }
    }
}
