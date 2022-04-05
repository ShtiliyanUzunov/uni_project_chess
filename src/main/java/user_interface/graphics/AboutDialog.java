package user_interface.graphics;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class AboutDialog extends JDialog {

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setBounds(100, 100, 272, 148);
		setTitle("About");
		setLocation(600, 300);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
	    setModal(true);
		contentPanel.setLayout(null);
		
		JLabel about = new JLabel("<html>This is my very first project :) </br>3 months after i started to studying programming.</html>");
		about.setBounds(10,11,236,40);
		contentPanel.add(about);

		setResizable(false);
		setVisible(true);
	}
}
