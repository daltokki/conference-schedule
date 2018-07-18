package com.schedule.services.application.member;

import com.schedule.interfaces.member.model.MemberRequestForm;
import com.schedule.repository.MemberRepository;
import com.schedule.repository.entity.Member;
import com.schedule.services.application.member.exception.AlreadyExistsMemberException;
import com.schedule.services.application.member.exception.PolicyViolationPasswordException;
import com.schedule.services.application.member.exception.UnMatchedEmailException;
import com.schedule.services.application.member.exception.UnMatchedPasswordException;
import com.schedule.services.domain.security.SecurityMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private Predicate<String> PASSWORD_VERIFY = (password) -> {
		String passwordPolicy = "((?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()-=+]).{8,})";
		Pattern pattern = Pattern.compile(passwordPolicy);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	};

	private Predicate<String> EMAIL_VERIFY = (email) -> Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());

	@Transactional
	public void create(MemberRequestForm memberRequestForm) {
		memberCreateValidation(memberRequestForm);

		Member member = Member.create(memberRequestForm.getEmail(), passwordEncoder.encode(memberRequestForm.getPassword()),
			new StringJoiner(",").add(memberRequestForm.getFirstName()).add(memberRequestForm.getLastName()).toString());

		memberRepository.save(member);
	}

	private void memberCreateValidation(MemberRequestForm memberRequestForm) {
		if (!EMAIL_VERIFY.test(memberRequestForm.getEmail())) {
			throw new UnMatchedEmailException("옳지 않은 email 형식입니다. 다시 확인해 주세요.");
		}
		if (!PASSWORD_VERIFY.test(memberRequestForm.getPassword())) {
			throw new PolicyViolationPasswordException("비밀번호 정책에 맞지 않습니다.\n 비밀번호는 8글자 이상이며, 하나 이상의 영문, 숫자, 특수문자를 포함해야 합니다.");
		}
		if (!memberRequestForm.getPassword().equals(memberRequestForm.getConfirmPassword())) {
			throw new UnMatchedPasswordException("비밀번호가 확인 비밀번호와 일치하지 않습니다.");
		}
		boolean isExistMember = memberRepository.existsByEmailEquals(memberRequestForm.getEmail());
		if (isExistMember) {
			throw new AlreadyExistsMemberException("이미 존재하는 아이디 입니다.");
		}
	}

	public Member findMember(String email) {
		if (!EMAIL_VERIFY.test(email)) {
			throw new UnMatchedEmailException("옳지 않은 email 형식입니다. 다시 확인해 주세요.");
		}
		return memberRepository.findByEmail(email);
	}

	public Member getMemberProfile() {
		String email = SecurityMember.getUserDetailsOptional().map(UserDetails::getUsername).orElseThrow(
			() -> new RuntimeException("invalid member."));
		return memberRepository.findByEmail(email);
	}

	@Transactional
	public String tmpPasswordPublish(Member member) {
		String uuid = UUID.randomUUID().toString();
		String encode = passwordEncoder.encode(uuid);

		member.updatePassword(encode);
		memberRepository.save(member);

		return uuid;
	}
}
