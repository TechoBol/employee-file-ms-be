package com.efms.employee_file_ms_be.model.mapper;

public interface CustomMapper<DTO, DTO_CREATE, E> {

    DTO toDTO(E e);

    E toEntity(DTO_CREATE dto);
}
