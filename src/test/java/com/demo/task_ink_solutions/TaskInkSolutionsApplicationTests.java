package com.demo.task_ink_solutions;

import com.demo.task_ink_solutions.service.CityImportService;
import com.demo.task_ink_solutions.service.NewsProcessService;
import com.demo.task_ink_solutions.service.NewsServiceImpl;
import com.demo.task_ink_solutions.service.ProcessOpenAI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
class TaskInkSolutionsApplicationTests {


	@Autowired
	private CityImportService cityImportService;

	@Autowired
	private NewsProcessService newsProcessService;

	@MockBean
	private NewsServiceImpl newsServiceImpl;

	@MockBean
	private ProcessOpenAI processOpenAI;

	@Test
	void contextLoads() {
	}

}
