package courses.SQLSupport;

import courses.model.Products;

import java.util.List;

public interface QuerySupportAdmin {

    boolean updateRow(Products products, int id);
    boolean removeRow(int id);
    boolean insertRow(Products products);
}
