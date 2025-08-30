package com.example.server.Service;

import com.example.server.Config.ApiResponse;
import com.example.server.Config.exceptions.EntityNotFoundException;
import com.example.server.GlobalContainer.GlobalUser;
import com.example.server.Model.User;
import com.example.server.Repository.UserRepository;
import com.example.server.dto.Request.UserLoginRequestDto;
import com.example.server.dto.Request.UserRequestDto;
import com.example.server.dto.Response.UserResponseDto;
import com.example.server.dto.UserDTO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    String signUp(UserRequestDto userRequestDto);
    ApiResponse login(UserLoginRequestDto userLoginRequestDto);
    boolean checkUserExists(String email);;
    boolean updatePassword(String email, String newPassword);
    List<Map<String, Object>> getAllUsersWithStudentId();

}
