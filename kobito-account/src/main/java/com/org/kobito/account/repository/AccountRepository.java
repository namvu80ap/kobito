package com.org.kobito.account.repository;

import com.org.kobito.account.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by v_nam on 2017/01/26.
 */
@RepositoryRestResource(collectionResourceRel = "account", path = "account")
public interface AccountRepository extends CrudRepository<Account, String> {
    Account findByLastname(String lastname);


    Account findByFirstnameAndLastname(String firstname, String lastname);
}
