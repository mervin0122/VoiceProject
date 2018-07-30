package com.cn.mcc.service;

import com.cn.mcc.bean.Employee;
import com.cn.mcc.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mervin on 2018/6/25.
 */
@Service
public class EmployeeService
{
    @Autowired
    EmployeeDao employeeDao;

    public List<Employee> empList(){
        return  employeeDao.empList();
    }

    public Employee getEmp(Integer id){
        Employee employee=employeeDao.getEmpById(id);
        return employee;
    }
}
