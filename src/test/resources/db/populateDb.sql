INSERT INTO users(id, username, password, first_name, last_name, email_address, acc_number, is_enabled, city, street,
                  postal_code)
VALUES (1, 'testuser', '$2a$10$CSd/KzbdUPKEyrJlHT2ehuvDnyBDsbtj8IvfLTmVopgm7VmCTPXTm', 'Mikael', 'Stanne',
        'mikael.stanne@email.test', '1111-2222-3333-4444', True, 'city', 'street', '12-345'); -- password='Password123!'

INSERT INTO cart(id, user_id, total_price)
VALUES (1, 1, 1000);

INSERT INTO product(id, name, description, category, price, img_url)
VALUES (1, 'specialized stumpjumper', 'description for specialized stumpjumper', 'MOUNTAIN', 1000,
        'specialized-stumpjumper-some-image-url.jpg');
INSERT INTO product(id, name, description, category, price, img_url)
VALUES (2, 'pinarello dogma', 'description for pinarello dogma', 'ROAD', 2000, 'pinarello-dogma-some-image-url.jpg');

INSERT INTO cart_item(id, cart_id, product_id, quantity)
VALUES (1, 1, 1, 1);
INSERT INTO cart_item(id, cart_id, product_id, quantity)
VALUES (2, 1, 2, 2);

INSERT INTO order_details(id, user_id, total)
VALUES (1, 1, 5000);

INSERT INTO payment_details(id, order_id, type, status)
VALUES (1, 1, 'TRANSFER', 'PENDING');

INSERT INTO order_items(id, order_id, product_id, quantity)
VALUES (1, 1, 1, 1);
INSERT INTO order_items(id, order_id, product_id, quantity)
VALUES (2, 1, 2, 2);