package org.Backend;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Swing_UI extends JFrame {
    private JComboBox<String> restaurantComboBox;
    private JTextField dinersCountTextField;
    private JTextField nameTextField;
    private JButton checkButton;
    private JButton reserveButton;
    private JButton searchAlternativeButton;
    private JButton availabilityButton;
    private JTextArea restaurantTableTextArea;
    private JTextArea suggestedTablesTextArea;
    private JLabel noTablesAvailableLabel;
    private JPanel mainPanel;
    private ResourceBundle messages;
    private Chain gourmetChain;
    private Restaurant italianBistro, sushiPalace, steakHouse;

    public Swing_UI() {
        // Load resource bundle
        try {
            messages = ResourceBundle.getBundle("messages", Locale.US);
        } catch (MissingResourceException e) {
            System.err.println("Warning: Resource bundle 'messages' not found. Using default.");
            messages = ResourceBundle.getBundle("messages");
        }

        initializeRestaurants();
        setupUI();
        populateRestaurantComboBox();
        addActionListeners();
    }

    private void initializeRestaurants() {
        gourmetChain = new Chain("Gourmet Delights", 3);
        italianBistro = new Restaurant("Italian Bistro", 5);
        sushiPalace = new Restaurant("Sushi Palace", 4);
        steakHouse = new Restaurant("Steak House", 6);

        gourmetChain.addRestaurant(italianBistro);
        gourmetChain.addRestaurant(sushiPalace);
        gourmetChain.addRestaurant(steakHouse);
    }

    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new GridLayout(6, 2, 5, 5)); // 6 rows for additional button
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Initialize components
        restaurantComboBox = new JComboBox<>();
        dinersCountTextField = new JTextField();
        nameTextField = new JTextField();
        checkButton = new JButton(messages.getString("check.button"));
        reserveButton = new JButton(messages.getString("reserve.button"));
        searchAlternativeButton = new JButton(messages.getString("search.alternative.button"));
        availabilityButton = new JButton(messages.getString("availability.button"));
        noTablesAvailableLabel = new JLabel(messages.getString("no.tables.available"));
        noTablesAvailableLabel.setForeground(Color.RED);
        noTablesAvailableLabel.setVisible(false);

        restaurantTableTextArea = new JTextArea(10, 20);
        restaurantTableTextArea.setEditable(false);
        suggestedTablesTextArea = new JTextArea(10, 20);
        suggestedTablesTextArea.setEditable(false);

        // Add components to top panel
        topPanel.add(new JLabel(messages.getString("restaurant.label")));
        topPanel.add(restaurantComboBox);
        topPanel.add(new JLabel(messages.getString("diners.label")));
        topPanel.add(dinersCountTextField);
        topPanel.add(new JLabel(messages.getString("name.label")));
        topPanel.add(nameTextField);
        topPanel.add(checkButton);
        topPanel.add(reserveButton);
        topPanel.add(searchAlternativeButton);
        topPanel.add(noTablesAvailableLabel);
        topPanel.add(availabilityButton);
        topPanel.add(new JLabel()); // Empty label for grid alignment

        // Add text areas with scroll panes to bottom panel
        bottomPanel.add(new JScrollPane(restaurantTableTextArea));
        bottomPanel.add(new JScrollPane(suggestedTablesTextArea));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        // Configure JFrame
        setTitle(messages.getString("window.title"));
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void populateRestaurantComboBox() {
        restaurantComboBox.addItem("Italian Bistro");
        restaurantComboBox.addItem("Sushi Palace");
        restaurantComboBox.addItem("Steak House");
    }

    private void addActionListeners() {
        restaurantComboBox.addActionListener(e -> updateRestaurantTableDisplay());
        checkButton.addActionListener(e -> checkTableAvailability());
        reserveButton.addActionListener(e -> makeReservation());
        searchAlternativeButton.addActionListener(e -> searchAlternativeRestaurant());
        availabilityButton.addActionListener(e -> getAvailabilityAcrossRestaurantsfront());
    }

    private void getAvailabilityAcrossRestaurantsfront() {
        try {
            String selected = (String) restaurantComboBox.getSelectedItem();
            int diners = Integer.parseInt(dinersCountTextField.getText());
            JOptionPane.showMessageDialog(this, gourmetChain.getAvailabilityAcrossRestaurants(diners));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, messages.getString("invalid.diners"));
        }
    }

    private void updateRestaurantTableDisplay() {
        String selected = (String) restaurantComboBox.getSelectedItem();
        Restaurant restaurant = gourmetChain.getRestaurant(selected);
        if (restaurant != null) {
            restaurantTableTextArea.setText(restaurant.toString());
        }
    }

    private void checkTableAvailability() {
        try {
            String selected = (String) restaurantComboBox.getSelectedItem();
            int diners = Integer.parseInt(dinersCountTextField.getText());
            Restaurant restaurant = gourmetChain.getRestaurant(selected);

            if (restaurant != null && restaurant.hasAvailableTables(diners)) {
                suggestedTablesTextArea.setText(restaurant.availableTablesInfo(diners));
                reserveButton.setEnabled(true);
                noTablesAvailableLabel.setVisible(false);
            } else {
                suggestedTablesTextArea.setText("");
                reserveButton.setEnabled(false);
                noTablesAvailableLabel.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, messages.getString("invalid.diners"));
        }
    }

    private void makeReservation() {
        try {
            String selected = (String) restaurantComboBox.getSelectedItem();
            int diners = Integer.parseInt(dinersCountTextField.getText());
            String name = nameTextField.getText();

            boolean success = gourmetChain.reserveRestaurant(diners, selected, name);
            String message;

            if (success) {
                message = MessageFormat.format(messages.getString("reservation.success"), name);
            } else {
                message = messages.getString("reservation.failure");
            }

            JOptionPane.showMessageDialog(this, message);

            if (success) {
                updateRestaurantTableDisplay();
                resetForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, messages.getString("invalid.number"));
        } catch (MissingResourceException e) {
            JOptionPane.showMessageDialog(this, "Reservation completed!");
        }
    }

    private void searchAlternativeRestaurant() {
        try {
            String selected = (String) restaurantComboBox.getSelectedItem();
            int diners = Integer.parseInt(dinersCountTextField.getText());

            Restaurant altRestaurant = gourmetChain.searchRestaurant(diners, selected);
            if (altRestaurant != null) {
                int choice = JOptionPane.showConfirmDialog(this,
                        MessageFormat.format(messages.getString("alternative.found"), altRestaurant.getName()),
                        messages.getString("alternative.title"), JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    restaurantComboBox.setSelectedItem(altRestaurant.getName());
                    checkTableAvailability();
                }
            } else {
                JOptionPane.showMessageDialog(this, messages.getString("no.alternative"));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, messages.getString("invalid.diners"));
        }
    }

    private void resetForm() {
        dinersCountTextField.setText("");
        nameTextField.setText("");
        reserveButton.setEnabled(false);
        suggestedTablesTextArea.setText("");
        noTablesAvailableLabel.setVisible(false);
        restaurantTableTextArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Swing_UI::new);
    }
}