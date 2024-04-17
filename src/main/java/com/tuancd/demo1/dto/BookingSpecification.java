package com.tuancd.demo1.dto;

import com.tuancd.demo1.model.BookedRoom;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookingSpecification implements Specification<BookedRoom> {
    private BookingRequest bookingRequest;

    public BookingSpecification(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }

    @Override
    public Predicate toPredicate(Root<BookedRoom> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (bookingRequest.getCheckInDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get("checkInDate"), bookingRequest.getCheckInDate()));
        }

        if (bookingRequest.getCheckOutDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get("checkOutDate"), bookingRequest.getCheckOutDate()));
        }

        if (bookingRequest.getGuestName() != null) {
            predicates.add(criteriaBuilder.like(root.get("guestName"), "%" + bookingRequest.getGuestName() + "%"));
        }

        if (bookingRequest.getConfirmCode() != null) {
            predicates.add(criteriaBuilder.like(root.get("confirmCode"), "%" + bookingRequest.getConfirmCode() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return query.getRestriction();
    }
}