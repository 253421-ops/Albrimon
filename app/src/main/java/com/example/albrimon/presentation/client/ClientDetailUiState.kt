package com.example.albrimon.presentation.client

import com.example.albrimon.data.model.Client

data class ClientDetailUiState(
    val client: Client? = null,
    val relatedProjects: Int = 0,
    val measurementsExpanded: Boolean = false
)
