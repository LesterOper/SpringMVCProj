package courses.SQLSupport.impl;

import courses.SQLSupport.QuerySupportAdmin;
import courses.SQLSupport.SQLSupport;
import courses.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(value = "queryAdmin")
public class QuerySupportAdminImpl implements QuerySupportAdmin {
    private ApplicationContext context;
    private SQLSupport sql;

    @Autowired
    public QuerySupportAdminImpl(ApplicationContext context, SQLSupport sql) {
        this.context = context;
        this.sql = sql;
    }

    @Override
    public boolean updateRow(Products products, int id) {
        try {
            ResultSet set = sql.getConnection().createStatement().executeQuery("select id from Products where id=" + id);
            if(set.next()) {
                sql.getConnection().createStatement().executeUpdate("update Products " +
                        "set name='" + products.getName() + "'," +
                        "type='" + products.getType() + "'," +
                        "cost=" + products.getCost() + " where id=" + id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            try {
                sql.getConnection().createStatement().executeQuery(SQLSupport.CRE_TABLE_PRODUCTS);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeRow(int id) {
        try {
            ResultSet set = sql.getConnection().createStatement().executeQuery("select id from Products where id=" + id);
            if(set.next()) {
                sql.getConnection().createStatement().executeUpdate("delete from Products where id=" + id);
                return true;
            }
        } catch (SQLException e) {
            try {
                sql.getConnection().createStatement().executeQuery(SQLSupport.CRE_TABLE_PRODUCTS);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertRow(Products products) {
        try {
            sql.getConnection().createStatement().executeUpdate("insert into Products(name, type, cost) " +
                    "values ('" + products.getName() + "','" + products.getType() + "','" + products.getCost() + "')");
            return true;
        } catch (SQLException e) {
            try {
                sql.getConnection().createStatement().executeQuery(SQLSupport.CRE_TABLE_PRODUCTS);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return false;
    }
}
