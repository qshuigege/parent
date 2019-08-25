package com.fs.testelasticsearch.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "testes", type = "accountinfo")
public class AccountInfo {

    @Id
    private String id;

    @Field(index = true,type = FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_smart")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
