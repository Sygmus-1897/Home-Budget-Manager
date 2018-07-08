/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseexpense;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Vivek Sharma
 */
public class ItemReportPageController implements Initializable {

    @FXML
    private Accordion accor = new Accordion();
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Map<String, List<List<String>>> mainMap = new HashMap<>();
        mainMap = HouseExpense.itemMap;
        System.out.println(mainMap.size());
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
                            System.out.println("INSIDE TC: "+param.getValue().get(lmao).toString());
                            return new SimpleStringProperty(param.getValue().get(lmao).toString());                        
                        }
                    });
                    tv.getColumns().add(col);
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
                System.out.println("THATS THE ROW: "+data);
                tv.setItems(data);
                accor.getPanes().add(tp);
                tp.setContent(tv);
                tp.setText(key+" > Qty : "+totalQty+" > Amt : "+totalAmt);
                numOfTP++;
            }
        }
    }    

