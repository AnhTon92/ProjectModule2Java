package presentation.teacher;

import bussiness.config.Config;
import bussiness.config.InputMethods;
import bussiness.config.Validate;
import bussiness.design.ICatalogService;
import bussiness.design.IExamService;
import bussiness.designImpl.AuthenService;
import bussiness.designImpl.CatalogServiceImpl;
import bussiness.designImpl.ExamServiceImpl;
import bussiness.entity.*;
import presentation.LoginMain;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import static bussiness.config.Color.*;

public class MenuTeacher {
    IExamService examService = new ExamServiceImpl();
    ICatalogService catalogService = new CatalogServiceImpl();

    public void displayMenuTeacher() {
        int choice = 0;
        while (choice != 10) {
            System.out.println(YELLOW_BOLD_BRIGHT + ".----------------------------------------------------------------------.");
            System.out.println("|  QUIZIZZ   Teacher      Xin chào, " + BLUE_BOLD_BRIGHT + "%-13s " + YELLOW_BOLD_BRIGHT + "|\n" + RESET);
            System.out.println(YELLOW_BOLD_BRIGHT + "|----------------------------TEACHER MANAGEMENT---------------------------|");

            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[1] Hiển thị tất cả danh mục                       " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[2] Thêm mới danh mục                       " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[3] Sửa danh mục theo id                       " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[4] Xóa danh mục                        " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[5] Hiển thị danh sách đề thi của bản thân                       " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[6] Thêm mới đề thi                       " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[7] Chỉnh sửa đề thi                                 " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[8] Xóa đề thi                                 " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[9] Thống kê kết quả thi của người dự thi                                 " + YELLOW_BOLD_BRIGHT + "|");
            System.out.println("|" + RESET + "" + WHITE_BOLD_BRIGHT + "[10] Đăng xuất                                 " + YELLOW_BOLD_BRIGHT + "|");
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
                System.out.println("[3]. Chỉnh sửa danh sách câu hỏi(1.Thêm / 2.Xóa / 3.Sửa) : ");
                System.out.println("[4]. Chỉnh sửa ngày đăng đề thi:  ");
                System.out.println("[5]. Chỉnh sửa trạng thái đề thi: ");
                System.out.println("[6]. Chỉnh sửa danh mục đề thi(1.Thêm / 2.Xóa /3.Sửa):  ");
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
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ, mời lựa chọn lại!");
                }
            } while (choice != 3);
            examService.save(exam);
        } else {
            System.out.println("Bài thi này không phải của bạn. Vui lòng nhập lại!");
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
                    if (catalogToDelete !=null) {
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
                // thêm ở chỗ nào đáy ??c
                // mới check thôi mà
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
                    System.out.println("Nhập id cho câu hỏi: ");
                    question.setQuestionId(InputMethods.getInteger());
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
        System.out.println("Nhập số lượng danh mục bạn muốn thêm: ");
        int countCatalog = InputMethods.getInteger();
        for (int i = 0; i < countCatalog; i++) {
            Catalog catalog = new Catalog();
            catalog.inputData();
            catalogService.save(catalog);
        }
    }

    private void displayAllExams() { // nên sửa thêm filter
        // lấy user đang đăng nhập (teacher) ra để so sánh
        Users users = Config.readDataLogin(Config.URL_USER_LOGIN);
        // trong exam có trường userId để check
        examService.findAll().stream().filter(exam->exam.getUserId()==users.getUserId()).forEach(exam -> exam.displayData(AuthenService.usersList));
    }

    private void displayAllCatalog() {
        catalogService.findAll().forEach(Catalog::displayData);
    }

    public void addNewExam() {

        Exam exam = new Exam();
        exam.setExamId(examService.getNewId());
        Users users = Config.readDataLogin(Config.URL_USER_LOGIN);
        exam.setUserId(users.getUserId());
        System.out.println("Nhập vào mô tả bài thi: ");
        exam.setDescription(InputMethods.getString());
        System.out.println("Nhập vào thời gian làm bài thi: ");
        exam.setDuration(InputMethods.getLong());
        exam.setCreatedAt(new Date());
        exam.setStatus(true);
        exam.setCategories(getCatalogForExam());
        exam.setListQuestion(getListQuestionForExam());
        // chưa thực hiện lưu vào list
        // gọi phương thức save trong examService đó
        examService.save(exam);
    }

    public List<Question> getListQuestionForExam() {
        List<Question> questionList = new ArrayList<>();
        int choice;
        do {
            System.out.println("========================ADD QUESTION TO EXAM MENU================================");
            System.out.println("[1]. Thêm câu hỏi cho bài thi: ");
            System.out.println("[2]. Thoát!");
            System.out.println("Xin mời lựa chọn: ");
            choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
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
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Lựa chọn 1 hoặc 2, lựa lại");
            }
        } while (choice != 2);
        return questionList;
    }

    private List<Answer> getAnswerOptionForQuestion() {
        List<Answer> answerList = new ArrayList<>();
        int choice;
        do {
            System.out.println("==============ADD ANSWER TO QUESTION MENU==============");
            System.out.println("[1]. Thêm câu trả lời:");
            System.out.println("[2]. Hoàn tất!");
            System.out.println("Xin mời lựa chọn:");
            choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
                    Answer answer = new Answer();
                    System.out.println("Nhập ID câu trả lời:");
                    answer.setAnswerId(InputMethods.getInteger());
                    System.out.println("Nhập nội dung câu trả lời:");
                    answer.setAnswerContent(InputMethods.getString());
                    System.out.println("Đây có phải là câu trả lời đúng không? (1: Có, 0: Không):");
                    answer.setAnswerTrue(InputMethods.getBoolean());
                    answerList.add(answer);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Lựa chọn 1 hoặc 2, lựa lại");
            }
        } while (choice != 2);
        return answerList;
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
                                catalogList.add(catalog);
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
