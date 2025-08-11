INSERT INTO users (email, password, nickname, profile_image)
    VALUES (
    'demo1@example.com',
    'pw',
    '데모유저1',
    '/uploads/sample1.png'
    );

INSERT INTO users (email, password, nickname, profile_image)
    VALUES (
    'demo2@example.com',
    'pw',
    '데모유저2',
    '/uploads/sample2.jpg'
    );

INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '고양이', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '맛집', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '게임', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (2, '강아지', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (2, '취미', CURRENT_TIMESTAMP(), NULL);

