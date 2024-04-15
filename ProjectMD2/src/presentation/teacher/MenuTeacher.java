package presentation.teacher;

import bussiness.config.Config;
import bussiness.config.InputMethods;
import bussiness.config.Validate;
import bussiness.design.ICatalogService;
import bussiness.design.IExamService;
import bussiness.design.IResultService;
import bussiness.design.IUserService;
import bussiness.designImpl.*;
import bussiness.entity.*;
import presentation.LoginMain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import static bussiness.config.Color.*;
import static presentation.user.MenuUser.userLogin;

public class MenuTeacher {
    IResultService resultService = new ResultServiceImpl();
    IExamService examService = new ExamServiceImpl();
    ICatalogService catalogService = new CatalogServiceImpl();
    IUserService userService = new UserServiceImpl();

    public void displayMenuTeacher() {
        int choice = 0;
        while (choice != 10) {
            System.out.println(YELLOW_BOLD_BRIGHT + ".----------------------------------------------------------------------.");
            System.out.printf("|     MY QUIZZIZ            Xin chào |");
            System.out.println(YELLOW_BOLD_BRIGHT + "|----------------------------TEACHER MANAGEMENT---------------------------|");

            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[1] Hiển thị tất cả danh mục                       " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[2] Thêm mới danh mục                              " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[3] Sửa danh mục theo id                           " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[4] Xóa danh mục                                   " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[5] Hiển thị danh sách đề thi của bản thân         " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[6] Thêm mới đề thi                                " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[7] Chỉnh sửa đề thi                               " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[8] Xóa đề thi                                     " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[9] Thống kê kết quả thi của người dự thi          " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "           " + WHITE_BOLD_BRIGHT + "[10] Đăng xuất                                     " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("'----------------------------------------------------------------------'" + RESET);
            System.out.print(WHITE_BOLD_BRIGHT + "Nhập lựa chọn : ");
            choice = Validate.validateInt();
            switch (choice) {
                case 1:
                    displayAllCatalog();
                    break;
                case 2:
                    addNewCatalog();
                    break;
                case 3:
                    updateCatalogById();
                    break;
                case 4:
                    deleteCatalogById();
                    break;
                case 5:
                    displayAllExams();
                    break;
                case 6:
                    addNewExam();
                    break;
                case 7:
                    editExam();
                    break;
                case 8:
                    deleteExam();
                    break;
                case 9:
                    displayExamStatistics();
                    break;
                case 10:
                    Config.writeFileLogin(Config.URL_USER_LOGIN, null);
                    System.out.println(GREEN_BOLD_BRIGHT + "Đăng xuất thành công !" + RESET);
                    break;
                default:
                    System.out.println(RED_BOLD_BRIGHT + "Không hợp lệ, vui lòng nhập lại." + RESET);
            }
        }
    }

    private void displayExamStatistics() {
        System.out.println("Nhập ID đề thi bạn muốn xem thống kê: ");
        int examId = InputMethods.getInteger();
        Exam exam = examService.findById(examId);
        if (exam != null) {
            List<Result> results = resultService.findByExamId(examId);
            System.out.println("Thống kê kết quả cho đề thi: " + exam.getDescription());
            // Thống kê số lượng người thi, điểm trung bình, cao nhất, thấp nhất
            double averageScore = results.stream().mapToDouble(Result::getTotalPoint).average().orElse(0.0);
            double maxScore = results.stream().mapToDouble(Result::getTotalPoint).max().orElse(0.0);
            double minScore = results.stream().mapToDouble(Result::getTotalPoint).min().orElse(0.0);
            System.out.println("Số lượng người thi: " + results.size());
            System.out.println("Điểm trung bình: " + averageScore);
            System.out.println("Điểm cao nhất: " + maxScore);
            System.out.println("Điểm thấp nhất: " + minScore);
            // Hiển thị chi tiết từng người dự thi
            results.forEach(result -> {
                Users user = userService.findById(result.getUserId());
                System.out.println("Người dự thi: " + user.getUserName() + ", Điểm: " + result.getTotalPoint());
            });
        } else {
            System.out.println("Không tìm thấy đề thi với ID đã nhập.");
        }
    }

    private void deleteExam() {
        // láy List<Exam> từ ExamService.examList kiểm tra tên biến trong class ExamServiceImpl
        List<Exam> examList = ExamServiceImpl.examList;
        System.out.println("Danh sách đề thi hiện có là: ");
        examList.forEach(exam -> System.out.println("ID: " + exam.getExamId() + ", Thuộc về Teacher: " + exam.getUserId()));
        System.out.println("Nhập ID của đề thi bạn muốn xóa:");
        int examIdToDelete = InputMethods.getInteger();
        Exam examToDelete = examList.stream()
                .filter(exam -> exam.getExamId() == examIdToDelete)
                .findFirst()
                .orElse(null);

        if (examToDelete != null) {
            System.out.println("Bạn có chắc chắn muốn xóa đề thi này không? (1: Có, 2: Không)");
            int confirmation = InputMethods.getInteger();
            if (confirmation == 1) {
                examList.remove(examToDelete);
                System.out.println("Đề thi đã được xóa thành công.");
            } else {
                System.out.println("Thao tác xóa đã bị hủy bỏ.");
            }
        } else {
            System.out.println("Không tìm thấy đề thi với ID đã nhập.");
        }
    }

    private void editExam() {
        Users users = Config.readDataLogin(Config.URL_USER_LOGIN);
        System.out.println("Vui lòng lựa chọn ID đề thi bạn muốn sửa: ");
        int choiceExamID = InputMethods.getInteger();
        Exam exam = ExamServiceImpl.examList.stream().filter(item -> item.getExamId() == (choiceExamID))
                .findFirst().orElse(null);
        if (exam == null) {
            System.out.println("Không tìm thấy. Mời nhập lại: ");
        } else if (users.getUserId() == exam.getUserId()) {
            int choice;
            do {
                System.out.println("==============================EDIT EXAM================================");
                System.out.println("[1]. Chỉnh sửa mô tả đề thi: ");
                System.out.println("[2]. Chỉnh sửa thời gian làm bài:  ");
                System.out.println("[3]. Chỉnh sửa danh sách câu hỏi: ");
                System.out.println("[4]. Chỉnh sửa ngày đăng đề thi:  ");
                System.out.println("[5]. Chỉnh sửa trạng thái đề thi: ");
                System.out.println("[6]. Chỉnh sửa danh mục đề thi:  ");
                System.out.println("[7]. Thoát ");
                System.out.println("Xin mời lựa chọn: ");
                choice = InputMethods.getInteger();
                switch (choice) {
                    case 1:
                        exam.setDescription(InputMethods.getString());
                        break;
                    case 2:
                        exam.setDuration(InputMethods.getLong());
                        break;
                    case 3:
                        editQuestionList(exam.getListQuestion());
                        break;
                    case 4:
                        exam.setCreatedAt(new Date());
                        break;
                    case 5:
                        changeExamStatus(exam);
                        break;
                    case 6:
                        editExamCatalog(exam);
                        break;
                    case 7:

                        return;

                    default:
                        System.out.println("Lựa chọn không hợp lệ, mời lựa chọn lại!");
                        break;
                }
            } while (choice != 3);
            examService.save(exam);
        } else {
            System.out.println("Đề thi này không phải của bạn. Vui lòng nhập lại!");
        }
    }

    private void editExamCatalog(Exam exam) {
        int catalogChoice;
        do {
            System.out.println("==============================EDIT EXAM CATALOG================================");
            System.out.println("[1]. Thêm danh mục mới cho đề thi: ");
            System.out.println("[2]. Xóa danh mục khỏi đề thi: ");
            System.out.println("[3]. Thoát ");
            System.out.println("Xin mời lựa chọn: ");
            catalogChoice = InputMethods.getInteger();
            switch (catalogChoice) {
                case 1:
                    // có list từ exam.getCategories ra rồi
                    addNewCatalogForExam(exam.getCategories());
                    break;
                case 2:
                    deleteCatalogInExam(exam.getCategories());
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Vui lòng nhập lại từ 1 đến 4");
            }
        } while (catalogChoice != 3);
    }

    private void deleteCatalogInExam(List<Catalog> categories) {
        // show ra list danh sách categories có trong bài thi (là tham số)
        System.out.println("Danh sách danh mục hiện có: ");
        categories.forEach((catalog -> System.out.println("ID: "
                + catalog.getCatalogId() + ", Tên: "
                + catalog.getCatalogName())));
        String catalogId;
        // cho nó lựa chọn id để xóa (cũng cho vào while(true)) để cho lựa chọn đúng thì thôi
        while (true) {
            System.out.println("Nhập Id danh mục bạn muốn xóa: ");
            catalogId = InputMethods.getString();
            String finalCatalogId = catalogId;
            Catalog catalogToDelete = categories.stream().filter(catalog -> Objects.equals(catalog.getCatalogId(), finalCatalogId))
                    .findFirst().orElse(null);
            if (catalogToDelete != null) {
                categories.remove(catalogToDelete);
                System.out.println("Danh mục đã được xóa thành công!");
                break;
            } else {
                System.out.println("Không tìm thấy danh mục với ID đã nhập, vui lòng nhập lại.");
            }
        }
        // xóa thì tự động cập nhật trong list rồi
    }

    private void addNewCatalogForExam(List<Catalog> categories) {
        // show ra tất cả catalog đang hiện có trong hệ thống
        System.out.println("Danh sách danh mục hiện có trong hệ thống:");
        // lấy ở CatalogService.catalogList để show ra dùng forEach đấy
        CatalogServiceImpl.catalogList.forEach(catalog -> System.out.println("ID: "
                + catalog.getCatalogId() + ", Tên: " + catalog.getCatalogName()));
        // cho nó lựa chọn 1 cái xong thêm vào categories ở tham số (trên phương thức)
        String catalogId;
        // cho nó lựa chọn đúng id thì thôi (cho vào vòng lặp while(true))
        while (true) {
            System.out.println("Nhập Id danh mục mà bạn muốn thêm: ");
            catalogId = InputMethods.getString();
            String finalCatalogId = catalogId;
            // xong add vào List<Catalog> categories ở trên
            Catalog catalogToAdd = CatalogServiceImpl.catalogList.stream().filter(catalog -> Objects.equals(catalog.getCatalogId(), finalCatalogId))
                    .findFirst().orElse(null);
            if (catalogToAdd != null && !categories.contains(catalogToAdd)) {
                categories.add(catalogToAdd);
                System.out.println("Danh mục đã được thêm thành công.");
                break;
            } else if (categories.contains(catalogToAdd)) {
                System.out.println("Danh mục này đã có trong đề thi, vui lòng chọn danh mục khác.");
            } else {
                System.out.println("Không tìm thấy danh mục với ID đã nhập, vui lòng nhập lại.");
            }
        }
    }

    private void changeExamStatus(Exam exam) {
        if (exam != null) {
            exam.setStatus(!exam.isStatus());
            System.out.println("Trạng thái của đề thi đã được thay đổi");
        } else {
            System.out.println("Đề thi không tồn tại");
        }
    }

    private void editQuestionList(List<Question> questionList) {
        int choice;
        do {
            System.out.println("========================EDIT QUESTION FOR EXAM MENU================================");
            System.out.println("[1]. Thêm câu hỏi cho bài thi: ");
            System.out.println("[2]. Chỉnh sửa câu hỏi cho bài thi: ");
            System.out.println("[3]. Xóa câu hỏi cho bài thi: ");
            System.out.println("[4]. Thoát!");
            System.out.println("Xin mời lựa chọn: ");
            choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
                    Question question = new Question();
                    int questionId = 0;
                    boolean idExist;
                    do {
                        System.out.println("Nhập id cho câu hỏi: ");
                        question.setQuestionId(InputMethods.getInteger());
                        idExist = questionList.stream().anyMatch(q -> q.getQuestionId() == questionId);
                        if (idExist) {
                            System.out.println("Id đã tồn tại. Vui lòng nhập id khác.");
                        }
                    } while (idExist);
                    System.out.println("Nhập nội dung câu hỏi: ");
                    question.setQuestionContent(InputMethods.getString());
                    question.setAnswerOption(getAnswerOptionForQuestion());
                    questionList.add(question);
                    break;
                case 2:
                    System.out.println("Vui lòng nhập ID câu hỏi bạn muốn sửa");
                    int choiceQuestionId = InputMethods.getInteger();
                    Question questionTopUpdate = findQuestionById(questionList, choiceQuestionId);
                    if (questionTopUpdate != null) {
                        System.out.println("Nhập nội dung câu hỏi mới: ");
                        questionTopUpdate.setQuestionContent(InputMethods.getString());
                        System.out.println("Bạn có muốn cập nhật lại danh sách trả lời không? (1: Có , 2: Không ");
                        int updateAnswer = InputMethods.getInteger();
                        if (updateAnswer == 1) {
                            questionTopUpdate.setAnswerOption(editAnswerForQuestion(questionTopUpdate.getAnswerOption()));
                        }
                    } else {
                        System.out.println("Không tìm thấy câu hỏi với ID đã nhập.");
                    }
                    break;
                case 3:
                    // show ra List Question ở trong exam
                    System.out.println("Danh sách câu hỏi hiện có:");
                    questionList.forEach(item -> System.out.println("ID: " + item.getQuestionId() + ", Nội dung: " + item.getQuestionContent()));
                    // cho lưa chọn id question muốn xóa
                    System.out.println("Nhập ID câu hỏi bạn muốn xóa:");
                    int questionIdToDelete = InputMethods.getInteger();
                    Question questionToDelete = findQuestionById(questionList, questionIdToDelete);
                    if (questionToDelete != null) {
                        questionList.remove(questionToDelete);
                        System.out.println("Câu hỏi đã được xóa thành công.");
                    } else {
                        System.out.println("Không tìm thấy câu hỏi với ID đã nhập.");
                    }
                    // khi xóa question tự động cái List<Answer> trong Question đó được xóa luôn
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Vui lòng nhập lại từ 1 đến 4");
            }
        } while (choice != 4);
    }

    private List<Answer> editAnswerForQuestion(List<Answer> answerList) {
        int choice;
        do {
            System.out.println("========================EDIT ANSWER FOR QUESTION MENU================================");
            System.out.println("[1]. Thêm câu trả lời cho câu hỏi: ");
            System.out.println("[2]. Chỉnh sửa câu trả lời cho câu hỏi: ");
            System.out.println("[3]. Xóa câu trả lời cho câu hỏi: ");
            System.out.println("[4]. Thoát!");
            System.out.println("Xin mời lựa chọn: ");
            choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
                    Answer answer = new Answer();
                    System.out.println("Nhập vào Id câu trả lời muốn thêm: ");
                    int newId = InputMethods.getInteger();
                    // Kiểm tra xem ID đã tồn tại trong danh sách chưa
                    if (findAnswerById(answerList, newId) == null) {
                        answer.setAnswerId(newId);
                        System.out.println("Nhập vào nội dung câu trả lời thêm vào: ");
                        answer.setAnswerContent(InputMethods.getString());
                        System.out.println("Nhập vào vị trí của câu trả lời đúng: ");
                        answer.setAnswerTrue(InputMethods.getBoolean());
                        answerList.add(answer);
                        System.out.println("Câu trả lời đã được thêm thành công.");
                    } else {
                        System.out.println("ID câu trả lời đã tồn tại, vui lòng nhập ID khác.");
                        break;
                    }
                case 2:
                    System.out.println("Nhập vào Id câu trả lời bạn muốn sửa: ");
                    int editId = InputMethods.getInteger();
                    Answer answerToEdit = findAnswerById(answerList, editId);
                    if (answerToEdit != null) {
                        System.out.println("Nhập vào nội dung câu trả lời mới:");
                        answerToEdit.setAnswerContent(InputMethods.getString());
                        System.out.println("Câu trả lời này có đúng không? (1: Có, 0: Không)");
                        answerToEdit.setAnswerTrue(InputMethods.getBoolean());
                    } else {
                        System.out.println("Không tìm thấy câu trả lời với ID đã nhập.");
                    }
                    break;
                case 3:
                    System.out.println("Nhập vào Id câu trả lời bạn muốn xóa: ");
                    int deleteId = InputMethods.getInteger();
                    Answer answerToDelete = findAnswerById(answerList, deleteId);
                    if (answerToDelete != null) {
                        answerList.remove(answerToDelete);
                        System.out.println("Câu trả lời đã được xóa thành công");
                    } else {
                        System.out.println("Không tìm thấy câu trả lời với Id đã nhập");
                    }
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ, mời nhập lại!");
            }
        } while (choice != 4);
        return answerList;
    }

    private Answer findAnswerById(List<Answer> answerList, int answerId) {
        for (Answer answer : answerList) {
            if (answer.getAnswerId() == answerId) {
                return answer;
            }
        }
        return null;
    }

    private Question findQuestionById(List<Question> questionList, int questionId) {
        for (Question question : questionList) {
            if (question.getQuestionId() == questionId) {
                return question;
            }
        }
        return null;
    }

    private void deleteCatalogById() {
        System.out.println("Vui lòng lựa chọn ID danh mục bạn muốn sửa: ");
        String choiceID = InputMethods.getString();
        Catalog catalog = CatalogServiceImpl.catalogList.stream().filter(item -> item.getCatalogId().equals(choiceID))
                .findFirst().orElse(null);
        if (catalog == null) {
            System.out.println("Không tìm thấy. Mời nhập lại: ");
        } else {
            catalogService.delete(choiceID);
        }
    }


    private void updateCatalogById() {
        System.out.println("Vui lòng lựa chọn ID danh mục bạn muốn sửa: ");
        String choiceID = InputMethods.getString();
        Catalog catalog = CatalogServiceImpl.catalogList.stream().filter(item -> item.getCatalogId().equals(choiceID))
                .findFirst().orElse(null);
        if (catalog == null) {
            System.out.println("Không tìm thấy. Mời nhập lại: ");
        } else {
            int choice;
            do {
                System.out.println("==============================UPDATE CATALOG================================");
                System.out.println("[1]. Cập nhật tên danh mục: ");
                System.out.println("[2]. Cập nhật mô tả danh mục: ");
                System.out.println("[3]. Thoát ");
                System.out.println("Xin mời lựa chọn: ");
                choice = InputMethods.getInteger();
                switch (choice) {
                    case 1:
                        catalog.setCatalogName(InputMethods.getString());
                        break;
                    case 2:
                        catalog.setDescription(InputMethods.getString());
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ, mời lựa chọn lại!");
                }
            } while (choice != 3);
            catalogService.save(catalog);
        }
    }

    private void addNewCatalog() {
        System.out.println("Nhập số lượng danh mục bạn muốn thêm: ");// tạo biến countCatalog để người dùng nhập
        //số lượng catalog muốn thêm mới vào
        int countCatalog = InputMethods.getInteger();
        for (int i = 0; i < countCatalog; i++) {//lặp qua từng danh mục cần thêm dựa trên số lượng danh mục đã nhập
            Catalog catalog = new Catalog();// mỗi lần lặp khởi tạo 1 đối tượng catalog mới được tạo ra
            catalog.inputData(); // đông thời gọi phương thức inputData(đã validate và check trùng) để nhập dữ liệu cho danh mục
            catalogService.save(catalog);// lưu vào file
        }
    }

    private void displayAllExams() { // nên sửa thêm filter
        // lấy user đang đăng nhập (teacher) ra để so sánh
        Users users = Config.readDataLogin(Config.URL_USER_LOGIN);
        // trong exam có trường userId để check
        examService.findAll().stream().filter(exam -> exam.getUserId() == users.getUserId()).forEach(exam -> exam.displayData(AuthenService.usersList));
    }

    private void displayAllCatalog() {
        catalogService.findAll().forEach(Catalog::displayData);
    }

    public void addNewExam() {
        Exam exam = new Exam();// khởi tạo đối tượng exam mới của lớp Exam
        exam.setExamId(examService.getNewId());//thiết lập id mới cho bài thi lấy từ examService.exam được thiết lập cho đối tượng exam
        Users users = Config.readDataLogin(Config.URL_USER_LOGIN);// lấy thông tin người dùng đang đăng nhập hiện tại từ file qua phương thức
        // đọc file readDataLogin ở lớp Config
        exam.setUserId(users.getUserId());//id người dùng hiện tại (giáo viên) được thiết lập cho đối tượng exam
        System.out.println("Nhập vào mô tả bài thi: ");
        exam.setDescription(InputMethods.getString());
        System.out.println("Nhập vào thời gian làm bài thi: ");
        exam.setDuration(InputMethods.getLong());
        exam.setCreatedAt(new Date());// thiết lập ngày tạo là ngày hiện tại
        exam.setStatus(true);// trạng thái được thiêt lập mặc định là true
        exam.setCategories(getCatalogForExam());// lấy danh mục bài thi thông qua phương thức getCatalogForExam()
        exam.setListQuestion(getListQuestionForExam());// lấy danh sách câu hỏi cho bài thi thông qua phương thức getListQuestionForExam()
        examService.save(exam);// lưu bài thi vào hệ thống thông qua phương thức save của examService, truyền vào đối tượng exam vừa tạo
    }

    public List<Question> getListQuestionForExam() {
        List<Question> questionList = new ArrayList<>();
        // nhập vào số câu hỏi muốn thêm
        System.out.println("Nhập vào số lượng câu hỏi: ");
        int n = InputMethods.getInteger();
        for (int i = 0; i < n; i++) {
            // b1: Tạo đối tượng Question
            Question question = new Question();
            // b2: nhập các thông tin cần thiết
            // 2.1 thêm thông tin cơ bản
            System.out.println("Nhập id cho câu hỏi: ");
            question.setQuestionId(InputMethods.getInteger());
            System.out.println("Nhập nội dung câu hỏi: ");
            question.setQuestionContent(InputMethods.getString());
            // 2.2 tạo hàm trả về List<Answer>
            question.setAnswerOption(getAnswerOptionForQuestion());
            // b3: add list
            questionList.add(question);
        }
        return questionList;
    }

    private List<Answer> getAnswerOptionForQuestion() {
        // bốn phương án trả lời fix cứng
        // vòng lặp do while cho người dùng nhập lại đến khi thỏa mãn điều kiện nhập ít nhất 1 câu trả lời đúng cho câu hỏi mới dừng
         do {
            List<Answer> answerList = new ArrayList<>(); // khởi tạo 1 danh sách answerList mới để lưu trữ các đối tượng Answer
            for (int i = 0; i < 4; i++) { // lặp 4 lần tương ứng nhập 4 phương án trả lời

                Answer answer = new Answer(); // khởi tạo đối tượng answer và nhập thông tin Answer
                //tạo biến để check trùng
                int answerId;
                String answerContent;
                boolean idExists, contentExists;
                do {
                    System.out.println("Phương án thứ " + (i + 1));
                    System.out.println("Nhập ID câu trả lời:"); //nhập ID cho mỗi câu trả lời
                    answerId = InputMethods.getInteger(); // gán biến answerId và answerContent là id và nội dung câu trả lời giáo viên nhập vào
                    System.out.println("Nhập nội dung câu trả lời:");
                    answerContent = InputMethods.getString();
                    int finalAnswerId = answerId;
                    idExists = answerList.stream().anyMatch(a -> a.getAnswerId() == finalAnswerId);// chạy stream của answerList
                    // dùng hàm anyMatch có sẵn trả về đúng nếu có bất kì phần tử a.getAnswerId trong answerList == id câu trả lời giáo viên nhập

                    String finalAnswerContent = answerContent;
                    contentExists = answerList.stream().anyMatch(a -> a.getAnswerContent().equalsIgnoreCase(finalAnswerContent));//chạy stream của answerList
                    // dùng hàm anyMatch có sẵn trả về đúng nếu có bất kì phần tử a.getAnswerContent trong answerList tương đương (không kể viết hoa hay thường)
                    // nội dung câu trả lời giáo viên nhập
                    if (idExists || contentExists) { // nếu 1 trong 2 biến check trùng đúng thì hiện ra thông báo để nhập lại
                        System.out.println("ID hoặc nội dung câu trả lời đã tồn tại, vui lòng nhập lại.");
                    }
                } while (idExists || contentExists);// dừng vòng lặp
                answer.setAnswerId(answerId); // set AnswerId truyền vào biến answerId giáo viên nhập vào
                answer.setAnswerContent(answerContent);// set answerContent truyền vào biến answerContent gióa viên nhập vào
                System.out.println("Đây có phải là câu trả lời đúng không? (1: Có, 0: Không):"); // cho giáo viên chọn xem phương án này có đúng ko?
                answer.setAnswerTrue(InputMethods.getInteger() == 1);// để mặc định 1 là true còn lại là false
                answerList.add(answer); // sau khi nhập và set xong thì thêm đối tượng answer vào answerList
            }
            // filter lọc ra cái list có câu trả lời đúng không lọc ra những cái AnswerTrue xong dùng count để đếm số phần tử
            if (answerList.stream().filter(Answer::getAnswerTrue).count() == 1) {// điều kiện nếu chỉ có đúng 1 câu trả lời đúng
                return answerList; // trả về answerList
            } else {// nếu nhiều hơn hoặc ít hơn 1 câu trả lời đúng thì in ra thông báo
                System.out.println("Danh sách câu hỏi chỉ có một câu đúng");
            }
            // nếu không thì quay lại đoạn đầu do sẽ lại khởi tạo lại cái answerList và thực hiện lại ban đầu
        } while (true);
    }

    public List<Catalog> getCatalogForExam() {
        List<Catalog> catalogList = new ArrayList<>();
        int choice;
        if (CatalogServiceImpl.catalogList.isEmpty()) {
            System.out.println("Chưa có danh mục nào cả hãy tạo danh mục mới");
        } else {
            do {
                System.out.println("==============================EXAM'S CATALOG'S CHOICE================================");
                System.out.println("[1]. Chọn loại danh mục cho bài thi: ");
                System.out.println("[2]. Thoát: ");
                System.out.println("Xin mời lựa chọn: ");
                choice = InputMethods.getInteger();
                switch (choice) {
                    case 1:
                        while (true) {
                            System.out.println("Danh sách danh mục: ");
                            for (int i = 0; i < CatalogServiceImpl.catalogList.size(); i++) {
                                Catalog catalog = CatalogServiceImpl.catalogList.get(i);
                                System.out.println((i + 1) + ". ID: " + catalog.getCatalogId() + ", Tên: " + catalog.getCatalogName());
                            }
                            System.out.println("Vui lòng lựa chọn ID danh mục bạn muốn thêm");
                            String choiceID = InputMethods.getString();

                            Catalog catalog = CatalogServiceImpl.catalogList.stream().filter(item -> item.getCatalogId().equals(choiceID))
                                    .findFirst().orElse(null);
                            if (catalog == null) {
                                System.out.println("Không tìm thấy. Mời nhập lại: ");
                            } else {
                                // check kiểm tra xem tồn tại catalog xem có trong list chưa
                                if (catalogList.contains(catalog)) {
                                    System.out.println("Đã tồn tại danh mục này trong list Catalog rồi");
                                } else {
                                    catalogList.add(catalog);
                                }
                                break;
                            }

                        }
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ");
                }

            } while (choice != 2);

        }
        return catalogList;

    }
}
