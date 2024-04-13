package bussiness.designImpl;

import bussiness.config.Config;
import bussiness.design.IUserService;
import bussiness.entity.RoleName;
import bussiness.entity.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements IUserService {

    public static List<Users> usersList;
    static {
        usersList = Config.readData(Config.URL_USERS);
        if (usersList == null) {
            usersList = new ArrayList<>();
            usersList.add(new Users( 0,  "admin", BCrypt.hashpw("abc123",BCrypt.gensalt(5)), "Tôn",  "Ngô", "Hà Nội", "anhton@gmail.com",  "0988928930",  true, RoleName.ADMIN));
            new UserServiceImpl().updateData();
        }
    }
    @Override
    public List<Users> findAll() {
        return usersList;
    }

    @Override
    public void save(Users users) {
    if (findById(users.getUserId())== null) {
        usersList.add(users);
        updateData();
    }else {
        usersList.set(usersList.indexOf(users), users);
        updateData();
    }
    }

    @Override
    public void delete(Integer id) {
    usersList.remove(findById(id));
    updateData();
    }

    @Override
    public Users findById(Integer id) {
        for (Users users : usersList) {
            if (users.getUserId() == id) {
                return users;
            }
        }
        return null;
    }

    @Override
    public void updateData() {
    Config.writeFile(Config.URL_USERS, usersList);
    }

    @Override
    public boolean existUserName(String username) {
        for (Users users : usersList) {
            if (users.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existEmail(String email) {
        for (Users users : usersList) {
            if (users.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Users checkLogin(String username, String password) {
        for(Users users : usersList) {
            if (users.getUserName().equals(username) && BCrypt.checkpw(password,users.getPassword())) {
                return users;
            }
        }
        return null;
    }
    public int getNewId() {
        int idMax = 0;
        for (Users users : usersList) {
            if (users.getUserId() > idMax) {
                idMax = users.getUserId();
            }
        }
        return (idMax + 1);
    }
}
