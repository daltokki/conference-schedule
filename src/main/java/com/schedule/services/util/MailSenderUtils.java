package com.schedule.services.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@Slf4j
@Component
public class MailSenderUtils {
	private final JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String from;

	@Autowired
	public MailSenderUtils(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail(String to, String subject, String content) {
		MimeMessagePreparator prepareMessage = mimeMessage -> {
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setFrom(new InternetAddress(from));
			mimeMessage.setSubject(subject);
			mimeMessage.setText(content, "utf-8", "html");
		};
		try {
			mailSender.send(prepareMessage);
		} catch (MailException e) {
			log.error("MailException", e);
			throw new RuntimeException("Mail send failed. To :" + to);
		}
	}

	@Getter
	@AllArgsConstructor
	public enum MailTemplate {
		FORGOT_PASSWORD("비민번호 분실 안내", "임시 비밀번호 안내드립니다. 임시 비밀번호로 로그인 후, 즉시 새 비밀번호로 변경해주세요.");

		private String subject;
		private String context;
	}
}
