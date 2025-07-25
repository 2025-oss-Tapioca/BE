package com.tapioca.BE.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Mcp {
    private final String type;

    public Mcp(String type){
        this.type=type;
    }

    public String getType(){
        return type;
    }

}