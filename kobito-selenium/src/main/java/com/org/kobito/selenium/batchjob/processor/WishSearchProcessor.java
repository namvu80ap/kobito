package com.org.kobito.selenium.batchjob.processor;

import com.org.kobito.selenium.batchjob.obj.WishSearch;
import com.org.kobito.selenium.services.WebSearchServiceFactory;
import org.openqa.selenium.WebElement;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WishSearchProcessor implements ItemProcessor<WishSearch, List<WebElement>> {

    @Autowired
    private WebSearchServiceFactory webSearchServiceFactory;

    @Override
    public List<WebElement> process(final WishSearch wishSearch) throws Exception {
        List<WebElement> elementList = webSearchServiceFactory.searchServiceFactory(wishSearch.getMarketType()).searchWebElementByUrl(wishSearch.getParam());
        return elementList;
    }
}
