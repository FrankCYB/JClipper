import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JScrollPane;


public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JLabel IPLabel;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JTextArea IPArea;
	private JTextArea PortArea;
	private JTextPane ClipArea;	
	private JLabel PortLabel;	
	private JRadioButton ClientRadioButton;
	private JRadioButton ServerRadioButton;
	private Boolean isServer = true;
	private Boolean isRunning = false;
	private Boolean isClipHOn = true;
	private Boolean isHistoryOpen = false;
	
	private List<Socket> connectedClients = new ArrayList<Socket>();
	public static String[] clipHistory = new String[25];
	public static int clipIndex = 0;
	private Properties prop;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	private JLabel AlertLabel;
	private JButton StartButton;
	private JLabel AlertLabel2;
	private JScrollPane scrollPane;
	
	private String curclipboard = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.getContentPane().setPreferredSize(new Dimension(434, 275));
		            frame.pack();
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
	public MainFrame() {
		setResizable(false);
		setTitle("JClipper - 1.0.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);	
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		
		PortLabel = new JLabel("Port: ");
		PortLabel.setBounds(10, 236, 40, 14);
		contentPane.add(PortLabel);
		
		PortArea = new JTextArea();
		PortArea.setBounds(42, 231, 56, 22);
		contentPane.add(PortArea);
		
		StartButton = new JButton("Start");
		StartButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				start();
		}
		});
		StartButton.setBounds(108, 232, 63, 23);
		contentPane.add(StartButton);
		
		ClientRadioButton = new JRadioButton("Client");
		buttonGroup.add(ClientRadioButton);
		ClientRadioButton.setBounds(80, 7, 70, 23);
		contentPane.add(ClientRadioButton);
		
		ServerRadioButton = new JRadioButton("Server");
		ServerRadioButton.setSelected(true);
		buttonGroup.add(ServerRadioButton);
		ServerRadioButton.setBounds(10, 7, 70, 23);
		contentPane.add(ServerRadioButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 37, 414, 151);
		contentPane.add(scrollPane);
		
		ClipArea = new JTextPane();
		ClipArea.setForeground(Color.LIGHT_GRAY);
		scrollPane.setViewportView(ClipArea);
		ClipArea.setEditable(false);
		ClipArea.setText(" ");
		
		IPLabel = new JLabel("IP:");
		IPLabel.setVisible(false);
		IPLabel.setBounds(10, 211, 22, 14);
		contentPane.add(IPLabel);
		
		IPArea = new JTextArea();
		IPArea.setVisible(false);
		IPArea.setBounds(42, 206, 95, 22);
		contentPane.add(IPArea);
		
		AlertLabel = new JLabel("");
		AlertLabel.setBounds(193, 214, 195, 14);
		contentPane.add(AlertLabel);
		
		AlertLabel2 = new JLabel("");
		AlertLabel2.setBounds(193, 246, 205, 14);
		contentPane.add(AlertLabel2);
		
		JButton ClipHistoryButton = new JButton("Clip History");
		ClipHistoryButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(isHistoryOpen != true) {
					ClipHistoryFrame cHistory = new ClipHistoryFrame();
					cHistory.getContentPane().setPreferredSize(new Dimension(432, 249));
		            cHistory.pack();
					cHistory.setVisible(true);
					isHistoryOpen = true;
					cHistory.addWindowListener(new java.awt.event.WindowAdapter() {
			            @Override
			            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			            	isHistoryOpen = false;
			            }
			        });
					}
			}
		});
		ClipHistoryButton.setBounds(323, 199, 101, 23);
		contentPane.add(ClipHistoryButton);
		
		 ServerRadioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				IPLabel.setVisible(false);
				IPArea.setVisible(false);
				
				isServer = true;
				}
		    });


		    ClientRadioButton.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        IPLabel.setVisible(true); 
		        IPArea.setVisible(true);		    
		        isServer = false;
		        }
		    });
		    
		    
		    final Clipboard SYSTEM_CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();

		    SYSTEM_CLIPBOARD.addFlavorListener(new FlavorListener() {
		        public void flavorsChanged(FlavorEvent e) {
		        	//if(isRunning) {
		            try {		       
		                String plainClipText = (String) SYSTEM_CLIPBOARD.getData(DataFlavor.stringFlavor);

		                SYSTEM_CLIPBOARD.setContents(new StringSelection(plainClipText), null);

		                if(!"".equals(plainClipText) && !plainClipText.equals(curclipboard)) {
			            	curclipboard = plainClipText;

			            	if(isClipHOn) {
			            		clipHistory[clipIndex] = curclipboard;
			            		nextIndex();
			            	}
			            	if(isRunning) {
				            if (isServer) {
				                broadcast(plainClipText);
				                
				            } else {
				                send(plainClipText, clientSocket);
				            }
				            
			            	}
			            	ClipArea.setText(plainClipText);
				            
			            }
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }
		        //}
		        }
		    });
		
		    prop = new Properties();
		    InputStream input = null;
		    
		    try {
				input = new FileInputStream("config.properties");
				prop.load(input);
				if(prop.getProperty("ip") != null && !"".equals(prop.getProperty("ip"))){
					IPArea.setText(prop.getProperty("ip"));
				}
				if(prop.getProperty("port") != null && !"".equals(prop.getProperty("port"))) {
					PortArea.setText(prop.getProperty("port"));
				}
				if(prop.getProperty("isServer") != null && !"".equals(prop.getProperty("isServer"))) {
					if(prop.getProperty("isServer").equals("false")) {
						isServer = false;
						ClientRadioButton.setSelected(true);
						IPLabel.setVisible(true); 
				        IPArea.setVisible(true);
					}
				}
				if(prop.getProperty("isClipHistoryOn") != null && !"".equals(prop.getProperty("isClipHistoryOn"))) {
					if(prop.getProperty("isClipHistoryOn").equals("false")) {
						isClipHOn = false;
					}
				}
				if(prop.getProperty("autostart") != null && !"".equals(prop.getProperty("autostart"))) {
					if(prop.getProperty("autostart").equals("true")) {
						start();
					}
				}
				
			} catch (FileNotFoundException e1) {
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}

	public void start() {
		if(!isRunning) {					
			if(isServer) {					
				try {
					serverSocket = new ServerSocket(Integer.parseInt(PortArea.getText()));
					isRunning = true;
					ClipArea.setForeground(Color.BLACK);
					//ClipArea.setText("");
					
					ClientRadioButton.setEnabled(false);
					ServerRadioButton.setEnabled(false);
					AlertLabel.setText("Active Connections: 0");
					StartButton.setText("Stop");
				} catch (Exception e) {
					return;
				}
				
				Thread thread = new Thread(new Runnable() {
				    public void run() {
				        while (isRunning) {
				            try {					            	
				                clientSocket = serverSocket.accept();
				                if (!connectedClients.contains(clientSocket)) {
				                	System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());					                
					                connectedClients.add(clientSocket);
				                }					                
				                AlertLabel.setText("Active Connections: " + connectedClients.size());
				                Thread thread2 = new Thread(new Runnable() {
								    public void run() {
								    	Socket tempSocket = connectedClients.get(connectedClients.size() - 1);
								    	
							            
								    	InetSocketAddress remoteAddress = (InetSocketAddress) tempSocket.getRemoteSocketAddress();
		        						InetAddress remoteInetAddress = remoteAddress.getAddress();
		                                String tempIP = remoteInetAddress.getHostAddress();
		                                AlertLabel2.setText("Latest Connected: " + tempIP);
		                                if(!"".equals(curclipboard)) {			                                	
							               send(curclipboard, tempSocket);
		                                }
								        while (isRunning) {
								        	try {									        	
					                            ObjectInputStream in = new ObjectInputStream(tempSocket.getInputStream());
					                           
					                                String clipboardData = (String) in.readObject();
					                                System.out.println(clipboardData);
					                                System.out.println("Received clipboard data from client " + tempSocket.getRemoteSocketAddress());
					                                
					                                	ClipArea.setText(clipboardData);
					                                	
						                                StringSelection selection = new StringSelection(clipboardData);
						                                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						                                clipboard.setContents(selection, selection);

						                                if(!"".equals(curclipboard) && !clipboardData.equals(curclipboard)) {
											            	curclipboard = clipboardData;	
											            	if(isClipHOn) {
											            		clipHistory[clipIndex] = curclipboard;
											            		nextIndex();
											            	}
						                                }
						                                
						                                
						                                broadcast(clipboardData, tempSocket);						                               						                                
					                            
					                        } catch (java.io.EOFException e) {
					                        	connectedClients.remove(tempSocket);
					                        	AlertLabel.setText("Active Connections: " + connectedClients.size());			                        
					                            e.printStackTrace();
					                            break;
					                        } catch (Exception e) {
					                        	
					                        }
								        	 try {
													Thread.sleep(50);
												} catch (InterruptedException e) {
											}
								        }
								    }
								});
								thread2.start();
				            } catch (IOException e) {
				                
				            }					                
				                try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
								}					            
				        }
				    }
				});
				thread.start();
													
			}else {
				try {						
					clientSocket = new Socket(IPArea.getText(), Integer.parseInt(PortArea.getText()));
					
					InetSocketAddress remoteAddress = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
					InetAddress remoteInetAddress = remoteAddress.getAddress();
					AlertLabel2.setText("Connected to: " + remoteInetAddress.getHostAddress());
					isRunning = true;
					ClipArea.setForeground(Color.BLACK);
					
					ClientRadioButton.setEnabled(false);
					ServerRadioButton.setEnabled(false);
					StartButton.setText("Stop");
				}catch(Exception e) {
					return;
				}
				Thread thread2 = new Thread(new Runnable() {
				    public void run() {
				        while (isRunning) {
				        	try {
				        		
	                            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

	                                String clipboardData = (String) in.readObject();
	                                System.out.println("Received clipboard data from client " + clientSocket.getRemoteSocketAddress());
	                                System.out.println(clipboardData);
	                           
	                                	ClipArea.setText(clipboardData);
	 		                           
		                                StringSelection selection = new StringSelection(clipboardData);
		                                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		                                clipboard.setContents(selection, selection);
		                                if(!"".equals(clipboardData) && !clipboardData.equals(curclipboard)) {
							            	curclipboard = clipboardData;	
							            	if(isClipHOn) {
							            		clipHistory[clipIndex] = curclipboard;
							            		nextIndex();
							            	}
		                                }
	                                
				        	} catch (Exception exc) {
				        		if(exc instanceof java.io.EOFException || exc instanceof java.net.SocketException && isRunning) {
	                        	AlertLabel2.setText("Connection Lost. Reconnecting...");
	                        	while(isRunning) {
	                        		try {
										clientSocket = new Socket(IPArea.getText(), Integer.parseInt(PortArea.getText()));
										InetSocketAddress remoteAddress = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
										InetAddress remoteInetAddress = remoteAddress.getAddress();
										AlertLabel2.setText("Connected to: " + remoteInetAddress.getHostAddress());
										break;
									} catch (Exception e3) {
										 try {
												Thread.sleep(50);
											} catch (InterruptedException e4) {
										}
									} 
	                        	}
				        		}
	                            exc.printStackTrace();   
	                        }
				        	 try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
							}
				        }
				    }
				});
				thread2.start();
				
			}
		}else {
			if(isServer) {
			for (Socket clientSocket : connectedClients) {
			    try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				connectedClients.clear();
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			AlertLabel.setText("");
			AlertLabel2.setText("");
			//ClipArea.setText("");
			
			isRunning = false;
			ClipArea.setForeground(Color.LIGHT_GRAY);
			
			ClientRadioButton.setEnabled(true);
			ServerRadioButton.setEnabled(true);
			StartButton.setText("Start");				
		}
	}
	
	//Increments index for clipHistory
	public void nextIndex() {
		if(clipIndex == 24) {
			clipIndex = 0;
		}else {
			clipIndex++;
		}
	}
	
	public void clipboardlistner() {
		Thread thread2 = new Thread(new Runnable() {
		    public void run() {
		    }
		});
		thread2.start();
	}
	
	public void send(String data, Socket s) {
		// Send data to a client
		try {
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void broadcast(String data) {
        // Send data to all connected clients
        for (Socket clientS : connectedClients) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(clientS.getOutputStream());
                out.writeObject(data);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	public void broadcast(String data, Socket excludedsocket) {
        // Send data to all connected clients
        for (Socket clientS : connectedClients) {
        	if(!clientS.equals(excludedsocket)) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(clientS.getOutputStream());
                out.writeObject(data);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
          }
       }
    }
}
