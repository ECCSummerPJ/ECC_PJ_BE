INSERT INTO users (login_id, email, password_hash, nickname, profile_image, created_at, updated_at)
    VALUES (
    'demo1',
    'demo1@example.com',
    'pw',
    '데모유저1',
    '/uploads/sample1.png',
    CURRENT_TIMESTAMP(),
    NULL
    );

INSERT INTO users (login_id, email, password_hash, nickname, profile_image, created_at, updated_at)
    VALUES (
    'demo2',
    'demo2@example.com',
    'pw',
    '데모유저2',
    '/uploads/sample2.jpg',
    CURRENT_TIMESTAMP(),
    NULL

    );

INSERT INTO users (login_id, email, password_hash, nickname, profile_image)
    VALUES (
    'demo3',
    'demo3@example.com',
    'pw',
    '데모유저3',
    '/uploads/sample3.jpg'
    );

INSERT INTO users (login_id, email, password_hash, nickname, profile_image)
    VALUES (
    'demo4',
    'demo4@example.com',
    'pw',
    '데모유저4',
    '/uploads/sample4.jpg'
    );


INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '고양이', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '맛집', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (1, '게임', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (2, '강아지', CURRENT_TIMESTAMP(), NULL);
INSERT INTO category(user_id, category_name, created_at, updated_at) VALUES (2, '취미', CURRENT_TIMESTAMP(), NULL);

INSERT INTO scrap(user_id, category_id, title, url, memo, is_favorite, is_public, created_at) VALUES (1, 1, '춘봉이', 'https://youtube.com/...', '귀여워요', true, true, CURRENT_TIMESTAMP());
INSERT INTO scrap(user_id, category_id, title, url, memo, is_favorite, is_public, created_at) VALUES (2, 3, 'LCK 명장면', 'https://youtube.com/...', '개잘해..', false, false, CURRENT_TIMESTAMP());
INSERT INTO scrap(user_id, category_id, title, url, memo, is_favorite, is_public, created_at) VALUES (3, 1, '동글이', 'https://youtube.com/...', '진심으로귀여워요', false, true, CURRENT_TIMESTAMP());
INSERT INTO scrap(user_id, category_id, title, url, memo, is_favorite, is_public, created_at) VALUES (1, 4, '우유', 'https://youtube.com/...', '사모예드는 눈으로 기른다', false, true, CURRENT_TIMESTAMP());
INSERT INTO scrap(user_id, category_id, title, url, memo, is_favorite, is_public, created_at) VALUES (1, 1, '치즈', 'https://www.instagram.com/reel/DH4qKa4OCYk/?igsh=aGIxaW5za2xkYjdz', 'cute cat', false, true, CURRENT_TIMESTAMP());

INSERT INTO rescrap(user_id, category_id, scrap_id, redirect_link, created_at) VALUES (2, 1, 1, '/api/scraps/1', CURRENT_TIMESTAMP());
INSERT INTO rescrap(user_id, category_id, scrap_id, redirect_link, created_at) VALUES (2, 1, 3, '/api/scraps/3', CURRENT_TIMESTAMP());
INSERT INTO rescrap(user_id, category_id, scrap_id, redirect_link, created_at) VALUES (2, 1, 4, '/api/scraps/4', CURRENT_TIMESTAMP());

INSERT INTO comment(user_id, scrap_id, content, created_at, updated_at) VALUES (1, 2, '사용자1의 댓글입니다.', CURRENT_TIMESTAMP(), NULL);
INSERT INTO comment(user_id, scrap_id, content, created_at, updated_at) VALUES (2, 3, '사용자2의 댓글입니다.', CURRENT_TIMESTAMP(), NULL);
INSERT INTO comment(user_id, scrap_id, content, created_at, updated_at) VALUES (3, 1, '사용자3의 댓글입니다.', CURRENT_TIMESTAMP(), NULL);
INSERT INTO comment(user_id, scrap_id, content, created_at, updated_at) VALUES (1, 1, '너무 귀여워요!', CURRENT_TIMESTAMP(), NULL);

INSERT INTO friend(user_id, friend_user_id) VALUES (1, 2);
INSERT INTO friend(user_id, friend_user_id) VALUES (1, 3);
INSERT INTO friend(user_id, friend_user_id) VALUES (2, 3);