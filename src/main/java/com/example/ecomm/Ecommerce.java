package com.example.ecomm;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Ecommerce extends Application {

//    Login login = new Login();

    private  final  int width=500, height=250, headerLine=50;
    ProductList productList = new ProductList();
    Pane bodyPane;
    GridPane footerBar;

    Order order = new Order();
    ObservableList<Product> cartItemList= FXCollections.observableArrayList();

    Button signInButton;
    Button placeOrderButton= new Button("Place Order");

    Label welcomeLable = new Label("Welcome Customer");

    Customer loggedInCustomer=null;

    private void addItemsToCart(Product product){
        if(cartItemList.contains(product))
            return;
        cartItemList.add(product);
        System.out.println("Products in Cart"+ cartItemList.stream().count());
    }

    private GridPane headerBar(){
        GridPane header = new GridPane();

        TextField searchBar = new TextField();
        Button searchButton = new Button("Search");
        Button cartButton= new Button("Cart");

        Button ordersButton= new Button("orders");

        ordersButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(order.getOrders());
            }
        });

        signInButton= new Button("Sign In");

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productList.getAllProducts());
            }
        });
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(loginPage());
            }
        });

        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productList.productsInCart(cartItemList));
            }
        });

//        GridPane header = new GridPane();
        header.setHgap(10);

        header.add(searchBar, 0, 0);
        header.add(searchButton, 1, 0);
        header.add(signInButton, 2,  0);
        header.add(welcomeLable, 3, 0);
        header.add(cartButton, 4, 0);
        header.add(ordersButton, 5, 0);

        return  header;
    }
    private GridPane loginPage(){
        Label userLable = new Label("User Name");
        Label passLable = new Label("Password");
        TextField userName= new TextField();
        userName.setText("jidnyasa@gmail.com");
        userName.setPromptText("Enter User Name");
        PasswordField password=new PasswordField();
        password.setText("abc");
        password.setPromptText("Enter Password");
        Button loginButton = new Button("Login");
        Label messageLable = new Label("Login - Message");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String user= userName.getText();
                String pass=password.getText();
                loggedInCustomer=login.customerLogin(user, pass);
                if(loggedInCustomer !=null){
                    messageLable.setText("Login Successful!");
                    welcomeLable.setText("Welcome "+loggedInCustomer.getName());
                }
                else{
                    messageLable.setText("Login failed");
                }

            }
        });

        GridPane loginPane=new GridPane();
        loginPane.setTranslateY(50);
        loginPane.setTranslateX(50);
        loginPane.setVgap(10);
        loginPane.setHgap(10);
        loginPane.add(userLable, 0, 0);
        loginPane.add(userName, 1, 0);
        loginPane.add(passLable, 0, 1);
        loginPane.add(password, 1,1);
        loginPane.add(loginButton, 0, 2);
        loginPane.add(messageLable, 1, 2);
        return loginPane;
    }

    private void showDialogue(String message){
        Dialog<String> dilogue= new Dialog<>();
        dilogue.setTitle("Order Status");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dilogue.setContentText(message);
        dilogue.getDialogPane().getButtonTypes().add(type);
        dilogue.showAndWait();
    }

    private GridPane footerBar(){
        Button buyNowButton = new Button("Buy Now");
        Button addToCartButton = new Button("Add To Cart");
        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProduct();
                boolean orderStatus=false;
                if (product!=null && loggedInCustomer!=null){
                    orderStatus=order.placeOrder(loggedInCustomer, product);
                }
                if (orderStatus==true){
                    showDialogue("Order Successful");
                }
                else {

                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                addItemsToCart(product);
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int orderCount=0;
                if (!cartItemList.isEmpty() && loggedInCustomer!=null){
                    orderCount=Order.placeOrderMultipleProducts(cartItemList, loggedInCustomer);
                }
                if (orderCount>0){
                    showDialogue("Orders for "+orderCount+" products Placed Successfully");
                }
                else {

                }
            }
        });

        GridPane footer = new GridPane();
        footer.setHgap(10);
        footer.setTranslateY(headerLine+height);
        footer.add(buyNowButton, 0 , 0);
        footer.add(addToCartButton, 1, 0);
        footer.add(placeOrderButton, 2, 0);
        return footer;
    }
    private Pane createContent(){
        Pane root=new Pane();
        root.setPrefSize(width,height+2*headerLine);

        bodyPane = new Pane();
        bodyPane.setPrefSize(width, height);
        bodyPane.setTranslateY(headerLine);
        bodyPane.setTranslateX(10);

        bodyPane.getChildren().addAll(loginPage());
        footerBar= footerBar();
        root.getChildren().addAll(headerBar()
//                , loginPage()
//                , productList.getAllProducts()
                , bodyPane
                , footerBar
        );
        return root;
    }
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Ecommerce.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.setTitle("ECommerce");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}