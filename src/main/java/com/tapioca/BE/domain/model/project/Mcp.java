package com.tapioca.BE.domain.model.project;

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