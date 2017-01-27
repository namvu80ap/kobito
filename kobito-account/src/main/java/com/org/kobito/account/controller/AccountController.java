package com.org.kobito.account.controller;

import com.datastax.driver.core.utils.UUIDs;
import com.org.kobito.account.model.Account;
import com.org.kobito.account.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by v_nam on 2017/01/27.
 */
@RestController
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/account")
    Flux<Account> list(){
        return accountRepository.findAll();
    }

    @PostMapping("/account")
    Mono<Account> save(@RequestBody Account account){
        logger.info("Account firstname : {}", account.getFirstname());
        logger.debug("Account lastname : {}", account.getLastname());
        account.setId(UUIDs.timeBased());
        return accountRepository.save(account);
    }
}
