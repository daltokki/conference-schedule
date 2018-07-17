package com.book.interfaces.api;

import com.book.interfaces.api.model.ApiResultDTO;
import com.book.interfaces.book.model.BookSearchForm;
import com.book.services.application.book.value.BookSearchType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BookSearchApiWrapperTest {

	@Autowired
	private BookSearchApiWrapper bookSearchApiWrapper;

	private BookSearchForm bookSearchForm;

	@Before
	public void setUp() {
		bookSearchForm = new BookSearchForm();
		bookSearchForm.setQuery("Spring");
		bookSearchForm.setTarget(BookSearchType.TITLE.name());

	}

	@Test
	public void getSearchResultTest() {
		ApiResultDTO searchResult = bookSearchApiWrapper.getSearchResult(bookSearchForm,
			PageRequest.of(bookSearchForm.getPage() - 1, bookSearchForm.getSize()));

		Assert.assertNotNull(searchResult);
		Assert.assertTrue(searchResult.isSuccessful());
	}
}