package com.google.drive.api.dto.response;


import java.util.List;

public class ErrorResponse {
    //General error message about nature of error
    private String message;

    //Specific details in API request processing
    List<ValidationErrorDTO> details;

    public ErrorResponse(String message, List<ValidationErrorDTO> details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ValidationErrorDTO> getDetails() {
        return details;
    }

    public void setDetails(List<ValidationErrorDTO> details) {
        this.details = details;
    }
}
