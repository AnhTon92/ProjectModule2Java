package bussiness.designImpl;

import bussiness.config.Config;
import bussiness.design.ICatalogService;
import bussiness.design.IExamService;
import bussiness.entity.Catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CatalogServiceImpl implements ICatalogService {
    public static IExamService examService = new ExamServiceImpl();

    public static List<Catalog> catalogList;
    static {
        catalogList = Config.readData(Config.URL_CATEGORY);
    }

    @Override
    public boolean existCategoryName(String categoryName) {
        for (Catalog catalog : catalogList) {
            if (catalog.getCatalogName().equalsIgnoreCase(categoryName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Catalog> findByName(String catalogName) {
        List<Catalog> matchedList = new ArrayList<>();
        for (Catalog catalog : catalogList) {
            if (catalog.getCatalogName().contains(catalogName)) {
                matchedList.add(catalog);
            }
        }
        return matchedList;
    }

    @Override
    public List<Catalog> findAll() {
        return catalogList;
    }

    @Override
    public void save(Catalog catalog) {
        if (findById(catalog.getCatalogId()) == null) {
            catalogList.add(catalog);
            updateData();
        } else {
            catalogList.set(catalogList.indexOf(catalog), catalog);
            updateData();
        }
    }

    @Override
    public void delete(String id) {
        boolean checkExistExam = ExamServiceImpl.examList.stream()
                .anyMatch(exam -> exam.getCategories().stream()
                        .anyMatch(catalog -> catalog.getCatalogId().equals(id))
                );
        if (checkExistExam) {
            System.err.println("Có bài thi trong danh mục, không xóa được");
        } else {
            Catalog catalogToDelete = findById(id);
            if (catalogToDelete != null) {
                catalogList.remove(catalogToDelete);
                updateData();
            }
        }
    }

    @Override
    public Catalog findById(String id) {
        for (Catalog catalog : catalogList) {
         if (Objects.equals(catalog.getCatalogId(), id)) {
             return catalog;
         }
        }
        return null;
    }
    

    @Override
    public void updateData() {
        Config.writeFile(Config.URL_CATEGORY, catalogList);
    }

}
