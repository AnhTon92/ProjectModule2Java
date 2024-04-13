package bussiness.designImpl;

import bussiness.config.Config;
import bussiness.design.IAuthication;
import bussiness.entity.RoleName;
import bussiness.entity.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Comparator;
import java.util.List;

public class AuthenService implements IAuthication {
    public static List<Users> usersList;



    static {
        usersList = Config.readData(Config.URL_USERS);
    }
    @Override
    public Users login(String userName, String password) {
        Users userLogin = getUserFromUsername(userName);
        if(userLogin != null){
            boolean checkLogin = BCrypt.checkpw(password,userLogin.getPassword());
            if(checkLogin){
                return userLogin;
            }
        }
        return null;
    }


    @Override
    public void register(Users user) {
        user.inputData(true);
        user.setUserId(getMaxUserId() + 1);
        AuthenService.usersList.add(user);
        Config.writeFile(Config.URL_USERS,AuthenService.usersList);

    }

    private Users getUserFromUsername(String username) {
        return usersList.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst().orElse(null);

    }
    private static int getMaxUserId() {
        return usersList.stream()
                .map(Users::getUserId)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
