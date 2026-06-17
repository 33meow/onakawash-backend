package com.yvonne.onakawash.controller;


import com.yvonne.onakawash.model.KanaSection;
import com.yvonne.onakawash.service.KanaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//CORS /kɔːrz/ 的感觉就是：
// 浏览器保安看到前端是 localhost:3000，
// 后端是 localhost:8080，
// 它觉得“不是同一个门牌号”，所以拦住了 🚧。
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class KanaController {

    //这是 Controller 自己的变量。
    private final KanaService kanaService;

    public KanaController(KanaService kanaService) {
        this.kanaService = kanaService;
    }
    @GetMapping("/")
    public String home(){
        return "IM BUTTERCUP";
    }


    //hiragana API

    //当前端访问 /hiragana 时
    //后端创建一些 KanaItem
    //把它们装进 KanaSection
    //最后 return 给前端
    @GetMapping("/hiragana")
    public List<KanaSection>getHiragana(){
        return kanaService.getHiraganaSectionsFromDatabase();

      }
      @GetMapping("/katakana")
     public List<KanaSection>getKatakana(){
        return kanaService.getKatakanaSectionsFromDatabase();
      }

}

