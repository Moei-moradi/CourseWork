package com.example.coursework.fxControllers;

import com.example.coursework.HelloApplication;
import com.example.coursework.hibernate.ShopHibernate;
import com.example.coursework.model.Customer;
import com.example.coursework.model.Manager;
import com.example.coursework.model.User;
import com.example.coursework.model.Warehouse;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public RadioButton customerCheckbox;
    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton managerCheckbox;
    @FXML
    public TextField addressField;
    @FXML
    public TextField cardNoField;
    @FXML
    public DatePicker birthDateField;
    @FXML
    public TextField employeeIdField;
    @FXML
    public TextField medCertificateField;
    @FXML
    public DatePicker employmentDateField;
    @FXML
    public CheckBox isAdminCheck;
    @FXML
    public TextField billingAddressField;

    private EntityManagerFactory entityManagerFactory;

    public void setData(EntityManagerFactory entityManagerFactory, boolean showManagerFields) {
        this.entityManagerFactory = entityManagerFactory;
        disableFields(showManagerFields);
    }

    public void setData(EntityManagerFactory entityManagerFactory, Manager manager) {
        this.entityManagerFactory = entityManagerFactory;
        toggleFields(customerCheckbox.isSelected(), manager);
    }

    private void disableFields(boolean showManagerFields) {
        if (!showManagerFields) {
            employeeIdField.setVisible(false);
            medCertificateField.setVisible(false);
            employmentDateField.setVisible(false);
            isAdminCheck.setVisible(false);
            managerCheckbox.setVisible(false);
        }
    }

    private void toggleFields(boolean isManager, Manager manager) {
        if (isManager) {
            addressField.setDisable(true);
            cardNoField.setDisable(true);
            employeeIdField.setDisable(false);
            medCertificateField.setDisable(false);
            employmentDateField.setDisable(false);
            if (manager.isAdmin()) isAdminCheck.setDisable(false);
        } else {
            addressField.setDisable(false);
            cardNoField.setDisable(false);
            employeeIdField.setDisable(true);
            medCertificateField.setDisable(true);
            employmentDateField.setDisable(true);
            isAdminCheck.setDisable(true);
        }
    }


    public void createUser() {
        ShopHibernate shopHibernate = new ShopHibernate(entityManagerFactory);
        if (customerCheckbox.isSelected()) {
            User user = new Customer(nameField.getText(), surnameField.getText(), loginField.getText(), passwordField.getText(), cardNoField.getText(), addressField.getText(), billingAddressField.getText(), birthDateField.getValue());
            shopHibernate.create(user);
        } else {
            boolean isAdmin = isAdminCheck.isSelected(); // Check if the user should be created as an admin
            Manager manager = new Manager(nameField.getText(), surnameField.getText(), loginField.getText(), passwordField.getText(), isAdmin);
            if (isAdmin) {
                // Assign a warehouse to the admin
                // For example, you might retrieve a warehouse from the database or create a new one
                Warehouse warehouse = new Warehouse(); // Create a new warehouse
                manager.setWarehouse(warehouse); // Assign the warehouse to the manager
            }
            shopHibernate.create(manager);
        }
    }



    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage) loginField.getScene().getWindow();
        Scene scene = new Scene(parent);
        FxUtils.setStageParameters(stage, scene, false);
    }
}
