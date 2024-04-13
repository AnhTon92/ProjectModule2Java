package presentation.admin;

import bussiness.config.Config;
import bussiness.config.InputMethods;
import bussiness.config.Validate;
import bussiness.design.IAuthication;
import bussiness.design.IExamService;
import bussiness.designImpl.AuthenService;
import bussiness.designImpl.CatalogServiceImpl;
import bussiness.designImpl.ExamServiceImpl;
import bussiness.designImpl.ResultServiceImpl;
import bussiness.entity.*;
import presentation.LoginMain;

import java.time.LocalDateTime;
import java.util.*;

import static bussiness.config.Color.*;
import static presentation.LoginMain.authication;
import static presentation.LoginMain.login;

public class MenuAdmin {
    static  Config<Users> config = new Config<>();
    public static List<Object> userLogin;
    static {
        userLogin = Config.readData(Config.URL_USERS);
    }
    private static MenuAdmin menuAdmin = new MenuAdmin();

    public static MenuAdmin getInstance() {
        return menuAdmin;
    }
    public static IAuthication authication = new AuthenService();

    private MenuAdmin() {

    }

    public void displayMenuAdmin() {
        int choice = 0;
        while (choice != 9) {
            System.out.println(YELLOW_BOLD_BRIGHT + ".----------------------------------------------------------------------.");
            System.out.printf("|  QUIZIZZ   ADMINISTATION                  Xin chào, " );
            System.out.println(YELLOW_BOLD_BRIGHT + "|----------------------------QUIZIZZ   MANAGEMENT---------------------------|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[1] Hiển thị danh sách toàn bộ người dùng                        " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[2] Khóa/mở người dùng theo id                         " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[3] Tìm kiếm thông tin người dùng theo tên                                 " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[4] Thống kê danh sách người dự thi                                 " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[5] Thống kê danh sách bài thi                                 " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[6] Thống kê điểm thi trung bình theo tháng                                " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[7] Thống kê top 10 bạn có điểm thi cao nhất trong tháng                                 " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[8] Thêm tài khoản giáo viên                        " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[9] Đăng xuất                                " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("'----------------------------------------------------------------------'" + RESET);
            System.out.print(WHITE_BOLD_BRIGHT + "Nhập lựa chọn : ");
            choice = Validate.validateInt();
            switch (choice) {
                case 1:
                    displayAllUsers();

                    break;
                case 2:
                    changeStatusUsers();
                    break;
                     case 3:
                         findUserByName();
                    break;
                     case 4:
                         listUserTested();
                    break;
                     case 5:
                         listExams();
                    break;
                     case 6:
                         compileScoreByMonth();
                    break;
                     case 7:
                         top10UserBestScore();
                    break;
                case 8:
                        teacherAccRegister();
                    break;
                case 9:
                    Config.writeFileLogin(Config.URL_USER_LOGIN, null);
                    System.out.println(GREEN_BOLD_BRIGHT + "Đăng xuất thành công !" + RESET);
                    break;
                default:
                    System.out.println(RED_BOLD_BRIGHT + "Không hợp lệ, vui lòng nhập lại." + RESET);
            }
        }
    }

    private void teacherAccRegister() {
        System.out.println("--------------Đăng ký tài khoản giáo viên----------------");
        Users user = new Users();
        // Thiết lập thông tin cơ bản cho tài khoản giáo viên
       user.inputData(true);
       user.setUserId(user.getNewId()+ 1);
       user.setRole(RoleName.TEACHER);
       AuthenService.usersList.add(user);
       Config.writeFile(Config.URL_USERS, AuthenService.usersList);
        System.out.println("Đã đăng kí tài khoản giáo viên thành công");
    }

    private void findUserByName() {
        System.out.println("Nhập vào tên người dùng bạn muốn tìm kiếm: ");
        String userNameToFind = InputMethods.getString();
        AuthenService.usersList.stream().filter(users -> users.getUserName().contains(userNameToFind)).forEach(Users :: display);
    }

    public Users findById(Integer id) {
        return AuthenService.usersList.stream()
                .filter(users -> users.getUserId()== id).findFirst().orElse(null);
    }
    private void changeStatusUsers() {
        displayAllUsers();
        System.out.println("Nhập vào id người dùng bạn muốn thay đổi trạng thái:  ");
        int  userId = InputMethods.getInteger();
        Users users = findById(userId);
        if (users != null){
            users.setStatus(!users.isStatus());
            System.out.println("Đã thay đổi thành công");
        } else {
            System.out.println("Lỗi trạng thái hoặc không tìm thấy");
        }
        AuthenService.usersList.set(AuthenService.usersList.indexOf(findById(userId)), users );
        Config.writeFile(Config.URL_USERS, AuthenService.usersList);
    }

    private void displayAllUsers() {
        List<Users> displayAll  = AuthenService.usersList.stream().filter(users ->
                users.getRole().equals(RoleName.USER) || users.getRole().equals(RoleName.TEACHER)).toList();
        for (int i = 0; i < displayAll.size(); i++) {
            displayAll.get(i).display();
        }
    }

    private void listExams() {
        List<Exam> examList = ExamServiceImpl.examList;
        for (Exam exam : examList) {
            exam.display();
        }
    }

    private void listUserTested() {
        List<Users> displayAll  = new java.util.ArrayList<>(AuthenService.usersList.stream().filter(users ->
                users.getRole().equals(RoleName.USER)).toList());

        List<Result> resultList = ResultServiceImpl.resultList;
        displayAll.removeIf(x -> (!resultList.contains(new Result(0, x.getUserId(), 0, 0, null))));

        for (Users users : displayAll) {
            users.display();
        }
    }

    private void compileScoreByMonth() {
        List<Users> displayAll  = new java.util.ArrayList<>(AuthenService.usersList.stream().filter(users ->
                users.getRole().equals(RoleName.USER)).toList());
        List<Result> resultList = ResultServiceImpl.resultList;

        Map<Integer, UserDataScore> data = new HashMap<>();

        for (Result result: resultList) {
            UserDataScore userDataScore = data.get(result.getUserId());
            if (Objects.isNull(userDataScore)) {
                Users users = displayAll.stream().filter(x->x.getUserId() == result.getUserId()).findFirst().orElse(null);
                userDataScore = new UserDataScore(users.getUserId(), users.getUserName(), users.getEmail(), users.isStatus(), 
                        new ArrayList<>(Collections.nCopies(12, 0.0)), 
                        new ArrayList<>(Collections.nCopies(12, 0)));
            }
            Integer month = result.getCreatedDate().getMonth().getValue();
            Double value = userDataScore.getScoreByMonth().get(month) + result.getTotalPoint();
            Integer count = userDataScore.getCount().get(month) + 1;
            userDataScore.getScoreByMonth().set(month, value);
            userDataScore.getCount().set(month, count);

            data.put(result.getResultId(), userDataScore);
        }

        List<UserDataScore> output = new ArrayList<>();
        for (Integer key : data.keySet()) {
            UserDataScore userDataScore = data.get(key);

            for (int i=0; i<12; i++) {
                if (userDataScore.getCount().get(i) > 0) {
                    userDataScore.getScoreByMonth().set(i, userDataScore.getScoreByMonth().get(i)/userDataScore.getCount().get(i));
                }
            }

            output.add(userDataScore);
        }

        for (UserDataScore userDataScore : output) {
            userDataScore.display();
        }

    }

    private void top10UserBestScore(){
        List<Users> displayAll  = new java.util.ArrayList<>(AuthenService.usersList.stream().filter(users ->
                users.getRole().equals(RoleName.USER)).toList());

        List<Result> resultList = ResultServiceImpl.resultList;

        LocalDateTime now = LocalDateTime.now();
        Integer currentMonth = now.getMonthValue();
        List<UserBestScore> userBestScores = new ArrayList<>();

        for (Result result : resultList) {

            if (currentMonth == result.getCreatedDate().getMonthValue()) {
                Integer index = userBestScores.indexOf(new UserBestScore(result.getUserId(), null, null, true));
                UserBestScore userBestScore = userBestScores.stream().filter(x-> x.getUserId() == result.getUserId()).findFirst().orElse(null);

                if (Objects.isNull(userBestScore)) {
                    Users users = displayAll.stream().filter(x->x.getUserId() == result.getUserId()).findFirst().orElse(null);
                    userBestScore = new UserBestScore(users.getUserId(), users.getUserName(), users.getEmail(), users.isStatus());
                }
                Double bestNumber = Math.max(result.getTotalPoint(), userBestScore.getScore());
                userBestScore.setScore(bestNumber);

                if (index == -1) {
                    userBestScores.add(userBestScore);
                } else {
                    userBestScores.set(index, userBestScore);
                }
            }

        }

        userBestScores.sort(Comparator.comparing(UserBestScore::getScore).reversed());
        // Retrieve the top 10 items
        List<UserBestScore> top10Items = userBestScores.subList(0, Math.min(userBestScores.size(), 10));

        for (UserBestScore userBestScore : top10Items) {
            userBestScore.display();
        }


    }

    private List<?> preData(Objects data) {
        return new ArrayList<>(Collections.nCopies(12, data));
    }

}


class UserDataScore{
    private int userId;
    private String userName;
    private String email;
    private boolean status;

    List<Double> scoreByMonth;

    List<Integer> count;

    public UserDataScore(int userId, String userName, String email, boolean status, List<Double> scoreByMonth, List<Integer> count) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.status = status;
        this.scoreByMonth = scoreByMonth;
        this.count = count;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Double> getScoreByMonth() {
        return scoreByMonth;
    }

    public void setScoreByMonth(List<Double> scoreByMonth) {
        this.scoreByMonth = scoreByMonth;
    }

    public List<Integer> getCount() {
        return count;
    }

    public void setCount(List<Integer> count) {
        this.count = count;
    }

    public void display() {
        System.out.printf(YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-4d " +
                YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-22s " +
                YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-24s " +
                "|" + (status ? (GREEN_BOLD_BRIGHT + " %-13s "
                + YELLOW_BOLD_BRIGHT) : (RED_BOLD_BRIGHT + " %-13s " + YELLOW_BOLD_BRIGHT))
                + "|\n", userId, userName, email, (status ? "Hoạt động" : "Ngừng hoạt động"));
        for(Double d: scoreByMonth) {
            System.out.printf(YELLOW_BOLD_BRIGHT + "|" + " %-4.2f " +  "|", d);
        }
    }
}

class UserBestScore {
    private int userId;
    private String userName;
    private String email;
    private boolean status;
    private double score;

    public UserBestScore(int userId, String userName, String email, boolean status) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.status = status;
        this.score = 0.0;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBestScore that)) return false;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public void display() {
        System.out.printf(YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-4d " +
                YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-22s " +
                YELLOW_BOLD_BRIGHT + "|" + WHITE_BOLD_BRIGHT + " %-24s " +
                "|" + (status ? (GREEN_BOLD_BRIGHT + " %-13s "
                + YELLOW_BOLD_BRIGHT) : (RED_BOLD_BRIGHT + " %-13s " + YELLOW_BOLD_BRIGHT))
                + YELLOW_BOLD_BRIGHT + "|" + " %-4.2f " +  "|"
                + "|\n", userId, userName, email, (status ? "Hoạt động" : "Ngừng hoạt động"), score);
    }
}


