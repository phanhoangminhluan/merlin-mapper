package com.merlin.mapper.test;

import com.merlin.mapper.annotations.MappingField;
import com.merlin.mapper.annotations.SourceFieldConfig;
import com.merlin.mapper.annotations.TargetFieldConfig;
import com.merlin.mapper.handler.AnySourceClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TestEntity {

    @MappingField(
            sourceFieldConfigs = {
                    @SourceFieldConfig(sourceGetterMethod = "getStrVal", forSourceClasses = {AnySourceClass.class})
            }
    )
    private String strValsss;

    @MappingField
    private Integer intVal;

    @MappingField()
    private List<TestListEntity> tests;

    @MappingField
    private LinkedList<TestListEntity> testLinkedList;

    @MappingField
    private Set<TestListEntity> testSet;

    @MappingField(
            sourceFieldConfigs = {
                    @SourceFieldConfig(sourceGetterMethod = "getTestObjectId", forSourceClasses = {TestDTO.class})
            },
            targetFieldConfigs = {
                    @TargetFieldConfig(targetSetterMethod = "initTestObject")
            }
    )
    private TestObject testObject;

    public void initTestObject(String testObjectId) {
        this.testObject = new TestObject();
        testObject.setSomeId(testObjectId);
    }
}
