package tatai.model;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.media.Media;
import tatai.util.Promise;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Recorder {

    private RecordingTask _recordingTask;
    private Promise<Media> _promise;
    public Recorder() { }

    public Promise<Media> start(File output, int duration) {
        if (_recordingTask != null && _recordingTask.isRunning()) {
            _recordingTask.cancel();
        }
        _recordingTask = new RecordingTask(output, duration);
        _recordingTask.setOnSucceeded(this::onRecordingFinished);
        _recordingTask.setOnCancelled(this::onRecordingFinished);
        _recordingTask.setOnFailed(this::onRecordingFinished);

        Thread t = new Thread(_recordingTask);
        t.setDaemon(true);
        t.start();

        _promise = new Promise<>();
        return _promise;
    }

    public ReadOnlyDoubleProperty progressProperty() {
        return _recordingTask == null ? null  : _recordingTask.progressProperty();
    }

    private void onRecordingFinished(WorkerStateEvent e) {
        if (_promise == null) return;

        if (e.getSource().getException() != null) {
            _promise.reject(e.getSource().getException());
        } else {
            _promise.resolve((Media) e.getSource().getValue());
        }

    }

    private class RecordingTask extends Task<Media> {

        File _output;
        int _duration;

        RecordingTask(File output, int duration) {
            _output = output;
            _duration = duration;
        }

        @Override
        protected Media call() throws Exception {
            String cmd = "ffmpeg -y -f alsa -i default -t " + _duration + " -acodec pcm_s16le -ar 22050 -ac 1 " + _output.getName();
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);

            long period = 10;
            Timer progressTimer = new Timer();
            progressTimer.scheduleAtFixedRate(new TimerTask() {
                long sum = 0;
                @Override
                public void run() {
                    updateProgress(sum += period, _duration * 1000);
                }
            }, 0, period);

            // Uncomment below if on linux
            try {
                builder.start().waitFor();
                progressTimer.cancel();
                updateProgress(_duration * 1000,  _duration * 1000);
            } catch (IOException | InterruptedException e) {
                // Cancel the timer (stop updating progress)
                // And re-throw the exception so that the callback can handle it
                progressTimer.cancel();
                throw e;
            }
            //Thread.sleep(_duration * 1000); // To simulate the recording task (comment this if on linux)
            return new Media(_output.toURI().toString());
        }
    }
}
