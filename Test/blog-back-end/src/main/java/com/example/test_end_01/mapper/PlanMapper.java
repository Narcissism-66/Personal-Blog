package com.example.test_end_01.mapper;

import com.example.test_end_01.entity.pojo.Plan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanMapper {
    @Insert("INSERT INTO plan(plan,finish,authorId,time) VALUES (#{plan},#{finish},#{authorId},#{time})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int addNewPlan(Plan plan);

    @Select("SELECT * FROM plan WHERE authorId=#{authorId}")
    List<Plan> getAllPlansByUserId(int authorId);

    @Update("UPDATE plan SET finish=#{finish} WHERE authorId=#{authorId} AND id=#{id}")
    int updatePlanFinishStatusByUserId(int authorId, String finish,int id);

    //删除计划
    @Delete("DELETE FROM plan WHERE authorId = #{authorId} AND id = #{id}")
    int deletePlanByUserIdAndId(int authorId, int id);
}
