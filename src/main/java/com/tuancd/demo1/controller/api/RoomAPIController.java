package com.tuancd.demo1.controller.api;

import com.tuancd.demo1.model.Room;
import com.tuancd.demo1.model.RoomType;
import com.tuancd.demo1.repository.IRoomRepository;
import com.tuancd.demo1.repository.IRoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class RoomAPIController {
    @Autowired
    private IRoomRepository roomRepository;

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/api/selectTop3FeaturedRoom")
    public List<Room> getTop3(){
        return roomRepository.getTop3();
    }

    @GetMapping("/api/getTop4VipRoom")
    public List<Room> getTop4Vip(){
        return roomRepository.getTop4();
    }

}