package com.tapioca.BE.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Mcp {
    private final JsonNode json;

    public Mcp(JsonNode json){
        this.json=json;
    }

    public JsonNode getJson(){
        return json;
    }
}