package com.message.chatservice.utils.restemplate;

import com.message.chatservice.exception.ChatException;
import com.message.chatservice.model.dto.UserInfo;


public interface RestAuthUtil {
    UserInfo getUserFromAuthService(String email, String token);

    UserInfo updateUserInfo(UserInfo userInfo, String token);
}
