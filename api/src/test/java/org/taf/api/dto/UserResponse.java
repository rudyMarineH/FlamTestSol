package org.taf.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
    int id;
    String name;
    String email;
    String phone;
    String website;
}
