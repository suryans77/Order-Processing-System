CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        amount NUMERIC(19, 2) NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        idempotency_key VARCHAR(255) UNIQUE NOT NULL
);
