package com.company.erp.controller;

import com.fs.diyutils.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getAllUser")
    public JsonResponse getAllUser(HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<>();
        map.put("msg", "test get all user");
        map.put("remoteAddr", request.getRemoteAddr());
        map.put("remoteHost", request.getRemoteHost());
        map.put("remotePort", request.getRemotePort());
        return JsonResponse.success(map);
    }

}
