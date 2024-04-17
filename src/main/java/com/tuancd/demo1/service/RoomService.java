package com.tuancd.demo1.service;

import com.tuancd.demo1.dto.PaginateRequest;
import com.tuancd.demo1.dto.RoomRequest;
import com.tuancd.demo1.dto.RoomSpecification;
import com.tuancd.demo1.model.Room;
import com.tuancd.demo1.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    IRoomRepository roomRepository;

    public Page<Room> findAll(RoomRequest roomRequest, PaginateRequest paginateRequest) {
        Specification<Room> specification = new RoomSpecification(roomRequest);
        Pageable pageable = PageRequest.of(paginateRequest.getPage(), paginateRequest.getSize());

        return roomRepository.findAll(specification, pageable);
    }
}
