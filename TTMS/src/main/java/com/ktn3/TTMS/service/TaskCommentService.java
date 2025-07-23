package com.ktn3.TTMS.service;

import com.ktn3.TTMS.dto.request.ReqCreateComment;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskCommentService {
    ResCommonApi<?> createComment(ReqCreateComment req, List<MultipartFile> files, User actor);
    ResCommonApi<?> getComments(Long taskId);
}
