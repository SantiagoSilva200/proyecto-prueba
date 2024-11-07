package cvds.ProyectoPrestamos.Exceptions;

public class BookApiException extends RuntimeException {

    private String userMessage;
    private String developerMessage;
    private ErrorType errorType;

    public BookApiException(ErrorType errorType) {
        super();
        this.errorType = errorType;
        this.setMessages(errorType);
    }

    public BookApiException(ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
        this.setMessages(errorType);
    }

    private void setMessages(ErrorType errorType) {
        switch (errorType) {
            case API_ERROR:
                this.userMessage = "No pudimos obtener la disponibilidad del libro. Por favor, inténtelo más tarde.";
                this.developerMessage = "Error al consumir la API externa.";
                break;
            case DATA_NOT_FOUND:
                this.userMessage = "No se encontraron datos para el libro solicitado.";
                this.developerMessage = "Datos no encontrados para el libro en la API.";
                break;
            case UPDATE_FAILED:
                this.userMessage = "No pudimos actualizar el estado del libro. Por favor, inténtelo más tarde.";
                this.developerMessage = "Error al actualizar el estado del libro en la API.";
                break;
            case UNKNOWN_ERROR:
            default:
                this.userMessage = "Hubo un error inesperado. Por favor, intente nuevamente.";
                this.developerMessage = "Error desconocido.";
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
        API_ERROR,
        DATA_NOT_FOUND,
        UPDATE_FAILED,
        UNKNOWN_ERROR
    }
}
