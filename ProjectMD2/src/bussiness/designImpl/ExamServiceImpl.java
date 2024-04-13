package bussiness.designImpl;

import bussiness.config.Config;
import bussiness.design.IExamService;
import bussiness.entity.Exam;
import bussiness.entity.Users;

import java.util.List;

public class ExamServiceImpl implements IExamService {
   public static List<Exam> examList;
    static {
        examList = Config.readData(Config.URL_EXAMS);
    }

    @Override
    public List<Exam> findAll() {
        return examList;
    }

    @Override
    public void save(Exam exam) {
        if (findById(exam.getExamId()) == null){
            examList.add(exam);
            updateData();
        }else {
            examList.set(examList.indexOf(exam), exam);
            updateData();
        }

    }

    @Override
    public void delete(Integer id) {
        Exam examToDelete = findById(id);
        if (examToDelete != null) {
            examList.remove(examToDelete);
            updateData();
        }
    }

    @Override
    public Exam findById(Integer id) {
        for (Exam exam : examList) {
            if (exam.getExamId() == id){
                return exam;
            }
        }
        return null;
    }

    @Override
    public int getNewId() {
        int idMax = 0;
        for (Exam exam : examList) {
            if (exam.getExamId() > idMax) {
                idMax = exam.getExamId();
            }
        }
        return (idMax + 1);
    }

    @Override
    public void updateData() {
        Config.writeFile(Config.URL_EXAMS, examList);
    }
}
