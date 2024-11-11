package ex.springsecurity_1805;

import ex.springsecurity_1805.Config.Configuration1;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.Test;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConverterStarter.class)// Используется для интеграции с JUnit
@ContextConfiguration(classes = Configuration1.class)
public class SpringSecurity1805ApplicationTests {




	@Test
	public void testMyService() {
		// тестируем myService
	}
}
