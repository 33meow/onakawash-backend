//controller可以理解为后端的门口接待员
//前端不会找数据库，也不会找repository，指挥访问url，controller负责接住这个url
package com.yvonne.onakawash.controller;

import com.yvonne.onakawash.entity.PracticeSessionEntity;
import com.yvonne.onakawash.service.PracticeSessionService;
import org.springframework.web.bind.annotation.CrossOrigin;
//处理GET请求
import org.springframework.web.bind.annotation.GetMapping;
//处理POST请求
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PracticeSessionController {
    //Controller 里面需要一个 Service 工具。
    //以后保存记录、查询记录，都交给 Service 做。
    private final PracticeSessionService practiceSessionService;

   //构造函数
    public PracticeSessionController(PracticeSessionService practiceSessionService){
        this.practiceSessionService = practiceSessionService;


    }
    @PostMapping("/practice-sessions")
    public PracticeSessionEntity createPracticeSession(@RequestBody PracticeSessionEntity practiceSession){
        return practiceSessionService.savePracticeSession(practiceSession);
    }
    @GetMapping("/practice-sessions")
    public List<PracticeSessionEntity> getPracticeSessions(){
        return practiceSessionService.getAllPracticeSessions();
    }
}
