package com.fs.testelasticsearch.controller;

import com.fs.diyutils.JsonResponse;
import com.fs.testelasticsearch.dao.ElasticsearchRepoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/testEs")
@Slf4j
public class TestEsController {

    @Resource
    private ElasticsearchRepoImpl esRepo;

    @RequestMapping("/testEs")
    public JsonResponse testEs(HttpServletRequest request){

        Iterable all = esRepo.findAll();

        all.forEach(item->{
            log.info("{}", item);
        });

        return JsonResponse.success(all);

    }

}
