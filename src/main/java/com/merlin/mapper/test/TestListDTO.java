package com.merlin.mapper.test;

import com.merlin.mapper.annotations.MappingField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestListDTO {

    @MappingField
    private String aString;

    @MappingField
    private Integer anInt;

}
