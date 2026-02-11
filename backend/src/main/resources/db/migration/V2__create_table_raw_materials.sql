CREATE TABLE raw_materials (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(150) NOT NULL,
                               code VARCHAR(60) UNIQUE,
                               stock_quantity INTEGER NOT NULL DEFAULT 0,
                               created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
