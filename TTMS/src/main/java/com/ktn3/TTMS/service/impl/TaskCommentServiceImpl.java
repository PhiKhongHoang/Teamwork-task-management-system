package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.dto.request.ReqCreateComment;
import com.ktn3.TTMS.dto.response.ResComment;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.*;
import com.ktn3.TTMS.repository.*;
import com.ktn3.TTMS.service.MailService;
import com.ktn3.TTMS.service.TaskCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskCommentServiceImpl implements TaskCommentService {
    private final TaskRepo taskRepo;
    private final TaskCommentRepo taskCommentRepo;
    private final TaskCommentMentionRepo mentionRepo;
    private final TaskCommentAttachmentRepo attachmentRepo;
    private final UserRepo userRepo;
    private final ProjectMemberRepo projectMemberRepo;
    private final MailService mailService;

    // Thêm bình luận mới (có gắn thẻ user, có thể đính kèm file)
    @Override
    public ResCommonApi<?> createComment(ReqCreateComment req, List<MultipartFile> files, User actor) {
        Task task = taskRepo.findById(req.getTaskId()).orElse(null);
        if (task == null) return ResCommonApi.error("Task không tồn tại", 404);

        // Kiểm tra quyền: chỉ thành viên project được comment
        ProjectMember pm = projectMemberRepo.findByProjectAndUser(task.getProject(), actor).orElse(null);
        if (pm == null) return ResCommonApi.error("Bạn không phải thành viên project", 403);

        TaskComment comment = TaskComment.builder()
                .task(task)
                .user(actor)
                .content(req.getContent())
                .build();
        taskCommentRepo.save(comment);

        // Xử lý đính kèm file
//        List<String> fileUrls = new ArrayList<>();
//        if (files != null) {
//            for (MultipartFile file : files) {
//                String fileUrl = fileStorageService.save(file); // Giả sử có service lưu file
//                TaskCommentAttachment attach = TaskCommentAttachment.builder()
//                        .comment(comment)
//                        .fileName(file.getOriginalFilename())
//                        .fileUrl(fileUrl)
//                        .uploadedBy(actor)
//                        .build();
//                attachmentRepo.save(attach);
//                fileUrls.add(fileUrl);
//            }
//        }

        // Xử lý mention
//        List<Long> mentionUserIds = req.getMentionUserIds();
//        if (mentionUserIds != null) {
//            for (Long userId : mentionUserIds) {
//                User mentionUser = userRepo.findById(userId).orElse(null);
//                if (mentionUser != null) {
//                    mentionRepo.save(TaskCommentMention.builder()
//                            .comment(comment)
//                            .user(mentionUser)
//                            .build());
//                    // Gửi thông báo (email/notification) cho user được tag
//                    mailService.sendMentionNotify(mentionUser.getEmail(), task.getTitle(), comment.getContent());
//                }
//            }
//        }

        // Build ResComment trả về
        ResComment res = ResComment.builder()
                .id(comment.getId())
                .taskId(task.getId())
                .userId(actor.getId())
                .userName(actor.getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
//                .attachmentUrls(fileUrls)
//                .mentionUserIds(mentionUserIds)
                .build();

        return ResCommonApi.success(res, "Bình luận thành công!");
    }

    // Lấy danh sách comment theo task
    @Override
    public ResCommonApi<?> getComments(Long taskId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null) return ResCommonApi.error("Task không tồn tại", 404);

        List<TaskComment> comments = taskCommentRepo.findByTaskOrderByCreatedAtAsc(task);
        List<ResComment> resList = comments.stream().map(comment -> {
            List<String> attachUrls = attachmentRepo.findByComment(comment)
                    .stream().map(TaskCommentAttachment::getFileUrl)
                    .collect(Collectors.toList());
            List<Long> mentionIds = mentionRepo.findByComment(comment)
                    .stream().map(m -> m.getUser().getId())
                    .collect(Collectors.toList());
            return ResComment.builder()
                    .id(comment.getId())
                    .taskId(comment.getTask().getId())
                    .userId(comment.getUser().getId())
                    .userName(comment.getUser().getName())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .attachmentUrls(attachUrls)
                    .mentionUserIds(mentionIds)
                    .build();
        }).collect(Collectors.toList());

        return ResCommonApi.success(resList, "Danh sách comment");
    }
}
