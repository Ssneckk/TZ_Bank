package com.example.bankcards.util.auxiliaryclasses.response;

/**
 * DTO ответа об ошибке.
 *
 * <p>Используется для передачи информации об ошибке клиенту,
 * включая HTTP-статус и описание проблемы.</p>
 */
public class ErrorResponse {

    private int status;

    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
