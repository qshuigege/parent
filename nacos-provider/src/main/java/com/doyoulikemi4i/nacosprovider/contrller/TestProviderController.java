package com.doyoulikemi4i.nacosprovider.contrller;

import com.doyoulikemi4i.nacosprovider.po.Payment;
import com.doyoulikemi4i.nacosprovider.utils.CommonResult;
import com.doyoulikemi4i.nacosprovider.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestProviderController {

    @RequestMapping("/testProvider")
    public String test(HttpServletRequest request, HttpServletResponse response){
        return JsonUtils.objectToJson(request.getParameterMap());
    }

    @RequestMapping(value = "/selectPaymentListByQuery")
    public CommonResult<Payment> selectPaymentListByQuery(@RequestBody Payment payment) {
        System.out.println(payment.money);
        return new CommonResult(200, "查询成功, 服务端口：" + payment.money);
    }

    @GetMapping(value = "/selectPaymentListByQuery2")
    public CommonResult<Payment> selectPaymentListByQuery2(Payment payment) {
        System.out.println(payment.money);
        return new CommonResult(200, "查询成功, 服务端口2：" + payment.money);
    }
}
