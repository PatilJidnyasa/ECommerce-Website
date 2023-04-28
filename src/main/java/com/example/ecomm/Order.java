package com.example.ecomm;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class Order {

     TableView<Product> orderTable;
    public  static boolean placeOrder(Customer customer, Product product){
        try{
            String placeOrder = "  INSERT INTO orders(customer_id, product_id, status) VALUES ("+customer.getId()+","+product.getId()+",'Ordered')";
            DatabaseConnection dbConn = new DatabaseConnection();

            return  dbConn.insertUpdate(placeOrder);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }
    public static int placeOrderMultipleProducts(ObservableList<Product> productObservableList, Customer customer){
        int count=0;
        for (Product product: productObservableList){
            if(placeOrder(customer, product))
                count++;
        }
        return count;
    }
    public  Pane getOrders(){

        ObservableList<Product> productList = Product.getAllProducts();
        return createTableFromList(productList);

    }

    public  Pane createTableFromList(ObservableList<Product> orderList){
        TableColumn id = new TableColumn("Id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name = new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn price = new TableColumn("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

//        ObservableList<Product> data= FXCollections.observableArrayList();
//        data.addAll(new Product(123, "Laptop", (double)234.5),
//                new Product(124, "Laptop", (double)234.5)
//        );

        orderTable = new TableView<>();
        orderTable.setItems(orderList);
        orderTable.getColumns().addAll(id, name, price);

        Pane tablePane = new Pane();
        tablePane.getChildren().add(orderTable);

        return tablePane;

    }
    public  Pane getOrders(Customer customer){
        String order = "SELECT orders.oid, products.name, products.price FROM orders INNER JOIN products ON orders.product_id=products.pid WHERE customer=+customer.getId()";
        ObservableList<Product> orderList = Product.getProducts(order);
        return createTableFromList(orderList);

    }

}
