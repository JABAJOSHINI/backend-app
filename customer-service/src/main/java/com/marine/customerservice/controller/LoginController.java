//package com.marine.customerservice.controller;
//
//import com.example.appcommons.utility.Response;
//import com.marine.customerservice.Model.AuthModel;
//import com.marine.customerservice.Model.ForgotPasswordModel;
//import com.marine.customerservice.Model.LoginModel;
//import com.marine.customerservice.api.LoginApi;
//import com.marine.customerservice.service.LoginService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//public class LoginController implements LoginApi {
//    @Autowired
//    private LoginService loginService;
//    @Override
//    public Response register(LoginModel loginModel) throws Exception {
//        return loginService.register(loginModel);
//    }
//
//    @Override
//    public Response forgotPassword(ForgotPasswordModel forgotPasswdModel) throws Exception {
//        return loginService.forgotPassword(forgotPasswdModel);
//    }
////    Logger logger = LoggerFactory.getLogger(LoginController.class);
////    @Autowired
////    private LoginService loginService;
////
////    @Override
////    public Response login(LoginModel loginModel) throws IOException {
////        return loginService.login(loginModel);
////    }
//}
