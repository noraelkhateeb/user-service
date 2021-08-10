package com.service.user.mappers;

import com.service.user.dto.UserDto;
import com.service.user.entities.DAOUser;


public class UserMapper {

    
    public static UserDto toUserDto(DAOUser user, String accesstoken,String refreshtoken) {
    	
    	return new UserDto(user.getId(),user.getLogin(),accesstoken,refreshtoken);
    	
    	
    }
    
}
