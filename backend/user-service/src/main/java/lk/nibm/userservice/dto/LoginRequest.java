package lk.nibm.userservice.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for login requests.
 * This class encapsulates the data sent by the client when attempting to log in.
 */
@Data
public class LoginRequest {

    /**
     * The email address of the user trying to log in.
     * This field is used as the username for authentication purposes.
     */
    private String email;

    /**
     * The password of the user trying to log in.
     */
    private String password;
}