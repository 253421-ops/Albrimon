package com.example.albrimon.presentation.project

import com.example.albrimon.data.model.Project
import com.example.albrimon.data.model.ProjectStatus

data class ProjectListUiState(
    val searchQuery: String = "",
    // null = "Todo"
    val selectedStatus: ProjectStatus? = null,
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList(),
    val error: String? = null
)
