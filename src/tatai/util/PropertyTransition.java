package tatai.util;

import javafx.animation.Transition;
import javafx.beans.property.Property;
import javafx.util.Duration;

public class PropertyTransition extends Transition {
    private Property _property;
    private Duration _duration;
    private double _fromValue;
    private double _toValue;
    private double _delta;

    public PropertyTransition(Duration duration, Property property) {
        _property = property;
        _fromValue = 0;
        _toValue = 100;
        _duration = duration;
        setCycleDuration(_duration);
    }

    public void setFromValue(double fromValue) {
        _fromValue = fromValue;
    }

    public void setToValue(double toValue) {
        _toValue = toValue;
    }

    @Override
    protected void interpolate(double frac) {
        _delta = (_toValue - _fromValue);
        double val = Math.floor(_fromValue + frac * _delta);
        System.out.println(val);
        _property.setValue(val);
    }

    @Override
    public void play() {
        super.play();
    }
}
