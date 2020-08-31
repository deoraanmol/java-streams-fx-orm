package utils;

public enum Personalities {
    LEADER("A"),
    SOCIALIZER("B"),
    THINKER("C"),
    SUPPORTER("D");

    private String code;

    Personalities(String action) {
        this.code = action;
    }
    public String getCode() {
        return this.code;
    }
}
