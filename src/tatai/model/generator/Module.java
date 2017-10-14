package tatai.model.generator;

/**
 * Module enum, used to represent the two modules a user can partake in;
 * either practice, or test.
 */
public enum Module {
    PRACTICE("Practice"),
    TEST("Test");

    private String _module;

    Module(String module) {
        _module = module;
    }

    /**
     * Public toString() method overridden to represent Module enum.
     * @return String representation of the enum.
     */
    @Override
    public String toString() {
        return _module;
    }

}
