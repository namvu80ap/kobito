package com.org.kobito.selenium;

import com.org.kobito.selenium.repositories.WishRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KobitoSeleniumApplicationTests {

	@Autowired
	WishRepository repository;

	@Test
	public void contextLoads() {
		System.out.println("---------------------------------------------");
		System.out.println(repository.count());
	}

	public void testGoogle(){
		// Create a new instance of the Firefox driver
		// Notice that the remainder of the code relies on the interface,
		// not the implementation.
		//System.setProperty("webdriver.gecko.driver", "/home/user/bin");
		HtmlUnitDriver driver = new HtmlUnitDriver();

		// And now use this to visit Google
		//driver.get("http://www.google.com");
		driver.get("https://www.mercari.com/jp/search/?sort_order=&keyword=bags&category_root=2&category_child=43&brand_name=&brand_id=&size_group=&price_min=1000&price_max=5000");

		// Alternatively the same thing can be done like this
		// driver.navigate().to("http://www.google.com");
		WebElement element = driver.findElement(By.className("items-box-content"));
//		System.out.println("Page title is: " + element.getText());
		List<WebElement> subElement = element.findElements(By.tagName("section"));

		for (WebElement e: subElement) {
			System.out.println(e.getText());
		}

		// Find the text input element by its name
		//WebElement element = driver.findElement(By.name("q"));

		// Enter something to search for
		//element.sendKeys("Cheese!");

		// Now submit the form. WebDriver will find the form for us from the element
		//element.submit();

		// Check the title of the page
		System.out.println("Page title is: " + driver.getTitle());

		// Google's search is rendered dynamically with JavaScript.
		// Wait for the page to load, timeout after 10 seconds
//		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//			public Boolean apply(WebDriver d) {
//				return d.getTitle().toLowerCase().startsWith("cheese!");
//			}
//		});

		// Should see: "cheese! - Google Search"
		System.out.println("Page title is: " + driver.getTitle());

		//Close the browser
		driver.quit();
	}

	public void testPhantomjs(){
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				"/usr/bin/phantomjs");
		WebDriver driver = new PhantomJSDriver(caps);
		driver.get("https://www.mercari.com/jp/search/?sort_order=&keyword=bags&category_root=2&category_child=43&brand_name=&brand_id=&size_group=&price_min=1000&price_max=5000");
		WebElement element = driver.findElement(By.className("items-box-content"));
		System.out.println( element.getText() );
	}
}
