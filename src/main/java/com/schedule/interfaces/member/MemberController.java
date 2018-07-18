package com.schedule.interfaces.member;

import com.schedule.interfaces.common.AjaxResult;
import com.schedule.interfaces.member.model.MemberRequestForm;
import com.schedule.repository.entity.Member;
import com.schedule.services.application.member.MemberService;
import com.schedule.services.application.member.exception.AlreadyExistsMemberException;
import com.schedule.services.application.member.exception.PolicyViolationPasswordException;
import com.schedule.services.application.member.exception.UnMatchedEmailException;
import com.schedule.services.application.member.exception.UnMatchedPasswordException;
import com.schedule.services.util.MailSenderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.StringJoiner;

@RestController
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MailSenderUtils mailSender;

	@PostMapping("/register")
	public AjaxResult create(@RequestBody MemberRequestForm memberRequestForm) {
		try {
			memberService.create(memberRequestForm);
			return AjaxResult.builder().success(true).message("회원가입 완료!").build();
		} catch (AlreadyExistsMemberException | UnMatchedPasswordException | UnMatchedEmailException | PolicyViolationPasswordException e) {
			return AjaxResult.builder().success(false).message(e.getMessage()).build();
		} catch (Exception e) {
			return AjaxResult.builder().success(false).message("죄송합니다 회원가입에 실패하였습니다. 다시 시도해주세요.").build();
		}
	}

	@GetMapping("/forgot-password")
	public AjaxResult forgetPassword(String email) {
		try {
			Member member = memberService.findMember(email);
			if (member == null) {
				throw new UsernameNotFoundException("등록되지 않은 사용자입니다.");
			}
			String tmpPassword = memberService.tmpPasswordPublish(member);

			MailSenderUtils.MailTemplate forgotPasswordTemplate = MailSenderUtils.MailTemplate.FORGOT_PASSWORD;
			String contents = new StringJoiner("\n").add(forgotPasswordTemplate.getContext()).add(tmpPassword).toString();

			mailSender.sendMail(email, forgotPasswordTemplate.getSubject(), contents);
			return AjaxResult.builder().success(true).message("임시 비밀번호가 전송되었습니다. 확인해 주세요.").build();
		} catch (UsernameNotFoundException | UnMatchedEmailException e) {
			return AjaxResult.builder().success(false).message(e.getMessage()).build();
		} catch (Exception e) {
			return AjaxResult.builder().success(false).message("이메일 전송에 실패하였습니다. 다시 시도해 주세요.").build();
		}
	}

	@GetMapping("/member/profile")
	public ModelAndView getUserAll() {
		ModelAndView view = new ModelAndView("/member/profile");
		view.addObject("member", memberService.getMemberProfile());
		return view;
	}
}
