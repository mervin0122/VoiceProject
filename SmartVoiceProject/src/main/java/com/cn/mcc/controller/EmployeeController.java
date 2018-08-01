package com.cn.mcc.controller;

import com.cn.mcc.bean.Employee;
import com.cn.mcc.service.EmployeeService;
import com.cn.mcc.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
