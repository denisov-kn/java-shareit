insert into users (user_name, email) VALUES ('User1', '1@mail.ru');
insert into users (user_name, email) VALUES ('User2', '2@mail.ru');
insert into users (user_name, email) VALUES ('User3', '3@mail.ru');
insert into users (user_name, email) VALUES ('User4', '4@mail.ru');
insert into users (user_name, email) VALUES ('User5', '5@mail.ru');
insert into users (user_name, email) VALUES ('User6', '6@mail.ru');
insert into requests (description, requestor_id, created) VALUES ('ЗапросВещь1', 1,
                                                                  '2025-04-10T10:30:00');
insert into requests (description, requestor_id, created) VALUES ('ЗапросВещь2', 2,
                                                                  '2025-04-10T10:30:00');
insert into requests (description, requestor_id, created) VALUES ('ЗапросВещь3', 2,
                                                                  '2025-04-10T10:30:00');

insert into items (item_name, description, is_available, owner_id)
    VALUES ('Вещь1', 'Описание вещь1', true, 3);
insert into items (item_name, description, is_available, owner_id)
    VALUES ('Вещь2', 'Описание вещь2', true, 3);
insert into items (item_name, description, is_available, owner_id)
    VALUES ('Вещь3', 'Описание вещь3', false, 3);
insert into items (item_name, description, is_available, owner_id)
    VALUES ('Вещь4', 'Описание вещь4', false, 5);
insert into items (item_name, description, is_available, owner_id)
VALUES ('Вещь5', 'Описание вещь4', false, 2);



insert into bookings (start_date, end_date, item_id, booker_id, status)
    VALUES ('2025-04-10T10:30:00', '2025-04-12T10:30:00', 1, 2, 'APPROVED');
insert into comments (text, item_id, author_id, created)
    VALUES ('Комментарий Вещь1-1', 1, 2, '2025-04-13T10:30:00');
insert into bookings (start_date, end_date, item_id, booker_id, status)
    VALUES ('2025-03-10T10:30:00', '2025-03-12T10:30:00', 1, 4, 'APPROVED');
insert into comments (text, item_id, author_id, created)
VALUES ('Комментарий Вещь1-2', 1, 4, '2025-03-13T10:30:00');

insert into bookings (start_date, end_date, item_id, booker_id, status)
VALUES ('2025-04-10T10:30:00', '2025-04-12T10:30:00', 4, 6, 'APPROVED');

insert into bookings (start_date, end_date, item_id, booker_id, status)
VALUES ('2025-04-10T10:30:00', '2025-04-12T10:30:00', 4, 6, 'WAITING');

insert into bookings (start_date, end_date, item_id, booker_id, status)
VALUES ( '2099-04-10T10:30:00', '2099-04-12T10:30:00', 5, 6, 'APPROVED');



insert into items (item_name, description, is_available, owner_id, request_id)
    VALUES ('Вещь3_1', 'Описание Вещь3_1', true, 1, 3);
insert into items (item_name, description, is_available, owner_id, request_id)
VALUES ('Вещь3_2', 'Описание Вещь3_2', true, 1, 3);


