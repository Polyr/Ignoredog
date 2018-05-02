package io.github.synchronousx.ignoredog.utils.request;

public class ErrorResponse {
    private String error;
    private String errorMessage;

    public ErrorResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void throwError() {
        throw new RuntimeException(this.errorMessage);
    }

    @Override
    public String toString() {
        return this.error + ": " + this.errorMessage;
    }
}
