package com.example.dynamicQuery.multipleSearch;

import com.example.dynamicQuery.User;
import com.example.dynamicQuery.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSearchService implements UserSearchable {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> search(Integer pageNum,Integer pageSize, Integer id, String name, Integer age) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Specification<User> spec = UserSpecs.getSpecs(id,name,age);
        return userRepository.findAll(spec,pageable).toList();
    }
}
