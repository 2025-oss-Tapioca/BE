package com.tapioca.BE.domain.model;

public class Mcp {
    private final String userRequest;

    public Mcp(String userRequest){
        this.userRequest=userRequest;
    }

    public String getUserRequest(){
        return userRequest;
    }
}
