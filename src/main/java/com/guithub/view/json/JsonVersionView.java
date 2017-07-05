package com.guithub.view.json;

import com.guithub.entity.Version;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JsonVersionView {

    //If version is not exist use this method, for send response with error and message
    public String getJsonVersionResponseView(HttpStatus httpStatus, String message) {
        return "{ \"response\" : null, "
                + "\"error\" : { "
                + "\"code\" : " + httpStatus.value() + ", "
                + "\"message\" : \"" + message + "\" } }";
    }

    //You should use this method if you have Version entity and you need to send information about him
    public String getJsonVersionResponseView(Version version) {
        return "{ \"response\" : { "
                + "\"id\" : " + version.getId() + ", "
                + "\"user_id\" : " + version.getUser().getId() + ", "
                + "\"version_number\" : \"" + version.getVersionNumber() + "\", "
                + "\"download_link\" : \"" + version.getDownloadLink() + "\", "
                + "\"approved\" : \"" + version.isApproved() + "\", "
                + "\"commit_message\" : \"" + version.getCommitMessage() + "\", "
                + "\"created_date\" : " + version.getCreatedDate() + ", "
                + " }, "
                + "\"error\" : null }";
    }
}
