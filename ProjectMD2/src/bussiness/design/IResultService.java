package bussiness.design;

import bussiness.entity.Result;

public interface IResultService extends IGenericService<Result, Integer>{
    int getNewId();
}
