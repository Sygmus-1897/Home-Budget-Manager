/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseexpense;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import java.sql.*;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Vivek Sharma
 */
public class FilterToItemReportController implements Initializable {

    @FXML
    private DatePicker startDatePicker, stopDatePicker;
    @FXML
    private AnchorPane filterPage;
    @FXML
    private GridPane optionsGrid = new GridPane();
    @FXML
    private ScrollPane scroller = new ScrollPane();
    @FXML
    private CheckBox allOption = new CheckBox();
    @FXML
    private CheckBox[] option;
    ArrayList<String> categories = new ArrayList<String>();
    ArrayList<String> catsToEnter = new ArrayList<String>();
 
    @FXML
    private void generateReport(ActionEvent e) throws ParseException, SQLException, IOException{
        System.out.println("Function Executed");
        int counterToEntry = 0;
        for(int i=0; i<categories.size(); i++){
            if(option[i].isSelected()){
                catsToEnter.add(option[i].getText());
                counterToEntry++;
            }
        }
        if(allOption.isSelected()){
            counterToEntry =0;
            for(int i=0; i<categories.size(); i++){
                option[i].setSelected(true);
                catsToEnter.add(option[i].getText());
                counterToEntry++;
            }
        }
        if(counterToEntry!=0){
            System.out.println("Date Picker Start");
            LocalDate startDate = startDatePicker.getValue();
            LocalDate stopDate = stopDatePicker.getValue();
            System.out.println(" ^ Stops");
            LocalDate datePointer = startDate;
            Map<String, List<List<String>>> mainMap = new HashMap<>();
            int rowsCount = 0;
            while (!datePointer.isAfter(stopDate)) {
                System.out.println("In The Loop");
                if(stopDate.isAfter(java.time.LocalDate.now())){
                    break;
                }
                System.out.println(datePointer);
                String query = "Select * from '"+datePointer.getMonthValue()+"' WHERE DATE="+datePointer.getDayOfMonth()+";";
                ResultSet rs;
                try{
                    rs = yearConnect(datePointer.getYear()).executeQuery(query);
                }
                catch(SQLException ev){
                    System.out.println(ev);
                    datePointer = datePointer.plusDays(1);
                    continue;
                }
                while(rs.next()){

                    String itemName = rs.getString("Item");
                    String itemCategory = rs.getString("Category");
                    String itemQty = rs.getString("Qty");
                    String itemAmt = rs.getString("Amt");
                    System.out.println(itemName);
                    
                    if(!catsToEnter.contains(itemCategory)){
                        System.out.println("Shit Plaec");
                        continue;   
                    }

                    ArrayList<List<String>> diffItems = new ArrayList<>();
                    ArrayList<String> itemElements = new ArrayList<>();
                //
                //
                //
                    //<editor-fold defaultstate="collapsed" desc="From DB TO HASHMAP">
                    if (mainMap.containsKey(itemCategory)) {
                        boolean found = false;
                        int i = 0;
                        while (i < mainMap.get(itemCategory).size()) {
                            if (mainMap.get(itemCategory).get(i).get(0).equals(itemName)) {
                                found = true;
                                break;
                            } else {
                                i++;
                                found = false;
                            }
                        }
                        if (found) {
                            double qqty = Double.parseDouble(mainMap.get(itemCategory).get(i).get(1));
                            double aamt = Double.parseDouble(mainMap.get(itemCategory).get(i).get(2));
                            qqty += Integer.parseInt(itemQty);
                            aamt += Integer.parseInt(itemAmt);
                            mainMap.get(itemCategory).get(i).set(1,Double.toString(qqty));
                            mainMap.get(itemCategory).get(i).set(2, Double.toString(aamt));
                        } else {
                            diffItems.addAll(mainMap.get(itemCategory));
                            itemElements.add(itemName);
                            itemElements.add(itemQty);
                            itemElements.add(itemAmt);
                            diffItems.add(itemElements);
                            mainMap.put(itemCategory, diffItems);
                            rowsCount++;
                        }
                    } else {
                        itemElements.add(itemName);
                        itemElements.add(itemQty);
                        itemElements.add(itemAmt);
                        diffItems.add(itemElements);
                        mainMap.put(itemCategory, diffItems);
                        rowsCount++;
                    }
                }
                //</editor-fold>
                datePointer = datePointer.plusDays(1);
                System.out.println(datePointer.getMonthValue());
            }
            HouseExpense.itemMap = mainMap;
            System.out.println("MAP: "+mainMap);
            goToItemReport();
        }
        else{
            System.out.println("Nothing is Selected");
        }
    }
    
    private Statement yearConnect(int year) throws SQLException{
        Connection con = DriverManager.getConnection("jdbc:sqlite:D:\\Java Programs\\HouseExpense\\src\\houseexpense\\"+year+".db");
        Statement stmt = con.createStatement();
        return stmt;
    }
    
    // GOTOS
    private void setMyScene(Parent paneLayout){
        Scene scene = new Scene(paneLayout);
        Stage curStage = (Stage) filterPage.getScene().getWindow();
        curStage.setScene(scene);
        curStage.show();
    }
    
    private void goToItemReport() throws IOException {
        Parent paneLayout = FXMLLoader.load(getClass().getResource("ItemReportPage.fxml"));
        setMyScene(paneLayout);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startDatePicker.setValue(LocalDate.now());
        stopDatePicker.setValue(LocalDate.now());
        optionsGrid.setVgap(20);
        optionsGrid.setHgap(30);
        scroller.setStyle("-fx-background-color:transparent;");
//        optionsGrid.setGridLinesVisible(true);
        try{
            String query = "Select Category from allItems;";
            ResultSet rs = HouseExpense.connectDB().executeQuery(query);
            while(rs.next()){
                categories.add(rs.getString("Category"));
            }
            option = new CheckBox[categories.size()];
            System.out.println("Dynamic Array: "+categories.size());
            for (int i=0; i<categories.size(); i++){
                option[i] = new CheckBox();
                option[i].setMaxWidth(150);
                
                option[i].setText(categories.get(i));
            }
            int counter=0;
            int row=0;
            while(counter<categories.size()){
                for (int column = 0; column<4; column++){
                    if(counter<categories.size()){
                    optionsGrid.add(option[counter], column, row);      
                    counter++;
                    }
                }
                row++;
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        
    }    
    
}
