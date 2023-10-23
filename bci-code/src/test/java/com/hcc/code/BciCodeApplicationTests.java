package com.hcc.code;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hcc.code.controller.CodeController;
import com.hcc.code.entity.CodeEntity;
import com.hcc.code.service.CodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class BciCodeApplicationTests {


    @Autowired
    CodeService codeService;

    @Test
    void contextLoads() {
    }

    @Test
    void date(){
//        Page<CodeEntity> page = codeService.query()
//                .eq("user_id", 1).eq("type", 0).orderByDesc("create_time").page(new Page<>(1, 5));
//        System.out.println(page.getTotal());
        Integer a = 127;
        Integer b = 127;
//        b++;
        System.out.println(a==b);
    }

    @Test
    void test1(){
        CodeEntity code = new CodeEntity();
//        code.setCreateTime(new Date());
        codeService.save(code);
    }
    @Test
    void rand(){
        Random random = new Random();
        System.out.println(random.nextInt(2));

    }
}
