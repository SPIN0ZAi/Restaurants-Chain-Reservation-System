import org.Backend.Chain;
import org.Backend.Restaurant;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BookEditor extends JFrame {
    private JComboBox<String> comboBox1;
    private JTextField textField3;
    private JButton checkButton;
    private JButton bookButton;
    private JButton searchAlternativeButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JPanel contentPane;
    private JTextField textField1;
    private JLabel nomoreoptions;
    private JLabel tablesbooked; // Unused in current logic
    private JLabel noTablesAvailableLabel;
    private JButton AvailabilityButton;     // New button for exam
    private ResourceBundle messages;
    private Chain gourmetChain;
    private Restaurant italianBistro, sushiPalace, steakHouse;

    public BookEditor() {
        // Load resource bundle
        try {
            messages = ResourceBundle.getBundle("messages", Locale.US);
        } catch (MissingResourceException e) {
            System.err.println("Warning: Resource bundle 'messages' not found. Using default.");
            messages = ResourceBundle.getBundle("messages");
        }

        initializeRestaurants();
        configureComponents();
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

    private void configureComponents() {
        // Set text areas to non-editable
        textArea1.setEditable(false);
        textArea2.setEditable(false);

        // Initialize visibility for labels
        nomoreoptions.setVisible(false);

        // Configure the content pane
        setContentPane(contentPane);
        setTitle("Restaurant Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void populateRestaurantComboBox() {
        comboBox1.addItem("Italian Bistro");
        comboBox1.addItem("Sushi Palace");
        comboBox1.addItem("Steak House");
    }



    private void addActionListeners() {
        comboBox1.addActionListener(e -> updateRestaurantTableDisplay());
        checkButton.addActionListener(e -> checkTableAvailability());
        bookButton.addActionListener(e -> makeReservation());
        searchAlternativeButton.addActionListener(e -> searchAlternativeRestaurant());
        AvailabilityButton.addActionListener(e-> getAvailabilityAcrossRestaurantsfront()); // for the exam --- setting the action listener to the
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void getAvailabilityAcrossRestaurantsfront(){  /////// new for the exam
        try {
            String selected = (String) comboBox1.getSelectedItem();
            int diners = Integer.parseInt(textField3.getText()); // taking the number of diners
            JOptionPane.showMessageDialog(this, gourmetChain.getAvailabilityAcrossRestaurants(diners)); // calling the getAvailability from chain class
        }catch (NumberFormatException ex) { // trying to avoid any errors
            JOptionPane.showMessageDialog(this, "Please enter a valid number of diners.");
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void updateRestaurantTableDisplay() {
        String selected = (String) comboBox1.getSelectedItem();
        Restaurant restaurant = gourmetChain.getRestaurant(selected);
        if (restaurant != null) {
            textArea1.setText(restaurant.toString());
        }
    }

    private void checkTableAvailability() {
        try {
            String selected = (String) comboBox1.getSelectedItem();
            int diners = Integer.parseInt(textField3.getText());
            Restaurant restaurant = gourmetChain.getRestaurant(selected);

            if (restaurant != null && restaurant.hasAvailableTables(diners)) {
                textArea2.setText(restaurant.availableTablesInfo(diners));
                bookButton.setEnabled(true);
                noTablesAvailableLabel.setVisible(false);
            } else {
                textArea2.setText("");
                bookButton.setEnabled(false);
                noTablesAvailableLabel.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of diners.");
        }
    }

    private void makeReservation() {
        try {
            String selected = (String) comboBox1.getSelectedItem();
            int diners = Integer.parseInt(textField3.getText());
            String name = textField1.getText();

            boolean success = gourmetChain.reserveRestaurant(diners, selected, name);
            String message;

            if (success) {
                // Format success message with name
                message = MessageFormat.format(messages.getString("reservation.success"), name);
            } else {
                message = messages.getString("reservation.failure");
            }

            JOptionPane.showMessageDialog(this, message);

            if (success) {
                updateRestaurantTableDisplay();
                resetForm();
                // Force UI refresh
                contentPane.revalidate();
                contentPane.repaint();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format");
        } catch (MissingResourceException e) {
            // Fallback message if resource is missing
            JOptionPane.showMessageDialog(this, "Reservation completed!");
        }
    }



    private void searchAlternativeRestaurant() {
        try {
            String selected = (String) comboBox1.getSelectedItem();
            int diners = Integer.parseInt(textField3.getText());

            Restaurant altRestaurant = gourmetChain.searchRestaurant(diners, selected);
            if (altRestaurant != null) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Alternative found: " + altRestaurant.getName() + ". Reserve here?",
                        "Alternative Available", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    comboBox1.setSelectedItem(altRestaurant.getName());
                    checkTableAvailability();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No alternative restaurants available.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of diners.");
        }
    }

    private void resetForm() {
        textField3.setText("");
        textField1.setText("");
        bookButton.setEnabled(false);
        textArea2.setText("");
        nomoreoptions.setVisible(false);
        comboBox1.updateUI();
        textArea1.setText("");
        noTablesAvailableLabel.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookEditor());
    }
}