//package com.alon.test;
//
//import com.alon.main.server.service.CustomerService;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.support.AbstractApplicationContext;
//
///**
// * Created by alon_ss on 6/26/16.
// */
//public class test {
//
//
//    public static void main(String args[]){
//        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
////        AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext2.xml");
//        String[] ss = context.getBeanDefinitionNames();
//        CustomerService service1 = (CustomerService) context.getBean("customerService");
//        CustomerService service2 = (CustomerService) context.getBean("customerService");
//        CustomerService service3 = (CustomerService) context.getBean("customerService");
//
//
//        /*
//         * Register employee using service
//         */
//
//        context.close();
//    }
//}