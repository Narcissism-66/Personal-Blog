package com.example.test_end_01.controller;

import com.example.test_end_01.entity.pojo.Plan;
import com.example.test_end_01.entity.pojo.RestBean;
import com.example.test_end_01.service.PlanService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping ("api/plan")
public class PlanController {
    @Resource
    PlanService planService;
    @PostMapping("addNewPlan")
    public RestBean<String> addNewPlan(@RequestParam("plan") String plan,
                                       HttpServletRequest request){
        Plan newPlan=new Plan(plan,"false", (Integer) request.getAttribute("id"),new Date());
        int status= planService.addNewPlan(newPlan);
        if (status>0)return RestBean.success("添加成功！");
        else return RestBean.failure(503,"出错啦！");
    }

    @GetMapping("getAllPlansByUserId")
    public RestBean<List<Plan>> getAllPlans(HttpServletRequest request){
        int userId= (Integer) request.getAttribute("id");
        return RestBean.success("获取成功！",planService.getAllPlansByUserId(userId));
    }

    @PostMapping("UpdateFinish")
    public RestBean<List<Plan>> updateFinish(HttpServletRequest request,
                                              @RequestParam("finish") String finish,
                                              @RequestParam("id") int id){
        int userId = (Integer) request.getAttribute("id");
        int status = planService.updatePlanFinishStatusByUserId(userId, finish,id);
        if (status>0)return RestBean.success("更新成功！");
        else return RestBean.failure(503,"出错��！");
    }

    @PostMapping("deletePlan")
    public RestBean<String> deletePlan(@RequestParam("id") int id,HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("id");
        int status = planService.deletePlanByUserIdAndId(userId, id);
        if (status > 0) return RestBean.success("删除成功！");
        else  return RestBean.failure(503,"出错了，请联系管理员！");
    }
}
