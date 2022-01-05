package entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserInfo {
    private final String city;
    private final String fullName;
    private final String phoneNumber;
}
