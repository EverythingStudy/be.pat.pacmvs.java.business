package cn.staitech.anno.service;

import java.text.ParseException;

public interface OtherService {

    void updateExamStatus(Long examineScoreId) throws ParseException;

    void atRegularTimeUpdateExamStatus() throws ParseException;
}
