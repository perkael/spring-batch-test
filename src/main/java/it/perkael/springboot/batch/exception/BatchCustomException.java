package it.perkael.springboot.batch.exception;

public class BatchCustomException extends Exception {

    private static final long serialVersionUID = 2519941321293604218L;

    public BatchCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public BatchCustomException(String message) {
        super(message);
    }

}
