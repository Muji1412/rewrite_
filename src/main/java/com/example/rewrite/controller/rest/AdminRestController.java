package com.example.rewrite.controller.rest;

import com.example.rewrite.command.user.ApiResponseDto;
import com.example.rewrite.command.user.ChangeRoleDto;
import com.example.rewrite.command.user.UserDTO;
import com.example.rewrite.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserDTO> getUsers( // Users 엔티티 대신 DTO 사용 권장
                                   @RequestParam(required = false) String search,
                                   @RequestParam(required = false) String role) {

        // if문으로 예외처리 해주기
        if (role.equals("none")) {
            String getall = null;
            return userService.findUsers(getall, getall);
        }
        return userService.findUsers(search, role);
    }

    @PostMapping("/setUserRole")
    public ResponseEntity<Object> setUserRole(@RequestBody ChangeRoleDto changeRoleDto) {
        try {
            userService.changeRole(changeRoleDto.getUid(), changeRoleDto.getRole());
            return ResponseEntity.ok(ApiResponseDto.success("role 변경 성공"));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.fail(e.getMessage()));
        }
    }
    @PostMapping("/deleteUser")
    public ResponseEntity<Object> deleteUser(@RequestBody UserDTO userDto) {

        return null;
    }

}