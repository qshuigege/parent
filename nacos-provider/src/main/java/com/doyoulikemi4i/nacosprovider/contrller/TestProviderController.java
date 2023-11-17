package com.doyoulikemi4i.nacosprovider.contrller;

import com.doyoulikemi4i.nacosprovider.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestProviderController {

    @RequestMapping("/testProvider")
    public String test(HttpServletRequest request, HttpServletResponse response){
        return JsonUtils.objectToJson(request.getParameterMap());
    }
}
