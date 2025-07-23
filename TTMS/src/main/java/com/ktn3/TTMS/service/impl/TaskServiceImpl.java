package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.constant.TaskPriority;
import com.ktn3.TTMS.constant.TaskStatus;
import com.ktn3.TTMS.dto.request.task.ReqCreateTask;
import com.ktn3.TTMS.dto.request.task.ReqUpdateTask;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.dto.response.task.ResTask;
import com.ktn3.TTMS.entity.*;
import com.ktn3.TTMS.repository.*;
import com.ktn3.TTMS.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepo taskRepo;
    private final TaskListRepo taskListRepo; // nếu dùng Kanban
    private final TaskAttachmentRepo taskAttachmentRepo;
    private final ProjectRepo projectRepo;
    private final ProjectMemberRepo projectMemberRepo;
    private final UserRepo userRepo;

    @Override
    public ResCommonApi<?> createTask(ReqCreateTask req, User actor) {
        // Kiểm tra quyền: chỉ member của project mới được tạo
        Project project = projectRepo.findById(req.getProjectId()).orElse(null);
        if (project == null) return ResCommonApi.error("Project không tồn tại", 404);

        ProjectMember pm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (pm == null) return ResCommonApi.error("Bạn không phải thành viên project", 403);

        // Nếu có assignee, kiểm tra user đó là member project
        User assignee = null;
        if (req.getAssigneeId() != null) {
            assignee = userRepo.findById(req.getAssigneeId()).orElse(null);
            if (assignee == null || projectMemberRepo.findByProjectAndUser(project, assignee).isEmpty())
                return ResCommonApi.error("Người nhận không hợp lệ!", 400);
        }

        Task task = Task.builder()
                .project(project)
                .title(req.getTitle())
                .description(req.getDescription())
                .createdBy(actor)
                .assignee(assignee)
                .deadline(req.getDeadline())
                .priority(req.getPriority() != null ? req.getPriority() : TaskPriority.MEDIUM)
                .status(TaskStatus.TODO)
                .build();
        // Nếu có listId:
        if (req.getListId() != null) {
            TaskList list = taskListRepo.findById(req.getListId()).orElse(null);
            if (list != null) task.setList(list);
        }
        taskRepo.save(task);
        return ResCommonApi.success(task.getId(), "Tạo task thành công!");
    }

    @Override
    public ResCommonApi<?> updateTask(ReqUpdateTask req, User actor) {
        Task task = taskRepo.findById(req.getId()).orElse(null);
        if (task == null) return ResCommonApi.error("Task không tồn tại", 404);

        // Check quyền
        Project project = task.getProject();
        ProjectMember pm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (pm == null) return ResCommonApi.error("Bạn không phải thành viên project", 403);

        boolean isLeader = pm.getRole() != null && "LEADER".equals(pm.getRole().getName());
        boolean isAssignee = task.getAssignee() != null && actor.getId().equals(task.getAssignee().getId());
        boolean isCreator = actor.getId().equals(task.getCreatedBy().getId());

        if (!isLeader && !isAssignee && !isCreator)
            return ResCommonApi.error("Chỉ người tạo, leader hoặc người được assign mới được cập nhật!", 403);

        // Update field (có thể update từng trường nếu truyền != null)
        if (req.getTitle() != null) task.setTitle(req.getTitle());
        if (req.getDescription() != null) task.setDescription(req.getDescription());
        if (req.getDeadline() != null) task.setDeadline(req.getDeadline());
        if (req.getPriority() != null) task.setPriority(req.getPriority());
        if (req.getStatus() != null) task.setStatus(req.getStatus());

        // Nếu đổi assignee
        if (req.getAssigneeId() != null) {
            User newAssignee = userRepo.findById(req.getAssigneeId()).orElse(null);
            if (newAssignee != null && projectMemberRepo.findByProjectAndUser(project, newAssignee).isPresent())
                task.setAssignee(newAssignee);
        }
        // Nếu đổi list
        if (req.getListId() != null) {
            TaskList list = taskListRepo.findById(req.getListId()).orElse(null);
            if (list != null) task.setList(list);
        }
        taskRepo.save(task);
        return ResCommonApi.success(null, "Cập nhật task thành công!");
    }

    @Override
    public ResCommonApi<?> changeTaskStatus(Long taskId, TaskStatus newStatus, User actor) {
        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null) return ResCommonApi.error("Task không tồn tại", 404);

        Project project = task.getProject();
        ProjectMember pm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (pm == null) return ResCommonApi.error("Bạn không phải thành viên project", 403);

        boolean isLeader = pm.getRole() != null && "LEADER".equals(pm.getRole().getName());
        boolean isAssignee = task.getAssignee() != null && actor.getId().equals(task.getAssignee().getId());
        boolean isCreator = actor.getId().equals(task.getCreatedBy().getId());

        if (!isLeader && !isAssignee && !isCreator)
            return ResCommonApi.error("Chỉ người tạo, leader hoặc người được assign mới được đổi trạng thái!", 403);

        task.setStatus(newStatus);
        taskRepo.save(task);
        return ResCommonApi.success(null, "Đã đổi trạng thái task!");
    }

    /*
     * 🤣😥😀😅😃 chưa làm nha 😎🙂😄🤩😂
     */
    @Override
    public ResCommonApi<?> uploadTaskAttachment(Long taskId, MultipartFile file, User actor) {
        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null) return ResCommonApi.error("Task không tồn tại", 404);
        // Kiểm tra quyền như các API trên

        // Lưu file lên server hoặc cloud, lấy fileUrl
//        String fileUrl = fileStorageService.save(file);

        TaskAttachment attach = TaskAttachment.builder()
                .task(task)
                .fileName(file.getOriginalFilename())
//                .fileUrl(fileUrl)
                .uploadedBy(actor)
                .uploadedAt(LocalDateTime.now())
                .build();
        taskAttachmentRepo.save(attach);

        return ResCommonApi.success(attach.getId(), "Upload file thành công!");
    }

    @Override
    public ResCommonApi<?> searchTasks(Long projectId, TaskStatus status, Long assigneeId, LocalDate deadline, Pageable pageable, User actor) {
        // Kiểm tra quyền user trong project
        Project project = projectRepo.findById(projectId).orElse(null);
        if (project == null) return ResCommonApi.error("Project không tồn tại", 404);
        ProjectMember pm = projectMemberRepo.findByProjectAndUser(project, actor).orElse(null);
        if (pm == null) return ResCommonApi.error("Bạn không phải thành viên project", 403);

        Page<Task> page = taskRepo.searchTasks(projectId, status, assigneeId, deadline, pageable);
        // Chuyển sang DTO trước khi trả về
        Page<ResTask> dtoPage = page.map(task -> ResTask.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .assigneeName(task.getAssignee() != null ? task.getAssignee().getName() : null)
                .projectId(task.getProject().getId())
                .listId(task.getList() != null ? task.getList().getId() : null)
                .build());

        return ResCommonApi.success(dtoPage, "Danh sách task!");
    }

}
