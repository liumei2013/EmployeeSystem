package com.meiliu.dao;

import com.meiliu.entity.Employee;

import java.util.List;

public interface EmployeeDao {
    List<Employee> lists();

    void save(Employee employee);

    Employee findById(Integer id);

    void update(Employee employee);

    void delete(Integer id);
}
