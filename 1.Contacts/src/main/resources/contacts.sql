CREATE TABLE IF NOT EXISTS users.contacts (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(32) NOT NULL,
    birth_date DATE,
    notes TEXT,
    is_important BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);