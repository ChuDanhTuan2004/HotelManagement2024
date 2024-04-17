package com.tuancd.demo1.dto;

import com.tuancd.demo1.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private UserDTO userDTO;

    public UserSpecification(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (userDTO.getUsername() != null) {
            predicates.add(criteriaBuilder.like(root.get("username"), "%" + userDTO.getUsername() + "%"));
        }

        if (userDTO.getEmail() != null) {
            predicates.add(criteriaBuilder.like(root.get("email"), "%" + userDTO.getEmail() + "%"));
        }

        if (userDTO.getRole() != null) {
            predicates.add(criteriaBuilder.equal(root.join("role").get("id"), userDTO.getRole()));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return query.getRestriction();
    }
}