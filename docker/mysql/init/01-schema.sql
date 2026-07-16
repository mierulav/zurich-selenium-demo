CREATE TABLE IF NOT EXISTS products (
    name VARCHAR(255) PRIMARY KEY,
    price DECIMAL(10,2) NOT NULL
);

INSERT INTO products (name, price) VALUES
    ('Sauce Labs Backpack', 29.99),
    ('Sauce Labs Bike Light', 9.99),
    ('Sauce Labs Bolt T-Shirt', 15.99),
    ('Sauce Labs Fleece Jacket', 49.99),
    ('Sauce Labs Onesie', 7.99),
    ('Test.allTheThings() T-Shirt (Red)', 15.99)
ON DUPLICATE KEY UPDATE price = VALUES(price);
