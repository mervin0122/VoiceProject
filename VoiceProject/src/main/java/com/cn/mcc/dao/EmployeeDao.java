
package com.cn.mcc.dao;

import com.cn.mcc.bean.Employee;
import com.cn.mcc.utils.MyBatisDao;

import java.util.List;

/**
 * Created by mervin on 2018/6/25.
 */
@MyBatisDao
public interface EmployeeDao {

    public List<Employee> empList();

    public Employee getEmpById(Integer id);

    public void updateEmp(Employee employee);

    public void deleteEmpById(Integer id);

    public  void insertEmployee(Employee employee);
}
