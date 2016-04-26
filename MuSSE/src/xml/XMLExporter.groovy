package xml

/*
 * Class to append data to the XML file.
 * 
 * Clean when user changes SpriteSheet.
 * 
 */
class XMLExporter {

	// String for the XML
	StringBuilder xmlString
	
	// False case new XML
	// True case XML already closed
	//def closed
	
	public XMLExporter()
	{
		//closed = false
		
		// Init xml
		xmlString = new StringBuilder(/*
			"<?xml version=\"1.0\"?>\n" +
			"<spritesheet>\n"*/)	
	}

	public void clean()
	{
		//closed = false
		
		// Restart xml
		xmlString = new StringBuilder(/*
			"<?xml version=\"1.0\"?>\n" +
			"<spritesheet>" + "\n"*/)
	}
	
	/*
	 * Return closed XML to be print in file.
	 */
	/*public String getXML()
	{ 
		// Finish xml
		if(!closed) {
			xmlString.append("</spritesheet>")
			closed = true
		}
		
		return xmlString.toString()
	}*/

	/*
	 * Return closed XML to be print in file with image path and color key.
	 */
	public String getXML(String sheet_name, int width, int height, Color sheet_ck)
	{ 
		//if(!closed) {
			// Open xml
			StringBuilder finalXml  = new StringBuilder(
				"<?xml version=\"1.0\"?>\n" +
				"<spritesheet " 
				+ "image_name=\"" + sheet_name + "\""
				+ " width=\"" + width + " height=\"" + height 
				+ " ck_r=" + sheet_ck.getRed() + " ck_g=" + sheet_ck.getGreen() + " ck_b=" + sheet_ck.getBlue()
				+ " >\n")

			// Added all Animations 
			finalXml.append(xmlString.toString())
			
			// Finish xml
			finalXml.append("</spritesheet>")
			//closed = true
		//}
		
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
