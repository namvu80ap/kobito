package com.org.kobito.kobitosimilarity.repository;

import com.org.kobito.kobitosimilarity.model.SimilarWord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SimilarWordRepository extends ReactiveCrudRepository<SimilarWord, String> {

}
