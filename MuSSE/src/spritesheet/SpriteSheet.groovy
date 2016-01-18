package spritesheet

import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import blobdetection.Label
import blobdetection.LabelEqTable

/*
 * Loads SpriteSheet image and do Blob detection algorithm.
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
	
	/*
	 * Collection of SpriteClips (blobs) for current SpriteSheet.
	 */
	Collection<SpriteClip> clips
		
	/*
	 * Connected-component Labeling algorithm.
	 */
	// Table of Label Equivalence.
	LabelEqTable existingLabels = new LabelEqTable()
		// Pixel ids for Connected-component Labeling.
		int nextId = 1
		/*
		 * Threshold for connected-component method.
		 * Smaller thresholds will result in a large number of blobs.
		 * Higher thresholds will result in a smaller number of blobs. 
		 */
		int halfEdge = 8
	
	/*
	 *  Loads SpriteSheet file
	 */
	public SpriteSheet(File src) throws IOException {
		file = src
		sheet = ImageIO.read(file)
		// Pixel color reference for background
		backgroundValue = sheet.getRGB(0,0)
	}
	// DEPRECATED
	// Loads SpriteSheet file with scaled image
	public SpriteSheet(File src, int screen_width, int screen_height) throws IOException {
		file = src
		sheet = ImageIO.read(file)
		// Pixel color reference for background
		backgroundValue = sheet.getRGB(0,0)
		// Gets scaled image based on screen size
		sheet = getScaledImage(screen_width, screen_height)
			// TODO - Não funciona pq perde a transparencia - consertar
	}
	public BufferedImage getScaledImage(int w, int h){
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(sheet, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}
	
	// Clean method to reset Clips (blobs) from SpriteSheet.
	public void clean(){
		clips = null
		existingLabels = new LabelEqTable()
		nextId = 1
	}
	
	// Find Clips (blobs) on current selection Box.
	public void findClips(Rectangle area)
	{
		this.clips = getSpriteClips(area)
	}
	/*
	 * The overall goal of connected-component is to label each pixel within
	 * a blob with the same identifier.
	 * 
	 * These identifiers are represented by label numbers. First stage is to 
	 * circle through all the pixels inside the selected area, verifying 
	 * corresponding label numbers from neighbor pixels.
	 * 
	 */
	private Collection<SpriteClip> getSpriteClips(Rectangle area)
	{
		int h = area.height
		int w = area.width
		int x = area.x
		int y = area.y
		
		/*
		 * Creates a list of labels for each pixel in selected area.
		 * w -------->
		 * h 0 0 0 0		Same image gets same label.
		 * | 0 1 1 0		If a pixel get no nearest neighbor, a new label is created.
		 * | 0 0 0 0
		 * | 0 2 0 0
		 * | 0 0 0 0
		 * v
		 * 
		 * All the labels are stored in a matrix of equal dimension as the original image. 
		 * This way, we can set one label entry per pixel in the image. This matrix starts
		 * completely unlabeled and, as the algorithm iterate through the image, is filled 
		 * up by the 8-neighborhood connectivity method.
		 * 
		 */
		Label[][] labelSheet = new Label[h][w]

		/*
		 * Checks every pixel in selected area.
		 */
		for (int row = 0; row < h; row++) 
		{
			for (int col = 0; col < w; col++)
			{
				// Gets current pixel's id.
				int currentPixel = sheet.getRGB(x + col, y + row)
				
				// Checks if pixel is foreground (true) or background (false)
				if (isForeground(currentPixel)) {
					
					// Checks nearest neighbors for a valid Label or gets a new label
					Label returnedLabel = labelFromNeighborhood(labelSheet, w, h, col, row, x, y)
					
					// Saves pixel Label on sheet
					labelSheet[row][col] = returnedLabel
					
					// Case null should throw an exception
					if(labelSheet[row][col] == null) System.out.println("Error null label (x,y) " + col + " " + row + " shouldn't be null.")
				}
			}
		}
	   
		// -- DEBUG --
		// Print labels on console.
		/*
		  for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++)
			{
			   if(labelSheet[row][col] != null) System.out.print(existingLabels.getRep(labelSheet[row][col]).getId() + " "); // System.out.print(labelSheet[row][col].getId() + " ");
			   else System.out.print("0 ");
			}
		 	System.out.println();
		  }
		*/

		/* 
		 * Reduce the labels to their equivalences.
		 * Get pixels and bounding boxes.
		 * Create SpriteClips (blobs).
		 * 
		 */
		Map<Label, SpriteClip> clipRegistry = new HashMap()
		// Checks every Label in selected area.
		for (int row = 0; row < h; row++) 
		{
			for (int col = 0; col < w; col++) 
			{
				// Gets current Label
				Label currentLabel = labelSheet[row][col]
				
				// Check if pixel is Labeled
				if (currentLabel != null) {
					
					// Get pixel's representative (origin/reduced Label)
					Label rep =  existingLabels.getRep(currentLabel)
					
					// First time checking Label
					if (!clipRegistry.containsKey(rep)) {
						// Create a new SpriteClip (blob) for that Label
						SpriteClip sc = new SpriteClip(sheet, y+row, x+col, rep.toString())
						clipRegistry.put(rep, sc)
					} 
					// Existing label
					else {
						// Add Label to existing SpriteClip (blob) based on their representative (origin/reduced Label)
						SpriteClip sc = clipRegistry.get(rep)
						sc.addLocation(y+row, x+col)
					}
				}
			}
		}

		/* 
		 * Now we have all the SpriteClips (blobs) stored in a map.
		 */
		Set<SpriteClip> returnList = new HashSet()
		returnList.addAll(clipRegistry.values())
		return returnList
	}
	/*
	 * Check if valid pixel.
	 * Return true  in case foreground (not background).
	 * Return false in case background.
	 */
	private boolean isForeground(int pixel)
	{
	   if(pixel != backgroundValue) return true
	   return false
	}
	/*
	 * Apply 8-adjacent search path, likely: 8-neighborhood connectivity,
	 * in order to Label a pixel based on its neighborhood.
	 * 
	 */
	private Label labelFromNeighborhood( Label[][] labelSheet,
		int width,		// Selection Box width
		int height,		// Selection Box height
		int colPixel,  	// Pixel Label on sheet
		int rowPixel, 	// Pixel Label on sheet
		int xImg,		// Selection Box start pos X
		int yImg)		// Selection Box start pos Y
	{
		
		int colStart = Math.max(0, colPixel - halfEdge)		// Starting X 	(on Label list)
		int rowStart = Math.max(0, rowPixel - halfEdge)		// Starting Y 	(on Label list)
		int colEnd = Math.min(width-1, colPixel + halfEdge)	// Ending X 	(on Label list)
		int rowEnd = Math.min(height-1, rowPixel + halfEdge)// Ending Y 	(on Label list)
	
		Label assignedLabel = null
	
		// -- DEBUG --
		// System.out.println("Clip start at (x,y): " + colStart + " " + rowStart);
		// System.out.println("Clip ends  at (x,y): " + colEnd + " " + rowEnd);
		
		/* 8-neighborhood connectivity
		 * 
		 * L1 L2 L3
		 * L4 C
		 * 
		 * C: Center Pixel
		 * Ln: Neighbors 1 to 4
		 * 
		 * Identify Labels of neighborhood pixels L1, L2, L3 and L4,
		 * and apply to given position C.
		 * 
		 */
		// Check nearest neighbors for a valid Label.
		eight_neighborhood: for (int row = rowStart; row <= rowEnd; row++) 
		{
			for (int col = colStart; col <= colEnd; col++) 
			{
				// If there's no valid label on neighborhood
				if (row == rowPixel && col == colPixel) {
					// Gets a new Label
					if (assignedLabel == null){
						assignedLabel = nextLabel()
						existingLabels.addLabel(assignedLabel)
					}
					// No need to check additional neighbors.
					break eight_neighborhood
				}
	
				// Get neighbor's Label, if any
				Label neighbor = labelSheet[row][col]
				if (    neighbor != null &&
						assignedLabel == null) {
					assignedLabel = neighbor
				}
	
				// Check neighbors and mark equivalences.
				if (neighbor != null && neighbor != assignedLabel) {
					if (!existingLabels.hasLabel(assignedLabel)) {
						existingLabels.addLabel(assignedLabel)
					}
					existingLabels.setComembers(neighbor, assignedLabel)
				}
			}
		}
	
		return assignedLabel
	}
	/*
	 * Returns next valid Label.
	 */
	private Label nextLabel() {
		return new Label(nextId++)
	}

	/*
	 * Auxiliary methods (get's).
	 */
	public File getFile() { return file }

	public BufferedImage getImage()	{ return sheet }

	public int getWidth() { return sheet.getWidth() }
	
	public int getHeight() { return sheet.getHeight() }

	public Collection<SpriteClip> getClips() { return clips }
	
	public void setThreshold(int t) { this.halfEdge = t }
}
