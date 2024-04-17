package com.tuancd.demo1.controller;

import com.tuancd.demo1.dto.PaginateRequest;
import com.tuancd.demo1.dto.RoomDTO;
import com.tuancd.demo1.dto.RoomRequest;
import com.tuancd.demo1.model.Room;
import com.tuancd.demo1.model.RoomType;
import com.tuancd.demo1.repository.IRoomRepository;
import com.tuancd.demo1.repository.IRoomTypeRepository;
import com.tuancd.demo1.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    IRoomRepository roomRepository;
    @Autowired
    IRoomTypeRepository roomTypeRepository;
    @Autowired
    RoomService roomService;

    @GetMapping("/listRoom")
    public String showListRoom(Model model,
                               @RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "roomType", required = false) Long roomType,
                               @RequestParam(name = "price", required = false) String price,
                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                               @RequestParam(name = "size", required = false, defaultValue = "5") int size) {
//        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setName(name);
        roomRequest.setRoomType(roomType);
        if (price != null && !price.isEmpty()) {
            roomRequest.setPrice(Double.parseDouble(price));
        } else {
            roomRequest.setPrice(0.0);
        }

        PaginateRequest paginateRequest = new PaginateRequest(page, size);
        Page<Room> rooms = roomService.findAll(roomRequest, paginateRequest);

        List<RoomType> roomTypes = roomTypeRepository.findAll();
        model.addAttribute("roomTypes", roomTypes);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rooms.getTotalPages());

        model.addAttribute("rooms", rooms.getContent());
        return "room/listRoom";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        RoomDTO roomDTO = new RoomDTO();
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        model.addAttribute("roomDTO", roomDTO);
        model.addAttribute("roomTypes", roomTypes);
        return "room/createRoom";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute RoomDTO roomDTO, BindingResult result, Model model) {
        if (roomDTO.getImageFile().isEmpty()) {
            result.addError(new FieldError("roomDTO", "imageFile", "The image file is required"));
        }

        if (result.hasErrors()) {
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            model.addAttribute("roomTypes", roomTypes);
            return "room/createRoom";
        }

        //Save image file
        MultipartFile image = roomDTO.getImageFile();
        Date createAt = new Date();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Room room = new Room();
        room.setName(roomDTO.getName());
        room.setRoomType(roomDTO.getRoomType());
        room.setPrice(roomDTO.getPrice());
        room.setImageFile(storageFileName);
        room.setDescription(roomDTO.getDescription());
        room.setCreateAt(createAt);
        room.setBooked(false);

        roomRepository.save(room);

        return "redirect:/rooms/listRoom";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam Long id) {
        try {
            Room room = roomRepository.findById(id).get();
            model.addAttribute("room", room);
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            model.addAttribute("roomTypes", roomTypes);

            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setName(room.getName());
            roomDTO.setRoomType(room.getRoomType());
            roomDTO.setPrice(room.getPrice());
            roomDTO.setDescription(room.getDescription());

            model.addAttribute("roomDTO", roomDTO);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/rooms/listRoom";
        }
        return "room/editRoom";
    }

    @PostMapping("/edit")
    public String edit(Model model, @RequestParam Long id,
                       @Valid @ModelAttribute RoomDTO roomDTO, BindingResult result) {
        try {
            Room room = roomRepository.findById(id).get();
            model.addAttribute("room", room);

            if (result.hasErrors()) {
                List<RoomType> roomTypes = roomTypeRepository.findAll();
                model.addAttribute("roomTypes", roomTypes);
                return "room/editRoom";
            }

            if (!roomDTO.getImageFile().isEmpty()) {
                //delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + room.getImageFile());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception exception) {
                    System.out.println("Exception: " + exception.getMessage());
                }

                //save new image
                MultipartFile image = roomDTO.getImageFile();
                Date createAt = new Date();
                String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                room.setImageFile(storageFileName);
            }

            room.setName(roomDTO.getName());
            room.setRoomType(roomDTO.getRoomType());
            room.setPrice(roomDTO.getPrice());
            room.setDescription(roomDTO.getDescription());

            roomRepository.save(room);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "redirect:/rooms/listRoom";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        try {
            Room room = roomRepository.findById(id).get();

            //delete room image
            Path imagePath = Paths.get("public/images/" + room.getImageFile());

            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }

            //delete room
            roomRepository.delete(room);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "redirect:/rooms/listRoom";
    }
}