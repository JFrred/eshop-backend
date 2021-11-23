CREATE TABLE users
(
    id            INT(10) AUTO_INCREMENT,
    username      VARCHAR(30) UNIQUE NOT NULL,
    password      VARCHAR(100)       NOT NULL,
    first_name    VARCHAR(50)        NOT NULL,
    last_name     VARCHAR(50)        NOT NULL,
    email_address VARCHAR(50) UNIQUE NOT NULL,
    acc_number    VARCHAR(19) UNIQUE NOT NULL,
    role          VARCHAR(10)        NOT NULL,
    is_enabled    BOOL DEFAULT FALSE,
    city          VARCHAR(30)        NOT NULL,
    street        VARCHAR(50)        NOT NULL,
    postal_code   VARCHAR(6)         NOT NULL,
    created_at    DATETIME           NOT NULL,
    modified_at   DATETIME           NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE acc_activation_token
(
    id          INT(10) AUTO_INCREMENT,
    user_id     INT UNIQUE          NOT NULL,
    token       VARCHAR(255) UNIQUE NOT NULL,
    expires_at  DATETIME,
    created_at  DATETIME            NOT NULL,
    modified_at DATETIME            NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_auth_token_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE product
(
    id          INT(10) AUTO_INCREMENT,
    seller_id   INT                 NOT NULL,
    name        VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(1000)       NOT NULL,
    category    VARCHAR(50)         NOT NULL,
    price       DECIMAL(11, 2)      NOT NULL,
    created_at  DATETIME            NOT NULL,
    modified_at DATETIME            NOT NULL,
    UNIQUE KEY prod_index (id),
    PRIMARY KEY (id)
);

CREATE TABLE shopping_cart
(
    id          INT(10) AUTO_INCREMENT,
    user_id     INT(10)              DEFAULT NULL,
    total_price DECIMAL(10, 2) NOT NULL DEFAULT '0.00',
    created_at  DATETIME    NOT NULL,
    modified_at DATETIME    NOT NULL,
    UNIQUE KEY cart_index (id, user_id) USING BTREE,
    PRIMARY KEY (id),
    CONSTRAINT fk_shopping_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL
);

CREATE TABLE cart_item
(
    id          INT(10) AUTO_INCREMENT,
    cart_id  INT      NOT NULL,
    product_id  INT      NOT NULL,
    quantity    INT DEFAULT 0,
    created_at  DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_cart_id
        FOREIGN KEY (cart_id)
            REFERENCES shopping_cart (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL,
    CONSTRAINT fk_cart_item_product_id
        FOREIGN KEY (product_id)
            REFERENCES product (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL
);

CREATE TABLE payment_details
(
    id          INT(10) AUTO_INCREMENT,
    order_id    INT         NOT NULL,
    amount      INT DEFAULT 0,
    status      VARCHAR(50) NOT NULL,
    created_at  DATETIME    NOT NULL,
    modified_at DATETIME    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE order_details
(
    id          INT(10) AUTO_INCREMENT,
    user_id     INT(10),
    total       DECIMAL(10) NOT NULL,
    payment_id  INT(20)     NOT NULL,
    created_at  DATETIME    NOT NULL,
    modified_at DATETIME    NOT NULL,
    UNIQUE KEY order_index (id) USING BTREE,
    UNIQUE KEY customer_order_index (id, user_id) USING BTREE,
    PRIMARY KEY (id),
    CONSTRAINT fk_shopping_user_order
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL,
    CONSTRAINT fk_order_payment
        FOREIGN KEY (payment_id)
            REFERENCES payment_details (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL
);

CREATE TABLE order_items
(
    id          INT(10) AUTO_INCREMENT,
    order_id    INT      NOT NULL,
    product_id  INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_id
        FOREIGN KEY (order_id)
            REFERENCES order_details (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL,
    CONSTRAINT fk_order_items_product_id
        FOREIGN KEY (product_id)
            REFERENCES product (id)
            ON DELETE SET NULL
            ON UPDATE SET NULL
);
