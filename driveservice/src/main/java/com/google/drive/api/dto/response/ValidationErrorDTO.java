package com.google.drive.api.dto.response;

public class ValidationErrorDTO {

    private String field;

    private String messages;

    public ValidationErrorDTO(String messages) {
        this.messages = messages;
    }


    public ValidationErrorDTO(String field, String messages) {
        this.field = field;
        this.messages = messages;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
