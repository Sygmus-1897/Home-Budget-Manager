/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseexpense;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Vivek Sharma
 */
public class DailyEntryPageController implements Initializable {
    
    @FXML
    private AnchorPane entryPage;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label dateLbl;
    @FXML
    private Button addBtn, submitBtn;
    @FXML
    private Button removeBtn[] = new Button[70];
    @FXML
    private HBox[] box = new HBox[70];    
    @FXML
    private ComboBox[] itemCat = new ComboBox[70];
    @FXML
    private ComboBox[] item = new ComboBox[70];
    @FXML
    private TextField[] qty = new TextField[70];
    @FXML
    private TextField[] amt = new TextField[70];
    @FXML 
    private CheckBox[] newItem = new CheckBox[70];
    @FXML
    private CheckBox postEntryCheck = new CheckBox();
    @FXML
    private DatePicker datePicker = new DatePicker();
    
    
    GridPane grid = new GridPane();
    static int counter;
    static int rowCount;
    
    @FXML
    private void removeAni(Node node, GridPane grid, int index){
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setNode(node);
        fadeOut.setFromValue(1);
        fadeOut.setDuration(Duration.millis(200));
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(finishHim -> {
            grid.getChildren().remove(node);
            item[index].setValue("del");
            qty[index].setText("del");
            amt[index].setText("del");
        });
        fadeOut.play();
        rowCount--;
    }
    
    
    @FXML
    private void addAni(Node node, GridPane pane, int index){
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setFromValue(0);
        fadeIn.setDuration(Duration.millis(200));
        fadeIn.setToValue(1);
        fadeIn.setNode(node);
        grid.addRow(index, node);
        fadeIn.play();
        rowCount++;
    }
    
    @FXML
    private void addContent(ActionEvent event){
        grid.setAlignment(Pos.CENTER_LEFT);
        // HBOX
        box[counter] = new HBox(30);
        box[counter].setId(""+counter);
        box[counter].setAlignment(Pos.CENTER);
        box[counter].setMinHeight(30);
        box[counter].setPrefWidth(600);
//        box[counter].setMinWidth(622);
        
        // HBOX ITEMS
        itemCat[counter] = new ComboBox();
        itemCat[counter].setPrefWidth(120);
        for (int i = 0; i < HouseExpense.catCounter; i++){
            itemCat[counter].getItems().addAll(HouseExpense.categories[i][0]);
            itemCat[counter].setValue(HouseExpense.categories[0][0]);
        }
        int in = counter;
        itemCat[counter].setOnAction(e -> {
            item[in].getItems().clear();
            int ind = itemCat[in].getSelectionModel().getSelectedIndex();
            for (int i = 1; i<HouseExpense.categories[ind].length; i++){
                if (HouseExpense.categories[ind][i] != null){
                item[in].getItems().add(HouseExpense.categories[ind][i]);
                item[in].setValue(HouseExpense.categories[ind][1]);
                }
            }
        });
        
        newItem[counter] = new CheckBox(); 
        newItem[counter].setPrefWidth(15);
                
        item[counter] = new ComboBox(); item[counter].setEditable(true); item[counter].setPrefWidth(120);
        item[counter].setValue(HouseExpense.categories[0][1]);
        
        qty[counter] = new TextField(); qty[counter].setPrefWidth(60);
        
        amt[counter] = new TextField(); amt[counter].setPrefWidth(60);
        removeBtn[counter] = new Button("X");
        
        // QTY FUNCTION
//        qty[counter].setOnKeyTyped(new EventHandler<KeyEvent>(){
//            @Override
//            public void handle(KeyEvent event) {
//                char c = event.;
//            }
//        });

        // BUTTON FUNCTION
        int index = counter;
        removeBtn[counter].setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                removeAni(box[index], grid, index);
            }
        });
        
        // ADD TO HBOX
        box[counter].getChildren().addAll(newItem[counter], itemCat[counter], item[counter], qty[counter], amt[counter], removeBtn[counter]);
        
        // ADD TO GRID
        addAni(box[counter], grid, counter);
//        grid.setVgap(10);
        
        // ADD TO SCROLLPANE
//        scrollPane.setContent(grid);
        counter++;
        scrollPane.setVvalue(1.0);
    }
    
    @FXML
    private void getData(ActionEvent event) throws SQLException {
        
        LocalDate entryDate = null;
        
        boolean rowCheck = true;
        boolean nullCheck = true;
        boolean newResolve = true;
        boolean oldFoundCheck = false;
        boolean tableExist = false;
        
        if(rowCount==0){
            rowCheck=false;
            System.out.println("CheckPost 1: No Row Found");
        }
        else{
            for(int i = 0; i < counter; i++){
                boolean newItemCheck = false;
                if(itemCat[i].getSelectionModel().getSelectedItem() == null || item[i].getSelectionModel().getSelectedItem() == null || qty[i].getText().equals("") || amt[i].getText().equals("")){
                    nullCheck = false;
                    System.out.println("CheckPost 2: Null Found");
                }
                else{
                    if(newItem[i].isSelected()){
                        newItemCheck = true;
                        System.out.println("CheckPost 3: New Item Found At "+i+" Index");
                    }
                    if(newItemCheck){
                        String greenItem = item[i].getSelectionModel().getSelectedItem().toString();
                        if(greenItem.contains("kg")){
                            System.out.println("KG Found");    //UPDATE TO DB
                            HouseExpense.connectDB().executeUpdate("INSERT INTO allItems (Item, Category, Unit) VALUES ('"+greenItem.replace(" kg", "")+"','"+itemCat[i].getSelectionModel().getSelectedItem().toString()+"','kg');");
                        }
                        else
                        if(greenItem.contains("ltr")){
                            System.out.println("ltr Found");   // UPDATE TO DB
                            HouseExpense.connectDB().executeUpdate("INSERT INTO allItems (Item, Category, Unit) VALUES ('"+greenItem.replace(" ltr", "")+"','"+itemCat[i].getSelectionModel().getSelectedItem().toString()+"','ltr');");
                        }
                        else
                        if(greenItem.contains("qty")){
                            System.out.println("qty Found");   // UPDATE TO DB
                            HouseExpense.connectDB().executeUpdate("INSERT INTO allItems (Item, Category, Unit) VALUES ('"+greenItem.replace(" qty", "")+"','"+itemCat[i].getSelectionModel().getSelectedItem().toString()+"','qty');");
                        }
                        else{
                            System.out.println("CheckPost 4: NO UNIT SPECIFIED TO NEW ITEM");    //ERROR POP UP !
                            newResolve = false;
                        }
                    }
                }
            }
            if(nullCheck){
                int itmComboBoxCounter = 0;
                boolean unCheckedTick = false;
                while(itmComboBoxCounter < counter){
                    int categoryCounter = 0;
                    while(HouseExpense.categories[categoryCounter][0]!=null){
                        final int itmCounter = itmComboBoxCounter;
                        if (Arrays.stream(HouseExpense.categories[categoryCounter]).anyMatch(x -> item[itmCounter].getSelectionModel().getSelectedItem().toString().equals(x))){
                            System.out.println(itmCounter+" : OK");
                            System.out.println(item[itmCounter].getSelectionModel().getSelectedItem().toString());
                            oldFoundCheck = true;
                        }
                        else
                        if (!newItem[itmComboBoxCounter].isSelected()) {
                            unCheckedTick = true;
                        }
                        categoryCounter++;
                    }
                    itmComboBoxCounter++;
                }
                if(oldFoundCheck == false && unCheckedTick == true){
                    System.out.println("CheckPost 5: UnChecked New Item Detected");
                }
            }
        }
        
        if(postEntryCheck.isSelected()){
            entryDate = datePicker.getValue();
        }
        else{
            entryDate = LocalDate.now();
        }
        
        if(rowCheck && nullCheck && newResolve && !entryDate.isAfter(LocalDate.now())){
            
            // EXCEPTION 1 : CHECK IF TABLE IS ALREADY EXIST
            try{
                makeConnection(entryDate.getYear()).executeUpdate("CREATE TABLE '"+entryDate.getMonthValue()+"'(Item TEXT NOT NULL UNIQUE, Category TEXT, Qty TEXT, Amt TEXT, Date INTEGER);");
                System.out.println("Table Created");
            }
            catch(Exception e){
                System.out.println("TABLE ALREADY EXIST");
                tableExist = true;
            }
            
            Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\Java Programs\\HouseExpense\\src\\houseexpense\\"+entryDate.getYear()+".db");
            Statement st = conn.createStatement();
            
            // 1. ENTER DATA TO ALREADY EXISTING TABLE
            if(tableExist){
                for(int i=0; i<counter; i++){
                    if (item[i].getSelectionModel().getSelectedItem() != "del") {
                        try{
                            st.executeUpdate("INSERT INTO '"+entryDate.getMonthValue()+"' VALUES('"+item[i].getSelectionModel().getSelectedItem().toString()+"','"+itemCat[i].getSelectionModel().getSelectedItem().toString()+"',"+qty[i].getText()+","+amt[i].getText()+","+entryDate.getDayOfMonth()+");");
                            System.out.println("INSERTING "+i);
                        }
                        catch(Exception e){
                            st.executeUpdate("UPDATE '"+entryDate.getMonthValue()+"' SET Qty="+qty[i].getText()+" , Amt = "+amt[i].getText()+" WHERE Item = '"+item[i].getSelectionModel().getSelectedItem().toString()+"';");
                            System.out.println("UPDATING "+i);
                        }
                    }
                }
            }
            // 2. ENTER DATA TO NEW TABLE
            else{
                String[] insertEntries = new String[counter];
                for(int i=0; i<counter; i++){
                    if(item[i].getSelectionModel().getSelectedItem() != "del"){
                        insertEntries[i] = "INSERT INTO '"+entryDate.getMonthValue()+"' VALUES('"+item[i].getSelectionModel().getSelectedItem().toString()+"','"+itemCat[i].getSelectionModel().getSelectedItem().toString()+"',"+qty[i].getText()+","+amt[i].getText()+","+entryDate.getDayOfMonth()+");";
                        st.addBatch(insertEntries[i]);
                        System.out.println("Adding "+i);
                    }
                }
                st.executeBatch();
                System.out.println("Done!");
            }
        }
        
    }
    
    private Statement makeConnection(int year) throws SQLException{
        Connection con = DriverManager.getConnection("jdbc:sqlite:D:\\Java Programs\\HouseExpense\\src\\houseexpense\\"+year+".db");
        Statement stmt = con.createStatement();
        return stmt;
    }
    
    
    /// GOTOS
    
    private void setMyScene(Parent paneLayout) {
        Scene scene = new Scene(paneLayout);
        Stage curStage = (Stage) entryPage.getScene().getWindow();
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
//        datePicker.setDayCellFactory(picker -> new DateCell() {
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                LocalDate today = LocalDate.now();
//
//                setDisable(empty || date.compareTo(today) < 0 );
//            }
//        });
        scrollPane.setContent(grid);
        try {
            makeConnection(LocalDate.now().getYear());
            System.out.println(java.time.LocalDate.now());
            dateLbl.setText((java.time.LocalDate.now()).toString());              
            grid.setPrefSize(50, 50);
//        scrollPane.setPrefSize(622, 300);
        } catch (SQLException ex) {
            System.out.println("Exception: "+ex+" at Line 190~");
        }
    }    
    
}
