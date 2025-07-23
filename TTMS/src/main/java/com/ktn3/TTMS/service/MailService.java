package com.ktn3.TTMS.service;

import com.ktn3.TTMS.entity.User;

import java.time.LocalDate;

public interface MailService {
    void sendVerificationEmail(User user);
    void sendResetPasswordEmail(String to, String token);
    void sendProjectInviteEmail(String toEmail, String projectName, String inviteLink);
    void sendDeadlineReminder(String email, String taskTitle, LocalDate deadline);
}
