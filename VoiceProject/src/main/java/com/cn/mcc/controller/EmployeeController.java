package com.cn.mcc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.mcc.bean.Employee;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.bean.Tts;
import com.cn.mcc.service.EmployeeService;
import com.cn.mcc.utils.BaseController;
import com.cn.mcc.utils.Config;
import com.cn.mcc.utils.Constants;
import com.cn.mcc.utils.Result;
import com.cn.mcc.utils.voice.VoiceUtil;
import com.cn.mcc.utils.voice.iat.FileUtil;
import com.cn.mcc.utils.voice.iat.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by mervin on 2018/6/25.
 */
@RestController
public class EmployeeController extends BaseController{
   // public  final  static Logger logger=Logger.getLogger(EmployeeController.class);
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/emp/{id}")
    public Employee getEmployee(@PathVariable("id") Integer id){
        return  employeeService.getEmp(id);
    }



}
