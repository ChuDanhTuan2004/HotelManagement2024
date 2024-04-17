package com.tuancd.demo1.dto;

import com.tuancd.demo1.model.Room;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoomSpecification implements Specification<Room> {
    private RoomRequest roomDTO;

    public RoomSpecification(RoomRequest roomDTO) {
        this.roomDTO = roomDTO;
    }

    @Override
    public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (roomDTO.getName() != null) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + roomDTO.getName() + "%"));
        }

        if (roomDTO.getRoomType() != null) {
            predicates.add(criteriaBuilder.equal(root.join("roomType").get("id"), roomDTO.getRoomType()));
        }

        if (roomDTO.getPrice() != 0) {
            predicates.add(criteriaBuilder.equal(root.get("price"), roomDTO.getPrice()));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return query.getRestriction();
    }
}