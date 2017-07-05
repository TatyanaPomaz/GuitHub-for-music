package com.guithub.controller;

import com.guithub.entity.Version;
import com.guithub.model.VersionModel;
import com.guithub.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VersionController {

    @Autowired
    VersionModel versionModel;

    @Autowired
    ProjectService projectService;

    @Autowired
    VersionService versionService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/add_version", method = RequestMethod.POST)
    public String addVersion(@RequestParam(value = "projectname") String projectName,
                             @RequestParam(value = "username") String userName) {


        User user = userService.findByUsernameAndArchivedFalse(userName);
        Project project = projectService.findProjectByProjectNameAndUserAndArchivedFalse(projectName, user);
        versionModel.addVersion(user, project);

        String redirection = "/" + user.getUsername() + "/" + project.getProjectName();

        return "redirect:" + redirection;
    }

    @RequestMapping(value = "/approve_version", method = RequestMethod.POST)
    public String approveVersion(@RequestParam(value = "versionid") Long id) {

        Version version = versionService.findById(id);
        versionModel.approveVersion(version);

        String redirection = versionModel.getRedirectionString(version);

        return "redirect:" + redirection;
    }

    @RequestMapping(value = "/delete_version", method = RequestMethod.POST)
    public String deleteVersion(@RequestParam(value = "versionid") Long id) {

        Version version = versionService.findById(id);
        versionModel.deleteVersion(version);

        String redirection = versionModel.getRedirectionString(version);

        return "redirect:" + redirection;
    }

    @RequestMapping(value = "/unapprove_version", method = RequestMethod.POST)
    public String unapproveVersion(@RequestParam(value = "versionid") Long id) {

        Version version = versionService.findById(id);
        versionModel.unapproveVersion(version);

        String redirection = versionModel.getRedirectionString(version);

        return "redirect:" + redirection;
    }
}
