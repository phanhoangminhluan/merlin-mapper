package com.merlin.mapper.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestDTO {

    private String strVal;

    private Integer intVal;

    private List<TestListDTO> tests;

    private LinkedList<TestListDTO> testLinkedList;

    private Set<TestListDTO> testSet;

    private String testObjectId;

}
