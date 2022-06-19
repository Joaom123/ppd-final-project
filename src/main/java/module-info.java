module ifce.ppd.finalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens ifce.ppd.finalproject to javafx.fxml;
    exports ifce.ppd.finalproject;
}