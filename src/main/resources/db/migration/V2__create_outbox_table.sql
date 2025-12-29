CREATE TABLE outbox (
                        id UUID PRIMARY KEY,
                        aggregate_type VARCHAR(100) NOT NULL,
                        aggregate_id UUID NOT NULL,
                        event_type VARCHAR(100) NOT NULL,
                        payload TEXT NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    --Tracks if the event has been sent to the broker
                        processed BOOLEAN DEFAULT FALSE,

    --for auditing and debugging
                        processed_at TIMESTAMP WITH TIME ZONE
);

    --Index for the background poller
CREATE INDEX idx_outbox_unprocessed ON outbox (created_at)
    WHERE processed = FALSE;