INSERT INTO users (user_id, email, password, nickname, profile_image)
    VALUES (
    1,
    'demo1@example.com',
    'pw',
    '데모유저1',
    X'89504E470D0A1A0A0000000D49484452' -- PNG 파일의 앞부분 예시 (짧게 넣음)
    );

INSERT INTO users (user_id, email, password, nickname, profile_image)
    VALUES (
    2,
    'demo2@example.com',
    'pw',
    '데모유저2',
    X'FFD8FFE000104A464946000101010048' -- JPG 파일의 앞부분 예시
    );

INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '고양이', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '맛집', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '게임', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (2, '강아지', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (2, '취미', CURRENT_TIMESTAMP(), NULL);