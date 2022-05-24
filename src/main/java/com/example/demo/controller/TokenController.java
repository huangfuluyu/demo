package com.example.demo.controller;

import cn.hutool.json.JSONObject;
import com.example.demo.config.JwtConfig;
import com.example.demo.enums.ExceptionEnum;
import com.example.demo.utils.ApiResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author : HuangFu
 * @Description : TokenController
 * @date : 2022-05-24 15:17
 **/
@RestController
public class TokenController {
    @Resource
    private JwtConfig jwtConfig ;

    /**
     * 用户登录接口
     * @param userName
     * @param passWord
     * @return
     */
    @PostMapping("/login")
    public ApiResponse login (@RequestParam("userName") String userName,
                                 @RequestParam("passWord") String passWord){
        JSONObject json = new JSONObject();

        /** 验证userName，passWord和数据库中是否一致，如不一致，直接return ResultTool.errer(); 【这里省略该步骤】*/

        // 这里模拟通过用户名和密码，从数据库查询userId
        // 这里把userId转为String类型，实际开发中如果subject需要存userId，则可以JwtConfig的createToken方法的参数设置为Long类型
        String userId = 5 + "";
        String token = jwtConfig.createToken(userId) ;
        if (StringUtils.hasLength(token)) {
            json.set("token",token) ;
        }
        return ApiResponse.ofStatus(ExceptionEnum.OK,json);
    }

    /**
     * 需要 Token 验证的接口
     */
    @PostMapping("/info")
    public ApiResponse info (){
        return ApiResponse.ofSuccess("info") ;
    }

    /**
     * 根据请求头的token获取userId
     * @param request
     * @return
     */
    @GetMapping("/getUserInfo")
    public ApiResponse getUserInfo(HttpServletRequest request){
        String usernameFromToken = jwtConfig.getUsernameFromToken(request.getHeader("token"));
        return ApiResponse.ofStatus(ExceptionEnum.OK,usernameFromToken);
    }

    /*
        为什么项目重启后，带着之前的token还可以访问到需要info等需要token验证的接口？
        答案：只要不过期，会一直存在，类似于redis
     */

}
