package io.kairos.delivery_system.modules.users.dtos;

import java.util.LinkedHashMap;
import java.util.Map;

public record UserDto (Long id, String name, String username, String email, String phone, Long role_id){
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id());
        map.put("name", name());
        map.put("username", username());
        map.put("email", email());
        map.put("phone", phone());
        map.put("role_id", role_id());
        return map;
    }
}
