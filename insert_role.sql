INSERT INTO roles (name, scope) VALUES
-- System roles
('ROLE_ADMIN', 'SYSTEM'),
('ROLE_USER', 'SYSTEM'),

-- Team roles
('OWNER', 'TEAM'),
('ADMIN', 'TEAM'),
('MEMBER', 'TEAM'),
('GUEST', 'TEAM'),

-- Project roles
('LEADER', 'PROJECT'),
('MEMBER', 'PROJECT'),
('GUEST', 'PROJECT');

SELECT * FROM ttms_ktn3.roles;