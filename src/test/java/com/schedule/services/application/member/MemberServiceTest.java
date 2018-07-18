package com.schedule.services.application.member;

import com.schedule.interfaces.member.model.MemberRequestForm;
import com.schedule.repository.MemberRepository;
import com.schedule.repository.entity.Member;
import com.schedule.services.application.member.exception.AlreadyExistsMemberException;
import com.schedule.services.application.member.exception.PolicyViolationPasswordException;
import com.schedule.services.application.member.exception.UnMatchedEmailException;
import com.schedule.services.application.member.exception.UnMatchedPasswordException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;

	private MemberRequestForm memberRequestForm;

	@Before
	public void setUp() throws Exception {
		memberRequestForm = new MemberRequestForm();
		memberRequestForm.setLastName("Yuna");
		memberRequestForm.setFirstName("Kim");
	}

	@Test(expected = UnMatchedEmailException.class)
	public void createTest_UnMatchedEmailException() {
		memberRequestForm.setEmail("yuna@gmail");
		memberRequestForm.setPassword("yuna135!");
		memberRequestForm.setConfirmPassword("yuna135!");
		memberService.create(memberRequestForm);
	}

	@Test(expected = PolicyViolationPasswordException.class)
	public void createTest_PolicyViolationPasswordException() {
		memberRequestForm.setEmail("yuna@gmail.com");
		memberRequestForm.setPassword("yuna13");
		memberRequestForm.setConfirmPassword("yuna13");
		memberService.create(memberRequestForm);
	}

	@Test(expected = UnMatchedPasswordException.class)
	public void createTest_UnMatchedPasswordException() {
		memberRequestForm.setEmail("yuna@gmail.com");
		memberRequestForm.setPassword("yuna135!");
		memberRequestForm.setConfirmPassword("yuna13");
		memberService.create(memberRequestForm);
	}

	@Test(expected = AlreadyExistsMemberException.class)
	public void createTest_AlreadyExistsMemberException() {
		memberRequestForm.setEmail("yuna@gmail.com");
		memberRequestForm.setPassword("yuna135!");
		memberRequestForm.setConfirmPassword("yuna135!");
		memberService.create(memberRequestForm);
		memberService.create(memberRequestForm);
	}

	@Test
	public void createTest() {
		memberRequestForm.setEmail("yuna@gmail.com");
		memberRequestForm.setPassword("yuna135!");
		memberRequestForm.setConfirmPassword("yuna135!");

		memberService.create(memberRequestForm);

		Member member = memberRepository.findByEmail(memberRequestForm.getEmail());
		Assert.assertNotNull(member);
	}
}