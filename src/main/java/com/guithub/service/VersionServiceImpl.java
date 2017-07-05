package com.guithub.service;

import com.guithub.dao.VersionRepository;
import com.guithub.entity.Project;
import com.guithub.entity.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    VersionRepository versionRepository;

    @Override
    public List<Version> findAllByProjectAndArchivedFalse(Project project) {
        return versionRepository.findAllByProjectAndArchivedFalse(project);
    }

    @Override
    public List<Version> findAllByProjectAndApprovedAndArchivedFalse(Project project, boolean approve) {
        return versionRepository.findAllByProjectAndApprovedAndArchivedFalse(project, approve);
    }

    @Override
    public Version saveAndFlush(Version version) {
        return versionRepository.saveAndFlush(version);
    }

    @Override
    public Version save(Version version) {
        return versionRepository.save(version);
    }

    @Override
    public Version findById(Long id) {
        return versionRepository.findById(id);
    }
}
