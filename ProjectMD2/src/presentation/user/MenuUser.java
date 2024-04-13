package presentation.user;

import bussiness.config.Config;
import bussiness.config.InputMethods;
import bussiness.config.Validate;
import bussiness.design.IExamService;
import bussiness.design.IResultDetailService;
import bussiness.design.IResultService;
import bussiness.design.IUserService;
import bussiness.designImpl.*;
import bussiness.entity.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bussiness.config.Color.GREEN_BOLD_BRIGHT;
import static bussiness.config.Color.RESET;
import static bussiness.designImpl.CatalogServiceImpl.examService;
import static bussiness.designImpl.ResultServiceImpl.resultList;

public class MenuUser {
    IExamService examService = new ExamServiceImpl();
    IUserService userService = new UserServiceImpl();
    IResultService resultService = new ResultServiceImpl();
    IResultDetailService resultDetailService = new ResultDetailServiceImpl();
    public static List<Users> usersList;

    static {
        userLogin = Config.readDataLogin(Config.URL_USER_LOGIN);
    }

    public static Users userLogin;

    public void displayMenuUser() {
        while (true) {
            System.out.println("Welcome to user page");
            System.out.println("1. Hiển thị danh sách đề thi");
            System.out.println("2. Tìm kiếm đề thi theo danh mục");
            System.out.println("3. Bắt đầu thi");
            System.out.println("4. Hiển thị thông tin cá nhân");
            System.out.println("5. Chỉnh sửa thông tin cá nhân");
            System.out.println("6. Hiển thị lịch sử bài thi và review bài thi");
            System.out.println("7. Đồi mật khẩu");
            System.out.println("8. Đăng xuất");

            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    displayPublicExam();
                    break;
                case 2:
                    findExamByName();
                    break;
                case 3:
                    startExam();
                    break;
                case 4:
                    displayUserInfo();
                    break;
                case 5:
                    updateInfo();
                    break;
                case 6:
                    seeResultExam();
                    break;
                case 7:
                    changePassword();
                    break;
                case 8:
                    Config.writeFileLogin(Config.URL_USER_LOGIN, null);
                    System.out.println(GREEN_BOLD_BRIGHT + "Đăng xuất thành công !" + RESET);
                    return;

                default:
                    System.out.println("Your choice out of range");
                    break;

            }
        }
    }

    private void displayUserInfo() {
        userLogin.display();
    }

    private void changePassword() {
        System.out.println("--------------------------THAY ĐỔI MẬT KHẨU-----------------------------");
        System.out.println("Nhập mật khẩu hiện tại: ");
        String currentPass = Validate.validateString();
        if (BCrypt.checkpw(currentPass, userLogin.getPassword())) {
            System.out.println("Nhập mật khẩu mới: ");
            String newPass = Validate.validateString();
            System.out.println("Xác nhận mật khẩu: ");
            while (true) {
                String confirmPass = Validate.validateString();
                if (newPass.equals(confirmPass)) {
                    userLogin.setPassword(BCrypt.hashpw(newPass, BCrypt.gensalt(5)));
                    Config.writeFileLogin(Config.URL_USER_LOGIN, userLogin);
                    Users userEdit = userService.findById(userLogin.getUserId());
                    userEdit.setPassword(userLogin.getPassword());
                    userService.save(userEdit);
                    System.out.println("Đã thay đổi mật khẩu thành công !");
                    Config.writeFileLogin(Config.URL_USER_LOGIN, null);
                    break;
                } else {
                    System.out.println("Mật khẩu không khớp, vui lòng nhập lại");
                }
            }
        } else {
            System.out.println("Sai mật khẩu");
        }
        System.out.println("======================================================================================");
    }

    private void seeResultExam() {
//        // Lấy ra danh sách kết quả bài thi của người dùng đang đăng nhập
		Users users = Config.readDataLogin(Config.URL_USER_LOGIN);
        List<Result> userResults = resultList.stream()
				  .filter(result -> result.getUserId() == users.getUserId())
				  .toList();
		if (userResults.isEmpty()) {
			System.out.println("Bạn chưa có lịch sử bài thi nào.");
			return;
		}
		// Hiển thị lịch sử bài thi
		for (Result result : userResults) {

			// tìm exam thông qua result
			Exam exam = examService.findById(result.getExamId());

			System.out.println("ID Bài Thi: " + result.getExamId() + ", Điểm: " + result.getTotalPoint() + ", Ngày Thi: " + result.getCreatedDate());
			// Lấy ra danh sách chi tiết kết quả bài thi
			List<ResultDetail> details = ResultDetailServiceImpl.resultDetailsList.stream()
					  .filter(detail -> detail.getResultId() == result.getResultId())
					  .toList();

			// Hiển thị chi tiết từng câu hỏi và câu trả lời của người dùng
			for (ResultDetail detail : details) {
				// Giả định rằng có phương thức tìm câu hỏi và câu trả lời theo ID
				Question question = findQuestionById(exam.getListQuestion(), detail.getIndexQuestion());
                if (question != null) {
                    Answer answer = findAnswerById(question.getAnswerOption(), detail.getIndexChoice());
                    if (answer != null){
                        System.out.println("Câu hỏi: " + question.getQuestionContent());
                        System.out.println("Câu trả lời của bạn: " + answer.getAnswerContent());
                        System.out.println("Đúng hay Sai: " + (detail.isCheck() ? "Đúng" : "Sai"));
                    } else {
                        System.out.println("Câu hỏi: " + question.getQuestionContent());
                        System.out.println("Câu trả lời của bạn: Không có thông tin");
                    }
                } else {
                    System.out.println("Không tìm thấy thông tin câu hỏi.");
                }
            }
			System.out.println("--------------------------------");
		}
    }

    private Answer findAnswerById(List<Answer> answerOption, int indexChoice) {
        return answerOption.stream().filter(answer -> answer.getAnswerId() == indexChoice).findFirst().orElse(null);
    }

    private Question findQuestionById(List<Question> questionList, int questionId) {
        return questionList.stream().filter(question -> question.getQuestionId() == questionId).findFirst().orElse(null);
    }

    private void updateInfo() {
        while (true) {
            System.out.println("Bạn muốn chỉnh sửa phần nào của thông tin cá nhân?: ");
            System.out.println("[1] UserName");
            System.out.println("[2]. Phone Number");
            System.out.println("[3]. Address");
            System.out.println("[4]. First Name");
            System.out.println("[5]. Last Name");
            System.out.println("[6]. Quit");
            int choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
                    updateUserName();
                    break;
                case 2:
                    updatePhoneNumber();
                    break;
                case 3:
                    updateAddress();
                    break;
                case 4:
                    updateFirstName();
                    break;
                case 5:
                    updateLastName();
                    break;
                case 6:

                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Mời lựa chọn từ 1 đến 6");

            }
        }
    }

    private void updateLastName() {
        System.out.println("Nhập vào tên mới: ");
        String newLastName = InputMethods.getString();
        userLogin.setLastName(newLastName);
        userService.save(userLogin);
        System.out.println("Tên đã được cập nhật.");
    }

    private void updateFirstName() {
        System.out.println("Nhập vào họ mới: ");
        String newFirstName = InputMethods.getString();
        userLogin.setFirstName(newFirstName);
        userService.save(userLogin);
        System.out.println("Họ đã được cập nhật.");

    }

    private void updateAddress() {
        System.out.println("Nhập vào địa chỉ mới: ");
        String newAddress = InputMethods.getString();
        userLogin.setAddress(newAddress);
        userService.save(userLogin);
        System.out.println("Địa chỉ đã được cập nhật.");
    }

    private void updatePhoneNumber() {
        System.out.println("Nhập vào số điện thoại mới: ");
        String newPhoneNumber = InputMethods.getString();
        userLogin.setPhone(newPhoneNumber);
        userService.save(userLogin);
        System.out.println("Số điện thoại đã được cập nhật.");
    }

    private void updateUserName() {
        System.out.println("Nhập vào tên người dùng mới: ");
        String newUserName = InputMethods.getString();
        userLogin.setUserName(newUserName);
        userService.save(userLogin);
        System.out.println("Tên người dùng đã được cập nhật.");
    }

    private void startExam() {
        displayPublicExam();
        System.out.println("Chọn Id đề thi bạn muốn bắt đầu: ");
        int examIdToChoose = InputMethods.getInteger();
        Exam exam = examService.findById(examIdToChoose);
        if (exam != null) {
            Result result = new Result();
            // điền các thông tịn
            result.setResultId(resultService.getNewId());
            Users users = Config.readDataLogin(Config.URL_USER_LOGIN);
            result.setUserId(users.getUserId());
            result.setExamId(exam.getExamId());
            result.setCreatedDate(LocalDateTime.now());
            int totalCorrectAnswers = 0;
            // for hết câu hỏi
            for (int i = 0; i < exam.getListQuestion().size(); i++) {
                Question question = exam.getListQuestion().get(i);
                // cho nó biết câu hỏi thứ mấy dựa vào index
                System.out.println("Đây là câu hỏi thứ " + (i + 1) + ":");
                // hiện nội dung câu hỏi : questionh.getContent()
                System.out.println(question.getQuestionContent());
                ResultDetail detail = new ResultDetail();
                // show ra list answer và cho nó lựa chọn
                for (int j = 0; j < question.getAnswerOption().size(); j++) {
                    System.out.println("Phương án thứ " + (j + 1) + ":" + question.getAnswerOption().get(j).getAnswerContent());
                }
                System.out.println("Chọn phương án trả lời (nhập số): ");
                int answerIndex = InputMethods.getInteger() - 1;
                Answer selectedAnswer = question.getAnswerOption().get(answerIndex);
                detail.setIndexQuestion(question.getQuestionId());
                detail.setIndexChoice(selectedAnswer.getAnswerId());
                detail.setResultId(result.getResultId());
                detail.setCheck(selectedAnswer.getAnswerTrue());
                resultDetailService.save(detail);
                if (selectedAnswer.getAnswerTrue()) {
                    totalCorrectAnswers++;
                }
            }
            double totalPoints = (double) totalCorrectAnswers / exam.getListQuestion().size();
            // xong vòng for thì mới tính tổng diểm
            // lấy ra list câu trả lời vừa thi (findAllByResultId)
            // đếm số lượng resultdetail = true
            // số lương true / số lượng list câu hỏi vừa thi
            result.setTotalPoint(totalPoints);
            resultService.save(result);
            System.out.println("Bài thi đã kết thúc. Tổng điểm của bạn là: " + totalPoints);
            // xong thực hiện gọi resultservice gọi phương thức save và luw
        } else {
            System.out.println("Không tìm thấy đề thi đấy");
        }
    }
    private void findExamByName() {
        System.out.println("Nhập vào danh mục của đề thi hoặc tên đề thi muốn tìm: ");
        String inputSearch = InputMethods.getString();
        //tìm kiếm theo danh mục đề thi
        List<Exam> examsByCatalog = ExamServiceImpl.examList.stream().filter(exam -> exam.getCategories().stream()
                .anyMatch(catalog -> catalog.getCatalogName().contains(inputSearch))).toList();
        // Tìm kiếm đề thi theo tên
        List<Exam> examsByName = ExamServiceImpl.examList.stream()
                .filter(exam -> exam.getDescription().contains(inputSearch))
                .toList();
        //kết hợp
        List<Exam> combinedResults = Stream.concat(examsByName.stream(), examsByCatalog.stream())
                .distinct()
                .filter(Exam::isStatus)
                .toList();
        //hiển thị
        if (!combinedResults.isEmpty()) {
            combinedResults.forEach(exam -> exam.displayData(AuthenService.usersList));
        }
    }
    private void displayPublicExam() {
        examService.findAll().stream().filter(Exam::isStatus).forEach(exam -> exam.displayData(AuthenService.usersList));
    }
}
