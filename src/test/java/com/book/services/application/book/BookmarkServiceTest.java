package com.book.services.application.book;

import com.book.interfaces.book.model.DateAndPageSearchForm;
import com.book.repository.BookmarkRepository;
import com.book.repository.entity.Bookmark;
import com.book.repository.entity.Member;
import com.book.services.application.DefaultUserDetails;
import com.book.services.application.book.model.BookSearchDocumentsDto;
import com.book.services.application.book.value.DateSearchType;
import com.book.services.application.member.MemberService;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookmarkServiceTest {
	@InjectMocks
	private BookmarkService bookmarkService = new BookmarkService();
	@Mock
	private MemberService memberService;
	@Mock
	private BookmarkRepository bookmarkRepository;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;

	private BookSearchDocumentsDto bookSearchDocumentsDto;
	private Bookmark bookmark;

	@Before
	public void setUp() throws Exception {
		bookSearchDocumentsDto = new BookSearchDocumentsDto();
		bookSearchDocumentsDto.setTitle("미움받을 용기");
		bookSearchDocumentsDto.setPublisher("민음사");
		bookSearchDocumentsDto.setDatetime(new Date());
		bookSearchDocumentsDto.setUrl("http://sampleurl.com");
		bookSearchDocumentsDto.setSale_yn("Y");
		bookSearchDocumentsDto.setCategory("소설");

		bookmark = new Bookmark();
		bookmark.setBookmarkId(1L);

		UserDetails userDetails = DefaultUserDetails.get();

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(memberService.findMember(Mockito.anyString())).thenReturn(new Member());
	}

	@Test
	public void addBookmarkTest() {
		Mockito.when(bookmarkRepository.save(Mockito.any(Bookmark.class))).thenReturn(bookmark);
		Long bookmarkId = bookmarkService.addBookmark(bookSearchDocumentsDto);
		Assert.assertNotNull(bookmarkId);
	}

	@Test
	public void deleteBookmarkTest() {
		Mockito.when(bookmarkRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(bookmark));
		bookmarkService.deleteBookmark(bookmark.getBookmarkId());
	}

	@Test(expected = RuntimeException.class)
	public void deleteBookmarkTest_Exception() {
		Mockito.when(bookmarkRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		bookmarkService.deleteBookmark(bookmark.getBookmarkId());
	}

	@Test
	public void getBookmarkList() {
		DateAndPageSearchForm searchForm = new DateAndPageSearchForm();
		searchForm.setDateSearchType(DateSearchType.WEEK);

		Mockito.when(bookmarkRepository.findByCreatedAtAfter(Mockito.any(Date.class), Mockito.any(PageRequest.class)))
			.thenReturn(new PageImpl<>(Lists.newArrayList(bookmark)));

		Page<Bookmark> bookmarkList = bookmarkService.getBookmarkList(searchForm);
		Assert.assertNotNull(bookmarkList);
	}
}