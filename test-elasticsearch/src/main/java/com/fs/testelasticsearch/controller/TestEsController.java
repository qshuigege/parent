package com.fs.testelasticsearch.controller;

import com.fs.diyutils.JsonResponse;
import com.fs.testelasticsearch.bean.Book;
import com.fs.testelasticsearch.dao.ElasticsearchRepoImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/testEs")
@Slf4j
public class TestEsController {

    @Resource
    private ElasticsearchRepoImpl esRepo;

    @Resource
    private ElasticsearchTemplate esTemplate;

    @RequestMapping("/testEsRepo")
    public JsonResponse testEsRepo(HttpServletRequest request){

        Iterable all = esRepo.findAll();

        all.forEach(item->{
            log.info("{}", item);
        });

        return JsonResponse.success(all);

    }

    @RequestMapping("/testEsTemplate")
    public JsonResponse testEsTemplate(HttpServletRequest request){

        Book book = new Book();
        book.setId(UUID.randomUUID().toString().toLowerCase());
        log.debug("{}", book.getId());
        book.setTitle("Journey to the West");
        book.setAuthor("Chengen Wu");
        IndexQuery indexQuery = new IndexQueryBuilder().withId(book.getId()).withObject(book).build();
        String index = esTemplate.index(indexQuery);
        return JsonResponse.success(index);

    }

    @RequestMapping("/testSearchQuery")
    public JsonResponse testSearchQuery(HttpServletRequest request){

        String field = request.getParameter("field");
        String content = request.getParameter("content");
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery(field, content)).build();
        List<Book> books = esTemplate.queryForList(searchQuery, Book.class);
        return JsonResponse.success(books);

    }

}
