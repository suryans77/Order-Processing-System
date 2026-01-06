-- ORDER SERVICE OUTBOX
CREATE TABLE order_outbox (
                              id UUID PRIMARY KEY,
                              aggregate_type VARCHAR(255) NOT NULL,
                              aggregate_id UUID NOT NULL, -- The Order ID
                              event_type VARCHAR(255) NOT NULL,
                              payload TEXT NOT NULL,
                              created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                              processed BOOLEAN NOT NULL DEFAULT FALSE,
                              processed_at TIMESTAMP WITH TIME ZONE,
);

-- PAYMENT SERVICE OUTBOX
CREATE TABLE payment_outbox (
                                id UUID PRIMARY KEY,
                                aggregate_type VARCHAR(255) NOT NULL,
                                aggregate_id UUID NOT NULL,
                                event_type VARCHAR(255) NOT NULL,
                                payload TEXT NOT NULL,
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                processed BOOLEAN NOT NULL DEFAULT FALSE,
                                processed_at TIMESTAMP WITH TIME ZONE,
);

-- INVENTORY SERVICE OUTBOX
CREATE TABLE inventory_outbox (
                                  id UUID PRIMARY KEY,
                                  aggregate_type VARCHAR(255) NOT NULL,
                                  aggregate_id UUID NOT NULL,
                                  event_type VARCHAR(255) NOT NULL,
                                  payload TEXT NOT NULL,
                                  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                  processed BOOLEAN NOT NULL DEFAULT FALSE,
                                  processed_at TIMESTAMP WITH TIME ZONE,
);