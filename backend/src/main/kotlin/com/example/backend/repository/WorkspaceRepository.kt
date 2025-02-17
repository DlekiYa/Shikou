package com.example.backend.repository

import com.example.backend.repository.data.Workspace
import org.springframework.data.jpa.repository.JpaRepository

interface WorkspaceRepository : JpaRepository<Workspace, Long> {
}