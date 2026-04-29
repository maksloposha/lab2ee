CREATE TABLE reviews (
                         id         SERIAL      PRIMARY KEY,
                         order_id   INTEGER     NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                         user_id    INTEGER     NOT NULL REFERENCES users(id)  ON DELETE CASCADE,
                         rating     INTEGER     NOT NULL CHECK (rating BETWEEN 1 AND 5),
                         comment    TEXT,
                         created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
                         UNIQUE (order_id, user_id)
);

ALTER TABLE menu_items ADD COLUMN IF NOT EXISTS rating NUMERIC(3,2) DEFAULT 0;
ALTER TABLE menu_items ADD COLUMN IF NOT EXISTS rating_count INTEGER DEFAULT 0;

CREATE INDEX idx_reviews_order ON reviews(order_id);
CREATE INDEX idx_reviews_user  ON reviews(user_id);