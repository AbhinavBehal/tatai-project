package tatai.model.data;

import com.google.gson.Gson;
import tatai.model.statistics.Score;
import tatai.model.theme.Theme;
import tatai.model.theme.ThemeManager;
import tatai.util.Encrypter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final File DATA_FILE = new File("data.json");
    private static DataManager _instance;
    private final Object fileLock = new Object();
    private UserDataModel _dataModel;

    private DataManager() {
        if (!DATA_FILE.exists()) {
            try {
                DATA_FILE.createNewFile();
                _dataModel = new UserDataModel(ThemeManager.defaultTheme(), new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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

    public static DataManager manager() {
        if (_instance == null) {
            _instance = new DataManager();
        }
        return _instance;
    }

    public List<Score> getScores() { return _dataModel.getScores(); }

    public void updateScore(Score score) {
        _dataModel.getScores().add(score);
        saveToFile();
    }

    public Theme getTheme() { return _dataModel.getTheme(); }

    public void updateTheme(Theme theme) {
        _dataModel.setTheme(theme);
        saveToFile();
    }

    private void saveToFile() {
        new Thread(() -> {
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
