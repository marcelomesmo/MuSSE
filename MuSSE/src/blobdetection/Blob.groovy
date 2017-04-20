package blobdetection

import java.awt.Point
import java.awt.Rectangle
import java.awt.image.BufferedImage

/*
 * Blob.
 * 
 * A blob is a region of an image that contains approximately close 
 * characteristics, such as brightness or color. A blob is defined 
 * as a region of connected pixels. Blob detection is used to identify 
 * these regions in images.
 * 
 * These blobs are used in MuSSE to save meta-data for Sprites inside a
 * SpriteSheet, i.e.:
 * 		offset_x, offset_y (position); and
 * 		width, height (size).
 * 
 */
class Blob {
	
	// Blob identification.
	def name
	// Blob boundingBox containing position and size.
	Rectangle boundingBox
		// Set of Points (pixels) contained in the boundingBox
		Set<Point> points = new HashSet()
	// Anchor point for the Blob image
	Point anchor
	// Related SpriteSheet.
	BufferedImage parentImage

	/*
	 *  Create SpriteClip (blob) for current SpriteSheet.
	 */
	public Blob(BufferedImage _parentImage, int row, int col, String _name) {
		parentImage = _parentImage
		name = _name
		addLocation(row, col)
	}
	// Add Point (pixel) to current SpriteClip (blob).
	public void addLocation(int row, int col) {
		Point p = new Point(col, row)
		points.add(p)
		if (boundingBox == null) {
			boundingBox = new Rectangle(col, row, 1, 1)
			anchor = new Point(col,row)
		} else {
			boundingBox.add(p)
		}
	}
	
	// Save Blob Image to File
		// TODO - Testar.
		/*
		 * la em MuSSE fazer um botao "extrair sprites individuais da selecao"
		 * foreach blob
		 * b.saveTo("blob1", "jpg")
		 * 
		 */
	// NYI.
	public void saveTo(File directory, String format) throws IOException {
		File outfile = new File(directory, name + "." + format);
		//ImageIO.write(makeCutout(null, null), format, outfile);
	}

	/*
	 * Auxiliary methods (get's).
	 */
	public Point getAnchorPoint() 	{ return anchor }
	public Rectangle getBoundingBox() {	return boundingBox }
		public int getX() 		{ return boundingBox.x }
		public int getY() 		{ return boundingBox.y }
		// Width of the bounding box is not the same as the number of pixels
		public int getWidth() 	{ return boundingBox.width + 1 }
		// Height of the bounding box is not the same as the number of pixels
		public int getHeight() 	{ return boundingBox.height + 1 }
		public String getName() { return name }
	
	/*
	 * Return String in Plain txt format.
	 * Create String description for the Sprite (Blob) with name and Bounding Box data (x, y, w, h) with points (opt).
	 * 
	 */
	public String toString() {
		StringBuilder s = new StringBuilder(
				"Data dump for clip " + getName() + "\n" +
				"Bounding box x: " + boundingBox.x +
						   ", y: " + boundingBox.y +
						   ", w: " + boundingBox.width +
						   ", h: " + boundingBox.height + "\n" +
				"Anchor point x: " + anchor.x +
						   ", y: " + anchor.y + "\n")
		/* 
			s.append("Points:\n")
			for (Point curPoint : points) {
		    	s.append("\t x: " + curPoint.x + ", y: " + curPoint.y + "\n")
			} 
		*/
		s.append("Clip W: " + getWidth() + ", H: " + getHeight() + "\n")

		return s.toString()
	}
	/*
	 * Return String in XML format.
	 * Create XML description for the Sprite (Blob) with name and Bounding Box data (x, y, w, h).
	 * 
	 */
	public String toXml() {
		StringBuilder s = new StringBuilder(
				"		<sprite name=\"" + getName() + "\">\n" +
				"			<offset_x>" + (int) boundingBox.x + "</offset_x>\n" +
			    "			<offset_y>" + (int) boundingBox.y + "</offset_y>\n" +
				"			<width>" + (int) boundingBox.width + "</width>\n" +
				"			<height>" + (int) boundingBox.height + "</height>\n" +
				"			<anchor_x>" + (int) anchor.x + "</anchor_x>\n" +
				"			<anchor_y>" + (int) anchor.y + "</anchor_y>\n" +
				"		</sprite>\n")
		return s.toString()
	}
}
