package com.book.services.application.category;

import com.book.repository.CategoryRepository;
import com.book.repository.entity.Category;
import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	public void initCategoryTest() {
		categoryService.initCategory();
		List<Category> allCategory = categoryRepository.findAll();
		Assert.assertNotNull(allCategory);
	}

	@Test
	public void getCategoryMapTest() {
		Map<String, List<Category>> categoryMap = categoryService.getCategoryMap();
		Assert.assertNotNull(categoryMap.values());

		String mainCategory = "국내도서";
		Assert.assertEquals(categoryMap.get(mainCategory).stream().findFirst().map(Category::getMainCategory).orElse(Strings.EMPTY), mainCategory);
	}
}