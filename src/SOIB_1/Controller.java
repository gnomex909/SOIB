package SOIB_1;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.apache.commons.math3.distribution.NormalDistribution;


public class Controller {
    private double prob;
    @FXML
    Button button_add;
    @FXML
    Button button_clear;
    @FXML
    TextField input_expected;
    @FXML
    TextField input_name;
    @FXML
    TextField input_variation;
    @FXML
    TextField input_amount;
    @FXML
    TextField input_prob;
    @FXML
    Text text_result_min;
    @FXML
    Text text_result_max;
    @FXML
    Text text_result_avg;
    @FXML
    ListView<TrackElement> list_track_items;
    private ObservableList<TrackElement> observableList;
    static class trackCell extends ListCell<TrackElement>{
        private Text name = new Text("name");
        private Text amount = new Text("amount");
        private Text attenuation = new Text("attenuation");
        private Text variation = new Text("variation");
        Button delete = new Button("Usuń");
        private BorderPane borderPane = new BorderPane(null, null, delete, amount, name);
        public trackCell(){
            super();
            delete.setOnAction(event -> getListView().getItems().remove(getItem()));
        }
        @Override
        protected void updateItem(TrackElement item, boolean empty) {
            super.updateItem(item,empty);
            if (item == null || empty){
                name.setText("");
                amount.setText("");
                attenuation.setText("");
                variation.setText("");
                delete.setVisible(false);
            }else{
                name.setText("Nazwa: "+item.getElementName());
                amount.setText("Śr. tłum.: "+ item.getExpectedDamping()+" Odchylenie: "+ item.getVariation()+" Liczba el.: "+item.getAmount());
                attenuation.setText(item.getExpectedDamping()+"");
                variation.setText(item.getVariation()+"");
                delete.setVisible(true);
                setGraphic(borderPane);
            }
        }

    }
    @FXML
    public void initialize(){
        observableList = FXCollections.observableArrayList();
        list_track_items.setCellFactory(param -> new trackCell());
        list_track_items.setItems(observableList);
        observableList.addListener(new ListChangeListener<TrackElement>() {
            @Override
            public void onChanged(Change<? extends TrackElement> c) {
                calculate(observableList);
            }
        });
    }
    @FXML
    public void addItem(){
        String name = input_name.getText();
        double expected = 0;
        double variation = 0;
        int amount = 0;
        boolean correct = true;
        try {
            expected = Double.parseDouble(input_expected.getText());
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zły format danych");
            alert.setHeaderText(null);
            alert.setContentText("Niepoprawne dane w polu 'Średnie tłumienie'");
            alert.showAndWait();
            correct = false;
        }
        try {
            variation = Double.parseDouble(input_variation.getText());
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zły format danych");
            alert.setHeaderText(null);
            alert.setContentText("Niepoprawne dane w polu 'Odchylenie standardowe'");
            alert.showAndWait();
            correct = false;
        }
        try{
        amount = Integer.parseInt(input_amount.getText());
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zły format danych");
            alert.setHeaderText(null);
            alert.setContentText("Niepoprawne dane w polu 'Liczba elementów'");
            alert.showAndWait();
            correct = false;
        }
        if(correct) {
            TrackElement trackElement = new TrackElement(expected, variation, amount, name);
            observableList.add(trackElement);
            clearFields();
        }
//        System.out.println(observableList);
//        list_track_items.setItems(observableList);
//        clearFields();
    }
    @FXML
    public void clearFields(){
        input_expected.setText("");
        input_amount.setText("");
        input_variation.setText("");
        input_name.setText("");
    }
    @FXML
    public void setProb(){
        try{
        prob = Double.parseDouble(input_prob.getText());
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zły format danych");
            alert.setHeaderText(null);
            alert.setContentText("Niepoprawne dane w polu 'Prawdopodobieństwo błędu'");
            alert.showAndWait();
        }
        calculate(observableList);
    }
    private void calculate(ObservableList<TrackElement> observableList){
        double expected = 0;
        double variation = 0;
        for(TrackElement t : observableList){
            expected += t.getExpectedDamping() * t.getAmount();
            variation += t.getVariation() * t.getVariation() * t.getAmount();
        }
        variation = Math.cbrt(variation);
        NormalDistribution normalDistribution = new NormalDistribution(expected, variation);
        double lowerBound = 0.50;
        double upperBound = 0.50;
        if(prob != 0){
            lowerBound = prob / 2;
            upperBound = 1 - lowerBound;
        }
        text_result_avg.setText(""+expected + "dB");
        text_result_min.setText(""+normalDistribution.inverseCumulativeProbability(lowerBound) +" dB");
        text_result_max.setText(""+normalDistribution.inverseCumulativeProbability(upperBound)+ " dB");
    }
}
