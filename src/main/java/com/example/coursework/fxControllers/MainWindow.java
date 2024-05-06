package com.example.coursework.fxControllers;
//

import com.example.coursework.HelloApplication;
import com.example.coursework.fxControllers.tableParameters.ManagerTableParameters;
import com.example.coursework.hibernate.ShopHibernate;
import com.example.coursework.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class MainWindow implements Initializable {
    //I add @FXML above all attributes that are linked to form element ids
    //<editor-fold desc="Here are the fields for Shop tab">
    @FXML
    //ListView without a type is just a raw usage. It is best to specify the type of objects that will be stored in that list
    //In our case it is Product
    public ListView<Product> shopProducts;
    @FXML
    public Tab shopTab;
    @FXML
    public ListView<Product> myCartItems;
    //</editor-fold>

    //<editor-fold desc="Here are the fields for products tab">

    @FXML ListView<Product> productList;

    @FXML
    public Tab productsTab;
    @FXML
    public ListView<Product> productAdminList;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea productDescriptionField;
    @FXML
    public TextField productQuantityField;
    @FXML
    public TextField productWeightField;

    @Setter
    @Getter
    @FXML
    public ComboBox productTypeMenu;

    @FXML
    public TextField productPriceField;




    @FXML
    public TextField productManufacturerField;

    @FXML
    public TextField productAgeField;

    @FXML
    public DatePicker productExpiryDate;
    @FXML
    public RadioButton productFoodRadio;
    @FXML
    public RadioButton productToyRadio;
    @FXML
    public RadioButton productMedicineRadio;
    //</editor-fold>

    //<editor-fold desc="Here are the fields for User tab">
    public TableColumn<ManagerTableParameters, Integer> managerColId;
    public TableColumn<ManagerTableParameters, String> managerColLogin;
    public TableColumn<ManagerTableParameters, String> managerColName;
    public TableView<ManagerTableParameters> managerTable;
    public TableView<Customer> customerTable;
    public TableColumn<ManagerTableParameters, Void> dummyCol;
    public Tab usersTab;
    private ObservableList<ManagerTableParameters> data = FXCollections.observableArrayList();
    //</editor-fold>

    public Tab ordersTab;
    public Tab warehousesTab;
    @FXML
    public TabPane tabPane;
    private EntityManagerFactory entityManagerFactory;
    //This class has methods for entity manipulation
    private ShopHibernate shopHibernate;
    //I need to know which user is selected
    private User user;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBox with pet types
        getProductTypeMenu().getItems().addAll("Dog", "Cat", "Bird", "Fish", "Reptile", "Small Mammal");

        // Initialize TableView for managers
        managerTable.setEditable(true);
        managerColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        managerColLogin.setCellValueFactory(new PropertyValueFactory<>("login"));

        // Allow editing of manager names
        managerColName.setCellFactory(TextFieldTableCell.forTableColumn());
        managerColName.setOnEditCommit(event -> {
            // Get the manager whose name is being edited
            ManagerTableParameters manager = event.getRowValue();
            // Update the manager's name
            manager.setName(event.getNewValue());
            // Update the manager in the database
            shopHibernate.update(manager);
        });

        managerColName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Add a delete button to each row in the manager table
        Callback<TableColumn<ManagerTableParameters, Void>, TableCell<ManagerTableParameters, Void>> callback = param -> {
            final TableCell<ManagerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    // When delete button is clicked, delete the corresponding manager
                    deleteButton.setOnAction(event -> {
                        ManagerTableParameters row = getTableView().getItems().get(getIndex());
                        shopHibernate.delete(Manager.class, row.getId());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };
        dummyCol.setCellFactory(callback);

        // Initialize TableView for customers
        customerTable.setEditable(true);
        // Add columns for customer details
        TableColumn<Customer, Integer> customerIdCol = new TableColumn<>("Customer ID");
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        // Add other columns as needed
        // Example:
        TableColumn<Customer, String> customerNameCol = new TableColumn<>("Name");
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        // Add columns to the table
        customerTable.getColumns().addAll(customerIdCol, customerNameCol);

        // Here you can add further initialization logic as needed
    }

    public void loadProductData() {
        // Get the selected product from the productAdminList
        Product product = productAdminList.getSelectionModel().getSelectedItem();

        // Check the type of the selected product and update the fields accordingly
        if (product instanceof Food) {
            Food food = (Food) product;
            productTitleField.setText(food.getTitle());
            productDescriptionField.setText(food.getDescription());
            productQuantityField.setText(String.valueOf(food.getQty()));
            productPriceField.setText(String.valueOf(food.getPrice()));
            productManufacturerField.setText(food.getManufacturer());
            productAgeField.setText(String.valueOf(food.getAge()));
            //productExpiryDate.setValue(food.getExpiryDate());
        } else if (product instanceof Toys) {
            Toys toys = (Toys) product;
            productTitleField.setText(toys.getTitle());
            productDescriptionField.setText(toys.getDescription());
            productQuantityField.setText(String.valueOf(toys.getQty()));
            productPriceField.setText(String.valueOf(toys.getPrice()));
            productManufacturerField.setText(toys.getManufacturer());
           // productAgeField.setText(String.valueOf(toys.getAge()));
           // productExpiryDate.setValue(toys.getExpiryDate());
        } else if (product instanceof Medicine) {
            Medicine medicine = (Medicine) product;
            productTitleField.setText(medicine.getTitle());
            productDescriptionField.setText(medicine.getDescription());
            productQuantityField.setText(String.valueOf(medicine.getQty()));
            productPriceField.setText(String.valueOf(medicine.getPrice()));
            productManufacturerField.setText(medicine.getManufacturer());
           // productMedicineTypeField.setText(medicine.getMedicineType());
            productExpiryDate.setValue(medicine.getExpiryDate());
        } else {
            // Handle other product types if needed
        }
    }


    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.shopHibernate = new ShopHibernate(entityManagerFactory);
        this.user = user;
        loadTabData();
        setCustomerView();
    }

    private void setCustomerView() {
        //Customer should not have any access or knowledge about tabs that are intended for Managers/Admins
        if (user instanceof Customer) {
            //You could simply disable tabs, but it is better to not render them
            tabPane.getTabs().remove(usersTab);
//            tabPane.getTabs().remove(productsTab);
            tabPane.getTabs().remove(warehousesTab);
        } else if (!((Manager) user).isAdmin()) {
            //TODO disable fields or functions that simple managers should not be able to access
        }
    }

    //<editor-fold desc="Logic for User Tab">
    private void fillManagerTable() {
        //get all records from the database for Manager TableView
        List<Manager> managers = shopHibernate.getAllRecords(Manager.class);
        for (Manager m : managers) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setName(m.getName());
            //TODO complete remaining columns
            data.add(managerTableParameters);
        }
        managerTable.setItems(data);
    }
    //</editor-fold>

    //<editor-fold desc="Logic for Products Tab">
    //A method that is called once Add button is clicked
    public void createRecord() {
        // Extracting common values from the form fields
        String title = productTitleField.getText();
        String description = productDescriptionField.getText();
        int quantity = Integer.parseInt(productQuantityField.getText());
        float weight = Float.parseFloat(productWeightField.getText());
        String manufacturer = productManufacturerField.getText();
        float price = Float.parseFloat(productPriceField.getText()); // Retrieving price from UI

        // Check if a product type is selected
        if (productFoodRadio.isSelected()) {
            // Extract additional values specific to Food products
            int age = Integer.parseInt(productAgeField.getText()); // Extracting age from form field
            String foodType = (String) productTypeMenu.getValue(); // Extracting food type from ComboBox

            // Create a new Food object
            Food food = new Food(title, description, quantity, price, manufacturer, weight, age, foodType);

            // Save the new Food object to the database
            shopHibernate.create(food);
        } else if (productToyRadio.isSelected()) {
            // Extract additional values specific to Toy products
            String toyType = (String) productTypeMenu.getValue();

            // Create a new Toy object
            Toys toy = new Toys(title, description, quantity, price, manufacturer, toyType);

            // Save the new Toy object to the database
            shopHibernate.create(toy);
        } else if (productMedicineRadio.isSelected()) {
            // Extract additional values specific to Medicine products
            String medicineType = (String) productTypeMenu.getValue();
            LocalDate expiryDate = productExpiryDate.getValue();

            // Create a new Medicine object
            Medicine medicine = new Medicine(title, description, quantity, manufacturer, medicineType, expiryDate);

            // Save the new Medicine object to the database
            shopHibernate.create(medicine);
        }

        // Refresh the productAdminList to display the newly added product
        productAdminList.getItems().clear();
        List<Product> products = shopHibernate.getAllRecords(Product.class);
        List<String> titles = products.stream().map(Product::getTitle).collect(Collectors.toList());
        productAdminList.getItems().addAll((Product) titles);



    }



    public void updateRecord() {
        // Get the selected product from the list
        Product product = productAdminList.getSelectionModel().getSelectedItem();

        // Check if a product is selected
        if (product != null) {
            // Extract values from the form fields
            String title = productTitleField.getText();
            String description = productDescriptionField.getText();

            // Update the product based on its type
            if (product instanceof Food) {
                Food food = (Food) product;
                food.setTitle(title);
                food.setDescription(description);
                // Update the record in the database
                shopHibernate.update(food);
            } else if (product instanceof Toys) {
                Toys toy = (Toys) product;
                toy.setTitle(title);
                toy.setDescription(description);
                // Update the record in the database
                shopHibernate.update(toy);
            } else if (product instanceof Medicine) {
                Medicine medicine = (Medicine) product;
                medicine.setTitle(title);
                medicine.setDescription(description);
                // Update the record in the database
                shopHibernate.update(medicine);
            }

            // Refresh the product list
            productAdminList.refresh();
        }
    }


    //Delete operations are more complicated, because we need to control what stays in the database and what should be removed
    //For this reason generic delete will not work for us, therefore I create custom delete methods
    public void deleteRecord() {
        Product product = productAdminList.getSelectionModel().getSelectedItem();
        shopHibernate.delete(ProductReview.class, product.getId());
        productAdminList.getItems().addAll(shopHibernate.getAllRecords(Product.class));
    }

    public void removeFromCart() {
        Product product = myCartItems.getSelectionModel().getSelectedItem();
        shopProducts.getItems().add(product);
        myCartItems.getItems().remove(product);
    }

    public void addToCart() {
        Product product = shopProducts.getSelectionModel().getSelectedItem();
        myCartItems.getItems().add(product);
        shopProducts.getItems().remove(product);
    }

    public void buyItems() {
        //When the user clicks buy, call a specific method, not generic, because this is more complicated
        shopHibernate.createCart(myCartItems.getItems(), user);
    }
    //</editor-fold>

    public void loadTabData() {
        if (shopTab.isSelected()) {
            shopProducts.getItems().addAll(shopHibernate.loadAvailableProducts());
        } else if (usersTab.isSelected()) {
            fillManagerTable();
            //TODO complete Customer TableView
            //fillCustomerTable();
        }
        //TODO fill only when the tab is clicked
    }

    public void loadProductReviewForm() throws IOException {
        //Get resources: fxml, controller, grapics, styles...
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("product-review.fxml"));
        //Load resources, without this step I cannot access controllers
        Parent parent = fxmlLoader.load();

        ProductReview productReview = fxmlLoader.getController();
        //Forms do not know about each other, therefore I must pass info between them
        productReview.setData(entityManagerFactory, user);
        //Create a completely new window
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        FxUtils.setStageParameters(stage, scene, true);
    }

}