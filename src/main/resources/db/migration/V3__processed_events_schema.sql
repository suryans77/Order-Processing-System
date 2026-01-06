CREATE TABLE order_processed_events (
                                        event_id UUID PRIMARY KEY,
                                        processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE payment_processed_events (
                                          event_id UUID PRIMARY KEY,
                                          processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE inventory_processed_events (
                                            event_id UUID PRIMARY KEY,
                                            processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE refund_processed_events (
                                         event_id UUID PRIMARY KEY,
                                         processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);