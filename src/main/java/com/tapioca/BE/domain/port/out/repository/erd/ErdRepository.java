package com.tapioca.BE.domain.port.out.repository.erd;

import com.tapioca.BE.adapter.out.entity.ErdEntity;

public interface ErdRepository {
    ErdEntity save(ErdEntity erd);
}
