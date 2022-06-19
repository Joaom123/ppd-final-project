module ifce.ppd.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires jini.core;
    requires jini.ext;

    requires java.naming;
    requires activemq.core;
    requires javax.jms.api;


    opens ifce.ppd.finalproject to javafx.fxml;
    exports ifce.ppd.finalproject;
}