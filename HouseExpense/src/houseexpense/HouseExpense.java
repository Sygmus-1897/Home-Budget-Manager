/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package houseexpense;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Vivek Sharma
 */
public class HouseExpense extends Application {
    
    private static Stage primaryStage = new Stage();
    public static String ID[] = new String[49];
    public static String categories[][] = new String[30][50];
    public static Map<String, List<List<String>>> itemMap = new HashMap<>();
    public static int catCounter, itemCounter = 1, idCounter;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FirstPage.fxml")); // TO CHANGE
        Scene scene = new Scene(root);
        primaryStage = stage;
        stage.setScene(scene);
        stage.show();
    }
    
    public static Stage getStage(){
        return primaryStage;
    }
    
    public static Statement connectDB() throws SQLException {
        Connection con = null;
        con = DriverManager.getConnection("jdbc:sqlite:D:\\Java Programs\\HouseExpense\\src\\houseexpense\\mainItems.db");
        Statement stmt = con.createStatement();
        return stmt;
    }
    public static void main(String[] args) throws SQLException {
        
        String query = "SELECT DISTINCT(Category) FROM allItems";
        ResultSet rs = connectDB().executeQuery(query);
        
        while(rs.next()){
            categories[catCounter][0] = rs.getString("Category");
            catCounter++;
        }
        for (int i = 0; i <catCounter; i++){
            
            query = "SELECT * FROM allItems where Category = '" + categories[i][0] + "';";
            rs = HouseExpense.connectDB().executeQuery(query);
            
            while(rs.next()){
                categories[i][itemCounter] = rs.getString("Item");
                ID[idCounter] = rs.getString("ID");
                idCounter++;
                itemCounter++;
            }
            itemCounter = 1;
            
        }
        launch(args);
    }
    
}
