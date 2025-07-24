package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="attribute")
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


}
