package tatai.model.generator;

/**
 * Interface used for subclasses which generate strings, used for
 * mathematical equations.
 */
public interface Generator {

    /**
     * Method implemented to return a generated string.
     * @return String generated.
     */
    String generate();

    /**
     * Method implemented to return the value associated with the generated
     * string if there exists one.
     * @return int value/representation of generated string.
     */
    int value();

    /**
     * Method implemented to return the value for the number of questions
     * associated with the Generator if there exists one.
     * @return int number of questions.
     */
    int questions();

    /**
     * Method implemented to return the Module associated with the Generator if
     * there exists one.
     * @return Module associated with Generator.
     */
    Module module();

    /**
     * Method implemented to return the Difficulty of the associated Generator
     * if there exists one.
     * @return Difficulty associated with Generator.
     */
    Difficulty difficulty();
}
