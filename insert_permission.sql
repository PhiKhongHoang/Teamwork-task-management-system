INSERT INTO permissions (name, group_name, description) VALUES 
-- TEAM
('TEAM_CREATE', 'TEAM', 'Tạo team mới'),
('TEAM_VIEW_ALL', 'TEAM', 'Xem tất cả team'),
('TEAM_VIEW_DETAIL', 'TEAM', 'Xem chi tiết team'),
('TEAM_EDIT', 'TEAM', 'Chỉnh sửa thông tin team'),
('TEAM_DELETE', 'TEAM', 'Xóa team'),
('TEAM_INVITE_MEMBER', 'TEAM', 'Mời thành viên vào team'),
('TEAM_REMOVE_MEMBER', 'TEAM', 'Loại bỏ thành viên khỏi team'),
('TEAM_ASSIGN_ROLE', 'TEAM', 'Gán vai trò cho thành viên team'),
('TEAM_CHANGE_OWNER', 'TEAM', 'Chuyển quyền owner team'),
-- BOARD
('BOARD_CREATE', 'BOARD', 'Tạo board mới trong team'),
('BOARD_VIEW', 'BOARD', 'Xem board'),
('BOARD_EDIT', 'BOARD', 'Chỉnh sửa board'),
('BOARD_DELETE', 'BOARD', 'Xóa board'),
('BOARD_MEMBER_MANAGE', 'BOARD', 'Quản lý thành viên board'),
-- LIST
('LIST_CREATE', 'LIST', 'Tạo list trong board'),
('LIST_VIEW', 'LIST', 'Xem list trong board'),
('LIST_EDIT', 'LIST', 'Chỉnh sửa list'),
('LIST_DELETE', 'LIST', 'Xóa list'),
-- TASK
('TASK_CREATE', 'TASK', 'Tạo task mới'),
('TASK_VIEW', 'TASK', 'Xem task'),
('TASK_EDIT', 'TASK', 'Sửa nội dung task'),
('TASK_DELETE', 'TASK', 'Xóa task'),
('TASK_ASSIGN_MEMBER', 'TASK', 'Phân công task cho thành viên'),
('TASK_CHANGE_STATUS', 'TASK', 'Thay đổi trạng thái task'),
('TASK_COMMENT', 'TASK', 'Bình luận vào task'),
-- FILE
('FILE_UPLOAD', 'FILE', 'Upload file lên task/board'),
('FILE_DOWNLOAD', 'FILE', 'Tải file từ task/board'),
('FILE_DELETE', 'FILE', 'Xóa file khỏi task/board'),
-- USER
('USER_VIEW_PROFILE', 'UpermissionspermissionsSER', 'Xem profile người dùng'),
('USER_EDIT_PROFILE', 'USER', 'Chỉnh sửa profile cá nhân'),
('USER_MANAGE', 'USER', 'Quản lý tài khoản người dùng (admin)'),
-- SYSTEM
('SYSTEM_VIEW_AUDIT', 'SYSTEM', 'Xem log hệ thống'),
('SYSTEM_MANAGE_CONFIG', 'SYSTEM', 'Cấu hình, quản trị hệ thống');

SELECT * FROM ttms_ktn3.permissions;