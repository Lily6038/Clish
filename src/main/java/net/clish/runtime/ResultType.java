package net.clish.runtime;

/**
 * Represents a Result type for explicit error handling.
 * Either Ok (success) or Error (failure).
 */
public class ResultType {
    public enum Kind { OK, ERROR }

    public final Kind kind;
    public final Object value;      // For OK
    public final String message;    // For ERROR
    public final int code;          // For ERROR

    private ResultType(Kind kind, Object value, String message, int code) {
        this.kind = kind;
        this.value = value;
        this.message = message;
        this.code = code;
    }

    public static ResultType ok(Object value) {
        return new ResultType(Kind.OK, value, null, 0);
    }

    public static ResultType error(String message) {
        return new ResultType(Kind.ERROR, null, message, 1);
    }

    public static ResultType error(String message, int code) {
        return new ResultType(Kind.ERROR, null, message, code);
    }

    public boolean isOk() {
        return kind == Kind.OK;
    }

    public boolean isError() {
        return kind == Kind.ERROR;
    }

    public Object getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        if (kind == Kind.OK) {
            return "Ok(" + value + ")";
        } else {
            return "Error(" + message + ", " + code + ")";
        }
    }
}
