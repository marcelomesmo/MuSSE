package blobdetection

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/*
 * Table of Label Equivalence.
 * 
 * The algorithm will reduce the labels to their corresponding representatives. 
 * A representative is given by the minimum value in the table of equivalence, 
 * i.e. minimum label id compared to surrounding neighbors.
 * 
 * The algorithm later creates a list for every blob in the selected area and 
 * match it accordingly to the reduced labels.
 * 
 * That will guaranty that all equivalent labels are assigned the same region 
 * value.
 * 
 * Example:
 * 	Label id 	Equivalent Labels
 * 		1 			1,6
 * 		2 			2,3,4,5
 * 		3 			2,3,4,5
 * 		4	 		2,3,4,5
 * 		5			2,3,4,5
 * 		6			1,6
 * 
 * 		As a result, the algorithm will reduce labels 1 and 6 to label 1, 
 * 		and labels 2, 3, 4 and 5, to label 2.
 * 
 */
class LabelEqTable {
	
	/*
	 *  Table of Label Equivalence
	 *  Map : Label id (parent) to Equivalent Labels (child)
	 *  
	 */
	Map<Label, Label> eqMap = null
	
	// Creates a new Table
	public LabelEqTable() {
		eqMap = new HashMap()
	}

	// Set a Label Equivalence
	private void setChild(Label parent, Label child) {
		eqMap.put(parent, child)
	}

	// Get equivalences for selected Label
	private Label getChild(Label parent) throws NoSuchElementException {
		if (!hasLabel(parent)) {
			throw new NoSuchElementException("Parent label not yet added.")
		}
		return eqMap.get(parent)
	}

	// Adds new Label to Table
	public boolean addLabel(Label lab) throws RuntimeException {
		if (lab == null) {
			throw new NullPointerException("Cannot add null Labels.")
		}
		boolean isNewLabel = !hasLabel(lab)
		if (isNewLabel) {
			// Every Label is Equivalent to itself
			setChild(lab, lab)
		}
		return isNewLabel
	}
	// Verifies if Table contains a specific Label.
	public boolean hasLabel(Label lab) {
		return eqMap.containsKey(lab)
	}

	/*
	 *  Reduce Equivalences.
	 *  
	 *  Add a Label to existing Clip (representative) based on their equivalence.
	 *  
	 */
	public void setComembers(Label first, Label second) {

		// First get the representative (origin Label) for each label.
		Label firstRep = getRep(first)
		Label secondRep = getRep(second)
		
		// Do nothing in case label has no equivalences besides itself
		if (firstRep.equals(secondRep)) { return; }

		// Sets the smaller Label as an equivalence to the higher Label
		Label max = max(firstRep, secondRep)
		Label min = min(firstRep, secondRep)
		setChild(max, min)
	}
	// Recursively gets the smaller Label in the Equivalence sequence (origin Label).
	public Label getRep(Label lab) {
		Label child = getChild(lab)
		if (child == lab) {
			return lab
		}

		Label rep = getRep(child)
		setChild(lab, rep)
		return rep
	}
	// Return smaller Label in the pair
	private Label min(Label first, Label second){
		if (first.id < second.id) {
			return first
		} else {
			return second
		}
	}
	// Return higher Label in the pair
	private Label max(Label first, Label second) {
		if (first.id > second.id) {
			return first
		} else {
			return second
		}
	}
}
