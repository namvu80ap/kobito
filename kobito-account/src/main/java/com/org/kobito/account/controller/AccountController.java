package com.org.kobito.account.controller;

import com.datastax.driver.core.utils.UUIDs;
import com.org.kobito.account.model.Account;
import com.org.kobito.account.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

/**
 * Created by v_nam on 2017/01/27.
 */
@RestController
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @Autowired
    AccountRepository accountRepository;

    private Twitter twitter;

    private ConnectionRepository connectionRepository;

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
