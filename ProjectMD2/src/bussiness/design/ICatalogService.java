package bussiness.design;

import bussiness.entity.Catalog;

import java.util.List;

public interface ICatalogService extends IGenericService<Catalog, String>{

List<Catalog> findByName(String catalogName);

}
