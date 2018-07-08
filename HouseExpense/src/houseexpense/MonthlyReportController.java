/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseexpense;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author Vivek Sharma
 */
public class MonthlyReportController implements Initializable {

    @FXML
    private Accordion accor = new Accordion();
    @FXML
    private AnchorPane monthlyPage;
    @FXML
    private ScrollPane scroller;
    
    //GOTOS
    private void setMyScene(Parent paneLayout){
        Scene scene = new Scene(paneLayout);
        Stage curStage = (Stage) monthlyPage.getScene().getWindow();
        curStage.setScene(scene);
        curStage.show();
    }
    
    @FXML
    private void goToHomePage() throws IOException {
        Parent paneLayout = FXMLLoader.load(getClass().getResource("FirstPage.fxml"));
        setMyScene(paneLayout);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scroller.setStyle("-fx-background-color: transparent;");
        System.out.println("Ateast Entered");
        try{
            String curDate = java.time.LocalDate.now().toString();
            Connection con = DriverManager.getConnection("jdbc:sqlite:D:\\Java Programs\\HouseExpense\\src\\houseexpense\\"+java.time.LocalDate.now().getYear()+".db");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM '"+java.time.LocalDate.now().getMonthValue()+"';");
            Map<String, List<List<String>>> mainMap = new HashMap<>();
            int rowsCount = 0;
            System.out.println("Ateast Entered Somewhere");
            while(rs.next()){
                    System.out.println("Ateast Entered");
                    String itemName = rs.getString("Item");
                    String itemCategory = rs.getString("Category");
                    String itemQty = rs.getString("Qty");
                    String itemAmt = rs.getString("Amt");
                    ArrayList<List<String>> diffItems = new ArrayList<>();
                    ArrayList<String> itemElements = new ArrayList<>();
                    System.out.println("Reached Here with "+itemName);
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
                            int qqty = Integer.parseInt(mainMap.get(itemCategory).get(i).get(1));
                            int aamt = Integer.parseInt(mainMap.get(itemCategory).get(i).get(2));
                            qqty += Integer.parseInt(itemQty);
                            aamt += Integer.parseInt(itemAmt);
                            mainMap.get(itemCategory).get(i).set(1,Integer.toString(qqty));
                            mainMap.get(itemCategory).get(i).set(2, Integer.toString(aamt));
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
            
            int numOfTP = 0;
            Set<String> keys = mainMap.keySet();
            for (String key : keys){
               double totalAmt = 0;
               double totalQty = 0;
               TitledPane tp = new TitledPane();
               TableView<Map.Entry<String, List<List<String>>>> tv = new TableView<>();
                ObservableList data = FXCollections.observableArrayList();
//                ArrayList sameCatItem = new ArrayList();      // SAME CATEGORY ITEM i.e. RS.RESULTSET
//                for(int monkey=0; monkey<mainMap.get(key).size(); monkey++){
//                    sameCatItem.add(mainMap.get(key).get(monkey));
//                }
//                System.out.println(sameCatItem+ " VS "+mainMap.get(key));
                // LOOP FOR COLUMNS
                String[] ColumnName = {"Item", "Quantity", "Amount"};
                for (int lol=0; lol<3; lol++){
                    final int lmao = lol;
                    TableColumn col = new TableColumn(ColumnName[lol]);
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
//                            System.out.println("INSIDE TC: "+param.getValue().get(lmao).toString());
                            return new SimpleStringProperty(param.getValue().get(lmao).toString());                        
                        }
                    });
                    tv.getColumns().add(col);
//                    if(numOfTP == 0){
//                        t1.getColumns().add(col);
//                    }
//                    if(numOfTP == 1){
//                        t2.getColumns().add(col);
//                    }
//                    if(numOfTP == 2){
//                        t3.getColumns().add(col);
//                    }
//                    System.out.println("Column ["+lol+"] ");
                }
                // SETTING ITEMS TO TABLE
                
                for(int items=0; items<mainMap.get(key).size(); items++){
                    // ACC TO COLUMNS
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int a=0; a<3; a++){
                        if(a==1){
                            totalQty+=Double.parseDouble(mainMap.get(key).get(items).get(a).toString());
                        }
                        if(a==2){
                            totalAmt+=Double.parseDouble(mainMap.get(key).get(items).get(a).toString());
                        }
                        System.out.println("ONE BY ONE ENTRY: "+mainMap.get(key).get(items).get(a).toString());  // COMMENTED OTHERS COZ IT IS ALREADY A ROW
                        row.add(mainMap.get(key).get(items).get(a).toString());
//                        System.out.println("Thats how row should like: "+data);
                    }
                    data.add(row);
                }
//                data.add(row);
//                System.out.println("THATS THE ROW: "+data);
                tv.setItems(data);
//                if(numOfTP == 0){
//                    t1.setItems(data);
//                }
//                if(numOfTP == 1){
//                    t2.setItems(data);
//                }
//                if(numOfTP == 2){
//                    t3.setItems(data);
//                }
                accor.getPanes().add(tp);
                tp.setContent(tv);
                tp.setText(key+" > Qty : "+totalQty+" > Amt : "+totalAmt);
                numOfTP++;
            }
            
        }
        catch(SQLException e){
            System.out.println("No DB Yet "+e);
        }
    }    
    
}
