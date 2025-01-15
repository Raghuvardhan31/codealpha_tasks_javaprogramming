import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.* ;


public class HotelReservationSystem {

    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTable searchTable, reservationTable, manageTable;
    private DefaultTableModel searchTableModel, reservationTableModel, manageTableModel;
    private JComboBox<String> categoryComboBox;

    private Connection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new HotelReservationSystem().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HotelReservationSystem() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "123456");
            initializeDatabase();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initialize() {
        frame = new JFrame("Hotel Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);

        createSearchTab();
        createReservationsTab();
        createManageTab();

        frame.setVisible(true);
    }

    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "room_number VARCHAR(10) UNIQUE, " +
                    "category VARCHAR(20), " +
                    "price_per_night DOUBLE, " +
                    "is_available TINYINT DEFAULT 1)");

            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "customer_name VARCHAR(50), " +
                    "check_in_date DATE, " +
                    "check_out_date DATE, " +
                    "room_id INT, " +
                    "total_price DOUBLE, " +
                    "payment_status VARCHAR(20), " +
                    "FOREIGN KEY (room_id) REFERENCES rooms (id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSearchTab() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        tabbedPane.add("Search Rooms", searchPanel);

        JPanel topPanel = new JPanel();
        searchPanel.add(topPanel, BorderLayout.NORTH);

        topPanel.add(new JLabel("Room Category:"));
        categoryComboBox = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        topPanel.add(categoryComboBox);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRooms());
        topPanel.add(searchButton);

        searchTableModel = new DefaultTableModel(new String[]{"Room Number", "Category", "Price/Night"}, 0);
        searchTable = new JTable(searchTableModel);
        searchPanel.add(new JScrollPane(searchTable), BorderLayout.CENTER);

        JButton bookButton = new JButton("Book Room");
        bookButton.addActionListener(e -> bookRoom());
        searchPanel.add(bookButton, BorderLayout.SOUTH);
    }

    private void createReservationsTab() {
        JPanel reservationPanel = new JPanel(new BorderLayout());
        tabbedPane.add("Reservations", reservationPanel);

        reservationTableModel = new DefaultTableModel(new String[]{"Customer Name", "Room", "Check-In", "Check-Out", "Total Price", "Payment Status"}, 0);
        reservationTable = new JTable(reservationTableModel);
        reservationPanel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadReservations());
        reservationPanel.add(refreshButton, BorderLayout.SOUTH);
    }

    private void createManageTab() {
        JPanel managePanel = new JPanel(new BorderLayout());
        tabbedPane.add("Manage Rooms", managePanel);

        manageTableModel = new DefaultTableModel(new String[]{"Room Number", "Category", "Price/Night", "Availability"}, 0);
        manageTable = new JTable(manageTableModel);
        managePanel.add(new JScrollPane(manageTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addRoomButton = new JButton("Add Room");
        addRoomButton.addActionListener(e -> addRoom());
        buttonPanel.add(addRoomButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadRooms());
        buttonPanel.add(refreshButton);

        managePanel.add(buttonPanel, BorderLayout.NORTH);
    }

    private void searchRooms() {
        String category = (String) categoryComboBox.getSelectedItem();
        if (category == null) {
            JOptionPane.showMessageDialog(frame, "Please select a room category.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement stmt = connection.prepareStatement("SELECT room_number, category, price_per_night FROM rooms WHERE category = ? AND is_available = 1")) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            searchTableModel.setRowCount(0);
            while (rs.next()) {
                searchTableModel.addRow(new Object[]{rs.getString("room_number"), rs.getString("category"), rs.getDouble("price_per_night")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bookRoom() {
        int selectedRow = searchTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a room to book.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String roomNumber = (String) searchTableModel.getValueAt(selectedRow, 0);
        String customerName = JOptionPane.showInputDialog(frame, "Enter your name:");
        String checkInDate = JOptionPane.showInputDialog(frame, "Enter check-in date (YYYY-MM-DD):");
        String checkOutDate = JOptionPane.showInputDialog(frame, "Enter check-out date (YYYY-MM-DD):");

        if (customerName == null || checkInDate == null || checkOutDate == null) {
            JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Date checkIn = new SimpleDateFormat("yyyy-MM-dd").parse(checkInDate);
            Date checkOut = new SimpleDateFormat("yyyy-MM-dd").parse(checkOutDate);
            if (!checkOut.after(checkIn)) {
                throw new ParseException("Check-out date must be after check-in date.", 0);
            }

            long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);

            try (PreparedStatement stmt = connection.prepareStatement("SELECT id, price_per_night FROM rooms WHERE room_number = ?")) {
                stmt.setString(1, roomNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int roomId = rs.getInt("id");
                    double pricePerNight = rs.getDouble("price_per_night");
                    double totalPrice = nights * pricePerNight;

                    try (PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO reservations (customer_name, check_in_date, check_out_date, room_id, total_price, payment_status) VALUES (?, ?, ?, ?, ?, ?)")) {

                        insertStmt.setString(1, customerName);
                        insertStmt.setString(2, checkInDate);
                        insertStmt.setString(3, checkOutDate);
                        insertStmt.setInt(4, roomId);
                        insertStmt.setDouble(5, totalPrice);
                        insertStmt.setString(6, "Pending");
                        insertStmt.executeUpdate();

                        try (PreparedStatement updateStmt = connection.prepareStatement("UPDATE rooms SET is_available = 0 WHERE id = ?")) {
                            updateStmt.setInt(1, roomId);
                            updateStmt.executeUpdate();
                        }

                        JOptionPane.showMessageDialog(frame, "Room booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        searchRooms();
                    }
                }
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error booking room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReservations() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT r.customer_name, rm.room_number, r.check_in_date, r.check_out_date, r.total_price, r.payment_status " +
                    "FROM reservations r JOIN rooms rm ON r.room_id = rm.id");

            reservationTableModel.setRowCount(0);
            while (rs.next()) {
                reservationTableModel.addRow(new Object[]{rs.getString("customer_name"), rs.getString("room_number"), rs.getString("check_in_date"), rs.getString("check_out_date"), rs.getDouble("total_price"), rs.getString("payment_status")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRoom() {
        String roomNumber = JOptionPane.showInputDialog(frame, "Enter room number:");
        String category = JOptionPane.showInputDialog(frame, "Enter room category (Single, Double, Suite):");
        String priceInput = JOptionPane.showInputDialog(frame, "Enter price per night:");

        if (roomNumber == null || category == null || priceInput == null) {
            JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceInput);

            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO rooms (room_number, category, price_per_night) VALUES (?, ?, ?)")) {
                stmt.setString(1, roomNumber);
                stmt.setString(2, category);
                stmt.setDouble(3, price);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Room added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadRooms();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRooms() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT room_number, category, price_per_night, is_available FROM rooms");

            manageTableModel.setRowCount(0);
            while (rs.next()) {
                String availability = rs.getInt("is_available") == 1 ? "Yes" : "No";
                manageTableModel.addRow(new Object[]{rs.getString("room_number"), rs.getString("category"), rs.getDouble("price_per_night"), availability});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
