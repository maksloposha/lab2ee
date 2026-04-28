DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders      CASCADE;
DROP TABLE IF EXISTS menu_items  CASCADE;
DROP TABLE IF EXISTS users       CASCADE;


CREATE TABLE users (
                       id            SERIAL       PRIMARY KEY,
                       username      VARCHAR(50)  NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       full_name     VARCHAR(100) NOT NULL,
                       email         VARCHAR(100) NOT NULL UNIQUE,
                       phone         VARCHAR(20)  DEFAULT '',
                       role          VARCHAR(10)  NOT NULL DEFAULT 'USER'
                           CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE menu_items (
                            id          SERIAL         PRIMARY KEY,
                            name        VARCHAR(100)   NOT NULL,
                            description TEXT,
                            price       NUMERIC(8, 2)  NOT NULL CHECK (price > 0),
                            category    VARCHAR(20)    NOT NULL
                                CHECK (category IN ('STARTERS','SOUPS','MAIN_COURSE','DESSERTS','DRINKS')),
                            available   BOOLEAN        NOT NULL DEFAULT TRUE,
                            calories    INTEGER        NOT NULL DEFAULT 0 CHECK (calories >= 0)
);

CREATE TABLE orders (
                        id               SERIAL       PRIMARY KEY,
                        customer_name    VARCHAR(100) NOT NULL,
                        customer_phone   VARCHAR(20)  NOT NULL,
                        delivery_address TEXT         NOT NULL,
                        notes            TEXT         DEFAULT '',
                        status           VARCHAR(15)  NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('PENDING','CONFIRMED','PREPARING','READY','DELIVERED','CANCELLED')),
                        created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
                        updated_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE order_items (
                             id                   SERIAL       PRIMARY KEY,
                             order_id             INTEGER      NOT NULL
                                 REFERENCES orders(id)     ON DELETE CASCADE,
                             menu_item_id         INTEGER      NOT NULL
                                 REFERENCES menu_items(id) ON DELETE RESTRICT,
                             quantity             INTEGER      NOT NULL CHECK (quantity >= 1 AND quantity <= 50),
                             special_instructions VARCHAR(200) DEFAULT ''
);


CREATE INDEX idx_menu_items_category  ON menu_items(category);
CREATE INDEX idx_menu_items_available ON menu_items(available);
CREATE INDEX idx_orders_status        ON orders(status);
CREATE INDEX idx_orders_created_at    ON orders(created_at DESC);
CREATE INDEX idx_order_items_order    ON order_items(order_id);

-- ============================================================
-- Тестові дані
-- ============================================================

INSERT INTO users (username, password_hash, full_name, email, phone, role) VALUES
                                                                               ('admin', 'admin123', 'Адміністратор Системи', 'admin@smakua.com',  '+380501110000', 'ADMIN'),
                                                                               ('user',  'user123',  'Іван Іваненко',          'ivan@example.com',  '+380502220001', 'USER'),
                                                                               ('vasyl', 'pass123',  'Василь Петренко',         'vasyl@example.com', '+380503330002', 'USER');

INSERT INTO menu_items (name, description, price, category, available, calories) VALUES
                                                                                     ('Брускета з томатами',  'Хрустка чіабата з томатами черрі та базиліком',         89.00,  'STARTERS',    TRUE,  210),
                                                                                     ('Крем-суп з гарбуза',   'Ніжний суп-пюре зі вершками та насінням гарбуза',        120.00, 'SOUPS',       TRUE,  180),
                                                                                     ('Борщ із пампушками',   'Традиційний червоний борщ з пампушками та часником',     145.00, 'SOUPS',       TRUE,  320),
                                                                                     ('Стейк рибай',          'Соковитий стейк 300г на грилі з картопляним пюре',       680.00, 'MAIN_COURSE', TRUE,  750),
                                                                                     ('Паста карбонара',      'Класична паста з панчетою та пармезаном',                245.00, 'MAIN_COURSE', TRUE,  520),
                                                                                     ('Лосось на грилі',      'Філе лосося з овочами гриль та соусом тартар',           395.00, 'MAIN_COURSE', FALSE, 420),
                                                                                     ('Курча по-київськи',    'Котлета по-київськи з вершковим маслом та зеленню',      285.00, 'MAIN_COURSE', TRUE,  580),
                                                                                     ('Тірамісу',             'Класичний десерт із маскарпоне та кавою',                135.00, 'DESSERTS',    TRUE,  380),
                                                                                     ('Чізкейк Нью-Йорк',    'Ніжний чізкейк з ягідним соусом',                        125.00, 'DESSERTS',    TRUE,  420),
                                                                                     ('Апельсиновий сік',     'Сік із свіжих апельсинів 300 мл',                         85.00,  'DRINKS',      TRUE,  110),
                                                                                     ('Лимонад домашній',     'Лимонад з м''ятою та імбиром 500 мл',                     75.00,  'DRINKS',      TRUE,  95),
                                                                                     ('Капучіно',             'Ароматна кава з молочною піною 200 мл',                   65.00,  'DRINKS',      TRUE,  85);

INSERT INTO orders (customer_name, customer_phone, delivery_address, status, created_at) VALUES
                                                                                             ('Іван Іваненко',   '+380502220001', 'вул. Хрещатик, 1, кв. 5',      'PENDING',   NOW() - INTERVAL '10 minutes'),
                                                                                             ('Василь Петренко', '+380503330002', 'вул. Велика Васильківська, 45', 'PREPARING', NOW() - INTERVAL '35 minutes'),
                                                                                             ('Іван Іваненко',   '+380502220001', 'вул. Хрещатик, 1, кв. 5',      'DELIVERED', NOW() - INTERVAL '2 hours'),
                                                                                             ('Василь Петренко', '+380503330002', 'вул. Велика Васильківська, 45', 'CANCELLED', NOW() - INTERVAL '1 hour');

INSERT INTO order_items (order_id, menu_item_id, quantity, special_instructions) VALUES
                                                                                     (1, 1, 2, ''),
                                                                                     (1, 3, 1, 'Без сметани'),
                                                                                     (1, 12, 2, ''),
                                                                                     (2, 4, 1, 'Medium прожарювання'),
                                                                                     (2, 8, 1, ''),
                                                                                     (3, 5, 2, ''),
                                                                                     (3, 9, 1, ''),
                                                                                     (4, 4, 2, '');