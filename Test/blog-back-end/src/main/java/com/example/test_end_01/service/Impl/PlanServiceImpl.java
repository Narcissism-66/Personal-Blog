package com.example.test_end_01.service.Impl;

import com.example.test_end_01.entity.pojo.Plan;
import com.example.test_end_01.mapper.PlanMapper;
import com.example.test_end_01.service.PlanService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Resource
    PlanMapper planMapper;
    @Override
    public int addNewPlan(Plan plan) {
        return planMapper.addNewPlan(plan);
    }

    @Override
    public List<Plan> getAllPlansByUserId(int authorId) {
        return planMapper.getAllPlansByUserId(authorId);
    }

    @Override
    public int updatePlanFinishStatusByUserId(int authorId, String finish,int id) {
        return planMapper.updatePlanFinishStatusByUserId(authorId, finish,id);
    }

    @Override
    public int deletePlanByUserIdAndId(int authorId, int id) {
        return planMapper.deletePlanByUserIdAndId(authorId, id);
    }
}
