# Albrimon (Kotlin + Jetpack Compose + MVVM)

Primeras vistas según el Figma y el documento de requerimientos.

## Estructura (igual que mvvm_c)
```
com.example.albrimon
├── data
│   ├── model        Client, Project, ProjectStatus
│   └── repository   ClientRepository, ProjectRepository (datos de ejemplo)
├── navigation       Routes, AppNavigation
├── presentation
│   ├── auth         Welcome, Login, Register + AuthViewModel / AuthUiState
│   ├── home         HomeScreen + HomeViewModel / HomeUiState
│   ├── client       ClientListScreen + UiState + ViewModel, ClientDetailScreen + UiState + ViewModel
│   ├── project      ProjectListScreen + UiState + ViewModel
│   └── components   ClientCard, ProjectCard, AlbrimonScaffold (barra inferior), etc.
└── ui.theme         Color, Type, Theme (claro y oscuro)
```
Flujo: Welcome → Login/Registro → Inicio → Clientes / Proyectos / Retazos.
Correo válido (con @) y contraseña de 6+ caracteres para entrar (validación local por ahora).
