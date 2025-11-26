package ru.netology.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String login;
    private String password;
    private String status; // "active" или "blocked"
}

