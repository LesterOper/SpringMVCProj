package courses.controllers;

import courses.SQLSupport.QuerySupportAdmin;
import courses.SQLSupport.QuerySupportUser;
import courses.SQLSupport.SQLSupport;
import courses.SQLSupport.impl.QuerySupportAdminImpl;
import courses.SQLSupport.impl.QuerySupportUserImpl;
import courses.model.Logs;
import courses.model.Products;
import courses.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProjectController {
    private final ApplicationContext context;
    private SQLSupport sql;

    @Autowired
    public ProjectController(ApplicationContext context, SQLSupport sql) {
        this.context = context;
        this.sql = sql;
    }

    @GetMapping(value = "/signIn")
    public ResponseEntity<?> greeting(@RequestParam(value = "login") String name,
                                      @RequestParam(value = "password") String password) {
        if (sql.findUserByLogin(name, password)) {
            return new ResponseEntity<>("Sign in complete", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Such user doesn't exist or data doesn't correct. Try to sign up by url /reg. Or such table is created just now", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/reg")
    public ResponseEntity<?> registration(@RequestParam(value = "login") String login,
                                          @RequestParam(value = "password") String password) {
        sql = (SQLSupport) context.getBean("sql_support");
        if (sql.createUser(login, password)) {
            Users users = new Users();
            users.setLogin(login);
            users.setPassword(password);
            return new ResponseEntity<>(users, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Such user already exist or this table is created just now",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/admin/creProd")
    public ResponseEntity<?> createNewProduct(@RequestBody Products products) {
        QuerySupportAdmin query = (QuerySupportAdminImpl) context.getBean("queryAdmin");
        if(query.insertRow(products)) {
            return new ResponseEntity<>(products, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Check your request body or this table is created just now", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> getListOfProducts() {
        List<Products> list = sql.getAllRows();
        return list != null ? new ResponseEntity<>(list, HttpStatus.OK) : new ResponseEntity<>("Smth happens with system", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/admin/update")
    public ResponseEntity<?> updateProd(@RequestParam(value = "id") int id, @RequestBody Products products) {
        QuerySupportAdmin query = (QuerySupportAdminImpl) context.getBean("queryAdmin");
        if(query.updateRow(products, id)) {
            return new ResponseEntity<>(sql.getAllRows(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Such products doesn't exist in DB or table is created just now", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/admin/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam(value = "id") int id) {
        QuerySupportAdmin query = (QuerySupportAdminImpl) context.getBean("queryAdmin");
        return query.removeRow(id) ? new ResponseEntity<>(sql.getAllRows(), HttpStatus.OK)
                : new ResponseEntity<>("Cannot delete the product. Check the index or this table is created just now", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/user/{login}/product")
    public ResponseEntity<?> getProduct(@RequestParam(value = "id") int id, @PathVariable(value = "login") String login) {
        QuerySupportUser user = (QuerySupportUserImpl) context.getBean("queryUser");
        if(sql.findUserForLogs(login)) {
            Products products = user.getProductByID(id);
            if (products != null) {
                sql.addLog(login, "select", "Get product by id: " + id);
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
            sql.addLog(login, "select", "Error while getting product by id: " + id);
            return new ResponseEntity<>("Such product doesn't exist or this table is created just now", HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>("Check user's login or this table is created just now", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/user/{login}/logs")
    public ResponseEntity<?> getLogs(@PathVariable(value = "login") String login) {
        QuerySupportUser user = (QuerySupportUserImpl) context.getBean("queryUser");

        List<Logs> logs = user.getLogsByUserID(login);
        if(logs != null) {
            sql.addLog(login, "select", "Get logs by login: " + login);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        }
        sql.addLog(login, "select", "Error while getting logs by login: " + login);
        return new ResponseEntity<>("Such user doesn't exist or table is created just now", HttpStatus.BAD_REQUEST);
    }
}


