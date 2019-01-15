package com.shanecodev.ppmtool.services;

import com.shanecodev.ppmtool.domain.Project;
import com.shanecodev.ppmtool.exceptions.ProjectIdException;
import com.shanecodev.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '"
                    + project.getProjectIdentifier().toUpperCase()
                    + " 'already exists");
        }
    }
    public Project findProjectByIdentifier(String projectId) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' does not exist");
        }

        return project;
    }
    // returns all json objects as a list
    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }
}
