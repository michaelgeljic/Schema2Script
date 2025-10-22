package exception;

public class SqlGenerationException extends RuntimeException{
    public SqlGenerationException(String message, Throwable cause){
        super(message, cause);
    }
}
