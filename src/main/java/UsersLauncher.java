import java.sql.Connection;


import com.revature.controllers.UsersController;
import com.revature.utils.ConnectionUtil;
import io.javalin.Javalin;

public class UsersLauncher {
    public static void main(String[] args) {
        try(Connection conn = ConnectionUtil.getConnection()){
            System.out.println("CONNECTION SUCCESSFUL :>");
        } catch (Exception e) {
            e.printStackTrace(); //this is what tells us our error message (the red text)
            System.out.println("CONNECTION FAILED :<");
        }
        Javalin app = Javalin.create().start(7000);

        UsersController usersController = new UsersController();
        app.post("/users/register", usersController.registerUserHandler);


//        // Create a new user
//        Users newUser = new Users("J", "W", "JW", "JW@example.com", "password123");
//        Users newUser1 = new Users("W", "MM", "W123", "w123@example.com", "test123");
//        Users newUser2 = new Users("W2", "U", "W777", "2123@example.com", "test777");
//
//        //insert new user
//        System.out.println(userDAO.insertUser(newUser));
//        System.out.println(userDAO.insertUser(newUser1));
//        System.out.println(userDAO.insertUser(newUser2));
//        System.out.println(newUser.getUser_id());
//        System.out.println(newUser1.getUser_id());
//        System.out.println(newUser2.getUser_id());
//
//        Users u = userDAO.getUserById(2);
//        System.out.println(u);
//
//        System.out.println("user name is update to: " + userDAO.updateUserName(2,"new username"));
//
//
//        ArrayList<Users> users = userDAO.getAllUsers();
//        for(Users user : users){
//            System.out.println(user);
//        }
//
//        userDAO.deleteUser(2);
//
//        System.out.println("After delete");
//        users = userDAO.getAllUsers();
//        for(Users user : users){
//            System.out.println(user);
//        }

    }
}
