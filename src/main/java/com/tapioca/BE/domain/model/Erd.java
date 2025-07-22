package com.tapioca.BE.domain.model;


import java.util.List;
import java.util.UUID;

public class Erd {
    private final UUID id;
    private final UUID teamId;
    private final List<String> attribute;
    private final List<ErdList> record;
    private final String primaryKey;
    private final String foreignKey;

    // mappingCardinality가 false면 1:1, true면 1:N 관계
    private final boolean mappingCardinality;

    public Erd(
            UUID id, UUID teamId, List<String> attribute,
            String primaryKey, String foreignKey,
            boolean mappingCardinality
    ) {
        this.id = id;
        this.teamId = teamId;
        this.attribute = attribute;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.mappingCardinality = mappingCardinality;
    }

    public Erd(
            UUID id, UUID teamId, List<String> attribute,
            String primaryKey, String foreignKey
    ) {
        this.id = id;
        this.teamId = teamId;
        this.attribute = attribute;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.mappingCardinality = false;
    }
}
