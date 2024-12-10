package com.example.test_end_01.service;

import com.example.test_end_01.entity.pojo.Plan;

import java.util.List;

public interface PlanService {
    int addNewPlan(Plan plan);
    List<Plan> getAllPlansByUserId(int authorId);
    int updatePlanFinishStatusByUserId(int authorId,String finish,int id);
    int deletePlanByUserIdAndId(int authorId,int id);
}
