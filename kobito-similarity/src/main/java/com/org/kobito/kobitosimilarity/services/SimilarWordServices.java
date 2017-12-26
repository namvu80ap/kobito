package com.org.kobito.kobitosimilarity.services;

import com.org.kobito.kobitosimilarity.model.SimilarWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.org.kobito.kobitosimilarity.repository.SimilarWordRepository;

@Service
public class SimilarWordServices {
    private static final Logger logger = LogManager.getLogger(SimilarWordServices.class);

    @Autowired
    private SimilarWordRepository repository;

    public void saveSimilarWord(String keyWord, String similarWord, Integer howSimilar){
        SimilarWord simi = new SimilarWord();
        simi.setId("1");
        simi.setKeyWord(keyWord);
        simi.setSimilarWord(similarWord);
        simi.setHowSimilar(howSimilar);
        repository.save(simi);
    }

    public Long count(){
        return repository.count().block();
    }
}
