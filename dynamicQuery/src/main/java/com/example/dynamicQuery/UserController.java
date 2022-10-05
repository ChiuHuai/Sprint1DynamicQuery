package com.example.dynamicQuery;

import com.example.dynamicQuery.multipleSearch.UserSearchable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSearchable userSearchable;

    @GetMapping("/multipleSearch")
    public List<User> multipleSearch(@RequestParam(required = false)Integer pageNum,
                                     @RequestParam(required = false)Integer pageSize,
                                     @RequestParam(required = false)Integer id,
                                     @RequestParam(required = false)String name,
                                     @RequestParam(required = false)Integer age){
        return userSearchable.search(pageNum,pageSize,id,name,age);
    }

    @GetMapping("/condition")
    public List<User> getUserListByCondition(@PathParam("name") String name, @PathParam("age") Integer age){
        return this.userService.getUserListByCondition(name,age);
    }

    @GetMapping("/FindByID")
    public User FindByID1(){
        return userService.FindByID1();
    }

    @GetMapping("like")
    public List<User> testFindBySpecificationLike(){
        return userService.testFindBySpecificationLike();
    }

    @GetMapping("page")
    public List<User> testFindBySpecificationPage(@RequestParam("page") int page){
        return userService.testFindBySpecificationPage(page);
    }


    @GetMapping("sort")
    public List<User> testFindBySpecificationSort(){
        return userService.testFindBySpecificationSort();
    }


}
