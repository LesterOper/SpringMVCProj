package courses.SQLSupport;

import courses.model.Logs;
import courses.model.Products;

import java.util.List;

public interface QuerySupportUser {

    Products getProductByID(int id);
    List<Logs> getLogsByUserID(String user);
}
