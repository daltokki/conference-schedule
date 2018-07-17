package com.book.services.application.book;

import com.book.interfaces.book.model.BookSearchForm;
import com.book.interfaces.book.model.DateAndPageSearchForm;
import com.book.repository.SearchHistoryRepository;
import com.book.repository.entity.Category;
import com.book.repository.entity.Member;
import com.book.repository.entity.SearchHistory;
import com.book.repository.value.RoleType;
import com.book.services.application.DefaultUserDetails;
import com.book.services.application.book.value.BookSearchType;
import com.book.services.application.book.value.DateSearchType;
import com.book.services.application.category.CategoryService;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookSearchHistoryServiceTest {
	@InjectMocks
	private BookSearchHistoryService bookSearchHistoryService = new BookSearchHistoryService();
	@Mock
	private SearchHistoryRepository searchHistoryRepository;
	@Mock
	private MemberService memberService;
	@Mock
	private CategoryService categoryService;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;

	private BookSearchForm bookSearchForm;
	private SearchHistory searchHistory;

	@Before
	public void setUp() {
		UserDetails userDetails = DefaultUserDetails.get();

		bookSearchForm = new BookSearchForm();
		bookSearchForm.setQuery("Spring");
		bookSearchForm.setTarget(BookSearchType.TITLE.name());

		searchHistory = new SearchHistory();
		searchHistory.setSearchHistoryId(1L);

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(memberService.findMember(Mockito.anyString())).thenReturn(new Member());
	}

	@Test
	public void saveHistory() {
		Category category = new Category();
		category.setMainCategory("국내도서");
		category.setSubCategory("소설");

		Mockito.when(categoryService.findCategory(Mockito.anyInt())).thenReturn(category);
		Mockito.when(searchHistoryRepository.save(Mockito.any(SearchHistory.class))).thenReturn(searchHistory);


		Long saveHistoryId = bookSearchHistoryService.saveHistory(bookSearchForm);
		Assert.assertNotNull(saveHistoryId);
	}

	@Test
	public void getHistoryList() {
		DateAndPageSearchForm searchForm = new DateAndPageSearchForm();
		searchForm.setDateSearchType(DateSearchType.WEEK);

		Mockito.when(searchHistoryRepository.findByCreatedAtAfter(Mockito.any(Date.class), Mockito.any(PageRequest.class)))
			.thenReturn(new PageImpl<>(Lists.newArrayList(searchHistory)));

		Page<SearchHistory> historyList = bookSearchHistoryService.getHistoryList(searchForm);
		Assert.assertNotNull(historyList);
	}
}