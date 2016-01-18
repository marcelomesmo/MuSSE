package ui

import java.awt.Color
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.filechooser.FileNameExtensionFilter


/*
 * TODO List -
 * 
 * 1. Adicionar botoes funcionando para:	
 * 		a) cortar o sheet.						- check
 * 		b) cortar todo o sheet.					- check
 * 		c) mudar o threshold.					- check
 * 
 * 2. Salvar XML.
 * 
 * 4. Separar SpriteSheet de BlobDetection.
 * 
 */

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
					final JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Only Image Files", "jpg", "png", "gif", "bmp");
					fc.setFileFilter(filter);
					
					JPanel container = new JPanel(new GridBagLayout());
						container.setBackground(Color.darkGray);
						container.setSize(1000,600);
						GridBagConstraints c = new GridBagConstraints();
					
					SpritePanel panel = new SpritePanel();
						JScrollPane scrollPane = new JScrollPane(panel);
						scrollPane.setViewportView(panel);
						scrollPane.setPreferredSize(new Dimension(800, 600));
						c.fill = GridBagConstraints.HORIZONTAL;
						container.add(scrollPane, c);
					
					ActionPanel panel2 = new ActionPanel();
						c.fill = GridBagConstraints.VERTICAL;
						container.add(panel2, c);
						
						// Listener for panel2 Buttons
						panel2.cutEntire.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("Button pressed");
								panel.cutEntireSheet();
							}		
						});
						panel2.cutSelection.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("Button pressed");
								panel.cutSheet();
							}		
						});
					
					app.getContentPane().add(container);
					
					JMenuBar menuBar = new JMenuBar();
					JMenu fileMenu = new JMenu("File");

					JMenuItem openMenuItem = new JMenuItem("Open");
					openMenuItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println("Button pressed");
								int returnVal = fc.showOpenDialog(app);
						
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = fc.getSelectedFile();
									
									// Loads new SpriteSheet from selected file
									panel.loadSpriteSheet(file);
									// Updates scrollPane to adjust to new SpriteSheet
									scrollPane.setViewportView(panel);
									
									//This is where a real application would open the file.
									System.out.println("Opening: " + file.getName() + "." + "\n");
								} else {
									System.out.println("Open command cancelled by user." + "\n");
								}
							}		
						});
					fileMenu.add(openMenuItem);

					JMenuItem saveMenuItem = new JMenuItem("Export Selection as XML");
					fileMenu.add(saveMenuItem);
					fileMenu.addSeparator();

					JMenuItem exitMenuItem = new JMenuItem("Exit");
					fileMenu.add(exitMenuItem);
					

					JMenu actionMenu = new JMenu("Actions");

					/*JMenuItem autoCutItem = new JMenuItem("Cut entire sheet");
					actionMenu.add(autoCutItem);
					JMenuItem changeThItem = new JMenuItem("Sprite size");
					actionMenu.add(changeThItem);*/

					JMenu helpMenu = new JMenu("Help");

					JMenuItem helpMenuItem = new JMenuItem("About");
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
