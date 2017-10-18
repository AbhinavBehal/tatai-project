package tatai.model.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.model.theme.ThemeManager;
import tatai.util.Triple;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static tatai.model.generator.Difficulty.EASY;
import static tatai.model.generator.Difficulty.HARD;
import static tatai.model.generator.Module.PRACTICE;
import static tatai.model.generator.Module.TEST;
import static tatai.model.statistics.Statistic.*;

public class StatsManager {

    private static final double MAX_SCORE = 10;

    // Extra entry used for testing
    public static void main(String[] args) {
        StatsManager.manager().checkem();
    }

    private static StatsManager _manager;
    private List<ScoreListener> _listeners;
    private Map<Triple<LocalDate, Module, Difficulty>, List<Integer>> _scoreLists;
    private Map<Triple<Module, Difficulty, Statistic>, Double> _statistics;
    private LocalDate _date;
    private boolean _practiceUnlocked;
    private boolean _testUnlocked;

    /**
     * Private constructor for singleton StatsManager instance, sets current date and
     * reads from data file for previous user data.
     */
    private StatsManager() {
        _listeners = new ArrayList<>();
        _scoreLists = new TreeMap<>();
        _statistics = new HashMap<>();
        _date = LocalDate.now();
        read();
    }

    /**
     * Public method used to get the singleton instance.
     * @return singleton StatsManager.
     */
    public static StatsManager manager() {
        if (_manager == null) {
            _manager = new StatsManager();
        }
        return _manager;
    }

    /**
     * Public method used to add listeners to StatsManager when scores change.
     * TODO: Discuss necessity
     * @param listener An object of class which implements ScoreListener.
     */
    public void addListener(ScoreListener listener) {
        _listeners.add(listener);
    }

    /**
     * Public method used to update scores managed by StatsManager singleton, calls 
     * the inherited updateScore method on all ScoreListeners and updates relevant 
     * statistics depending on input arguments.
     * @param date Date to add score to.
     * @param module Module the score was obtained in.
     * @param difficulty Difficulty the score was obtained in.
     * @param score The final score obtained.
     */
    public void updateScore(LocalDate date, Module module, Difficulty difficulty, int score) {
        if (date != null && module != null && difficulty != null) {
            Triple dateScores = new Triple<>(date, module, difficulty);
            _scoreLists.get(dateScores).add(score);
            _listeners.forEach(l -> l.updateScore(module, difficulty, score));

            int games = _scoreLists.get(dateScores).size();

            double average = _statistics.get(new Triple<>(module, difficulty, AVERAGE));
            average = (average * games + score) / (games + 1);
            double max = _statistics.get(new Triple<>(module, difficulty, MAX));
            max = max < score ? score : max;
            double correct = _statistics.get(new Triple<>(module, difficulty, CORRECT));

            _statistics.put(new Triple<>(module, difficulty, AVERAGE), average);
            _statistics.put(new Triple<>(module, difficulty, LAST), (double) score);
            _statistics.put(new Triple<>(module, difficulty, MAX), max);
            _statistics.put(new Triple<>(module, difficulty, CORRECT), correct + score);
            _statistics.put(new Triple<>(module, difficulty, TOTAL), games * MAX_SCORE);
        }
    }

    /**
     * Public method used to get the lifetime maximum scores obtained in each mode,
     * currently returns data only usable for an XYChart.
     * TODO: change to return list or map?
     * @return Observable collection of the series for each mode and maximum score
     * for chart to use.
     */
    public ObservableList<XYChart.Series<String, Number>> getTopScores() {
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();

        for (Module module : Module.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                XYChart.Series<String, Number> mdMax = new XYChart.Series<>();
                Triple<Module, Difficulty, Statistic> mdMaxStat = new Triple<>(module, difficulty, MAX);
                mdMax.setName(mdMaxStat.first() + "-" + mdMaxStat.second());
                XYChart.Data<String, Number> mdMaxScore =
                        new XYChart.Data<>("", _statistics.get(mdMaxStat));
                mdMax.getData().add(mdMaxScore);
                series.add(mdMax);
            }
        }

//        series.forEach(s -> System.out.println(s.getData()));

        return series;
    }

    /**
     * Public method used to get the lifetime correct scores obtained in each mode,
     * currently returns data only usable for a PieChart.
     * TODO: change to return list or map?
     * @return Observable collection of PieChart.Data for each mode and total
     * answers correct (and incorrect? - discuss) for chart to use.
     */
    public ObservableList<PieChart.Data> getTotalCorrect() {
        ObservableList<PieChart.Data> series = FXCollections.observableArrayList();
        double totalCorrect = 0;
        double total = 0;
        for (Module module : Module.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                double correct = _statistics.get(new Triple<>(module, difficulty, CORRECT));
                series.add(new PieChart.Data(module + " " + difficulty, correct));
                totalCorrect += correct;
                total += _statistics.get(new Triple<>(module, difficulty, TOTAL));
            }
        }

        series.add(new PieChart.Data("Attempted", total - totalCorrect));

//        series.forEach(System.out::println);

        return series;
    }

    /**
     * Public method used to get the list scores obtained in a given mode.
     * TODO: change to return list?
     * @param module Module of the list of scores.
     * @param difficulty Difficulty of the list of scores.
     * @return XYChart.Series of the scores for that list.
     */
    public XYChart.Series<Number, Number> getScoreList(Module module, Difficulty difficulty) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        int i = 1;
        for (int score : _scoreLists.get(new Triple<>(_date, module, difficulty))) {
            series.getData().add(new XYChart.Data<>(i++, score));
        }

        return series;
    }

    /**
     * Public method used to check if PRACTICE Module's HARD Difficulty has been
     * unlocked.
     * @return boolean of whether or not HARD is unlocked.
     */
    public boolean practiceUnlockedUnlocked() {
        return _practiceUnlocked;
    }

    /**
     * Public method used to check if TEST Module's HARD Difficulty has been
     * unlocked.
     * @return boolean of whether or not HARD is unlocked.
     */
    public boolean testUnlockedUnlocked() {
        return _testUnlocked;
    }

    /**
     * Private method used to read from data file and store data into the relevant
     * Maps, and variables.
     */
    private void read() {
        File dataFile = new File("data.txt");
        if (!dataFile.exists()) {
            try {
                if (dataFile.createNewFile()) {
                    System.out.println("?");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
                    writer.write(ThemeManager.manager().getCurrentTheme().simpleName() + ";\n");
                    writer.write(_practiceUnlocked + "," + _testUnlocked + ";\n");
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("data.txt")))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.split(",").length == 2) {
                    String[] unlocked = line.split(",");

                    _practiceUnlocked = unlocked[0].contains("true");
                    _testUnlocked = unlocked[1].contains("true");

                } else if (line.split(";").length == 2) {
                    String[] dateScores = line.split(";");
                    LocalDate date = LocalDate.parse(dateScores[0]);
                    makeLists(date);

                    String[] scores = dateScores[1].split(",");
                    for (String scoreString : scores) {
                        if (scoreString.length() == 3) {
                            Module module;
                            Difficulty difficulty;
                            int score;

                            module = scoreString.charAt(0) == 'p' ? PRACTICE : TEST;
                            difficulty = scoreString.charAt(1) == 'e' ? EASY : HARD;
                            score = Integer.parseInt(scoreString.substring(2));

                            updateScore(date, module, difficulty, score);
                        }
                    }
                } else {
                    makeLists(_date);
                }
            }

            reader.close();

            // for checking if data read in is correct
            System.out.println(_practiceUnlocked + ", " + _testUnlocked);
            _scoreLists.keySet().forEach(d -> System.out.println(d.toString() + " : " + _scoreLists.get(d)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Private helper method used to make each of the 4 lists of modes for PRACTICE, TEST;
     * and EASY, HARD for a given date.
     * @param date Date to make lists for.
     */
    private void makeLists(LocalDate date) {
        for (Module module : Module.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                _scoreLists.put(new Triple<>(date, module, difficulty), new ArrayList<>());
                for (Statistic statistic : Statistic.values()) {
                    _statistics.put(new Triple<>(module, difficulty, statistic), 0.0);
                }
            }
        }
    }

    // Private method to check stats, used for testing
    private void checkem() {
        for (Module m : Module.values()) {
            for (Difficulty d : Difficulty.values()) {
                for (Statistic s : Statistic.values()) {
                    System.out.println(m + ":" + d + ":" + s + "=" + _statistics.get(new Triple<>(m, d, s)));
                }
            }
        }
    }
}
