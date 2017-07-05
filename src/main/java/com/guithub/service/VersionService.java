package com.guithub.service;

import com.guithub.entity.Project;
import com.guithub.entity.Version;

import java.util.List;

public interface VersionService {

    List<Version> findAllByProjectAndArchivedFalse(Project project);

    List<Version> findAllByProjectAndApprovedAndArchivedFalse(Project project, boolean approve);

    Version saveAndFlush(Version version);

    Version save(Version version);

    Version findById(Long id);
}
