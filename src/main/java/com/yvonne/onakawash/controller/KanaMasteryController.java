package com.yvonne.onakawash.controller;

import com.yvonne.onakawash.model.KanaMasteryResult;
import com.yvonne.onakawash.service.KanaMasteryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//允许本地前端以后从 localhost:3000 调这个 API。
//虽然这次不做前端，但项目里其他 controller 都有这个，所以保持一致。
@CrossOrigin(origins = "http://localhost:3000")
//这个 class 是 API controller。
//它的方法返回值要直接变成 HTTP response。
//比如返回 List<KanaMasteryResult>，Spring Boot 会自动变 JSON。
@RestController
public class KanaMasteryController {

    //Controller 自己不计算。
    //它只拿一个 service 工具。
    private final KanaMasteryService kanaMasteryService;

    //构造函数注入。Spring Boot 会自动把 KanaMasteryService 传进来。
    public KanaMasteryController(KanaMasteryService kanaMasteryService) {
        this.kanaMasteryService = kanaMasteryService;
    }

    //当浏览器或 HTTP client 访问 GET /kana-mastery 时，
    //执行下面这个方法。
    @GetMapping("/kana-mastery")
    public List<KanaMasteryResult> getKanaMasteryResults() {
        //返回很多条假名掌握度结果。
        return kanaMasteryService.getKanaMasteryResults();
    }
}