package com.example.dynamicQuery.multipleSearch;

import com.example.dynamicQuery.User;

import java.util.List;

public interface UserSearchable {
    List<User> search(Integer pageNum,Integer pageSize,Integer id, String name, Integer age);
}
