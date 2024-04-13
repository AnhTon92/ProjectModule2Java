package bussiness.design;

import bussiness.entity.Users;

public interface IAuthication {
    Users login(String username, String password);
    void register(Users users);
}
