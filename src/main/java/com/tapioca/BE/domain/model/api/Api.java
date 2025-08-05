package com.tapioca.BE.domain.model.api;

import java.util.List;
import java.util.UUID;

public class Api {
    private final UUID id;
    private final UUID teamId;
    private final List<ApiList> apiLists;

    public Api(
        UUID id,UUID teamId,
        List<ApiList> apiLists
    ){
        this.id=id;
        this.teamId=teamId;
        this.apiLists=List.copyOf(apiLists);
    }

    // Api Service //

    public static class Attribute {
        private UUID id;
        private UUID diagramId;
        private String name;

    }
}
