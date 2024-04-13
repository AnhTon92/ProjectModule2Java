package presentation;

import bussiness.config.Config;
import bussiness.config.InputMethods;
import bussiness.design.IAuthication;
import bussiness.designImpl.AuthenService;
import bussiness.entity.RoleName;
import bussiness.entity.Users;
import presentation.admin.MenuAdmin;
import presentation.teacher.MenuTeacher;
import presentation.user.MenuUser;

public class LoginMain {

    private static MenuUser menuUser = new MenuUser();
    private static MenuTeacher menuTeacher = new MenuTeacher();
    private static IAuthication authication = new AuthenService();
    public static Users user = null;
    public static Users currentUser = Config.readDataLogin(Config.URL_USER_LOGIN);
    public static void main(String[] args) {
        while (true){
            System.out.println("++++++++++++++++++++++++MENU+++++++++++++++++++++++");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("3. Đăng xuất");
            System.out.println("Nhập chức năng");
            byte choice = InputMethods.getByte();
            switch (choice){
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Thoát");
                default:
                    System.err.println("Lựa chọn không hợp lệ");
            }
            if (choice==3){
                break;
            }
        }
    }
    public static void login(){
        System.out.println("----------Đăng nhập--------------");
        System.out.println("Nhập username :");
        String username  = InputMethods.getString();
        System.out.println("Nhập password :");
        String password  = InputMethods.getString();

        Users userLogin = authication.login(username, password);
        if (userLogin==null){
            System.err.println("Tài khoản hoặc mật khẩu không chính xác");
            System.out.println("1. Tiếp tục đăng nhập");
            System.out.println("2. Bạn chưa có tài khoản? Đăng kí ngay ");
            System.out.println("3. Thoát");
            System.out.println("------Nhập lựa chọn--------");
            byte choice = InputMethods.getByte();
            switch (choice){
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    return;
                default:
                    System.err.println("Nhập lựa chọn không chính xác");
            }
        }else {
            Config.writeFileLogin(Config.URL_USER_LOGIN, userLogin);
            if (userLogin.getRole().equals(RoleName.ADMIN)){
                user = userLogin;
                MenuAdmin.getInstance().displayMenuAdmin();
            }else if (userLogin.getRole().equals(RoleName.USER)){
                if (!userLogin.isStatus()){
                    System.err.println("Tài khoản đã bị khóa");
                }else {
                    user = userLogin;
                    menuUser.displayMenuUser();
                }
            }else if (userLogin.getRole().equals(RoleName.TEACHER)){
                if (!userLogin.isStatus()){
                    System.err.println("Tài khoản đã bị khóa");
                }else {
                    user = userLogin;
                    menuTeacher.displayMenuTeacher();
                }
            }else {
                System.err.println("Không có quyền truy cập");
            }
        }
    }
    public static void register(){
        System.out.println("--------------Đăng ký----------------");
        Users user = new Users();
        authication.register(user);
        System.out.println("Đăng kí thành công!");
        login();
    }
}
