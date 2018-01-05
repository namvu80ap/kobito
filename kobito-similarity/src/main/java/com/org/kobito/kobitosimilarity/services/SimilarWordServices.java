package com.org.kobito.kobitosimilarity.services;

import com.datastax.driver.core.utils.UUIDs;
import com.org.kobito.kobitosimilarity.model.SimilarWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.org.kobito.kobitosimilarity.repository.SimilarWordRepository;

import java.util.Map;
import java.util.UUID;

@Service
public class SimilarWordServices {
    private static final Logger logger = LogManager.getLogger(SimilarWordServices.class);

    @Autowired
    private SimilarWordRepository repository;

    public void saveSimilarWord(UUID id, String keyWord, String similarWord, Integer howSimilar){
        SimilarWord simi = new SimilarWord();
        simi.setId(id);
        simi.setKeyWord(keyWord);
        simi.setSimilarWord(similarWord);
        simi.setHowSimilar(howSimilar);
        repository.save(simi).subscribe();
    }

    public void saveSimilarWordGenerated( String word ){
        Map<String, Map<String, Integer>> list =  SimilarGenrator.getAll( word, 50 , true);
        for ( Map.Entry<String, Map<String, Integer>> item : list.entrySet() ) {
            for ( Map.Entry<String, Integer> entry: item.getValue().entrySet() ) {
                if( entry.getKey().contains("keith") ){
                    //System.out.print(entry.getKey() + " ");
                }
                else {
                    SimilarWord simi = new SimilarWord();
                    simi.setId(UUIDs.timeBased());
                    simi.setKeyWord(word);
                    simi.setSimilarWord(entry.getKey());
                    simi.setHowSimilar(entry.getValue());
                    repository.save(simi).subscribe();
                }
            }
        }
    }

    public Long count(){
        return repository.count().block();
    }
}
