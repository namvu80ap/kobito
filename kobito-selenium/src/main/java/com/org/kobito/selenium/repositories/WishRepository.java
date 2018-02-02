package com.org.kobito.selenium.repositories;

import com.org.kobito.selenium.dto.Wish;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishRepository extends MongoRepository<Wish, String> {

}
