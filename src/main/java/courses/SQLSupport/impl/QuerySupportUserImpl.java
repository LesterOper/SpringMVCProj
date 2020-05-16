package courses.SQLSupport.impl;

import courses.SQLSupport.QuerySupportUser;
import courses.SQLSupport.SQLSupport;
import courses.model.Logs;
import courses.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(value = "queryUser")
public class QuerySupportUserImpl implements QuerySupportUser {

    private final ApplicationContext applicationContext;

    @Autowired
    public QuerySupportUserImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public Products getProductByID(int id) {
        SQLSupport sqlSupport = (SQLSupport) applicationContext.getBean("sql_support");
        try {
            ResultSet set = sqlSupport.getConnection().createStatement().executeQuery("select * from Products where id=" + id);
            if(set.next()) {
                Products products = new Products();
                products.setId(set.getInt("id"));
                products.setName(set.getString("name"));
                products.setType(set.getString("type"));
                products.setCost(set.getInt("cost"));
                return products;
            }
        } catch (SQLException e) {
            try {
                sqlSupport.getConnection().createStatement().executeQuery(SQLSupport.CRE_TABLE_PRODUCTS);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Logs> getLogsByUserID(String login) {
        SQLSupport sqlSupport = (SQLSupport) applicationContext.getBean("sql_support");

        try {
            ResultSet set = sqlSupport.getConnection().createStatement().executeQuery("select id from Users where login='" + login + "'");
            int index;
            List<Logs> logsList = null;
            if(set.next()) {
                index = set.getInt("id");
                set = sqlSupport.getConnection().createStatement().executeQuery("select * from Logs where id_user=" + index);
                logsList = new ArrayList<>();
                while (set.next()) {
                    Logs logs = new Logs();
                    logs.setId(set.getInt("id"));
                    logs.setType(set.getString("type"));
                    logs.setQuery(set.getString("query"));
                    logsList.add(logs);
                }
                return logsList;
            }

        } catch (SQLException e) {
            try {
                sqlSupport.getConnection().createStatement().executeQuery(SQLSupport.CRE_TABLE_LOGS);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return null;
    }
}
