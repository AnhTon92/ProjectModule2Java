package bussiness.designImpl;

import bussiness.config.Config;
import bussiness.design.IResultService;
import bussiness.entity.Exam;
import bussiness.entity.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultServiceImpl implements IResultService {
    public static List<Result> resultList =  new ArrayList<>();
    static {
        resultList = Config.readData(Config.URL_RESULTS);
    }
    @Override
    public List<Result> findAll() {

        return resultList;
    }

    @Override
    public void save(Result result) {
        if (findById(result.getResultId()) == null){
            resultList.add(result);
            updateData();
        }else {
            resultList.set(resultList.indexOf(result), result);
            updateData();
        }

    }

    @Override
    public void delete(Integer id) {
        Result resultToDelete = findById(id);
        if (resultToDelete != null) {
            resultList.remove(resultToDelete);
            updateData();
        }

    }

    @Override
    public Result findById(Integer id) {
        for (Result result : resultList) {
            if (result.getResultId() == id){
                return result;
            }
        }
        return null;
    }

    @Override
    public void updateData() {
        Config.writeFile(Config.URL_RESULTS, resultList);
    }

    @Override
    public int getNewId() {
        int idMax = 0;
        for (Result result : resultList) {
            if (result.getResultId() > idMax) {
                idMax = result.getResultId();
            }
        }
        return (idMax + 1);
    }

    @Override
    public List<Result> findByExamId(int examId) {
        List<Result> resultsByExamId = new ArrayList<>();
        for (Result result : resultList) {
            if (result.getExamId() == examId) {
                resultsByExamId.add(result);
            }
        }
        return resultsByExamId;

    }
}
