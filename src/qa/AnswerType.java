/**
 * 
 */
package qa;

/**
 * @author Deepak
 *
 */
public enum AnswerType {
	// Stanford NER 7 class model
	TIME,
	LOCATION,
	ORGANIZATION,
	PERSON,
	MONEY,
	PERCENT,
	DATE,
	
	// Additional NER tags from NERClassifierCombiner
	NUMBER,
	MISC,
	ORDINAL,
	DURATION,
	SET,
	
	// Other Answer Types
	REASON,
	DESCRIPTION,
	DEFINITION,
	
	// POS Tags
	NP,
	NNP,
	NN,
	VB,
	
	// NONE
	NONE,

}
