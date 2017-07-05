package com.guithub.api;

import com.guithub.entity.Project;
import com.guithub.entity.User;
import com.guithub.entity.Version;
import com.guithub.logging.LoggingModel;
import com.guithub.model.StorageModel;
import com.guithub.model.VersionModel;
import com.guithub.service.ProjectService;
import com.guithub.service.UserCookieService;
import com.guithub.service.VersionService;
import com.guithub.view.json.JsonVersionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/project/{projectId}/versions")
public class VersionApi {
    @Autowired
    VersionService versionService;
    @Autowired
    ProjectService projectService;
    @Autowired
    UserCookieService userCookieService;
    @Autowired
    LoggingModel loggingModel;
    @Autowired
    JsonVersionView jsonVersionView;
    @Autowired
    VersionModel versionModel;

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
    public ResponseEntity<?> updateVersion(@RequestBody Version updatedVersion,
                                           @RequestParam("file") MultipartFile file,
                                           @PathVariable(name = "versionId") long versionId) {
        String json;
        String message;

        if (file == null && updatedVersion == null) {
            message = "File and Version not found";
            loggingModel.makeLog(message);
            json = jsonVersionView.getJsonVersionResponseView(HttpStatus.NOT_FOUND, message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
        }

        Version version = versionService.findById(versionId);
        if(!version.getUser().equals(updatedVersion.getUser()) && updatedVersion.getUser() != null) {
            version.setUser(updatedVersion.getUser());
        }
        if (!version.getProject().equals(updatedVersion.getProject()) && updatedVersion.getProject() != null) {
            version.setProject(updatedVersion.getProject());
        }
        if (version.getVersionNumber() != updatedVersion.getVersionNumber() && updatedVersion.getVersionNumber() != 0) {
            version.setVersionNumber(updatedVersion.getVersionNumber());
        }
        if (!version.getCommitMessage().equals(updatedVersion.getCommitMessage()) && updatedVersion.getCommitMessage() != null) {
            version.setCommitMessage(updatedVersion.getCommitMessage());
        }
        if (version.isApproved() != updatedVersion.isApproved()) {
            version.setApproved(updatedVersion.isApproved());
        }
        if (version.getCreatedDate() != updatedVersion.getCreatedDate()) {
            version.setCreatedDate(updatedVersion.getCreatedDate());
        }
        if (file != null) {
            StorageModel storageModel = new StorageModel();
            String versionUrl = storageModel.uploadVersionXMLToStorage(file);
            version.setDownloadLink(versionUrl);
        }

        versionService.save(version);
        json = jsonVersionView.getJsonVersionResponseView(version);
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewVersion(@PathVariable(name = "projectId") long projectId,
                                           @RequestBody Version newVersion) {
        String json;
        Project project = projectService.findProjectByIdAndArchivedFalse(projectId);
        Version version = versionModel.addVersion(project.getUser(), project);

        json = jsonVersionView.getJsonVersionResponseView(version);

        return ResponseEntity.status(HttpStatus.OK).body(json);
    }

    @DeleteMapping("/{versionId}")
    public ResponseEntity<?> deleteVersion(@CookieValue(name = "param", required = false) String cookie,
                                           @PathVariable(name = "versionId") long versionId,
                                           @PathVariable(name = "projectId") long projectId) {
        String json;
        String message;

        Version version = versionService.findById(versionId);
        Project project = projectService.findProjectByIdAndArchivedFalse(projectId);
        User user = userCookieService.findByCookieAndArchivedFalse(cookie).getUser();
        if (version != null && (user.equals(version.getUser()) || user.equals(project.getUser()))) {
            versionModel.deleteVersion(version);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        message = "You don't have permissions for this action";
        json = jsonVersionView.getJsonVersionResponseView(HttpStatus.FORBIDDEN, message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(json);
    }

}
