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
	def closed
	
	public XMLExporter()
	{
		closed = false
		
		// Init xml
		xmlString = new StringBuilder(
			"<?xml version=\"1.0\"?>\n" +
			"<spritesheet>\n")	
	}
	
	public void clean()
	{
		closed = false
		
		// Restart xml
		xmlString = new StringBuilder(
			"<?xml version=\"1.0\"?>\n" +
			"<spritesheet>" + "\n")
	}
	
	/*
	 * Return closed XML to be print in file.
	 */
	public String getXML()
	{ 
		// Finish xml
		if(!closed) {
			xmlString.append("</spritesheet>")
			closed = true
		}
		
		return xmlString.toString()
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
