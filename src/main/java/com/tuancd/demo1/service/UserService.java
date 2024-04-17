package com.tuancd.demo1.service;

import com.tuancd.demo1.dto.PaginateRequest;
import com.tuancd.demo1.dto.UserDTO;
import com.tuancd.demo1.dto.UserSpecification;
import com.tuancd.demo1.model.User;
import com.tuancd.demo1.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;

    public Page<User> findAll(UserDTO userDTO, PaginateRequest paginateRequest){
        Specification<User> specification = new UserSpecification(userDTO);
        Pageable pageable = PageRequest.of(paginateRequest.getPage(), paginateRequest.getSize());

        return userRepository.findAll(specification, pageable);
    }
}
