package com.fs.testelasticsearch.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "idx_book", type = "book")
public class Book {

    @Id
    private String id;

    @Field(index = true,type = FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_smart")
    private String title;

    @Field(index = true,type = FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_smart")
    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
