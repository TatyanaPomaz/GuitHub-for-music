package com.guithub.dao;

import com.guithub.entity.Project;
import com.guithub.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {

    List<Version> findAllByProjectAndArchivedFalse(Project project);

    List<Version> findAllByProjectAndApprovedAndArchivedFalse(Project project, boolean approve);

    Version saveAndFlush(Version version);

    Version findById(Long id);
}
