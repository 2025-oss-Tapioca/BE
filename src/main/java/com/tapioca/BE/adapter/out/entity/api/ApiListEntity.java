package com.tapioca.BE.adapter.out.entity.api;

import com.tapioca.BE.domain.model.enumType.HttpMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "api_list")
public class ApiListEntity {
    @Id
    @Column(name = "api_list_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "api_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ApiEntity apiEntity;

    @Column(name = "api_list_name")
    private String name;

    @Column(name = "api_list_method")
    private HttpMethod method;

    @Column(name = "api_list_url")
    private String url;

    @Column(name = "api_list_request")
    private String request;

    @Column(name = "api_list_response")
    private String response;

}
