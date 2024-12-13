package com.sample.quiz.crud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sample.quiz.connections.ConnectionDB;

public class ProductDao {

    private String queryInsert = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
    private String querySelectAll = "SELECT * FROM products";
    private String queryUpdate = "UPDATE products SET name = ?, price = ?, stock = ? WHERE id = ?";
    private String queryRemoveById = "DELETE FROM products WHERE id = ?";
    private String queryFindById = "SELECT * FROM products WHERE id = ?";

    /**
     * Menyimpan produk ke database.
     */
    public void saved(Products product) throws Exception {
        Connection c = new ConnectionDB().connect();
        PreparedStatement ps = c.prepareStatement(queryInsert);
        ps.setString(1, product.getName());
        ps.setDouble(2, product.getPrice());
        ps.setInt(3, product.getStock());
        ps.executeUpdate();
        c.close();
    }

    /**
     * Mengambil semua produk dari database.
     */
    public List<Products> findAll() throws Exception {
        Connection c = new ConnectionDB().connect();
        PreparedStatement ps = c.prepareStatement(querySelectAll);
        ResultSet rs = ps.executeQuery();

        List<Products> products = new ArrayList<>();
        while (rs.next()) {
            Products product = new Products();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setStock(rs.getInt("stock"));
            products.add(product);
        }
        c.close();
        return products;
    }

    /**
     * Menghapus produk berdasarkan ID.
     */
    public void removeById(int id) throws Exception {
        Connection c = new ConnectionDB().connect();
        PreparedStatement ps = c.prepareStatement(queryRemoveById);
        ps.setInt(1, id);
        ps.executeUpdate();
        c.close();
    }

    /**
     * Memperbarui produk berdasarkan ID.
     */
    public void update(Products product) throws Exception {
        Connection c = new ConnectionDB().connect();
        PreparedStatement ps = c.prepareStatement(queryUpdate);
        ps.setString(1, product.getName());
        ps.setDouble(2, product.getPrice());
        ps.setInt(3, product.getStock());
        ps.setInt(4, product.getId());
        ps.executeUpdate();
        c.close();
    }

    /**
     * Mencari produk berdasarkan ID.
     */
    public Products findById(int id) throws Exception {
        Connection c = new ConnectionDB().connect();
        PreparedStatement ps = c.prepareStatement(queryFindById);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Products product = new Products();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setStock(rs.getInt("stock"));
            c.close();
            return product;
        }
        c.close();
        return null; // Jika produk tidak ditemukan
    }
}
