package bussiness.design;

import bussiness.entity.ResultDetail;

import java.util.List;

public interface IResultDetailService extends IGenericService<ResultDetail, Integer>{
    int getNewId();
    List<ResultDetail> findAllByResultId(int resultId);
}
