package com.tapioca.BE.domain.model.api;

import com.tapioca.BE.domain.model.enumType.HttpMethod;

import java.util.UUID;

public class ApiList {
    private final UUID id;
    private final UUID apiId;
    private final String name;
    private final HttpMethod method;
    private final String url;
    private final String request;
    private final String response;

    public ApiList(
            UUID id, UUID apiId,
            String name, HttpMethod method,
            String url, String request,
            String response
    ){
          this.id=id;
          this.apiId=apiId;
          this.name=name;
          this.method=method;
          this.url=url;
          this.request=request;
          this.response=response;
    }

    // ApiList Service //
}
