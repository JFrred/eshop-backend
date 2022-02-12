INSERT INTO users(id, username, password, first_name, last_name, email_address, acc_number,
                  is_enabled, is_locked, is_credentials_non_expired, city, street, postal_code, role)
VALUES (1, 'testuser', '$2a$10$CSd/KzbdUPKEyrJlHT2ehuvDnyBDsbtj8IvfLTmVopgm7VmCTPXTm', 'Mikael', 'Stanne',
        'mikael.stanne@email.test', '1111-2222-3333-4444', True, False, True, 'city', 'street', '12-345',
        'USER'); -- password='Password123!'
INSERT INTO users(id, username, password, first_name, last_name, email_address, acc_number,
                  is_enabled, is_locked, is_credentials_non_expired, city, street, postal_code, role)
VALUES (2, 'admin', '$2a$10$lmXL33l/.E6V/MdZzNQCEOtocpjoVn/yFFRO8vErIKi/MAQ2t1HTy', 'admin', 'admin',
        'admin@admin.test', '0000-0000-0000-0000', True, False, True, 'city', 'street', '12-345',
        'ADMIN'); -- password='admin!'


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

INSERT INTO order_details(id, created_at, modified_at, user_id, total, full_name, email, city, street, postal_code)
VALUES (1, now(), now(), 1, 5000, 'Mikael Stanne', 'mikael.stanne@email.test', 'city', 'street', '12-345');

INSERT INTO payment_details(id, created_at, modified_at, order_id, type, status)
VALUES (1, now(), now(), 1, 'TRANSFER', 'PENDING');

INSERT INTO order_items(id, created_at, modified_at, order_id, product_id, quantity)
VALUES (1, now(), now(), 1, 1, 1);
INSERT INTO order_items(id, created_at, modified_at, order_id, product_id, quantity)
VALUES (2, now(), now(), 1, 2, 2);