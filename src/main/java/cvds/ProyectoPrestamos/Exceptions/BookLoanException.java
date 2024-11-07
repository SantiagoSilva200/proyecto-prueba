package cvds.ProyectoPrestamos.Exceptions;

public class BookLoanException extends RuntimeException {

    private String userMessage;
    private String developerMessage;
    private ErrorType errorType;

    public BookLoanException(ErrorType errorType) {
        super();
        this.errorType = errorType;
        this.setMessages(errorType);
    }

    public BookLoanException(ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
        this.setMessages(errorType);
    }

    private void setMessages(ErrorType errorType) {
        switch (errorType) {
            case ALREADY_BORROWED:
                this.userMessage = "El libro ya está prestado.";
                this.developerMessage = "Intento de préstamo fallido. El libro ya está prestado.";
                break;
            case NOT_AVAILABLE:
                this.userMessage = "El libro no está disponible para préstamo en este momento.";
                this.developerMessage = "Intento de préstamo fallido. El libro no está disponible.";
                break;
            case INVALID_BOOK:
                this.userMessage = "El código del libro no es válido.";
                this.developerMessage = "Intento de préstamo fallido. Código de libro inválido.";
                break;
            case NO_LOAN_FOUND:
                this.userMessage = "El libro no tiene un préstamo activo registrado.";
                this.developerMessage = "Intento de devolución fallido. El libro no está registrado como prestado.";
                break;
            default:
                this.userMessage = "Hubo un error inesperado con el préstamo.";
                this.developerMessage = "Error desconocido al intentar procesar el préstamo.";
                break;
        }
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public enum ErrorType {
        ALREADY_BORROWED,
        NOT_AVAILABLE,
        INVALID_BOOK,
        NO_LOAN_FOUND
    }
}
