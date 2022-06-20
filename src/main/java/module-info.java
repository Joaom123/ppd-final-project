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
    opens ifce.ppd.finalproject.controller;
    exports ifce.ppd.finalproject.controller to javafx.fxml;
    opens ifce.ppd.finalproject.model;
    exports ifce.ppd.finalproject.model to javafx.fxml;
    opens ifce.ppd.finalproject.rmi;
    exports ifce.ppd.finalproject.rmi to javafx.fxml;
}