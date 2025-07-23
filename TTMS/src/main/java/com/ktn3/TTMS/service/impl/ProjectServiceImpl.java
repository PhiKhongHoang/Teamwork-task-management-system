package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.constant.ProjectStatus;
import com.ktn3.TTMS.constant.RoleScope;
import com.ktn3.TTMS.dto.request.project.ReqCreateProject;
import com.ktn3.TTMS.dto.request.project.ReqInviteProjectMember;
import com.ktn3.TTMS.dto.request.project.ReqUpdateProject;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.*;
import com.ktn3.TTMS.repository.*;
import com.ktn3.TTMS.security.JwtProvider;
import com.ktn3.TTMS.service.MailService;
import com.ktn3.TTMS.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final TeamRepo teamRepo;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ProjectMemberRepo projectMemberRepo;
    private final MailService mailService;
    private final ProjectInviteRepo projectInviteRepo;

    private static final int INVITE_EXPIRED_DAYS = 7;

    // Helper: Kiểm tra member có phải Leader/Admin không
    private boolean isLeaderOrAdmin(ProjectMember pm) {
        if (pm == null || pm.getRole() == null) return false;
        String roleName = pm.getRole().getName();
        return "LEADER".equals(roleName) || "ADMIN".equals(roleName);
    }

    @Override
    public ResCommonApi<?> create(ReqCreateProject req) {
        try {
            Team team = null;
            if (req.getIdTeam() != null) {
                team = teamRepo.findById(req.getIdTeam()).orElse(null);
            }

            String email = JwtProvider.getAuthenticatedUserEmail();
            User user = userRepo.findByEmail(email).orElse(null);
            if (user == null) return ResCommonApi.error("Không tìm thấy tài khoản tạo project!", 401);

            Project project = Project.builder()
                    .name(req.getName())
                    .description(req.getDescription())
                    .deadline(req.getDeadline())
                    .status(ProjectStatus.NEW)
                    .team(team)
                    .createdBy(user)
                    .build();
            Project saveProject = projectRepo.save(project);

            // Gán vai trò LEADER cho người tạo project
            Optional<Role> leaderRoleOpt = roleRepo.findByNameAndScope("LEADER", RoleScope.PROJECT);
            if (leaderRoleOpt.isEmpty()) {
                return ResCommonApi.error("Không tìm thấy role LEADER", 400);
            }
            Role leaderRole = leaderRoleOpt.get();

            ProjectMember pm = ProjectMember.builder()
                    .project(saveProject)
                    .user(user)
                    .role(leaderRole)
                    .joinedAt(LocalDateTime.now())
                    .build();
            projectMemberRepo.save(pm);

            return ResCommonApi.success(saveProject, "Tạo project thành công!");
        } catch (Exception e) {
            return ResCommonApi.error("Lỗi tạo project: " + e.getMessage(), 500);
        }
    }

    /**
     * Mời thành viên vào project qua email
     */
    @Override
    public ResCommonApi<?> inviteMember(Long projectId, ReqInviteProjectMember req, User inviter) {
        if (projectId == null)
            return ResCommonApi.error("Project không tồn tại!", 400);

        Optional<Project> projectOptional = projectRepo.findById(projectId);
        if (projectOptional.isEmpty())
            return ResCommonApi.error("Project không tồn tại!", 400);
        Project project = projectOptional.get();

        if (req.getRoleId() == null)
            return ResCommonApi.error("Role không hợp lệ!", 400);
        Optional<Role> roleOptional = roleRepo.findById(req.getRoleId());
        if (roleOptional.isEmpty())
            return ResCommonApi.error("Role không hợp lệ!", 400);
        Role role = roleOptional.get();

        if (req.getEmail() == null || req.getEmail().isBlank())
            return ResCommonApi.error("Email không hợp lệ!", 400);

        if (inviter == null)
            return ResCommonApi.error("Không tìm thấy tài khoản mời!", 401);

        ProjectMember inviterPm = projectMemberRepo.findByProjectAndUser(project, inviter).orElse(null);
        if (!isLeaderOrAdmin(inviterPm))
            return ResCommonApi.error("Chỉ Leader/Admin mới được mời thành viên!", 403);

        Optional<ProjectInvite> inviteOpt = projectInviteRepo.findByProjectIdAndEmail(projectId, req.getEmail());
        if (inviteOpt.isPresent()) {
            ProjectInvite existInvite = inviteOpt.get();
            if ("PENDING".equals(existInvite.getStatus()) &&
                    (existInvite.getExpiredAt() == null || existInvite.getExpiredAt().isAfter(LocalDateTime.now()))) {
                return ResCommonApi.error("Email này đã được mời, vui lòng kiểm tra email!", 400);
            }
        }

        String token = UUID.randomUUID().toString();
        ProjectInvite invite = ProjectInvite.builder()
                .project(project)
                .email(req.getEmail())
                .role(role)
                .token(token)
                .status("PENDING")
                .invitedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(INVITE_EXPIRED_DAYS))
                .build();
        projectInviteRepo.save(invite);

        String inviteLink = "http://your-app.com/accept-invite?token=" + token;
        mailService.sendProjectInviteEmail(req.getEmail(), project.getName(), inviteLink);

        return ResCommonApi.success(null, "Đã gửi email mời tham gia. Người nhận phải xác nhận để trở thành thành viên!");
    }

    /**
     * Chấp nhận lời mời tham gia
     */
    @Override
    public ResCommonApi<?> acceptInvite(String token, User currentUser) {
        if (token == null || token.isBlank())
            return ResCommonApi.error("Token invite không hợp lệ!", 400);

        Optional<ProjectInvite> opt = projectInviteRepo.findByToken(token);
        if (opt.isEmpty())
            return ResCommonApi.error("Token invite không hợp lệ!", 400);

        ProjectInvite invite = opt.get();
        if (!"PENDING".equals(invite.getStatus()))
            return ResCommonApi.error("Lời mời đã được xử lý hoặc không còn hiệu lực!", 400);

        if (invite.getExpiredAt() != null && invite.getExpiredAt().isBefore(LocalDateTime.now()))
            return ResCommonApi.error("Lời mời đã hết hạn!", 400);

        if (currentUser == null || !currentUser.getEmail().equalsIgnoreCase(invite.getEmail()))
            return ResCommonApi.error("Bạn cần đăng nhập đúng email được mời!", 403);

        if (projectMemberRepo.findByProjectAndUser(invite.getProject(), currentUser).isPresent()) {
            invite.setStatus("ACCEPTED");
            invite.setAcceptedAt(LocalDateTime.now());
            projectInviteRepo.save(invite);
            return ResCommonApi.success(null, "Bạn đã là thành viên project này!");
        }
        // Thêm vào project_members
        ProjectMember pm = ProjectMember.builder()
                .project(invite.getProject())
                .user(currentUser)
                .role(invite.getRole())
                .joinedAt(LocalDateTime.now())
                .build();
        projectMemberRepo.save(pm);

        invite.setStatus("ACCEPTED");
        invite.setAcceptedAt(LocalDateTime.now());
        projectInviteRepo.save(invite);

        return ResCommonApi.success(null, "Bạn đã tham gia project thành công!");
    }

    /**
     * Mời lại thành viên vào project qua email
     */
    @Override
    public ResCommonApi<?> resendInvite(Long projectId, String inviteEmail, User actor) {
        if (projectId == null || inviteEmail == null || inviteEmail.isBlank())
            return ResCommonApi.error("Thiếu dữ liệu!", 400);

        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null)
            return ResCommonApi.error("Project không tồn tại!", 404);

        ProjectMember actorPm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (!isLeaderOrAdmin(actorPm))
            return ResCommonApi.error("Chỉ Leader/Admin mới được gửi lại invite!", 403);

        Optional<ProjectInvite> opt = projectInviteRepo.findByProjectIdAndEmail(projectId, inviteEmail);
        if (opt.isEmpty())
            return ResCommonApi.error("Không tìm thấy lời mời!", 400);

        ProjectInvite invite = opt.get();

        if (!"PENDING".equals(invite.getStatus()))
            return ResCommonApi.error("Lời mời đã được xử lý hoặc hết hiệu lực!", 400);
        if (invite.getExpiredAt() != null && invite.getExpiredAt().isBefore(LocalDateTime.now()))
            return ResCommonApi.error("Lời mời đã hết hạn, hãy mời lại!", 400);

        String inviteLink = "http://your-app.com/accept-invite?token=" + invite.getToken();
        mailService.sendProjectInviteEmail(inviteEmail, project.getName(), inviteLink);

        return ResCommonApi.success(null, "Đã gửi lại email mời!");
    }

    /**
     * Thay đổi vai trò thành viên
     */
    @Override
    public ResCommonApi<?> changeMemberRole(Long projectId, Long userId, Long newRoleId, User actor) {
        if (projectId == null || userId == null || newRoleId == null)
            return ResCommonApi.error("Thiếu dữ liệu!", 400);

        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null)
            return ResCommonApi.error("Project không tồn tại!", 404);

        ProjectMember actorPm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (!isLeaderOrAdmin(actorPm))
            return ResCommonApi.error("Chỉ Leader/Admin được thay đổi vai trò!", 403);

        User user = userRepo.findById(userId).orElse(null);
        if (user == null)
            return ResCommonApi.error("User không tồn tại!", 404);

        ProjectMember memberPm = projectMemberRepo.findByProjectAndUser(project, user).orElse(null);
        if (memberPm == null)
            return ResCommonApi.error("User không phải thành viên project", 404);

        Role newRole = roleRepo.findById(newRoleId).orElse(null);
        if (newRole == null)
            return ResCommonApi.error("Role không tồn tại", 404);

        memberPm.setRole(newRole);
        projectMemberRepo.save(memberPm);
        return ResCommonApi.success(null, "Đã cập nhật vai trò thành viên!");
    }

    /**
     * Xóa thành viên
     */
    @Override
    public ResCommonApi<?> removeMember(Long projectId, Long userId, User actor) {
        if (projectId == null || userId == null)
            return ResCommonApi.error("Thiếu dữ liệu!", 400);

        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null)
            return ResCommonApi.error("Project không tồn tại!", 404);

        ProjectMember actorPm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (!isLeaderOrAdmin(actorPm))
            return ResCommonApi.error("Chỉ Leader/Admin được xoá thành viên!", 403);

        User user = userRepo.findById(userId).orElse(null);
        if (user == null)
            return ResCommonApi.error("User không tồn tại!", 404);

        ProjectMember memberPm = projectMemberRepo.findByProjectAndUser(project, user).orElse(null);
        if (memberPm == null)
            return ResCommonApi.error("User không phải thành viên project", 404);

        projectMemberRepo.delete(memberPm);
        return ResCommonApi.success(null, "Đã xoá thành viên!");
    }

    /**
     * Đổi leader
     */
    @Override
    public ResCommonApi<?> transferLeader(Long projectId, Long newLeaderId, User actor) {
        if (projectId == null || newLeaderId == null)
            return ResCommonApi.error("Thiếu dữ liệu!", 400);

        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null)
            return ResCommonApi.error("Project không tồn tại!", 404);

        ProjectMember actorPm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (actorPm == null || !"LEADER".equals(actorPm.getRole().getName()))
            return ResCommonApi.error("Chỉ Leader được chuyển quyền!", 403);

        User newLeader = userRepo.findById(newLeaderId).orElse(null);
        if (newLeader == null)
            return ResCommonApi.error("User không tồn tại!", 404);

        ProjectMember newLeaderPm = projectMemberRepo.findByProjectAndUser(project, newLeader).orElse(null);
        if (newLeaderPm == null)
            return ResCommonApi.error("User không phải thành viên project", 404);

        Role leaderRole = roleRepo.findByNameAndScope("LEADER", RoleScope.PROJECT).orElse(null);
        Role memberRole = roleRepo.findByNameAndScope("MEMBER", RoleScope.PROJECT).orElse(null);
        if (leaderRole == null || memberRole == null)
            return ResCommonApi.error("Role không tồn tại!", 404);

        // Actor thành MEMBER, newLeader thành LEADER
        actorPm.setRole(memberRole);
        newLeaderPm.setRole(leaderRole);
        projectMemberRepo.save(actorPm);
        projectMemberRepo.save(newLeaderPm);

        return ResCommonApi.success(null, "Chuyển quyền leader thành công!");
    }

    /**
     * Cập nhật project
     */
    @Override
    public ResCommonApi<?> updateProject(Long projectId, ReqUpdateProject req, User actor) {
        if (projectId == null)
            return ResCommonApi.error("Thiếu dữ liệu!", 400);

        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null)
            return ResCommonApi.error("Project không tồn tại!", 404);

        ProjectMember actorPm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (!isLeaderOrAdmin(actorPm))
            return ResCommonApi.error("Chỉ Leader/Admin được cập nhật project!", 403);

        project.setName(req.getName());
        project.setDescription(req.getDescription());
        project.setDeadline(req.getDeadline());
        project.setStatus(req.getStatus());
        projectRepo.save(project);

        return ResCommonApi.success(null, "Đã cập nhật project!");
    }

    /**
     * Xóa project
     */
    @Override
    public ResCommonApi<?> deleteProject(Long projectId, User actor) {
        if (projectId == null)
            return ResCommonApi.error("Thiếu dữ liệu!", 400);

        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null)
            return ResCommonApi.error("Project không tồn tại!", 404);

        ProjectMember actorPm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (!isLeaderOrAdmin(actorPm))
            return ResCommonApi.error("Chỉ Leader/Admin được xoá project!", 403);

        projectRepo.delete(project);
        return ResCommonApi.success(null, "Đã xoá project!");
    }

    /**
     * Xem danh sách project tham gia (theo user)
     */
    public ResCommonApi<?> findProjectsByUser(User user) {
        if (user == null) {
            return ResCommonApi.error("User không hợp lệ!", 400);
        }
        List<ProjectMember> list = projectMemberRepo.findByUser(user);
        List<Project> projects = list.stream()
                .map(ProjectMember::getProject)
                .collect(Collectors.toList());

        return ResCommonApi.success(projects, "Lấy danh sách project thành công!");
    }


}
