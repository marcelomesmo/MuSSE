package ui

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

import javax.swing.JPanel

import spritesheet.SpriteSheet
import xml.XMLExporter
import blobdetection.Blob
import blobdetection.BlobDetection

/*
 * Display for SpriteSheet images using JPanel inheritance.
 * SpriteSheets are loaded here. 
 * Implements listeners to detect mouse clicks'n drags on sheet.
 * 
 * The user has two options to apply the Blob detection
 * algorithm on the sprite sheets:
 1. by manually selecting a target area from the sheet; or
 2. by selecting the option “Cut entire sheet” from the “Actions” menu.
 * Option 1 can be achieved if the user clicks and drags 
 * the mouse over the image. That way, the Blob detection
 * algorithm is only applied to that sprite sheet subset 
 * (defined by 'selectionBox' rectangle).
 * 
 */
class SpritePanel extends JPanel implements MouseListener, MouseMotionListener {

	/*
	 *  Load SpriteSheet image
	 */
	SpriteSheet spritesheet
		// Sheet image name
		String sheet_image_source
	
	/*
	 * XMLExporter to create XML
	 */
	XMLExporter xml
	
	/*
	 * 	Load Blob detection algorithm
	 */
	BlobDetection blobDetection

	/*
	 *  Selection box rectangle with:
	 * 	 	start pos x,
	 *  	start pos y,
	 *  	width,
	 *  	height.
	 */
	Rectangle selectionBox = new Rectangle(0, 0, 0, 0)
		// Start position on screen for the selection box
		Point selectionStart = new Point()
		// Release position on screen for the selection box
		Point selectionEnd = new Point()

	public SpritePanel() throws IOException {
		//setBorder(javax.swing.BorderFactory.createTitledBorder("Sprite Sheet"))
		setSize(800, 600)
		//setPreferredSize(new Dimension(800, 600))
		addMouseListener(this)
		addMouseMotionListener(this)

		// Default image
		spritesheet = new SpriteSheet("/img/startscreen.jpg")
		//spritesheet = new SpriteSheet(new File("img/startscreen.jpg"))
		setPreferredSize(new Dimension(spritesheet.width,spritesheet.height))
		
		// Default blob detection algorithm
		blobDetection = new BlobDetection()
		
		// Default XMLExporter
		xml = new XMLExporter()
	}

	/*
	 * Drawn box as user drags the mouse on screen.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// Set location at mouse current position
		selectionEnd.setLocation(e.getX(), e.getY())
		// Quick-fix for mouse position outside image limits
		if(selectionEnd.x < 0) selectionEnd.x = 0
		if(selectionEnd.x > spritesheet.width) selectionEnd.x = spritesheet.width
		if(selectionEnd.y < 0) selectionEnd.y = 0
		if(selectionEnd.y > spritesheet.height) selectionEnd.y = spritesheet.height

		// Defines box width and height
		int  w = selectionStart.x - selectionEnd.x
		int  h = selectionStart.y - selectionEnd.y
		int x = w < 0 ? selectionStart.x : selectionEnd.x
		int y = h < 0 ? selectionStart.y : selectionEnd.y
		w = Math.abs( w )
		h = Math.abs( h )

		// Set box position x and y, width and height.
		selectionBox.setBounds(x, y, w, h)

		// Repaint panel
		repaint()
	}

	/*
	 *  Recognize mouse clicks on panel.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		mousePressed e
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// Set location at mouse click
		selectionStart.setLocation(e.getX(), e.getY())

		// Clean SpriteSheet's previous blobs for new selection
		blobDetection.clean()

		// Repaint panel
		repaint()
	}

	/*
	 * Recognize mouse release on panel.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// Set location at mouse release
		selectionEnd.setLocation(e.getX(), e.getY())
		// Quick-fix for mouse position outside image limits
		if(selectionEnd.x < 0) selectionEnd.x = 0
		if(selectionEnd.x > spritesheet.width) selectionEnd.x = spritesheet.width
		if(selectionEnd.y < 0) selectionEnd.y = 0
		if(selectionEnd.y > spritesheet.height) selectionEnd.y = spritesheet.height

		// Defines box width and height
		int  w = selectionStart.x - selectionEnd.x;
		int  h = selectionStart.y - selectionEnd.y;
		int x = w < 0 ? selectionStart.x : selectionEnd.x;
		int y = h < 0 ? selectionStart.y : selectionEnd.y;
		w = Math.abs( w );
		h = Math.abs( h );

		// Set box position x and y, width and height.
		selectionBox.setBounds(x, y, w, h)

		// Repaint panel
		repaint()
	}

	/*
	 * Paint Method.
	 * Draw SpriteSheet.
	 * Draw Selection Box on screen.
	 * Draw SpriteClips (blobs).
	 * 
	 */
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent(g)

		// Draw SpriteSheet on Panel
		if(spritesheet != null) g.drawImage(spritesheet.getImage(), 0, 0, null)

		// Set box border color to BLACK.
		g.setColor(Color.BLACK);

		// Draw Box on Panel
		g.drawRect((int) selectionBox.x, (int) selectionBox.y, (int) selectionBox.width, (int) selectionBox.height)

		// Draw SpriteClips (blobs) on Panel, if any
		if(blobDetection.getClips() != null)
		{
			for(Blob s : blobDetection.getClips()){
				Rectangle d = s.getBoundingBox()
				g.drawRect( (int) d.x, (int) d.y, (int) d.width, (int) d.height )
			}
		}
	}

	/* 
	 * Loads SpriteSheet from file.
	 */
	public void loadSpriteSheet(File f)
	{
		// Cleans previous blobs, if any
		if(blobDetection != null) blobDetection.clean()
		
		// Cleans previous XML, if any
		if(xml != null) xml.clean()

		// Loads new SpriteSheet from file
		spritesheet = new SpriteSheet(f)

		// Adjust Panel Size to new SpriteSheet size
		setPreferredSize(new Dimension(spritesheet.width,spritesheet.height))

		// Repaint screen
		repaint()
	}
	
	/*
	 * Define SpriteClips (blobs) for entire sheet
	 */
	public void cutEntireSheet()
	{
		if(spritesheet != null)
		{
			// Clean previous selection box
			selectionBox = new Rectangle(0, 0, 0, 0)
			
			// Find clips for full image
			blobDetection.findClips(spritesheet, new Rectangle(0, 0, spritesheet.width, spritesheet.height))
			if(blobDetection.getClips() != null)
			{
				for(Blob s : blobDetection.getClips()){
					System.out.println(s.toString())
				}
				System.out.println("Sucess define blobs.")
			}
			// Repaint screen
			repaint();
		}
	}
	/*
	 * Define SpriteClips (blobs) for selected area
	 */
	public void cutSheet()
	{
		if(selectionBox != null && spritesheet != null)
		{
			blobDetection.findClips(spritesheet, selectionBox)
			if(blobDetection.getClips() != null)
			{
				for(Blob s : blobDetection.getClips()){
					System.out.println(s.toString())
				}
				System.out.println("Sucess define blobs.")
			}
			// Repaint screen
			repaint();
		}
	}
	/*
	 * Defines threshold for blob detection algorithm
	 */
	public void setThreshold(int t)
	{
		if(blobDetection != null) blobDetection.setThreshold(t)
	}
	
	// Adds new Animation to the XML file in XMLExporter
	
	/*
	 * Add Selection as Animation to XML.
	 */
	public void addAnimationToXML(String s)
	{
		if(blobDetection != null && blobDetection.getClips() != null) {
			
			// Start new Animation
			xml.openAnimation(s)
			
			// Add every Sprite (Blob) for that Animation
			for(Blob b : blobDetection.getClips()){
				xml.addSprite(b.toXml())
			}
			
			// Closes Animation
			xml.closeAnimation()
			
			System.out.println("Sucess create animation.")
		}
	}
	
	// Gets the XML file from XMLExporter
	public String getXML()
	{
		return xml.getXML()
		// return xml.getXML(sheet.getName(), sheet.getWidth(), sheet.getHeight(), sheet.getColorkey())
	}

	/*
	 * String sheet_path;
	 * 
	 * public void setPath(String path)
	 * {
	 * 		sheet_path = path;
	 * }
	 *
	 */

	/*
	 * UNINPLEMENTED METHODS
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
