package tatai.model.theme;

/**
 * Interface used as a listener such that subclasses are notified of theme changes.
 */
public interface ThemeListener {

    /**
     * Method called when a new theme is set.
     * @param previousTheme Theme the user previously used.
     * @param newTheme Theme the user has changed to.
     */
    void updateTheme(Theme previousTheme, Theme newTheme);
}
