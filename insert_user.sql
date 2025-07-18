-- Admins
INSERT INTO users (email, password, name, active) VALUES
('admin1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Admin One', true),
('admin2@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Admin Two', true);

-- System user
INSERT INTO users (email, password, name, active) VALUES
('user1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'User One', true),
('user2@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'User Two', true),
('user3@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'User Three', true);

-- Team Owner
INSERT INTO users (email, password, name, active) VALUES
('owner1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Owner One', true);

-- Team Admin
INSERT INTO users (email, password, name, active) VALUES
('teamadmin1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Team Admin One', true);

-- Team Member
INSERT INTO users (email, password, name, active) VALUES
('teammember1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Team Member One', true);

-- Project Leader
INSERT INTO users (email, password, name, active) VALUES
('leader1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Project Leader', true);

-- Project Member
INSERT INTO users (email, password, name, active) VALUES
('projectmember1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Project Member', true);

-- Project Guest
INSERT INTO users (email, password, name, active) VALUES
('projectguest1@example.com', '$2a$10$7sFrwA.JaVLzVwWeYhGh0OIl42ecQYdNHI5slBOpZEjBHDri86qKu', 'Project Guest', true);


SELECT * FROM ttms_ktn3.users;