package bussiness.design;

import bussiness.entity.Exam;

public interface IExamService extends IGenericService<Exam, Integer> {
    int getNewId();
}