package com.org.kobito.selenium.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MercariSearchService implements WebSearchServices {
    @Override
    public List<WebElement> searchWebElementByUrl(String url) {
        System.out.println("---------------------------------MercariSearchService");
        System.out.println(url);

        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.get("https://www.mercari.com/jp/search/?"+ url);
        WebElement element = driver.findElement(By.className("items-box-content"));
        List<WebElement> subElement = element.findElements(By.tagName("section"));

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        return subElement;
    }
}
