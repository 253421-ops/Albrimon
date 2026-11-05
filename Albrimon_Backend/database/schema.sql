-- Esquema de referencia. Hibernate/JPA genera las tablas automáticamente.
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
  id TEXT PRIMARY KEY, name TEXT NOT NULL, email TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL, role TEXT NOT NULL CHECK(role IN ('DISENADOR','CLIENTE')),
  created_at TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS clients (
  id INTEGER PRIMARY KEY AUTOINCREMENT, designer_id TEXT NOT NULL REFERENCES users(id),
  linked_user_id TEXT UNIQUE REFERENCES users(id), name TEXT NOT NULL, phone TEXT, age INTEGER,
  size TEXT, active INTEGER NOT NULL DEFAULT 1, bust_cm INTEGER, waist_cm INTEGER,
  hip_cm INTEGER, length_cm INTEGER, link_code TEXT UNIQUE
);
CREATE TABLE IF NOT EXISTS projects (
  id INTEGER PRIMARY KEY AUTOINCREMENT, designer_id TEXT NOT NULL REFERENCES users(id),
  client_id INTEGER NOT NULL REFERENCES clients(id), name TEXT NOT NULL,
  status TEXT NOT NULL, due_date TEXT, notes TEXT
);
CREATE TABLE IF NOT EXISTS project_pieces (
  id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL REFERENCES projects(id),
  name TEXT NOT NULL, width_mm INTEGER NOT NULL, height_mm INTEGER NOT NULL,
  can_rotate INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS scraps (
  id INTEGER PRIMARY KEY AUTOINCREMENT, designer_id TEXT NOT NULL REFERENCES users(id),
  name TEXT NOT NULL, fabric_type TEXT NOT NULL, color_hex TEXT NOT NULL,
  width_mm INTEGER NOT NULL, height_mm INTEGER NOT NULL, has_grain INTEGER NOT NULL,
  photo_path TEXT, available INTEGER NOT NULL DEFAULT 1, created_at TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS scrap_usage (
  id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL REFERENCES projects(id),
  scrap_id INTEGER NOT NULL REFERENCES scraps(id), used_area_mm2 INTEGER NOT NULL,
  leftover_wmm INTEGER NOT NULL DEFAULT 0, leftover_hmm INTEGER NOT NULL DEFAULT 0,
  created_at TEXT NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_usage_project ON scrap_usage(project_id);
CREATE INDEX IF NOT EXISTS idx_usage_scrap ON scrap_usage(scrap_id);
