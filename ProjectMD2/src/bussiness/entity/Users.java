package bussiness.entity;

import bussiness.config.InputMethods;
import bussiness.design.IUserService;
import bussiness.designImpl.AuthenService;
import bussiness.designImpl.UserServiceImpl;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static bussiness.config.Color.*;
import static bussiness.entity.RoleName.*;

public class Users implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName, address;
    private String email;
    private String phone;
    private boolean status;
    private RoleName role;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(032|033|034|035|036|037|038|039|096|097|098|086|083|084|085|081|082|088|091|094|070|079|077|076|078|090|093|089|056|058|092|059|099)[0-9]{7}$");

    public Users() {
    }

    public Users(int userId, String userName, String password, String firstName, String lastName, String address, String email, String phone, boolean status, RoleName role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.role = role;
    }

    public Users(int i, String admin, String mail, String admin1, String admin2, String abc123, RoleName roleName, boolean b, String number) {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }

    public int getNewId() {
        int idMax = AuthenService.usersList.stream().map(Users::getUserId).max(Comparator.naturalOrder()).orElse(0);
        return idMax + 1;
    }

    public boolean validateUsername(String userName) {
        return USERNAME_PATTERN.matcher(userName).matches();
    }

    public boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean validatePhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public boolean validateName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean validateAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    public void inputData(boolean isAdd) {
        IUserService userService = new UserServiceImpl();
        if (isAdd) {
            this.userId = getNewId();
            this.role = USER;
            this.status = true;
        }
        System.out.println("input User name");
        this.userName = inputUsername(userService);
        System.out.println("input first name");
        this.firstName = inputFirstName();
        System.out.println("input last name");
        this.lastName = inputLastName();
        System.out.println("input address");
        this.address = inputUserAddress();
        System.out.println("input email");
        this.email = inputEmail(userService);
        System.out.println("input password");
        this.password = inputPassword();
        System.out.println("input phone");
        this.phone = inputPhoneNumber();
    }

    public String inputUserAddress() {

        while (true) {
            String address = InputMethods.getString();
            if (validateAddress(address)) {
                return address;
            } else {
                System.out.println("địa chỉ không được để trống");
            }
        }
    }

    public String inputUsername(IUserService userService) {
        while (true) {
            String userName = InputMethods.getString();
            if (validateUsername(userName)) {
                if (userService.existUserName(userName)) {
                    System.out.println("tài khoản đã bị trùng");
                } else {
                    return userName;
                }
            } else {
                System.out.println("Sai định đạng tên đăng nhập. Phải nhập vào tên đăng nhập ít nhất 6 kí tự và không chứa kí tự đặc biệt!");
            }
        }
    }

    public String inputPassword() {
        while (true) {
            String password = InputMethods.getString();
            if (validatePassword(password)) {
                return BCrypt.hashpw(password, BCrypt.gensalt(5));
            } else {
                System.out.println("Sai định dạng mật khẩu. Phải chứa ít nhất 8 kí tự và chứa cả số và chữ");
            }
        }
    }

    public String inputEmail(IUserService userService) {
        while (true) {
            String email = InputMethods.getString();
            if (validateEmail(email)) {
                if (userService.existEmail(email)) {
                    System.out.println("Email bị trùng");
                } else {
                    return email;
                }

            } else {
                System.out.println("Email không hợp lệ, sai định dạng");
            }
        }
    }

    public String inputPhoneNumber() {
        while (true) {
            String phone = InputMethods.getString();
            if (validatePhone(phone)) {
                return phone;
            } else {
                System.out.println("Sai định dạng số điện thoại. Số điện thoại phải thuộc quốc gia Việt Nam");
            }
        }
    }

    public String inputFirstName() {
        while (true) {
            String firstName = InputMethods.getString();
            if (validateName(firstName)) {
                return firstName;
            } else {
                System.out.println("Tên không được để trống.");
            }
        }
    }

    public String inputLastName() {
        while (true) {
            String lastName = InputMethods.getString();
            if (validateName(lastName)) {
                return lastName;
            } else {
                System.out.println("Họ không được để trống.");
            }
        }
    }

    public void display() {
        System.out.printf(YELLOW_BOLD_BRIGHT + "| userId" + WHITE_BOLD_BRIGHT + " %-4d " +
                YELLOW_BOLD_BRIGHT + "| lastName: " + WHITE_BOLD_BRIGHT + " %-22s " +
                YELLOW_BOLD_BRIGHT + "| firstName: " + WHITE_BOLD_BRIGHT + " %-24s " +
                YELLOW_BOLD_BRIGHT + "| userName: " + WHITE_BOLD_BRIGHT + " %-23s " +
                YELLOW_BOLD_BRIGHT + "| email: " + WHITE_BOLD_BRIGHT + " %-19s " +
                YELLOW_BOLD_BRIGHT + "| phone: " + WHITE_BOLD_BRIGHT + " %-15s " +
                YELLOW_BOLD_BRIGHT + "| vai trò: " + (role == ADMIN ? (BLUE_BOLD_BRIGHT + " %-14s" + YELLOW_BOLD_BRIGHT) :
                (WHITE_BOLD_BRIGHT + " %-14s" + YELLOW_BOLD_BRIGHT)) +
                "|" + (status ? (GREEN_BOLD_BRIGHT + " %-13s "
                + YELLOW_BOLD_BRIGHT) : (RED_BOLD_BRIGHT + " %-13s " + YELLOW_BOLD_BRIGHT))
                + "|\n", userId, lastName, firstName, userName, email, phone, role, (status ? "Hoạt động" : "Bị khoá"))
        ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users users)) return false;
        return userId == users.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}


