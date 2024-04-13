////package presentation.user;
////
////public class demo {
////    System.out.println("Input Catalog Name Or Exam Title To Look For");
////    String inputSearch = QuizConFig.inputFromUser(ErrorAndRegex.REGEX_STRING, ErrorAndRegex.ERROR_VALUE);
////        if (ExamService.examList.stream().anyMatch(exam -> exam.getDescription().contains(inputSearch))) {
////        List<Exam> exams = ExamService.examList.stream().filter(exam -> exam.getDescription().contains(inputSearch)).toList();
////        exams.stream().filter(Exam::isStatus).forEach(Exam::displayData);
////
////
////    } else if (CatalogService.catalogList.stream().anyMatch(catalog -> catalog.getCatalogName().contains(inputSearch))) {
////        int examId = CatalogService.catalogList.stream()
////                .filter(catalog -> catalog.getCatalogName().equals(inputSearch)).findFirst().orElse(null).getExamId();
////        List<Exam> exams = ExamService.examList.stream().filter(exam -> exam.getExamId() == examId).toList();
////        exams.stream().filter(Exam::isStatus).forEach(Exam::displayData);
////
////        for (int i = 0; i < exams.size(); i++) {
////            if(exams.get(i).isStatus()){
////                exams.get(i).setSearchNumber(exams.get(i).getSearchNumber() + 1);
////                ExamService.examList.set(ExamService.examList.indexOf(exams.get(i)), exams.get(i));
////                IOFile.writeData(IOFile.EXAM_PATH, ExamService.examList);
////            }
////        }
////
////
////    } else {
////        System.out.println(ErrorAndRegex.ERROR_NOT_FOUND);
////    }
////}
//ExamService.examList.stream().filter(exam -> exam.isStatus()).forEach(Exam::displayData);
//        System.out.println("Input ExamId To Start");
//
//int examId = QuizConFig.getInt(ErrorAndRegex.REGEX_NUMBER, ErrorAndRegex.ERROR_VALUE);
//Exam examStart = findExamById(examId);
//        if (examStart != null) {
//List<Question> questions = QuestionService.questionList.stream().filter(question -> question.getExamId() == examStart.getExamId()).toList();
//            System.out.printf("--------------Exam %-5s ----------------- \n" +
//                                      "--------------Student: %-5s  ----------------- \n" +
//                                      "--------------DURATION: %-3s Minutes ----------------- \n", examStart.getDescription()
//                    , UserService.userList.stream().filter(user -> user.getUserId() == examStart.getUserId()).findFirst().orElse(null).getUserName(), examStart.getDuration());
//Result result = new Result();
//ResultDetail resultDetail = null;
//byte totalPoint = 0;
//            for (int i = 0; i < questions.size(); i++) {
//resultDetail = new ResultDetail();
//                questions.get(i).showExamQuestion(i);
//                System.out.println("your choice  ");
//int choice = QuizConFig.getInt(ErrorAndRegex.REGEX_NUMBER, ErrorAndRegex.ERROR_VALUE);
//                if (choice == questions.get(i).getAnswerTrue()) {
//totalPoint += 1;
//        resultDetail.inputData(result.getResultId(), questions.get(i).getQuestionId(), choice, true);
//        ResultDetailService.resultDetailList.add(resultDetail);
//                } else {
//                        resultDetail.inputData(result.getResultId(), questions.get(i).getQuestionId(), choice, false);
//        ResultDetailService.resultDetailList.add(resultDetail);
//                }
//
//                        }
//                        System.out.println("Input Catalog Name Or Exam Title To Look For");
//String inputSearch = QuizConFig.inputFromUser(ErrorAndRegex.REGEX_STRING, ErrorAndRegex.ERROR_VALUE);
//        if (ExamService.examList.stream().anyMatch(exam -> exam.getDescription().contains(inputSearch))) {
//List<Exam> exams = ExamService.examList.stream().filter(exam -> exam.getDescription().contains(inputSearch)).toList();
//            exams.stream().filter(Exam::isStatus).forEach(Exam::displayData);
//
//            for (int i = 0; i < exams.size(); i++) {
//        if(exams.get(i).isStatus()){
//        exams.get(i).setSearchNumber(exams.get(i).getSearchNumber() + 1);
//        ExamService.examList.set(ExamService.examList.indexOf(exams.get(i)), exams.get(i));
//        IOFile.writeData(IOFile.EXAM_PATH, ExamService.examList);
//                }
//                        }
//
//                        } else if (CatalogService.catalogList.stream().anyMatch(catalog -> catalog.getCatalogName().contains(inputSearch))) {
//int examId = CatalogService.catalogList.stream()
//        .filter(catalog -> catalog.getCatalogName().equals(inputSearch)).findFirst().orElse(null).getExamId();
//List<Exam> exams = ExamService.examList.stream().filter(exam -> exam.getExamId() == examId).toList();
//            exams.stream().filter(Exam::isStatus).forEach(Exam::displayData);
//
//            for (int i = 0; i < exams.size(); i++) {
//        if(exams.get(i).isStatus()){
//        exams.get(i).setSearchNumber(exams.get(i).getSearchNumber() + 1);
//        ExamService.examList.set(ExamService.examList.indexOf(exams.get(i)), exams.get(i));
//        IOFile.writeData(IOFile.EXAM_PATH, ExamService.examList);
//                }
//                        }
//
//
//                        } else {
//                        System.out.println(ErrorAndRegex.ERROR_NOT_FOUND);
//        }
