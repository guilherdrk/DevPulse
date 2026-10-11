CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    repo_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);