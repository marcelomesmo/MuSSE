/*
	MuSSE - Multi-sized Sprite Sheet meta-data Exporter
	Copyright (C) 2015-2016 Marcelo de Barros <marcelo.barbosa@ifrn.edu.br>

	This software is provided 'as-is', without any express or implied
	warranty.  In no event will the authors be held liable for any damages
	arising from the use of this software.

	Permission is granted to anyone to use this software for any purpose,
	including commercial applications, and to alter it and redistribute it
	freely, subject to the following restrictions:

	1. The origin of this software must not be misrepresented; you must not
	claim that you wrote the original software. If you use this software
	in a product, an acknowledgment in the product documentation would be
	appreciated but is not required.
	2. Altered source versions must be plainly marked as such, and must not be
	misrepresented as being the original software.
	3. This notice may not be removed or altered from any source distribution.
*/
package ui

import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent

import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.JFormattedTextField
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSpinner
import javax.swing.KeyStroke
import javax.swing.SpinnerNumberModel
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.text.NumberFormatter

/*
 * "MuSSE is a tool developed to extract XML data from sprite sheet images with non-uniform – multi-sized – sprites.
 * MuSSE (Multi-sized Sprite Sheet meta-data Exporter) is based on a Blob detection algorithm that incorporates a 
 * connected-component labeling system. Hence, blobs of arbitrary size can be extracted by adjusting component 
 * connectivity parameters. This image detection algorithm defines boundary blobs for each individual sprite in a 
 * sprite sheet. Every specific blob defines a sprite characteristic within the sheet: position, name and size, 
 * which allows for subsequent data specification for each blob/image." 			
 * 																							- (BARBOSA et al., 2015)
 * 
 * Welcome to MuSSE - Multi-sized Sprite Sheet meta-data Exporter.
 * 
 * Groovy adaptation.
 * 
 * Authors’ contact:
 * https://marcelomesmo.github.io
 * marcelo.barbosa (at) ifrn.edu.br
 * 
 */
class MuSSE extends JFrame {
	
	static main(args) {
		/*
		 * Inits MuSSE interface with:
		 * SpritePanel: for SpriteSheet's display;
		 * MenuBar.
		 */
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MuSSE app = new MuSSE();
				try {
					// This is a fix to properly load resource image inside jar
					app.setIconImage(new ImageIcon(this.getClass().getResource("/img/icon.png")).getImage());
					//app.setIconImage(ImageIO.read(new File("img/icon.png")));
					
					final JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter openFilter = new FileNameExtensionFilter("Only Image Files", "jpg", "png", "gif", "bmp");
					FileNameExtensionFilter saveFilter = new FileNameExtensionFilter("Xml Files", "xml");
					String currentPath;		// Remember last path used at open or save

					SpritePanel panel = new SpritePanel();
						JScrollPane scrollPane = new JScrollPane(panel);
						scrollPane.setViewportView(panel);
						scrollPane.setPreferredSize(new Dimension(1000, 600));
					
					app.getContentPane().add(scrollPane);
					
					JMenuBar menuBar = new JMenuBar();
					JMenu fileMenu = new JMenu("File");
					fileMenu.setMnemonic(KeyEvent.VK_F);

					JMenuItem openMenuItem = new JMenuItem("Load");
					openMenuItem.setMnemonic(KeyEvent.VK_L);
					openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_L, ActionEvent.CTRL_MASK));
					openMenuItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("Button pressed");

								// Sets FileChooser Filter to Open file
								fc.setFileFilter(openFilter);

								// Gets last used path
								if (currentPath != null) fc.setCurrentDirectory(new File(currentPath));
								
								int returnVal = fc.showOpenDialog(app);
						
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = fc.getSelectedFile();
									
									// Loads new SpriteSheet from selected file
									panel.loadSpriteSheet(file);
									// Updates scrollPane to adjust to new SpriteSheet
									scrollPane.setViewportView(panel);
									
									// Saves path to be used later
									currentPath = fc.getSelectedFile().getPath();
									//This is where a real application would open the file.
									System.out.println("Opening: " + file.getName() + "." + "\n");
								} else {
									System.out.println("Open command cancelled by user." + "\n");
								}
							}		
						});
					fileMenu.add(openMenuItem);

					JMenuItem saveMenuItem = new JMenuItem("Export Selection as XML");
					saveMenuItem.setMnemonic(KeyEvent.VK_X);
					saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_X, ActionEvent.CTRL_MASK));
					saveMenuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println("Button pressed");
							
							String s = panel.getXML()

							//If a string was returned, say so.
							if ((s != null) && (s.length() > 0)) {
								System.out.println("XML created.\n");
								
								// Sets FileChooser Filter to Save file
								fc.setFileFilter(saveFilter);
								
								// Gets last used path
								if (currentPath != null) fc.setCurrentDirectory(new File(currentPath));
								
								// Opens FILECHOOSER save
								int returnVal = fc.showSaveDialog(app);

								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = fc.getSelectedFile();
								
									// Forces .xml file type
									if(!file.getAbsolutePath().endsWith(".xml") ) { file = new File(file.getAbsolutePath() + ".xml"); }

									try {
										// Creates a new Writer
										FileWriter fw = new FileWriter(file);
										
										// Write xml to file
										fw.write( panel.getXML() );
										
										// Close Writer
										fw.close();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
									
									// Saves path to be used later
									currentPath = fc.getSelectedFile().getPath();
									//This is where a real application would save the file.
									System.out.println("Saving: " + file.getName() + "." + "\n");
								} else {
									System.out.println("Save command cancelled by user.\n");
								}

								return;
							}
							//If you're here, the return value was null/empty.
							System.out.println("XML unsucefully created.");
						}
					});
					fileMenu.add(saveMenuItem);
					
					fileMenu.addSeparator();

					JMenuItem exitMenuItem = new JMenuItem("Exit");
					//exitMenuItem.setMnemonic(KeyEvent.VK_X);
						exitMenuItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.exit(0);
							}
						});
					fileMenu.add(exitMenuItem);


					JMenu actionMenu = new JMenu("Actions");
					actionMenu.setMnemonic(KeyEvent.VK_C);
					
					// Cut Selection Button
					JMenuItem cutSelectionItem = new JMenuItem("Cut Selection");
						cutSelectionItem.setMnemonic(KeyEvent.VK_S);
						cutSelectionItem.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_S, ActionEvent.CTRL_MASK));
						cutSelectionItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("Button pressed");
								panel.cutSheet();
							}
						});
					actionMenu.add(cutSelectionItem);
					
					// Cut Entire Sheet Button
					JMenuItem cutEntireItem = new JMenuItem("Cut Entire Sheet");
						cutEntireItem.setMnemonic(KeyEvent.VK_E);
						cutEntireItem.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_E, ActionEvent.CTRL_MASK));
						cutEntireItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("Button pressed");
								panel.cutEntireSheet();
							}
						});
					actionMenu.add(cutEntireItem);
					
					actionMenu.addSeparator();
					
					JMenuItem changeThItem = new JMenuItem("Sensibility (threshold)");
						changeThItem.setMnemonic(KeyEvent.VK_T);
						changeThItem.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_T, ActionEvent.CTRL_MASK));

					JPanel panel1 = new JPanel(new BorderLayout(10,0));
						JLabel thresholdLabel = new JLabel("Threshold");

						JSpinner threshold = new JSpinner();
							threshold.setModel(new SpinnerNumberModel(8,1,99,1));
							threshold.setEditor(new JSpinner.NumberEditor(threshold,"##"));
							JFormattedTextField txt = ((JSpinner.NumberEditor) threshold.getEditor()).getTextField();
							((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

							threshold.setAlignmentX(Component.CENTER_ALIGNMENT);

						panel1.add(thresholdLabel, BorderLayout.WEST);
						panel1.add(threshold, BorderLayout.CENTER);

					// Change Blob Detection Threshold
					changeThItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JOptionPane.showMessageDialog(null, panel1, "Change pixel Threshold", JOptionPane.PLAIN_MESSAGE);
								panel.setThreshold(threshold.getValue());
							}
						});

					actionMenu.add(changeThItem);
					
					actionMenu.addSeparator();
					
					// Add to XML button
					JMenuItem addToXmlItem = new JMenuItem("Add Selection to XML");
						addToXmlItem.setMnemonic(KeyEvent.VK_A);
						addToXmlItem.setAccelerator(KeyStroke.getKeyStroke(
								KeyEvent.VK_A, ActionEvent.CTRL_MASK));
						addToXmlItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("Button pressed");
								
								String s = (String)JOptionPane.showInputDialog(
									null,
									"Animation name:",
									"Add Selection to XML",
									JOptionPane.PLAIN_MESSAGE,
									null,
									null,
									"newAnimation1");

								//If a string was returned, say so.
								if ((s != null) && (s.length() > 0)) {
									System.out.println("Animation added " + s + " !");
									
									// Add Selection of Sprites (Blobs) to XML as new Animation
									panel.addAnimationToXML(s)
									
									return;
								}
								//If you're here, the return value was null/empty.
								System.out.println("Come on, finish the sentence!");
							}
						});
					actionMenu.add(addToXmlItem);

					JMenu helpMenu = new JMenu("Help");

					JMenuItem helpMenuItem = new JMenuItem("About");
					helpMenuItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JOptionPane.showMessageDialog(null, "Visit https://marcelomesmo.github.io", "About", JOptionPane.PLAIN_MESSAGE);
							}
						});
					
					helpMenu.add(helpMenuItem);
					
					menuBar.add(fileMenu);
					menuBar.add(actionMenu);
					menuBar.add(helpMenu);
					
					app.setJMenuBar(menuBar);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//app.setVisible(true);
			}
			
		})
	}
	
	public MuSSE() {
		initUI()
	}
	
	/*
	 * Inits MuSSE JFrame and configs.
	 */
	private void initUI() {
		setTitle "MuSSE"
		setSize(1100, 700)
		setLocationRelativeTo(null)
		setDefaultCloseOperation(EXIT_ON_CLOSE)
		//setExtendedState(JFrame.MAXIMIZED_BOTH)
		setVisible(true)
		setResizable(false)
	}
}
