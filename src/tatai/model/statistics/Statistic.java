package tatai.model.statistics;

public enum Statistic {
    AVERAGE,
    LAST,
    MAX,
    CORRECT,
    TOTAL;

    private double _val;

    public void update(double val) {
        _val = val;
    }

    public double val() {
        return _val;
    }
}
