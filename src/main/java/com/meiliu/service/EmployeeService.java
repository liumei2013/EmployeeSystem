package com.meiliu.service;

import com.meiliu.entity.Employee;

import java.util.List;

public interface EmployeeService {

    public abstract List<Employee> lists();

    public abstract void save(Employee employee);

    public abstract Employee findById(Integer id);

    public abstract void update(Employee employee);

    void delete(Integer id);
}
