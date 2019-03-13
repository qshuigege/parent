package com.fs.everything.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by FS on 2018/7/17.
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
   private KafkaSender kafkaSender;

    @RequestMapping(value = "sender", method = RequestMethod.GET)
    public String getUserById (){

        try {
            kafkaSender.send();
//            r.setResult(user);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return "ok";
    }
    
    
    public static void main(String[] args) {
        String str = "aaa,bbb,ccc";
        int i = str.indexOf("-");
        String[] split = str.split("\\-");
        if (split.length>=3){
            System.out.println("klskalkkflk");
        }else {
            System.out.println("...........");
        }
        System.out.println(i);
    }
}
