INSERT INTO USERS(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, ACC_NUMBER, CITY, STREET,
                  POSTAL_CODE, ROLE, IS_ENABLED, CREATED_AT, MODIFIED_AT)
VALUES (5, 'johncena', '$2a$10$kcknIelQgM/CAgFX.BgmcuQN4klRjPfmd7O159D9tn3Jyhyenfnfi', 'John', 'Cena',
        'john@cena.io', '1234 4321 0000 0666',
        'Wroclaw', 'Fabryczna', '12-345', 'USER', TRUE, '2021-11-12 17:32:22.404119', '2021-11-12 17:32:22.404119');

INSERT INTO acc_activation_token(id, user_id, token, expires_at, created_at, modified_at)
VALUES (1, 5, '56a157a1-2cfe-4c83-a52a-e45a0cf2820a', '2021-11-12 17:47:22.434585',
        '2021-11-12 17:32:22.437529', '2021-11-12 17:32:22.437529');

INSERT INTO SHOPPING_CART(ID, USER_ID, TOTAL_PRICE, CREATED_AT, MODIFIED_AT)
VALUES (1, 5, 0, NOW(), NOW());

INSERT INTO product
VALUES (1, 5, 'product 0', 'descriptionnnnnnnnnn000', 'BEAUTY', 1.50, NOW(), NOW());
INSERT INTO product
VALUES (2, 5, 'product 1', 'descriptionnnnnnnnnn111', 'BEAUTY', 91.23, NOW(), NOW());
INSERT INTO product
VALUES (3, 5, 'product 2', 'descriptionnnnnnnnnn222', 'BEAUTY', 6.66, NOW(), NOW());
INSERT INTO product
VALUES (4, 5, 'product 3', 'descriptionnnnnnnnnn333', 'ENTERTAINMENT', 21.50, NOW(), NOW());
INSERT INTO product
VALUES (5, 5, 'product 4', 'descriptionnnnnnnnnn000', 'ENTERTAINMENT', 101.00, NOW(), NOW());
