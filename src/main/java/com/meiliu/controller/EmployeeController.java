package com.meiliu.controller;

import com.meiliu.entity.Employee;
import com.meiliu.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("employee")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private EmployeeService employeeService;

    @Value("${photo.file.dir}")
    private String realpath;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * delete employee by id
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer id){
        log.debug("delete id: {}",id);
        //1.delete data
        String photo = employeeService.findById(id).getPhoto();
        employeeService.delete(id);
        //2.delete photo
        File file = new File(realpath, photo);
        if(file.exists()){
            file.delete();
        }
        return "redirect:/employee/lists";
    }

    /**
     * update employee information
     * @return
     */
    @RequestMapping("update")
    public String update(Employee employee, MultipartFile img) throws IOException {
        log.debug("updated information: id:{},name:{},salary:{},birthday:{}",employee.getId(),employee.getName(),employee.getSalary(),
                employee.getBirthday());
        boolean notEmpty = !img.isEmpty();
        log.debug("update image or not: {}",notEmpty);

        if(notEmpty){//1.delete previous photo,check by searching id; 2.upload new photo;
            //delete
            String oldPhoto = employeeService.findById(employee.getId()).getPhoto();
            File file = new File(realpath, oldPhoto);
            if(file.exists()){
                file.delete();
            }

            //upload
            String originalFilename = img.getOriginalFilename();
            String newFileName = uploadPhoto(img, originalFilename);
            employee.setPhoto(newFileName);
        }

        //no update photo
        employeeService.update(employee);
        return "redirect:/employee/lists";
    }

    private String  uploadPhoto(MultipartFile img, String originalFilename) throws IOException {
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String fileNameSuffix = originalFilename.substring(img.getOriginalFilename().lastIndexOf("."));
        String newFileName = fileNamePrefix+fileNameSuffix;
        img.transferTo(new File(realpath,newFileName));
        return newFileName;
    }

    /**
     * find employee information according to id
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("detail")
    public String detail(Integer id,Model model){
        log.debug("Searching for id: {}",id);
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee",employee);
        return "updateEmp";
    }



    /**
     * save employee information
     * @return
     */
    @RequestMapping("save")
    public String save(Employee employee, MultipartFile img) throws IOException {
        log.debug("name:{},salary:{},birthday:{}",employee.getName(),employee.getSalary(),employee.getBirthday());
        String originalFilename = img.getOriginalFilename();
        log.debug("img name: {}",originalFilename);
        log.debug("img size: {}",img.getSize());
        log.debug("upload path: {}",realpath);

        String newFileName = uploadPhoto(img, originalFilename);
        employee.setPhoto(newFileName);
        employeeService.save(employee);

        return "redirect:/employee/lists";
    }



    @RequestMapping("lists")
    public String lists(Model model){
        log.debug("Search all employee information");
        List<Employee> employeeLists = employeeService.lists();
        model.addAttribute("employeeList",employeeLists);
        return "emplist";
    }




}

















