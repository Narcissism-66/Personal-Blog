package com.example.test_end_01.controller;

/*
 * @Auther:fz
 * @Date:2025/9/1
 * @Description:
 */

import com.example.test_end_01.entity.pojo.RestBean;
import com.example.test_end_01.service.CodeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Resource
    CodeService codeService;

    /**
     * 运行用户提交的代码
     * @param code 用户代码
     * @param input 输入数据
     * @param language 编程语言
     * @return 运行结果
     */
    @PostMapping("/runCode")
    public RestBean<String> runCode(@RequestParam("code") String code,
                                                 @RequestParam("input") String input,
                                                 @RequestParam("language") String language) {
        // 调用服务执行代码
        Map<Integer, String> result = codeService.runCode(code, input, language);
        
        // 构建返回结果
        Map<String, Object> response = new HashMap<>();
//        response.put("status", result.keySet().iterator().next()); // 获取状态码
        Integer status=result.keySet().iterator().next();
//        response.put("message", result.values().iterator().next()); // 获取结果信息
        String message=result.values().iterator().next();
        
        // 根据状态返回不同的消息
        if (status == 1) {
            return RestBean.success("代码运行成功", message);
        } else {
            return RestBean.failure(400, message);
        }
    }
}
