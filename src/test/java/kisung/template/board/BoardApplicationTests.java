package kisung.template.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BoardApplicationTests {
	@Autowired
	private ApplicationContext applicationContext;
	@Test
	void contextLoads() {
		if (applicationContext != null) {
			String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
			for (String bean: beanDefinitionNames) {
				System.out.println("bean = " + applicationContext.getBean(bean).getClass());
			}
		}
	}

}
