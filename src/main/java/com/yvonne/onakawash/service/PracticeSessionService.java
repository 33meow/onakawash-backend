package com.yvonne.onakawash.service;

//一轮练习记录的数据结构
import com.yvonne.onakawash.entity.PracticeSessionEntity;
//数据库工具
import com.yvonne.onakawash.repository.PracticeSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//告诉spring boot这个类是业务逻辑层，交给spring管理
@Service
public class PracticeSessionService {

    //Service 需要用 Repository 去保存和查询数据库
    private final PracticeSessionRepository practiceSessionRepository;

    public PracticeSessionService(PracticeSessionRepository practiceSessionRepository){
        this.practiceSessionRepository = practiceSessionRepository;

}
//PracticeSessionEntity practiceSession可以理解为，使用这个方法是外部传进来的一整条练习记录
public PracticeSessionEntity savePracticeSession(PracticeSessionEntity practiceSession){
    //如果 accuracy 没有值
    //并且 score 有值
    //并且 totalQuestions 有值
    //并且 totalQuestions 大于 0
    //那后端就自己计算正确率
     if (
             practiceSession.getAccuracy() == null &&
                     practiceSession.getScore() != null &&
                     practiceSession.getTotalQuestions() != null &&
                     practiceSession.getTotalQuestions() >0
     ) {
         int accuracy = Math.round(
                 practiceSession.getScore() * 100.0f / practiceSession.getTotalQuestions()
         );
         //把刚刚算出来的accuracy放到联系记录里
         practiceSession.setAccuracy(accuracy);
     }
     //把这条记录保存回数据库
     return practiceSessionRepository.save(practiceSession);
}

//最后写查询所有记录的方法：
public List<PracticeSessionEntity> getAllPracticeSessions(){
       //返回很多条 PracticeSessionEntity
        return practiceSessionRepository.findAll();
}
}