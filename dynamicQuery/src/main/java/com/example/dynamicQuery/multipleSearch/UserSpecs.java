package com.example.dynamicQuery.multipleSearch;

import com.example.dynamicQuery.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs {
    public static Specification<User> getSpecs(Integer id, String name, Integer age) {
        Specification<User> spec = null;
        Specification<User> temp = null;

        if(id != null){
            temp = getUserById(id);
            spec = temp != null? Specification.where(spec).and(temp):temp;
        }

        if(null != name){
            temp = getUserByName(name);
            spec = temp != null? Specification.where(spec).and(temp):temp;
        }

        if(age != null){
            temp = getUserByAge(age);
            spec = temp != null? Specification.where(spec).and(temp):temp;
        }
        return spec;
    }

    private static Specification<User> getUserByAge(Integer age) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.gt(root.get("age"),age));
    }

    private static Specification<User> getUserByName(String name) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase()+"%"));
    }


    private static Specification<User> getUserById(Integer id) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),id));
    }
}
