import Classes.LoginFrame;
import model.DbConnection;

import javax.swing.*;
import java.sql.*;

public class Main {
    static Connection connection = DbConnection.getConnection();
   static boolean login=false;
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setTitle("Expense Tracker");
        SwingUtilities.invokeLater(() -> new LoginFrame()); // Start with Login/Register Screen
    }


}

