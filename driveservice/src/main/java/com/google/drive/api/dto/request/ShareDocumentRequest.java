package com.google.drive.api.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ShareDocumentRequest {

    private List<Long> documentIds;

    @NotNull
    private String usernameToShare;

    public ShareDocumentRequest() {

    }

    public ShareDocumentRequest(List<Long> documentIds, String usernameToShare) {
        this.documentIds = documentIds;
        this.usernameToShare = usernameToShare;
    }

    public List<Long> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<Long> documentIds) {
        this.documentIds = documentIds;
    }


    public String getUsernameToShare() {
        return usernameToShare;
    }

    public void setUsernameToShare(String usernameToShare) {
        this.usernameToShare = usernameToShare;
    }
}
