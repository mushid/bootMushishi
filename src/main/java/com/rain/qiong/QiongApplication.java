package com.rain.qiong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.rain.qiong.*.dao")
@SpringBootApplication
public class QiongApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiongApplication.class, args);
        System.out.print("oisngiodnfhoirnsdiopjggggjdofgshgjfgkhidfnmhfjsdghngjhagdfhkljhl;jklhjkyfhkopropio");
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ    rain启动成功      ヾ(◍°∇°◍)ﾉ");
    }
}
