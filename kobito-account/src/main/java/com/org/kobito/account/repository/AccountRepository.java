package com.org.kobito.account.repository;

import com.org.kobito.account.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Created by v_nam on 2017/01/26.
 */
//@RepositoryRestResource(collectionResourceRel = "account", path = "account")
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {
    Flux<Account> findByLastname(String lastname);
}
