package com.plog.demo.controller;

import com.plog.demo.Dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.plog.demo.repository.userRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private userRepository userRepository;

    @PostMapping("/signup")
    public String signup(UserDto userDto) {
        userRepository.save(userDto.toEntity());
        return "redirect:/";
    }

}
