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
    // phương thức login: Cho phép người dùng đăng nhập bằng cách kiểm tra tên đăng nhập và mật khẩu.
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
// Phương thức register Đăng ký người dùng mới vào hệ thống và ghi thông tin vào file.

    @Override
    public void register(Users user) {
        user.inputData(true);
        user.setUserId(getMaxUserId() + 1);
        AuthenService.usersList.add(user);
        Config.writeFile(Config.URL_USERS,AuthenService.usersList);

    }
// phương thức Tìm kiếm người dùng dựa trên tên đăng nhập.
    private Users getUserFromUsername(String username) {
        return usersList.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst().orElse(null);

    }
    // Lấy ID lớn nhất từ danh sách người dùng để tạo ID mới.
    private static int getMaxUserId() {
        return usersList.stream()
                .map(Users::getUserId)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
