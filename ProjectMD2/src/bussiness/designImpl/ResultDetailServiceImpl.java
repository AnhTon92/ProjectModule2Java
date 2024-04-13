package bussiness.designImpl;

import bussiness.config.Config;
import bussiness.design.IResultDetailService;
import bussiness.entity.Result;
import bussiness.entity.ResultDetail;

import java.util.ArrayList;
import java.util.List;

public class ResultDetailServiceImpl implements IResultDetailService {
    public static List<ResultDetail> resultDetailsList =  new ArrayList<>();
    static {
        resultDetailsList = Config.readData(Config.URL_RESULTDETAIL);
    }
    @Override
    public List<ResultDetail> findAll() {
        return resultDetailsList;
    }

    @Override
    public void save(ResultDetail resultDetail) {
        resultDetailsList.add(resultDetail);
        updateData();
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public ResultDetail findById(Integer id) {

        return null;
    }

    @Override
    public void updateData() {
        Config.writeFile(Config.URL_RESULTDETAIL, resultDetailsList);
    }

    @Override
    public int getNewId() {
        return 0;
    }

    @Override
    public List<ResultDetail> findAllByResultId(int resultId) {
        return resultDetailsList.stream().filter(resultDetail -> resultDetail.getResultId() == resultId).toList();
    }

}
