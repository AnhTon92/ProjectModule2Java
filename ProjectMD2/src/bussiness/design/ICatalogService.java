package bussiness.design;

import bussiness.entity.Catalog;

import java.util.List;

public interface ICatalogService extends IGenericService<Catalog, String>{
boolean existCategoryName (String categoryName);
List<Catalog> findByName(String catalogName);

}
