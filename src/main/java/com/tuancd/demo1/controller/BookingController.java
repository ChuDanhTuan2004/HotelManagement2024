package com.tuancd.demo1.controller;

import com.tuancd.demo1.dto.BookingRequest;
import com.tuancd.demo1.dto.PaginateRequest;
import com.tuancd.demo1.model.BookedRoom;
import com.tuancd.demo1.model.Room;
import com.tuancd.demo1.repository.IBookingRepository;
import com.tuancd.demo1.repository.IRoomRepository;
import com.tuancd.demo1.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    IBookingRepository bookingRepository;
    @Autowired
    BookingService bookingService;
    @Autowired
    IRoomRepository roomRepository;

    @GetMapping("/listBooking")
    public String showListBooking(Model model,
                                  @RequestParam(name = "checkInDate", required = false) @DateTimeFormat LocalDate checkInDate,
                                  @RequestParam(name = "checkOutDate", required = false) @DateTimeFormat LocalDate checkOutDate,
                                  @RequestParam(name = "guestName", required = false) String guestName,
                                  @RequestParam(name = "confirmCode", required = false) String confirmCode,
                                  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                  @RequestParam(name = "size", required = false, defaultValue = "5") int size,
                                  HttpServletRequest httpServletRequest
    ) {
        //set booking request model
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCheckInDate(checkInDate);
        bookingRequest.setCheckOutDate(checkOutDate);
        bookingRequest.setGuestName(guestName);
        bookingRequest.setConfirmCode(confirmCode);

        //find all with paginate
        PaginateRequest paginateRequest = new PaginateRequest(page, size);
        Page<BookedRoom> bookings = bookingService.findAll(bookingRequest, paginateRequest);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookings.getTotalPages());
        String noti = httpServletRequest.getParameter("suc");
        model.addAttribute("suc",noti);
        model.addAttribute("bookings", bookings.getContent());
        return "booking/listBooking";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        BookingRequest bookingRequest = new BookingRequest();
        model.addAttribute("bookingRequest", bookingRequest);
        model.addAttribute("rooms", roomRepository.findAll());
        return "booking/addBooking";
    }

    @PostMapping("/create")
    public String saveBooking(@RequestParam Long roomId, Model model,
                              @Valid @ModelAttribute BookingRequest bookingRequest,
                              BindingResult result, RedirectAttributes redirectAttributes) {
        String href;
        if (result.hasErrors()) {
            model.addAttribute("bookingRequest", bookingRequest);
            model.addAttribute("rooms", roomRepository.findAll());
            href = "booking/addBooking";
            return href;
        }

        BookedRoom bookedRoom = new BookedRoom();
        bookedRoom.setCheckInDate(LocalDate.from(bookingRequest.getCheckInDate()));
        bookedRoom.setCheckOutDate(LocalDate.from(bookingRequest.getCheckOutDate()));
        bookedRoom.setGuestName(bookingRequest.getGuestName());
        bookedRoom.setNumberOfAdults(bookingRequest.getNumberOfAdults());
        bookedRoom.setNumberOfChildren(bookingRequest.getNumberOfChildren());
        bookedRoom.calculateTotalNumberOfGuest();

        try {
            String confirmationCode = bookingService.saveBooking(roomId, bookedRoom);
            System.out.println("Room booked successfully, Your booking confirmation code is :" + confirmationCode);

            bookingRepository.save(bookedRoom);
        } catch (Exception e) {
            String string = "err";
            model.addAttribute("err",string);
            System.out.println(e.getMessage());
            href = "booking/addBooking";
            return href;
        }
        String string = "suc";
        redirectAttributes.addAttribute("suc",string);
        return "redirect:/bookings/listBooking";
    }

    @GetMapping("/delete")
    public String cancelBooking(@RequestParam Long id) {
        try {
            BookedRoom bookedRoom = bookingRepository.findById(id).get();
            bookingRepository.delete(bookedRoom);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/bookings/listBooking";
    }
}