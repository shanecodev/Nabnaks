package com.shanecodev.ppmtool.services;

import com.shanecodev.ppmtool.domain.Backlog;
import com.shanecodev.ppmtool.domain.Project;
import com.shanecodev.ppmtool.domain.ProjectTask;
import com.shanecodev.ppmtool.exceptions.ProjectNotFoundException;
import com.shanecodev.ppmtool.repositories.BacklogRepository;
import com.shanecodev.ppmtool.repositories.ProjectRepository;
import com.shanecodev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        try {
            // Project tasks to be added to a specific project, project != null, Backlog exists
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

            // Set the backlog to the projecttask
            projectTask.setBacklog(backlog);
            // we want our project sequence to be like this: IDPRO-1 IDPRO-2 ...100 101
            Integer BacklogSequence = backlog.getPTSequence();
            // Update the backlog sequence
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);
            // Add sequence to project task
            projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            // INITIAL priority when priority null

            // INITIAL status when status is null
            if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }

            /* TODO: We need projectTask.getPriority() == 0 to handle the form */
            if(projectTask.getPriority() == null) {
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found");
        }
    }

    public Iterable<ProjectTask>findBacklogById(String id) {
        Project project = projectRepository.findByProjectIdentifier(id);
        if (project == null) {
            throw new ProjectNotFoundException("Project with ID: '" + id + "' does not exist");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

}
