package spritesheet

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/*
 * Blob.
 */
class SpriteClip {
	
	Set<Point> points = new HashSet()
	def name
	Rectangle boundingBox
	BufferedImage parentImage

	/*
	 *  Create SpriteClip (blob) for current SpriteSheet.
	 */
	public SpriteClip(BufferedImage _parentImage, int row, int col, String _name) {
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
		} else {
			boundingBox.add(p)
		}
	}
	
	// Save Image to File
		// TODO - Testar.
	public void saveTo(File directory, String format) throws IOException {
		File outfile = new File(directory, name + "." + format);
		//ImageIO.write(makeCutout(null, null), format, outfile);
	}
	
	/*
	 * Auxiliary methods (get's).
	 */
	public Rectangle getBoundingBox() {	return boundingBox }
		public int getX() 		{ return boundingBox.x }
		public int getY() 		{ return boundingBox.y }
		// Width of the bounding box is not the same as the number of pixels
		public int getWidth() 	{ return boundingBox.width + 1 }
		// Height of the bounding box is not the same as the number of pixels
		public int getHeight() 	{ return boundingBox.height + 1 }
		public String getName() { return name }
	
	/*
	 * Return String.
	 * SpriteClip (blob) name and Bounding Box data (x, y, w, h).
	 * 
	 */
	public String toString() {
		StringBuilder s = new StringBuilder(
				"Data dump for clip " + getName() + "\n" +
				"Bounding box x: " + boundingBox.x +
						   ", y: " + boundingBox.y +
						   ", w: " + boundingBox.width +
						   ", h: " + boundingBox.height + "\n")
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
	 * Save String in XML format.
	 * SpriteClip (blob) name and Bounding Box data (x, y, w, h).
	 * 
	 */
	public String toXml() {
		StringBuilder s = new StringBuilder(
				"<sprite name=\"" + getName() + "\">\n" +
				"<offset_x>" + boundingBox.x + "</offset_x>\n" +
			    "<offset_y>" + boundingBox.y + "</offset_y>\n" +
				"<width>" + boundingBox.width + "</width>\n" +
				"<height>" + boundingBox.height + "</height>\n" +
				"</sprite>\n")
		return s.toString()
	}
}
