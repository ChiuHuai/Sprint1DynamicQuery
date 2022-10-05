package com.example.dynamicQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUserListByCondition(String name, Integer age) {
        List<User> all = userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + name));
                predicateList.add(criteriaBuilder.greaterThan(root.get("age"), age));
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        });
        return all;
    }

    public List<User> getUserListWithPageByCondition(Pageable page, String name, Integer age) {
        return null;
    }

    public List<User> getUserListWithPage(Pageable page) {
        return null;
    }

    //find by ID
    public User FindByID1(){
        //repo.findOne(Specification<T> spec) T為entity
        //參數有Specification -> 先構建(實作抽象方法 toPredicate)
        Specification<User> specification = (root,cq,cb)-> cq.where(cb.equal(root.get("id"),1)).getRestriction();
        //Expression為cb.equal(參數), root extends from extends Expression
        //root.get("屬性名")
        //cb.equal(root.get("id"),1) 查詢 id 為  1
        Optional<User> optionalUser = userRepository.findOne(specification);

        if(optionalUser.isPresent()){
            System.out.println(optionalUser.get());
            return optionalUser.get();
        }
        return null;
    }

    //anonymous
    public void FindByID2(){
        Specification<User> specification = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return query.where(criteriaBuilder.equal(root.get("id"),1)).getRestriction();
            }
        };

        Optional<User> optionalUser = userRepository.findOne(specification);
        if(optionalUser.isPresent()){
            System.out.println(optionalUser.get());
        }
    }

    //多個查詢條件
    public void testFindBySpecification(){
        //CriteriaQuery<T> where(Predicate... var1); where 可以傳入多個args
        Specification<User> specifications = (root,cq,cb)-> cq.where(
                cb.equal(root.get("id"),0),
                cb.equal(root.get("name"),"xxx")).getRestriction();

        Optional<User> optionalUser = userRepository.findOne(specifications);
        if(optionalUser.isPresent()){
            System.out.println(optionalUser.get());
        }
    }

    //in(?,?,?) 多ID
    public void testFindBySpecificationIn(){
        // <T> In<T> in(Expression<? extends T> var1);
        // arg->Expression -> root.get("fieldName").value(?).value(?)...
        //in(?,?,?)
        Specification<User> specification = (root,cq,cb)-> cq.where(
                cb.in(root.get("id")).value(1).value(2)).getRestriction();
        List<User> userList = userRepository.findAll(specification);
        userList.forEach(System.out::println);
    }

    //like name
    public List<User> testFindBySpecificationLike(){
        // Predicate like(Expression<String> var1, String var2);
        //like?
        // arg->Expression -> root.get("fieldName")
        Specification<User> specification = (root,cq,cb)->
                cq.where(cb.like(root.get("name"),"%"+"x"+"%")).getRestriction();
        List<User> all = userRepository.findAll(specification);

        return all;
    }

    //page
    public List<User> testFindBySpecificationPage(int page){
        // Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable);
        // public static PageRequest of(int page, int size, Sort sort)
        // PageRequest extends AbstractPageRequest implements Pageable
        Specification<User> specification = (root,cq,cb)->
                cq.where(cb.like(root.get("name"),"%"+"x"+"%")).getRestriction();

        //PageRequest.of(0,1) 第0頁，每頁1筆
        //return Page<T> -> toList()
        List<User> all = userRepository.findAll(specification, PageRequest.of(page,1)).toList();
        return all;

//        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(page, 1));
//        userPage.getTotalPages() -> 總共幾頁
//        userPage.getTotalElements() -> 總共幾筆
//        userPage.getSize() -> 每頁幾筆
//        userPage.getNumber() -> 現在第幾頁(index)
    }

    //sort
    public List<User> testFindBySpecificationSort(){
        // List<T> findAll(@Nullable Specification<T> spec, Sort sort);
        Specification<User> specification = (root,cq,cb)->
                cq.where(cb.like(root.get("name"),"%"+"x"+"%")).getRestriction();

        //PageRequest.of(0,1) 第0頁，每頁1筆
        //return Page<T> -> toList()
        List<User> all = userRepository.findAll(specification, Sort.by("id").descending());
        return all;

    }

    //使用CriteriaBuilder cb 操作 cq
    //單條件
    public void testFindBySpecificationCb1(){
        Specification<User> specification = (root,cq,cb)->
                cb.like(root.get("name"),"%"+"x"+"%");

        List<User> all = userRepository.findAll(specification);
    }
    //多條件
    public void testFindBySpecificationCb2(){
        //ge greater equal
        Specification<User> specification = (root,cq,cb)->
                cb.and(cb.ge(root.get("id"),1),cb.like(root.get("name"),"%"+"x"+"%"));
        List<User> all = userRepository.findAll(specification);
    }

}
