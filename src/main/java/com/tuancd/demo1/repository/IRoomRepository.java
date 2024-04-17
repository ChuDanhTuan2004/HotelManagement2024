package com.tuancd.demo1.repository;

import com.tuancd.demo1.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    @Modifying
    @Query(value = "select * from rooms order by id desc limit 3 ", nativeQuery = true)
    List<Room> getTop3();

    @Modifying
    @Query(value = "select * from rooms order by id desc limit 4 ", nativeQuery = true)
    List<Room> getTop4();
}