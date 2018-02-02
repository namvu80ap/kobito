package com.org.kobito.selenium.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSearchServiceFactory {

    @Autowired MercariSearchService mercariSearchService;

    public WebSearchServices searchServiceFactory(int searchServiceType) throws IllegalArgumentException {
        switch (searchServiceType){
            case 1 : return mercariSearchService;
            default: throw new IllegalArgumentException("Can't create search service with searchServiceType " + searchServiceType);
        }
    }
}
