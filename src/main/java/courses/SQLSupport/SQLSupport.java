package courses.SQLSupport;

import courses.model.Products;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(value = "sql_support")
public class SQLSupport {

    private Connection connection;
    public static String ADMIN_LOGIN = "admin";
    public static String ADMIN_PASSWORD = "password";
    public final static String CRE_TABLE_USER = "create table Users (" +
            "id int primary key identity(1,1)," +
            "login varchar(20) not null," +
            "password varchar(30)" +
            ")";
    public final static String CRE_TABLE_PRODUCTS = "create table Products (" +
            "id int primary key identity(1,1)," +
            "name varchar(30) not null," +
            "type varchar(30) not null," +
            "cost int default(0)" +
            ")";
    public final static String CRE_TABLE_LOGS = "create table Logs (" +
            "id int primary key identity(1,1)," +
            "type varchar(40) not null," +
            "query varchar(90) not null," +
            "id_user int," +
            "constraint FK_Users_Logs foreign key (id_user) references Users" +
            ")";
    public SQLSupport() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:65367;database=SpringTest;integratedSecurity=true;");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean createUser(String login, String password) {
        try {
            ResultSet set = connection.createStatement().executeQuery("select login, password from Users where login='" + login + "'");
            if(set.next()) return false;
            else {
                connection.createStatement().executeUpdate("insert into Users(login, password) VALUES ('"+ login + "', '" + password + "')");
                return true;
            }
        } catch (SQLException e) {
            try {
                connection.createStatement().executeQuery(CRE_TABLE_USER);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return false;
    }

    public boolean findUserByLogin(String login, String password) {
        try {
            ResultSet set = connection.createStatement().executeQuery("select login, password from Users where login='" + login + "'");
            if(set.next()) {
                String log = set.getString("login");
                String pass = set.getString("password");
                if(log.equals(login) && pass.equals(password)) {
                    return true;
                }
                else {
                    return false;
                }
            }

        } catch (SQLException e) {
            try {
                connection.createStatement().executeQuery(CRE_TABLE_USER);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return false;
    }

    public List<Products> getAllRows() {
        try {
            List<Products> listProducts = new ArrayList<>();
            ResultSet set = connection.createStatement().executeQuery("select * from Products");
            Products products;
            while (set.next()) {
                products = new Products();
                products.setId(set.getInt("id"));
                products.setName(set.getString("name"));
                products.setType(set.getString("type"));
                products.setCost(set.getInt("cost"));
                listProducts.add(products);
            }
            return listProducts;
        } catch (SQLException e) {
            try {
                connection.createStatement().executeQuery(CRE_TABLE_PRODUCTS);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return null;
    }

    public void addLog(String login, String types, String request) {
        try {
            int index = 1;
            ResultSet set = connection.createStatement().executeQuery("select id from Users where login='" + login + "'");
            if(set.next()) {
                index = set.getInt("id");
                connection.createStatement().executeUpdate("insert into Logs(type, query, id_user)" +
                        " VALUES ('" + types + "','" + request + "','" + index + "')");
            }
        } catch (SQLException e) {
            try {
                connection.createStatement().executeQuery(CRE_TABLE_LOGS);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
    }

    public boolean findUserForLogs(String login) {
        try {
            ResultSet set = connection.createStatement().executeQuery("select login, password from Users where login='" + login + "'");
            if(set.next()) {
                return true;
            }
        } catch (SQLException e) {
            try {
                connection.createStatement().executeQuery(CRE_TABLE_USER);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        }
        return false;
    }
}
