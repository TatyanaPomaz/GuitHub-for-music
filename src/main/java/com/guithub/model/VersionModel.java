package com.guithub.model;

import com.guithub.entity.Version;
import com.guithub.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VersionModel {

    @Autowired
    CollaboratorService collaboratorService;
    @Autowired
    VersionService versionService;
    @Autowired
    ProjectService projectService;

    public Version approveVersion(Version version) {
        if (version != null) {
            version.setVersionNumber(getMaxVersionNumber(version.getProject()) + 1);
            version.setApproved(true);
        }
        versionService.save(version);
        return version;
    }

    public Version deleteVersion(Version version) {
        if (version != null) {
            version.setArchived(true);
        }
        versionService.save(version);
        return version;
    }

    public Version unapproveVersion(Version version) {
        if (version != null) {
            version.setApproved(false);
        }
        versionService.save(version);
        return version;
    }

    public Version addVersion(User user, Project project) {
        Version version = new Version();
        int lastVersionNumber = project.getLastVersionNumber();

        project.setLastVersionNumber(lastVersionNumber + 1);
        projectService.save(project);

        version.setProject(project);
        version.setVersionNumber(lastVersionNumber + 1);
        version.setDownloadLink("");
        version.setApproved(false);
        version.setCommitMessage("");
        version.setArchived(false);
        version.setUser(user);
        Date data = new Date();
        version.setCreatedDate(data.getTime());

        versionService.saveAndFlush(version);
        return version;
    }

    public int getRole(Project project, User user) {
        int role = 2;
        List<Collaborator> collaborators = collaboratorService.findByProjectAndArchivedFalse(project);
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getUser().equals(user)) {
                role = collaborator.getRole();
            }
        }
        return role;
    }

    public int getMaxVersionNumber(Project project) {
        int result = 0;
        List<Version> versions = versionService.findAllByProjectAndArchivedFalse(project);
        for (Version version : versions) {
            if (version.isApproved()) {
                result++;
            }
        }
        return result;
    }

    public String getRedirectionString(Version version) {
        Project project = version.getProject();
        String projectName = project.getProjectName();

        User user = version.getUser();
        String userName = user.getUsername();

        return "/" + userName + "/" + projectName + "/";
    }
}