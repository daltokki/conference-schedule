package com.schedule.services.util.handlebars;

import com.github.jknack.handlebars.Options;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;

@HandlebarsHelper
public class PagingHelper {
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final String LI = "<li>";
	private static final String LI_END = "</li>";

	public CharSequence paging(Integer nowPage, Options options) {
		int maxPage = options.param(0);
		int pageCount = getPageCount(options);
		int startPage = getStartPage(nowPage, pageCount);
		int lastPage = getLastPage(maxPage, pageCount, startPage);
		int beforePage = startPage - pageCount;
		int afterPage = startPage + pageCount;
		int firstPage = 1;

		StringBuilder template = new StringBuilder();
		template.append("<ul class=\"pagination justify-content-center\" >");
		try {
			buildFirstPageButton(nowPage, options, firstPage, template);
			buildBeforePageButton(options, beforePage, template);
			buildPageButtons(nowPage, options, startPage, lastPage, template);
			buildAfterPageButton(options, maxPage, afterPage, template);
			buildLastPageButton(nowPage, options, maxPage, template);
		} catch (IOException e) {
			e.printStackTrace();
		}
		template.append("</ul>");

		return template.toString();
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
	public static class Page {
		private int no;
		private String text;
		private boolean current;

		private Page(int no, String text) {
			this.no = no;
			this.text = text;
		}
	}

	private int getPageCount(Options options) {
		int pageCount = DEFAULT_PAGE_SIZE;
		if (options.params.length >= 2) {
			pageCount = (Integer) options.param(1);
		}
		return pageCount;
	}

	//<li class="page-item next disabled"><a href="#" class="page-link">Next</a></li>

	private int getLastPage(int maxPage, int pageCount, int startPage) {
		int lastPage = startPage + pageCount - 1;
		if (lastPage > maxPage) {
			lastPage = maxPage;
		}
		return lastPage;
	}

	private int getStartPage(Integer nowPage, int pageCount) {
		int startPage = nowPage - ((nowPage - 1) % pageCount);
		if (startPage < 1) {
			startPage = 1;
		}
		return startPage;
	}

	private void buildLastPageButton(Integer nowPage, Options options, int maxPage, StringBuilder template) throws IOException {
		if (nowPage < maxPage) {
			template.append(LI);
			template.append(options.fn(new Page(maxPage, "»»")));
			template.append(LI_END);
		} else {
			template.append("<li class=\"page-item disabled\" ><a class=\"page-link\">»»</a></li>");
		}
	}

	private void buildAfterPageButton(Options options, int maxPage, int afterPage, StringBuilder template) throws IOException {
		if (afterPage <= maxPage) {
			template.append(LI);
			template.append(options.fn(new Page(afterPage, "»")));
			template.append(LI_END);
		} else {
			template.append("<li class=\"page-item disabled\" ><a class=\"page-link\">»</a></li>");

		}
	}

	private void buildPageButtons(Integer nowPage, Options options, int startPage, int lastPage, StringBuilder template) throws IOException {
		for (int i = startPage; i <= lastPage; i++) {
			if (nowPage == i) {
				template.append("<li class=\"page-item active\">");
				template.append("<a class=\"page-link\">");
				template.append(i);
				template.append("</a>");
			} else {
				template.append(LI);
				template.append(options.fn(new Page(i, Integer.toString(i))));
			}
			template.append(LI_END);
		}
	}

	private void buildBeforePageButton(Options options, int beforePage, StringBuilder template) throws IOException {
		if (beforePage > 0) {
			template.append(LI);
			template.append(options.fn(new Page(beforePage, "«")));
			template.append(LI_END);
		} else {
			template.append("<li class=\"page-item disabled\" ><a class=\"page-link\">«</a></li>");
		}
	}

	private void buildFirstPageButton(Integer nowPage, Options options, int firstPage, StringBuilder template) throws IOException {
		if (nowPage != firstPage) {
			template.append(LI);
			template.append(options.fn(new Page(firstPage, "««")));
			template.append(LI_END);
		} else {
			template.append("<li class=\"page-item disabled\" ><a class=\"page-link\">««</a></li>");
		}
	}
}
