package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.entity.User;
import com.ktn3.TTMS.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(User user) {
        String to = user.getEmail();
        String subject = "Xác thực tài khoản";
        String verificationUrl = "http://localhost:8080/api/v1/auth/verify?token=" + user.getEmailVerificationToken();
        String content = "Nhấn vào link để xác thực: " + verificationUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmail(String to, String token) {
        String subject = "Yêu cầu đặt lại mật khẩu";
        String resetUrl = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;
        String content = "Nhấn vào link để đặt lại mật khẩu: " + resetUrl;

        // Tạo SimpleMailMessage và gửi như phần xác thực email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    @Override
    public void sendProjectInviteEmail(String toEmail, String projectName, String inviteLink) {
        String subject = "Mời bạn tham gia dự án: " + projectName;
        String content = String.format(
                "Bạn được mời tham gia dự án \"%s\" trên hệ thống Teamwork Task Management System.\n\n"
                        + "Hãy click vào link dưới đây để tham gia dự án:\n%s\n\n"
                        + "Nếu bạn chưa có tài khoản, vui lòng đăng ký trước khi tham gia.",
                projectName,
                inviteLink
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    public void sendDeadlineReminder(String email, String taskTitle, LocalDate deadline) {
        String subject = "Nhắc nhở deadline task: " + taskTitle;
        String content = "Bạn có một task sắp đến hạn vào ngày: " + deadline +
                "\nVui lòng kiểm tra và hoàn thành đúng hạn!";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }


}
