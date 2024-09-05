package com.productmgmt.service;

import com.productmgmt.model.Users;

public interface UserService {

    Users register(Users user);

    String verify(Users user);
}
