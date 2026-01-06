CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        amount DECIMAL(19, 2) NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        idempotency_key VARCHAR(255) NOT NULL,
    -- Unique constraint to prevent duplicate orders from the same request
                        CONSTRAINT uk_order_idempotency UNIQUE (idempotency_key)
);