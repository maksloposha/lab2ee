ALTER TABLE orders ADD COLUMN IF NOT EXISTS user_id INTEGER REFERENCES users(id) ON DELETE SET NULL;

UPDATE orders SET user_id = 2 WHERE customer_name = 'Іван Іваненко';
UPDATE orders SET user_id = 3 WHERE customer_name = 'Василь Петренко';

CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);