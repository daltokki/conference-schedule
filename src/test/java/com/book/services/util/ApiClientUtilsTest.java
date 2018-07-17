package com.book.services.util;

import com.book.interfaces.api.model.BookSearchResultDTO;
import com.book.interfaces.book.model.BookSearchForm;
import com.book.services.application.book.value.BookSearchType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiClientUtilsTest {
	@Autowired
	private ApiClientUtils apiClient;

	@Value("${book.api.server}")
	private String apiServer;

	@Value("${book.api.auth.key}")
	private String apiAuthKey;

	private String url;
	private BookSearchForm bookSearchForm;
	private Map<String, Object> buildValueMap;

	@Before
	public void setUp() {
		bookSearchForm = new BookSearchForm();
		bookSearchForm.setQuery("Spring");
		bookSearchForm.setTarget(BookSearchType.TITLE.name());

		url = apiServer + "?" + ApiClientUtils.toUrl(bookSearchForm);
		buildValueMap = ApiClientUtils.buildValueMap(bookSearchForm);
	}

	@Test
	public void sendGetTest() {
		URI uri = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(buildValueMap).encode().toUri();
		BookSearchResultDTO resultDTO = apiClient.sendGet(uri, apiAuthKey, BookSearchResultDTO.class);

		Assert.assertNotNull(resultDTO);
		Assert.assertEquals(resultDTO.getDocuments().size(), bookSearchForm.getSize());
	}

	@Test
	public void toUrlTest() {
		String url = ApiClientUtils.toUrl(bookSearchForm);
		Assert.assertNotNull(url);
		Assert.assertEquals(url, "page={page}&size={size}&query={query}&target={target}");
	}

	@Test
	public void buildValueMapTest() {
		Map<String, Object> buildValueMap = ApiClientUtils.buildValueMap(bookSearchForm);
		Assert.assertEquals(buildValueMap.get("query"), bookSearchForm.getQuery());
	}
}