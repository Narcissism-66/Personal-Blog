package com.example.test_end_01.service;

/*
 * @Auther:fz
 * @Date:2025/9/1
 * @Description:
 */

import java.util.Map;

public interface CodeService {
    Map<Integer, String> runCode(String code, String input, String language);
}
