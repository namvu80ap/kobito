package com.org.kobito.kobitosimilarity.controller;

import com.datastax.driver.core.utils.UUIDs;
import com.org.kobito.kobitosimilarity.services.SimilarWordServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/similar")
public class SimilarWordController {
    @Autowired
    private SimilarWordServices services;

    @GetMapping(value = "/")
    public String index( ) {
        services.saveSimilarWord(UUIDs.timeBased(),"TEST","TESTY",100);
        return "OK";
    }

    @GetMapping(value = "/{word}")
    public String index( @PathVariable String word ) {
        services.saveSimilarWordGenerated(word);
        return "OK";
    }
}
