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
	
	// Other Answer Types
	REASON,
	DESCRIPTION,
	DEFINITION,
	
	// GENERICS - Hypernyms
	NUMBER,
	
	// POS Tags
	NOUN,
	VERB,
	
	// NONE
	NONE,

}
