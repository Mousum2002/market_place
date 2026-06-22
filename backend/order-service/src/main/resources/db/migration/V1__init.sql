CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    products JSONB NOT NULL,
    total_price NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);