package cn.saisiawa.ideacollector.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Saisaiwa
 * @Date: 2024/08/17/ $
 * @Description:
 */
@RestController
public class OkController {

    @RequestMapping("/ok")
    public String ok() {
        return "ok";
    }

}
