-- Gán ROLE_ADMIN cho 2 user đầu (user_id = 1,2; role_id = 1)
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1), (2, 1);

-- Gán ROLE_USER cho 3 user tiếp theo (user_id = 3,4,5; role_id = 2)
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2), (4, 2), (5, 2);

-- Gán OWNER cho user 6 (user_id = 6; role_id = 3)
INSERT INTO user_roles (user_id, role_id) VALUES (6, 3);

-- Gán ADMIN cho user 7 (user_id = 7; role_id = 4)
INSERT INTO user_roles (user_id, role_id) VALUES (7, 4);

-- Gán TEAM MEMBER cho user 8 (user_id = 8; role_id = 5)
INSERT INTO user_roles (user_id, role_id) VALUES (8, 5);

-- Gán PROJECT LEADER cho user 9 (user_id = 9; role_id = 7)
INSERT INTO user_roles (user_id, role_id) VALUES (9, 7);

-- Gán PROJECT MEMBER cho user 10 (user_id = 10; role_id = 8)
INSERT INTO user_roles (user_id, role_id) VALUES (10, 8);

-- Gán PROJECT GUEST cho user 11 (user_id = 11; role_id = 9)
INSERT INTO user_roles (user_id, role_id) VALUES (11, 9);


SELECT * FROM ttms_ktn3.user_roles;