package com.org.kobito.selenium.dto;

import org.springframework.data.annotation.Id;

public class Wish {
    @Id
    public String id;

    public Integer marketType;
    public String wishParam;

    public Wish(Integer marketType, String wishParam) {
        this.marketType = marketType;
        this.wishParam = wishParam;
    }
}
