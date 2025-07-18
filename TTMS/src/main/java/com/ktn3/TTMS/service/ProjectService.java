package com.ktn3.TTMS.service;

import com.ktn3.TTMS.dto.request.project.ReqCreateProject;
import com.ktn3.TTMS.dto.request.project.ReqInviteProjectMember;
import com.ktn3.TTMS.dto.request.project.ReqUpdateProject;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.User;

public interface ProjectService {
    ResCommonApi<?> create(ReqCreateProject req);
    ResCommonApi<?> inviteMember(Long projectId, ReqInviteProjectMember req, User inviter);
    ResCommonApi<?> acceptInvite(String token, User currentUser);
    ResCommonApi<?> resendInvite(Long projectId, String inviteEmail, User actor);
    ResCommonApi<?> changeMemberRole(Long projectId, Long userId, Long newRoleId, User actor);
    ResCommonApi<?> removeMember(Long projectId, Long userId, User actor);
    ResCommonApi<?> transferLeader(Long projectId, Long newLeaderId, User actor);
    ResCommonApi<?> updateProject(Long projectId, ReqUpdateProject req, User actor);
    ResCommonApi<?> deleteProject(Long projectId, User actor);
}
