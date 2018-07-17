package com.book.services.application.book;

import com.book.interfaces.api.BookSearchApiWrapper;
import com.book.interfaces.api.model.ApiResultDTO;
import com.book.interfaces.book.model.BookSearchForm;
import com.book.services.application.DefaultUserDetails;
import com.book.services.application.book.model.BookSearchResultDto;
import com.book.services.application.book.value.BookSearchType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookSearchServiceTest {
	@InjectMocks
	private BookSearchService bookSearchService = new BookSearchService();
	@Mock
	private BookSearchHistoryService bookSearchHistoryService;
	@Mock
	private BookSearchApiWrapper bookSearchApiWrapper;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;

	private BookSearchForm bookSearchForm;

	@Before
	public void setUp() {
		UserDetails userDetails = DefaultUserDetails.get();

		bookSearchForm = new BookSearchForm();
		bookSearchForm.setQuery("Spring");
		bookSearchForm.setTarget(BookSearchType.TITLE.name());

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	public void searchTest() {
		Mockito.when(bookSearchHistoryService.saveHistory(bookSearchForm)).thenReturn(1L);

		Mockito.when(bookSearchApiWrapper.getSearchResult(Mockito.any(BookSearchForm.class), Mockito.any(PageRequest.class)))
			.thenReturn(ApiResultDTO.succeed(BookSearchResultDto.builder().build()));
		BookSearchResultDto search = bookSearchService.search(bookSearchForm);
		Assert.notNull(search, "search is not null.");
	}
}