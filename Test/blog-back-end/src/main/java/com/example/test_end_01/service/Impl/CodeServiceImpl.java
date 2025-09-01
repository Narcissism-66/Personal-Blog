package com.example.test_end_01.service.Impl;

/*
 * @Auther:fz
 * @Date:2025/9/1
 * @Description:
 */

import com.example.test_end_01.service.CodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class CodeServiceImpl implements CodeService {
    @Value("${code.execution.timeout:5000}")
    private long executionTimeout; // 默认执行超时时间为5秒

    @Value("${code.execution.tmp.dir:./code}")
    private String tempDir; // 临时文件目录

    @Value("${code.execution.clean-interval:12000}")
    private long cleanInterval; // 清理间隔，默认1小时
    /**
     * @param code 用户提交的代码
     * @param input 用户提供的输入
     * @param language 编程语言
     * @return Map<Integer, String> 键为状态(1成功,0失败)，值为结果信息
     */
    @Override
    public Map<Integer, String> runCode(String code, String input, String language) {
        try {
            // 确保临时目录存在
            File dir = new File(tempDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String uuid = UUID.randomUUID().toString();

            // 根据语言决定不同的执行方式
            switch (language.toLowerCase()) {
                case "java":
                    return runJava(code, input, uuid);
                case "cpp":
                    return runCpp(code, input, uuid);
                case "c":
                    return runC(code, input, uuid);
                default:
                    Map<Integer, String> result = new HashMap<>();
                    result.put(0, "不支持的语言: " + language);
                    return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<Integer, String> result = new HashMap<>();
            result.put(0, "代码执行错误: " + e.getMessage());
            return result;
        }
    }


    // 运行Java代码
    private Map<Integer, String> runJava(String code, String input, String uuid) {
        try {
            // 从代码中提取类名
            String className = "Main"; // 默认类名
            if (code.contains("class ")) {
                String[] parts = code.split("class\\s+");
                if (parts.length > 1) {
                    String afterClass = parts[1].trim();
                    int firstSpace = afterClass.indexOf(" ");
                    int firstBracket = afterClass.indexOf("{");
                    int endIndex = Math.min(
                            firstSpace > -1 ? firstSpace : Integer.MAX_VALUE,
                            firstBracket > -1 ? firstBracket : Integer.MAX_VALUE
                    );
                    if (endIndex != Integer.MAX_VALUE) {
                        className = afterClass.substring(0, endIndex).trim();
                    }
                }
            }

            // 创建临时Java文件
            String fileName = tempDir + "/" + className + ".java";
            Files.write(Paths.get(fileName), code.getBytes());

            // 编译Java代码
            Process compileProcess = new ProcessBuilder("javac", fileName).start();
            int compileExitCode = compileProcess.waitFor();

            if (compileExitCode != 0) {
                // 编译错误
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(compileProcess.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                Map<Integer, String> result = new HashMap<>();
                result.put(0, "编译错误: \n" + errorOutput);
                return result;
            }

            // 运行编译后的Java程序
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", tempDir, className);

            // 执行命令并获取结果
            return executeProcess(pb, input);
        } catch (Exception e) {
            e.printStackTrace();
            Map<Integer, String> result = new HashMap<>();
            result.put(0, "Java代码执行错误: " + e.getMessage());
            return result;
        }
    }

    // 运行C++代码
    private Map<Integer, String> runCpp(String code, String input, String uuid) {
        try {
            // 创建临时C++文件
            String cppFile = tempDir + "/" + uuid + ".cpp";
            String exeFile = tempDir + "/" + uuid + (System.getProperty("os.name").toLowerCase().contains("win") ? ".exe" : "");

            Files.write(Paths.get(cppFile), code.getBytes());

            // 编译C++代码
            Process compileProcess;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                compileProcess = new ProcessBuilder("g++", cppFile, "-o", exeFile).start();
            } else {
                compileProcess = new ProcessBuilder("g++", cppFile, "-o", exeFile).start();
            }

            int compileExitCode = compileProcess.waitFor();

            if (compileExitCode != 0) {
                // 编译错误
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(compileProcess.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                Map<Integer, String> result = new HashMap<>();
                result.put(0, "编译错误: \n" + errorOutput);
                return result;
            }

            // 运行编译后的C++程序
            ProcessBuilder pb = new ProcessBuilder(exeFile);

            // 执行命令并获取结果
            return executeProcess(pb, input);
        } catch (Exception e) {
            e.printStackTrace();
            Map<Integer, String> result = new HashMap<>();
            result.put(0, "C++代码执行错误: " + e.getMessage());
            return result;
        }
    }

    // 运行C代码
    private Map<Integer, String> runC(String code, String input, String uuid) {
        try {
            // 创建临时C文件
            String cFile = tempDir + "/" + uuid + ".c";
            String exeFile = tempDir + "/" + uuid + (System.getProperty("os.name").toLowerCase().contains("win") ? ".exe" : "");

            Files.write(Paths.get(cFile), code.getBytes());

            // 编译C代码
            Process compileProcess;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                compileProcess = new ProcessBuilder("gcc", cFile, "-o", exeFile).start();
            } else {
                compileProcess = new ProcessBuilder("gcc", cFile, "-o", exeFile).start();
            }

            int compileExitCode = compileProcess.waitFor();

            if (compileExitCode != 0) {
                // 编译错误
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(compileProcess.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                Map<Integer, String> result = new HashMap<>();
                result.put(0, "编译错误: \n" + errorOutput);
                return result;
            }

            // 运行编译后的C程序
            ProcessBuilder pb = new ProcessBuilder(exeFile);

            // 执行命令并获取结果
            return executeProcess(pb, input);
        } catch (Exception e) {
            e.printStackTrace();
            Map<Integer, String> result = new HashMap<>();
            result.put(0, "C代码执行错误: " + e.getMessage());
            return result;
        }
    }

    // 执行进程并获取结果，统一处理输入和超时
    private Map<Integer, String> executeProcess(ProcessBuilder pb, String input) throws IOException, InterruptedException {
        // 重定向错误流
        pb.redirectErrorStream(true);

        // 启动进程
        Process process = pb.start();

        // 如果有输入，写入到进程的标准输入流
        if (input != null && !input.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(input);
                writer.flush();
            }
        }

        // 创建一个线程来读取输出
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            Future<String> future = executorService.submit(() -> {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                return output.toString();
            });

            // 设置超时
            String output;
            try {
                output = future.get(executionTimeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                process.destroy();
                Map<Integer, String> result = new HashMap<>();
                result.put(0, "代码执行超时（" + executionTimeout + "毫秒）");
                return result;
            } catch (ExecutionException e) {
                process.destroy();
                Map<Integer, String> result = new HashMap<>();
                result.put(0, "读取输出异常：" + e.getCause().getMessage());
                return result;
            }

            // 等待进程结束
            int exitCode = process.waitFor();

            Map<Integer, String> result = new HashMap<>();
            if (exitCode == 0) {
                result.put(1, output);
            } else {
                result.put(0, "程序异常退出（代码：" + exitCode + "）\n" + output);
            }
            return result;
        } finally {
            executorService.shutdownNow();
        }
    }

    /**
     * 定时清理临时文件
     */
    @Scheduled(fixedRateString = "${code.execution.clean-interval:12000}")
    public void cleanTempFiles() {
        try {
            File tempFolder = new File(tempDir);
            if (!tempFolder.exists() || !tempFolder.isDirectory()) {
                return;
            }

            // 获取当前时间
            long cutoffTime = System.currentTimeMillis() - 12000; // 1分钟

            // 遍历并清理旧文件
            Files.walkFileTree(tempFolder.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.creationTime().toMillis() < cutoffTime) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("临时代码文件清理完成: " + new Date());
        } catch (Exception e) {
            System.err.println("清理临时文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
