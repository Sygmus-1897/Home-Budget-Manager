/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseexpense;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Vivek Sharma
 */
public class FirstPageController implements Initializable {
    
    @FXML
    private AnchorPane homePage;
    @FXML
    private Button dailyBtn, monthlyBtn, itemBtn;
    @FXML
    private Label descripBoxDaily, descripBoxItem, descripBoxMonthly;
    
    private void setMyScene(Parent paneLayout){
        Scene scene = new Scene(paneLayout);
        Stage curStage = (Stage) homePage.getScene().getWindow();
        curStage.setScene(scene);
        curStage.show();
    }
    
    @FXML
    private void showDesc(Event e){
        FadeTransition fadeIn = new FadeTransition();
        
        if ((Button)e.getSource() == dailyBtn){
            fadeIn.setNode(descripBoxDaily);
        }
        else
        if ((Button)e.getSource() == monthlyBtn){
            fadeIn.setNode(descripBoxMonthly);
        }
        else
        if ((Button)e.getSource() == itemBtn){
            fadeIn.setNode(descripBoxItem);
        }
        
        fadeIn.setFromValue(0);
        fadeIn.setDuration(Duration.millis(200));
        fadeIn.setToValue(1);
        fadeIn.play();
        
    }
    
    @FXML
    private void goToEntryPage() throws IOException {
        Parent paneLayout = FXMLLoader.load(getClass().getResource("DailyEntryPage.fxml"));
        setMyScene(paneLayout);
    }
    
    @FXML
    private void goToFilterPage() throws IOException {
        Parent paneLayout = FXMLLoader.load(getClass().getResource("filterToItemReport.fxml"));
        setMyScene(paneLayout);
    }
    
    @FXML
    private void goToMonthlyPage() throws IOException {
        Parent paneLayout = FXMLLoader.load(getClass().getResource("monthlyReport.fxml"));
        setMyScene(paneLayout);
    }
    
    
    @FXML
    private void hideDesc(Event e){
        FadeTransition fadeOut = new FadeTransition();
        
        if ((Button)e.getSource() == dailyBtn){
            fadeOut.setNode(descripBoxDaily);
        }
        else
        if ((Button)e.getSource() == monthlyBtn){
            fadeOut.setNode(descripBoxMonthly);
        }
        else
        if ((Button)e.getSource() == itemBtn){
            fadeOut.setNode(descripBoxItem);
        }
        
        fadeOut.setFromValue(1);
        fadeOut.setDuration(Duration.millis(200));
        fadeOut.setToValue(0);
        fadeOut.play();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        descripBoxDaily.setOpacity(0);
        descripBoxItem.setOpacity(0);
        descripBoxMonthly.setOpacity(0);
    }    
    
}
