package tatai.model.generator;

public enum Module {
    PRACTICE("Practice"),
    TEST("Test");

    private String _module;

    Module(String module) {
        _module = module;
    }

    @Override
    public String toString() {
        return _module;
    }
}
