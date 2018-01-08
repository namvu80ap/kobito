package com.org.kobito.kobitosimilarity.services;

import com.datastax.driver.core.utils.UUIDs;
import com.org.kobito.kobitosimilarity.model.SimilarWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.org.kobito.kobitosimilarity.repository.SimilarWordRepository;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;

@Service
public class SimilarWordServices {
    private static final Logger logger = LogManager.getLogger(SimilarWordServices.class);

    /**
     * The domain name format just include a-z , A-Z , 0-9 , "-" , "_" ( but the "-" and "_" not at the begin or the end )
     */
    private static final String[] DOMAIN_NAME_CHAR = new String[]{ "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
            "0","1","2","3","4","5","6","7","8","9",
            "-" };

    /**
     * The accessible length of
     */
    private static final int CHANGE_ONE_ACCESSIBLE = 4;
    private static final int ADD_ONE_ACCESSIBLE = 5;
    private static final int ADD_TWO_ACCESSIBLE = 12;
    private static final int CHANGE_TWO_ACCESSIBLE = 10;
    private static final int CHANGE_TWO_TYPOS_ACCESSIBLE = 4;
    private static final int ADD_ONE_CHANGE_ONE_ACCESSIBLE = 11;
    private static final int SUBTRACT_ONE_ACCESSIBLE = 6;
    private static final int SUBTRACT_TWO_ACCESSIBLE = 14;

    /**
     * Max word length accessible
     */
    private static final int MAX_LENGTH_ACCESSIBLE = 14;

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
        changeOne(word);
    }

    /**
     * Generate the words with just replace one char
     */
    public void changeOne( String word ){
        //check permission
        if( word.length() < CHANGE_ONE_ACCESSIBLE ){
            //Do nothing
        }

        word = word.toLowerCase();
        StringBuilder processBuilder = new StringBuilder( word );

        int domainCharLength = DOMAIN_NAME_CHAR.length;

        int wordLength = processBuilder.length();

        Flux rangerLength = Flux.range(0, processBuilder.length());
        rangerLength.subscribe( item -> { logger.debug("The item ----------------------- {} -------------",item); } );

        for (int i = 0; i < processBuilder.length(); i++) {
            for (int j = 0; j < domainCharLength ; j++) {
                StringBuilder wordBuilder = new StringBuilder( word );
                String tmp = wordBuilder.replace( i , i+1 , DOMAIN_NAME_CHAR[j]).toString();

                //Domain format do not accept - and _ at start and end of string
                if( !tmp.matches("^-.*") && !tmp.matches("^\\_.*")
                        && tmp.indexOf("-") != (wordLength -1) && tmp.indexOf("_") != (wordLength -1) ){
//                    logger.debug(" Work and tmp ---------------------------------- {}, {} ",word, tmp);
                    saveSimilarWord(word,tmp);
                }
            }
        }
    }

    public Long count(){
        return repository.count().block();
    }

    private void saveSimilarWord( String word, String tmp ){
        SimilarWord simi = new SimilarWord();
        simi.setId(UUIDs.timeBased());
        simi.setKeyWord(word);
        simi.setSimilarWord(tmp);
        simi.setHowSimilar(50);
        repository.save(simi).subscribe();
    }
}
