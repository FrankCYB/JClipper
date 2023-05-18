import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClipHistoryFrame extends JFrame {

	private JPanel contentPane;
	private JLabel IndexLabel;
	private JTextPane HistoryArea;	
	private int HistoryIndex;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClipHistoryFrame frame = new ClipHistoryFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ClipHistoryFrame() {
		setResizable(false);
		setTitle("Clip History");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setBounds(100, 100, 450, 279);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		HistoryArea = new JTextPane();
		HistoryArea.setEditable(false);
		HistoryArea.setBounds(10, 42, 412, 149);
		contentPane.add(HistoryArea);
		
		JButton BackButton = new JButton("<==");
		BackButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(HistoryIndex == 0) {
					HistoryIndex = 24;
				}else {
					HistoryIndex--;
				}
				HistoryArea.setText(MainFrame.clipHistory[HistoryIndex]);
				IndexLabel.setText("Index: " + (HistoryIndex));
			}
		});
		BackButton.setBounds(10, 8, 66, 23);
		contentPane.add(BackButton);
		
		JButton CURButton = new JButton("CUR");
		CURButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				HistoryIndex = MainFrame.clipIndex - 1;
				HistoryArea.setText(MainFrame.clipHistory[HistoryIndex]);
				IndexLabel.setText("Index: " + (HistoryIndex));
			}
		});
		CURButton.setBounds(86, 8, 66, 23);
		contentPane.add(CURButton);
		
		JButton FowardButton = new JButton("==>");
		FowardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(HistoryIndex == 24) {
					HistoryIndex = 0;
				}else {
					HistoryIndex++;
				}
				HistoryArea.setText(MainFrame.clipHistory[HistoryIndex]);
				IndexLabel.setText("Index: " + (HistoryIndex));
			}
		});
		FowardButton.setBounds(162, 8, 66, 23);
		contentPane.add(FowardButton);
		
		JButton CopyButton = new JButton("Copy Locally");
		CopyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(new StringSelection(MainFrame.clipHistory[HistoryIndex]), null);
			}
		});
		CopyButton.setBounds(315, 206, 107, 23);
		contentPane.add(CopyButton);
		
		IndexLabel = new JLabel("Index: 0");
		IndexLabel.setBounds(367, 15, 55, 14);
		contentPane.add(IndexLabel);
		
		if(MainFrame.clipIndex != 0) {
		HistoryIndex = MainFrame.clipIndex - 1;
		}
		HistoryArea.setText(MainFrame.clipHistory[HistoryIndex]);
		IndexLabel.setText("Index: " + HistoryIndex);
		
		JButton SaveButton = new JButton("Save To File");
		SaveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				File defaultDir = new File(".");
			      JFileChooser fileChooser = new JFileChooser(defaultDir);
			      fileChooser.setDialogTitle("Save chat");

			      int result = fileChooser.showSaveDialog(null);

			      if (result == JFileChooser.APPROVE_OPTION) {

			         File selectedFile = fileChooser.getSelectedFile();

			         try {
			        	 
			            FileWriter writer = new FileWriter(selectedFile);		            
			            String plaintext = MainFrame.clipHistory[HistoryIndex];			            			 
			            writer.write(plaintext);			 
			            writer.close();		 
			            JOptionPane.showMessageDialog(null, "Clipboard saved successfully.");
			            
			         } catch (IOException e1) {			        	 
			            e1.printStackTrace();
			            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			         }
			      }
			}
		});
		SaveButton.setBounds(10, 206, 107, 23);
		contentPane.add(SaveButton);
	}
}
