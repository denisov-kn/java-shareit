
CREATE TABLE IF NOT EXISTS users (
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS bookings(
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id INT NOT NULL,
    booker_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items (
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    is_available BOOLEAN,
    owner_id INT NOT NULL,
    request_id INT,
    CONSTRAINT pk_item PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS comments(
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(512) NOT NULL,
    item_id INT NOT NULL,
    author_id INT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE
    );


ALTER TABLE items
    ADD CONSTRAINT fk_item_users FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE items
    ADD CONSTRAINT fk_item_bookings FOREIGN KEY (request_id) REFERENCES bookings(id) ON DELETE CASCADE;


ALTER TABLE bookings
    ADD CONSTRAINT fk_booking_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE;

ALTER TABLE bookings
    ADD CONSTRAINT fk_booking_user FOREIGN KEY (booker_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_user FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE;