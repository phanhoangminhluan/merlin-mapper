package com.merlin.mapper.test;

import com.merlin.mapper.annotations.MappingField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestListEntity {

    @MappingField
    private String aString;

    @MappingField
    private Integer anInt;

}
