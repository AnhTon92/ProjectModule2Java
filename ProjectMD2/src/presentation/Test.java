package presentation;

import bussiness.config.Config;
import bussiness.entity.RoleName;
import bussiness.entity.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Users> users = Config.readData(Config.URL_USERS);
        Users admin = new Users();
        admin.setUserId(1);
        admin.setUserName("admin123");
        admin.setEmail("example@gmail.com");
        admin.setPhone("0988928931");
        admin.setStatus(true);
        admin.setPassword(BCrypt.hashpw("admin123",BCrypt.gensalt(5)));
        admin.setRole(RoleName.ADMIN);
        users.add(admin);
        Config.writeFile(Config.URL_USERS,users);
    }
}
