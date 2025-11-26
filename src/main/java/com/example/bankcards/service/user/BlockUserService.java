package com.example.bankcards.service.user;


import java.util.Map;

public interface BlockUserService {

   Map<String, String> block(Integer user_id, Boolean blockOrNot);

}
