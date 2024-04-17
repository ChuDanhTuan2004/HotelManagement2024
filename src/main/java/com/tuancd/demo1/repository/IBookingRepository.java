package com.tuancd.demo1.repository;

import com.tuancd.demo1.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingRepository extends JpaRepository<BookedRoom, Long>, JpaSpecificationExecutor<BookedRoom> {
}
