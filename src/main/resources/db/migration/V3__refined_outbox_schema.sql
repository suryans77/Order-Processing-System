-- Make created_at NOT NULL and ensure TIMESTAMPTZ with default
ALTER TABLE outbox
    ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN created_at SET DEFAULT now();

-- Make processed NOT NULL with default false
ALTER TABLE outbox
    ALTER COLUMN processed SET NOT NULL,
ALTER COLUMN processed SET DEFAULT false;

-- Ensure processed_at is TIMESTAMPTZ (already likely is)
ALTER TABLE outbox
ALTER COLUMN processed_at TYPE TIMESTAMPTZ;

-- (Optional) If it was TIMESTAMP without TZ before:
ALTER TABLE outbox
ALTER COLUMN created_at TYPE TIMESTAMPTZ
    USING created_at AT TIME ZONE 'UTC';

-- Drop old index if it exists
DROP INDEX IF EXISTS idx_outbox_unprocessed;

-- Recreate partial index
CREATE INDEX idx_outbox_unprocessed
    ON outbox (created_at)
    WHERE processed = FALSE;
