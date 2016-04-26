package spritesheet

import java.awt.image.BufferedImage
import java.awt.Color

import javax.imageio.ImageIO

/*
 * Load SpriteSheet image.
 */
class SpriteSheet {

	/* 
	 * Image files and configs.
	 */
		// System file
		File file
		// Image
		BufferedImage sheet
		// Value for background (defines blank pixel/transparency), taken from 0,0 position on sheet
		int backgroundValue
		// Color Key
		Color colorkey;
		// Image path
		String image_name;

	/*
	 *  Loads SpriteSheet file
	 */
	public SpriteSheet(File src) throws IOException {
		file = src
		image_name = file.getName()
		sheet = ImageIO.read(file)
		// Pixel color reference for background
		backgroundValue = sheet.getRGB(0,0)
		colorkey = new Color(sheet.getRGB(0,0))
	}
	// This is a fix to properly load resource image inside jar
	public SpriteSheet(String src) throws IOException {
		//file = src
		image_name = src
		sheet = ImageIO.read(this.getClass().getResourceAsStream(src))
		// Pixel color reference for background
		backgroundValue = sheet.getRGB(0,0)
	}

	/*
	 * Auxiliary methods (gets and sets).
	 */
	public File getFile() { return file }

	public BufferedImage getImage()	{ return sheet }

	public int getWidth() { return sheet.getWidth() }
	
	public int getHeight() { return sheet.getHeight() }

	public String getName() { return image_name }
}
