package com.sample.quiz.MainGUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.sample.quiz.crud.dao.ProductDao;
import com.sample.quiz.crud.dao.Products;

public class MainApp extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainApp frame = new MainApp();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 500); // Perbesar lebar frame
        setTitle("Product Management App");

        // Warna background utama
        contentPane = new JPanel();
        contentPane.setBorder(new LineBorder(new Color(70, 130, 180), 3)); // Dark blue border
        contentPane.setLayout(null); // Absolute layout
        setContentPane(contentPane);

        // Tambahkan JScrollPane dengan tabel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(450, 20, 720, 420); // Ukuran tabel, pindah ke kanan
        scrollPane.setBorder(new LineBorder(new Color(100, 149, 237), 2)); // Border biru
        contentPane.add(scrollPane);

        // Inisialisasi tabel dengan model
        table = new JTable();
        tableModel = new DefaultTableModel(
            new Object[][] {}, // Data tabel
            new String[] { "ID", "Name", "Price", "Stock" } // Header kolom
        );
        table.setModel(tableModel);
        table.setBackground(new Color(245, 245, 245)); // Light Gray
        table.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Ukuran font tabel
        table.setRowHeight(25);
        scrollPane.setViewportView(table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    // Ambil data dari baris yang dipilih
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    String priceString = (String) tableModel.getValueAt(selectedRow, 2);
                    priceField.setText(priceString.replaceAll("[^\\d.]", "")); // Menghapus format mata uang
                    stockField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
                }
            }
        });

        // Tambahkan label dan input untuk produk
        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setBounds(20, 10, 400, 30);
        contentPane.add(titleLabel);

        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setBounds(20, 70, 100, 20);
        contentPane.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 70, 250, 25);
        contentPane.add(nameField);
        nameField.setColumns(10);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setBounds(20, 110, 100, 20);
        contentPane.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(130, 110, 250, 25);
        contentPane.add(priceField);
        priceField.setColumns(10);

        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        stockLabel.setBounds(20, 150, 100, 20);
        contentPane.add(stockLabel);

        stockField = new JTextField();
        stockField.setBounds(130, 150, 250, 25);
        contentPane.add(stockField);
        stockField.setColumns(10);

        // Tambahkan tombol untuk menyimpan produk
        JButton addButton = new JButton("Add Product");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.setBackground(new Color(50, 205, 50)); // Green background
        addButton.setForeground(Color.WHITE); // White text
        addButton.setBounds(130, 200, 150, 30);
        contentPane.add(addButton);

        addButton.addActionListener(e -> {
            try {
                addProductToDatabase();
                JOptionPane.showMessageDialog(null, "Product added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to add product: " + ex.getMessage());
            }
        });

        // Tombol Delete
        JButton deleteButton = new JButton("Delete Product");
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteButton.setBackground(new Color(220, 20, 60)); // Red background
        deleteButton.setForeground(Color.WHITE); // White text
        deleteButton.setBounds(130, 250, 150, 30);
        contentPane.add(deleteButton);

        deleteButton.addActionListener(e -> {
            deleteSelectedProduct();
        });

        // Tombol Update
        JButton updateButton = new JButton("Update Product");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateButton.setBackground(new Color(30, 144, 255)); // Blue background
        updateButton.setForeground(Color.WHITE); // White text
        updateButton.setBounds(130, 300, 150, 30);
        contentPane.add(updateButton);

        updateButton.addActionListener(e -> {
            updateSelectedProduct();
        });

        // Load data produk dari database ke tabel
        loadProductDataFromDatabase();
    }

    private void addProductToDatabase() throws Exception {
        String name = nameField.getText();
        String priceText = priceField.getText();
        String stockText = stockField.getText();

        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            throw new Exception("All fields are required!");
        }

        double price = Double.parseDouble(priceText);
        int stock = Integer.parseInt(stockText);

        Products product = new Products();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);

        ProductDao dao = new ProductDao();
        dao.saved(product);

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        tableModel.addRow(new Object[] {
            tableModel.getRowCount() + 1,
            product.getName(),
            currencyFormatter.format(product.getPrice()),
            product.getStock()
        });

        clearInputFields();
    }

    private void deleteSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                ProductDao dao = new ProductDao();
                dao.removeById(id);

                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(null, "Product deleted successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to delete product: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No row selected!");
        }
    }

    private void updateSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                Products product = new Products();
                product.setId(id);
                product.setName(name);
                product.setPrice(price);
                product.setStock(stock);

                ProductDao dao = new ProductDao();
                dao.update(product);

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                tableModel.setValueAt(name, selectedRow, 1);
                tableModel.setValueAt(currencyFormatter.format(price), selectedRow, 2);
                tableModel.setValueAt(stock, selectedRow, 3);

                clearInputFields();
                JOptionPane.showMessageDialog(null, "Product updated successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to update product: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No row selected!");
        }
    }

    private void loadProductDataFromDatabase() {
        try {
            ProductDao dao = new ProductDao();
            List<Products> products = dao.findAll();

            tableModel.setRowCount(0);
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            for (Products product : products) {
                tableModel.addRow(new Object[] {
                    product.getId(),
                    product.getName(),
                    currencyFormatter.format(product.getPrice()),
                    product.getStock()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to load products: " + e.getMessage());
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
    }
}
