package com.yvonne.onakawash;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
//CORS /kɔːrz/ 的感觉就是：
// 浏览器保安看到前端是 localhost:3000，
// 后端是 localhost:8080，
// 它觉得“不是同一个门牌号”，所以拦住了 🚧。
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from onakawash backend";
    }
    @GetMapping("/buttercup")
    public String buttercup(){
        return "IM BUTTERCUP";
    }
    @GetMapping("/")
    public String home(){
        return "不要再显示白色页面";
    }
    @GetMapping("/aiueo")
    public String aiueo(){
        return "あ <br>い<br> う<br>  お";
    }
    @GetMapping("/kana")
    public Kana getKna(){

        return new Kana("あ","a");
    }
    @GetMapping("/kana-list")
    public List<Kana> getKanaList(){
        List<Kana> kanaList = new ArrayList<>();
         kanaList.add(new Kana("あ","a"));
         kanaList.add(new Kana("い","i"));
         kanaList.add(new Kana("う","u"));

         return kanaList;
    }
}

