package com.guithub.api;

import com.guithub.entity.Version;
import com.guithub.logging.LoggingModel;
import com.guithub.service.VersionService;
import com.guithub.view.json.JsonVersionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/v1/project/{projectId}/versions")
public class VersionApi {

    @Autowired
    VersionService versionService;

    @Autowired
    LoggingModel loggingModel;

    @Autowired
    JsonVersionView jsonVersionView;

    @Autowired
    CookieModel cookieModel;

    @GetMapping("/{versionId}")
    public ResponseEntity<?> getVersion(@PathVariable(name = "versionId") long versionId) {
        String json;
        String message;
        Version version = versionService.findById(versionId);

        if (version == null) {
            message = "This version of project is not exist";
            loggingModel.makeLog(message);
            json = jsonVersionView.getJsonVersionResponseView(HttpStatus.NOT_FOUND, message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
        }

        json = jsonVersionView.getJsonVersionResponseView(version);
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @PutMapping("/{versionId}")
    public ResponseEntity<?> updateVersion(@CookieValue(name = "param", required = false) String cookie,
                                           @RequestBody Version updatedVersion,
                                           @RequestParam("file") MultipartFile file,
                                           @PathVariable(name = "versionId") Long versionId) {
        String json;
        String message;
        if (!cookieModel.checkCookie(cookie)) {
            return getResponseEntityForUnauthorizedUser();
        }
        if (file == null && updatedVersion == null) {
            message = "File and Version not found";
            loggingModel.makeLog(message);
            json = jsonVersionView.getJsonVersionResponseView(HttpStatus.NOT_FOUND, message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
        }
        Version version = versionService.findById(versionId);
        if (!version.getUser().equals(updatedVersion.getUser()) && updatedVersion.getUser() != null) {
            version.setUser(updatedVersion.getUser());
        }
//        if (version.getProject().getId() != updatedVersion.getProject().getId() && updatedVersion.getProject().getId() != 0) {
//            userByCookie.setAge(updatedVersion.getAge());
//        }
//        if (!version.getInstrument().equals(updatedUser.getInstrument()) && updatedUser.getInstrument() != null) {
//            userByCookie.setInstrument(updatedVersion.getInstrument());
//        }
//        if (!version.getPhoneNumber().equals(updatedVersion.getPhoneNumber()) && updatedVersion.getPhoneNumber() != null) {
//            userByCookie.setPhoneNumber(updatedVersion.getPhoneNumber());
//        }
//        if (file != null) {
//            StorageModel storageModel = new StorageModel();
//            String photoUrl = storageModel.uploadPhotoToStorage(file);
//            userByCookie.setPhoto(photoUrl);
//        }
        versionService.save(version);
        json = jsonVersionView.getJsonVersionResponseView(version);
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewVersion(@RequestBody Version newVersion, HttpServletResponse response) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVersion() {
        return null;
    }

    private ResponseEntity<?> getResponseEntityForUnauthorizedUser() {
        String message = "User is not authorized";
        String json = jsonVersionView.getJsonVersionResponseView(HttpStatus.UNAUTHORIZED, message);
        loggingModel.makeLog(message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(json);
    }
}
