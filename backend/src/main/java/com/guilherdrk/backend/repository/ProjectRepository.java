package com.guilherdrk.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guilherdrk.backend.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
