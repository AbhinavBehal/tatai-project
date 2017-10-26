package tatai.model.data;

import com.google.gson.Gson;
import tatai.model.statistics.Score;
import tatai.model.theme.Theme;
import tatai.model.theme.ThemeManager;
import tatai.util.Encrypter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class used to manage user data.
 * Handles saving scores and other user data to a file.
 * Data is saved in json format and encrypted using {@link tatai.util.Encrypter}.
 */
public class DataManager {

    private static final File DATA_FILE = new File(".data.json");
    private static DataManager _instance;
    private final Object fileLock = new Object();
    private UserDataModel _dataModel;

    private DataManager() {
        if (!DATA_FILE.exists()) {
            // Create a new data file if it doesn't exist
            try {
                DATA_FILE.createNewFile();
                _dataModel = new UserDataModel(ThemeManager.defaultTheme(), new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If the data file exists, read it and populate the model from the data
            Gson gson = new Gson();
            try {
                Encrypter.decrypt(DATA_FILE);
                _dataModel = gson.fromJson(new FileReader(DATA_FILE), UserDataModel.class);
                Encrypter.encrypt(DATA_FILE);
                if (_dataModel == null) {
                    _dataModel = new UserDataModel(ThemeManager.defaultTheme(), new ArrayList<>());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the singleton instance of the DataManager
     * @return the singleton DataManager instance
     */
    public static DataManager manager() {
        if (_instance == null) {
            _instance = new DataManager();
        }
        return _instance;
    }

    /**
     * Method to get the users current scores.
     * @return List of the users scores.
     */
    public List<Score> getScores() { return _dataModel.getScores(); }

    /**
     * Update the users scores.
     * @param score The users score to add.
     */
    public void updateScore(Score score) {
        _dataModel.getScores().add(score);
        saveToFile();
    }

    /**
     * Get the user's currently set theme.
     * @return The users current theme.
     */
    public Theme getTheme() { return _dataModel.getTheme(); }

    /**
     * Update the users currently set theme.
     * @param theme The theme to change to.
     */
    public void updateTheme(Theme theme) {
        _dataModel.setTheme(theme);
        saveToFile();
    }

    /**
     * Function that handles saving the users current data to a file.
     * Saving to a file is done on a separate thread to avoid concurrency issues.
     */
    private void saveToFile() {
        new Thread(() -> {
            // Lock access to the file, so that different threads don't try to write to it at the same time
            synchronized (fileLock) {
                Encrypter.decrypt(DATA_FILE);
                try (PrintStream stream = new PrintStream(new FileOutputStream(DATA_FILE))) {
                    Gson gson = new Gson();
                    stream.print(gson.toJson(_dataModel));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Encrypter.encrypt(DATA_FILE);
            }
        }).start();
    }
}
