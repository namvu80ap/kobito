package com.org.kobito.selenium.services;

import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WebSearchServices {
    /* Search WebElements with a GET request via url string */
    public List<WebElement> searchWebElementByUrl( String url );
}
