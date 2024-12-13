package com.sample.quiz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class test extends JFrame {

	private JPanel paneRegistrasi;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public test() {
		setTitle("Registrasi");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 655, 339);
		paneRegistrasi = new JPanel();
		paneRegistrasi.setToolTipText("");
		paneRegistrasi.setForeground(new Color(199, 21, 133));
		paneRegistrasi.setBackground(new Color(192, 192, 192));
		paneRegistrasi.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(paneRegistrasi);
		paneRegistrasi.setLayout(new GridLayout(3, 1, 20, 15));
		
		JButton btnconnection = new JButton("Test Connection");
		btnconnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnconnection.setFont(new Font("Tahoma", Font.BOLD, 16));
		paneRegistrasi.add(btnconnection);
		
		JButton btninsert = new JButton("Insert");
		btninsert.setFont(new Font("Tahoma", Font.BOLD, 16));
		paneRegistrasi.add(btninsert);
		
		JButton btnview = new JButton("View");
		btnview.setFont(new Font("Tahoma", Font.BOLD, 16));
		paneRegistrasi.add(btnview);
	}

}
