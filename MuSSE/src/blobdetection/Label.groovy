package blobdetection

/*
 * Define Labels for the SpriteClip (blob).
 * 
 */
class Label {
	
	def id

	public Label(int _id){
		id = _id
	}

	@Override
	public String toString() {
		return "Label" + id
	}

	public int getId() {
		return this.id
	}
}
