package xml

import java.awt.Color

/*
 * Class to append data to the XML file.
 * 
 * Clean when user changes SpriteSheet.
 * 
 */
class XMLExporter {

	// String for the XML
	StringBuilder xmlString
	
	public XMLExporter()
	{
		// Init xml
		xmlString = new StringBuilder()	
	}

	public void clean()
	{
		// Restart xml
		xmlString = new StringBuilder()
	}

	/*
	 * Return closed XML to be print in file with image path and color key.
	 */
	public String getXML(String sheet_name, int width, int height, Color sheet_ck)
	{ 
		// Open xml
		StringBuilder finalXml  = new StringBuilder(
			"<?xml version=\"1.0\"?>\n" +
			"<spritesheet " 
			+ "image_name=\"" + sheet_name + "\""
			+ " width=\"" + width + "\" height=\"" + height 
			+ "\" ck_r=\"" + sheet_ck.getRed() + "\" ck_g=\"" + sheet_ck.getGreen() + "\" ck_b=\"" + sheet_ck.getBlue()
			+ "\" >\n")

		// Add all Animations saved in xmlString
		finalXml.append(xmlString.toString())
			
		// Close xml
		finalXml.append("</spritesheet>")
		
		return finalXml.toString()
	}
	
	/* 
	 * Every time user adds a new animation,
	 * SpritePanel calls:
	 * 
	 * 		openAnimation("name")
	 * 		add Sprites for that Animation
	 * 		closeAnimation()
	 * 
	 */
	public void openAnimation(String s)
	{
		xmlString.append("	<animation name=\"" + s + "\">\n")
	}
	public void closeAnimation(String s)
	{
		xmlString.append("	</animation>\n")
	}
	
	/*
	 * Adds individual Sprites (Blobs) to the XML
	 * 
	 */
	public void addSprite(String s)
	{
		xmlString.append(s)
	}
}
