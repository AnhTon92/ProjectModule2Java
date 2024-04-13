package bussiness.design;

import bussiness.entity.Users;

public interface IUserService extends IGenericService<Users, Integer>{
    boolean existUserName(String username);
    boolean existEmail(String email);

    Users checkLogin(String username, String password);
}
