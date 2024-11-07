package cvds.ProyectoPrestamos.Exceptions;

public class StudentException extends RuntimeException {

    private String userMessage;
    private String developerMessage;
    private ErrorType errorType;

    public StudentException(ErrorType errorType) {
        super();
        this.errorType = errorType;
        this.setMessages(errorType);
    }

    public StudentException(ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
        this.setMessages(errorType);
    }

    private void setMessages(ErrorType errorType) {
        switch (errorType) {
            case STUDENT_NOT_FOUND:
                this.userMessage = "El estudiante no fue encontrado.";
                this.developerMessage = "Intento de préstamo fallido. Estudiante no encontrado.";
                break;
            case INVALID_STUDENT:
                this.userMessage = "El estudiante no es válido para este préstamo.";
                this.developerMessage = "Intento de préstamo fallido. Estudiante no válido.";
                break;
            case BOOK_ALREADY_ISSUED:
                this.userMessage = "El estudiante ya tiene este libro.";
                this.developerMessage = "Intento de préstamo fallido. El estudiante ya posee este libro.";
                break;
            default:
                this.userMessage = "Hubo un error inesperado con la información del estudiante.";
                this.developerMessage = "Error desconocido relacionado con el estudiante.";
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
        STUDENT_NOT_FOUND,
        INVALID_STUDENT,
        BOOK_ALREADY_ISSUED
    }
}
