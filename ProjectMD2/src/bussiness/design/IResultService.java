package bussiness.design;

import bussiness.entity.Result;

import java.util.List;

public interface IResultService extends IGenericService<Result, Integer>{
    int getNewId();

    List<Result> findByExamId(int examId);
}
