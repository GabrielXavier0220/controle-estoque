CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(150) NOT NULL,
                          sku VARCHAR(60) UNIQUE,
                          description TEXT,
                          quantity INTEGER NOT NULL DEFAULT 0,
                          min_quantity INTEGER NOT NULL DEFAULT 0,
                          cost_price NUMERIC(12,2) DEFAULT 0,
                          sale_price NUMERIC(12,2) DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
