/**
 * 
 */
package com.org.kobito.kobitosimilarity.services;

import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @author Vu Hoai Nam 
 * <p>This class generate the words not base on similar mapping character
 */
@Service
public class SimilarGenrator {

	/**
	 * The domain name format just include a-z , A-Z , 0-9 , "-" , "_" ( but the "-" and "_" not at the begin or the end )
	 */
	private static final String[] DOMAIN_NAME_CHAR = new String[]{ "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",   
																   "0","1","2","3","4","5","6","7","8","9",  
																   "-" };
	/**
	 * The accessible length of 
	 */
	private static final int CHANGE_ONE_ACCESSIBLE = 4;
	private static final int ADD_ONE_ACCESSIBLE = 5;
	private static final int ADD_TWO_ACCESSIBLE = 12;
	private static final int CHANGE_TWO_ACCESSIBLE = 10;
	private static final int CHANGE_TWO_TYPOS_ACCESSIBLE = 4;
	private static final int ADD_ONE_CHANGE_ONE_ACCESSIBLE = 11;
	private static final int SUBTRACT_ONE_ACCESSIBLE = 6;
	private static final int SUBTRACT_TWO_ACCESSIBLE = 14;
	
	/**
	 * Max word length accessible
	 */
	private static final int MAX_LENGTH_ACCESSIBLE = 14;
	
	/**
	 * The default threshold
	 */
	private static int threshold ;
	
	/**
	 * Generate the words with just replace one char
	 */
	public static Set<String> changeOneChar( String word ){
		//check permission
		if( word.length() < CHANGE_ONE_ACCESSIBLE ) return new HashSet<String>();
			
		word = word.toLowerCase();
		StringBuilder processBuilder = new StringBuilder( word );
		Set<String> returnList = new HashSet<String>();
		
		int domainCharLength = DOMAIN_NAME_CHAR.length;
		
		int wordLength = processBuilder.length();
		for (int i = 0; i < processBuilder.length(); i++) {
			
			for (int j = 0; j < domainCharLength ; j++) {
				StringBuilder wordBuilder = new StringBuilder( word );
				String tmp = wordBuilder.replace( i , i+1 , DOMAIN_NAME_CHAR[j]).toString();
				
				//Domain format do not accept - and _ at start and end of string
				if( !tmp.matches("^-.*") && !tmp.matches("^\\_.*") 
						&& tmp.indexOf("-") != (wordLength -1) && tmp.indexOf("_") != (wordLength -1) )
					returnList.add(tmp);
			}
		}
		
		return returnList;
	}
	
	/**
	 * Generate words by changing two character inside
	 */
	public static Set<String> changeTwoChar( String word ){
		
		if( word.length() < CHANGE_TWO_TYPOS_ACCESSIBLE ) 
			return new HashSet<String>();
		else if( word.length() >= CHANGE_TWO_TYPOS_ACCESSIBLE && word.length() < CHANGE_TWO_ACCESSIBLE )
			return changeTwoNextChar(word);
		
		word = word.toLowerCase();
		StringBuilder processBuilder = new StringBuilder( word );
		Set<String> returnList = new HashSet<String>();
		
		int wordLength = processBuilder.length();
		for (int i = 0; i < processBuilder.length(); i++) {
			
			for (String str1 : DOMAIN_NAME_CHAR) {
				StringBuilder wordBuilder = new StringBuilder( word );
				String tmp1 = wordBuilder.replace( i , i+1 , str1).toString();
				for( int j = i+1 ; j < wordLength ; j ++ ){
					for (String str2 : DOMAIN_NAME_CHAR) {
						String tmp2 = new StringBuilder(tmp1).replace( j , j+1 , str2).toString();
						//Domain format do not accept - and _ at start and end of string
						if( !tmp2.matches("^-.*") && !tmp2.matches("^\\_.*") 
								&& tmp2.indexOf("-") != (wordLength -1) && tmp2.indexOf("_") != (wordLength -1) )
							returnList.add(tmp2);
					}
				}
			}
		}
		
		return returnList;
	}
	
	/**
	 * Changing two next char, copied form Typo Transpose
	 * @param word
	 * @return List<ItemGenerate> No duplicate words
	 */
	public static Set<String> changeTwoNextChar(String word){
		word = word.toLowerCase();
		StringBuffer worfBuffer = new StringBuffer(word);
		Set<String> typos = new HashSet<String>();
		int lenght = worfBuffer.length();
		// check each character
		for (int i = 0; i < lenght ; i++) {
			if( ( i + 1 ) == lenght )
			{
				// could have simplified the test by throwing it in the for loop but I didn't to keep it readable
				// at the end no transposition
			} else {
				StringBuffer tempWord = new StringBuffer(word);
				String tempChar = "" + tempWord.charAt(i);
				String tempNextChar = "" + tempWord.charAt(i+1);
				
				tempWord.replace(i, i+1, tempNextChar);
				tempWord.replace(i+1, i+2, tempChar);
				
				typos.add(tempWord.toString());
				}
		}
		
		return typos;
	}
	
	/**
	 * Generate words by adding one character
	 * @param word
	 * @return List<ItemGenerate> No duplicate words
	 */
	public static Set<String> addOneChar( String word ){
		
		if( word.length() < ADD_ONE_ACCESSIBLE ) return new HashSet<String>();
		
		word = word.toLowerCase();
		StringBuilder processBuilder = new StringBuilder( word );
		Set<String> returnList = new HashSet<String>();
		
		//process
		for (int i = 0; i <= processBuilder.length(); i++) {
			for (String str : DOMAIN_NAME_CHAR) {
				String tmp = new StringBuilder(word).insert(i, str).toString();
				if( !tmp.matches("^-.*") && !tmp.matches("^\\_.*") )
					returnList.add(tmp);
			}
		}
		
		
		return returnList;
	}
	
	/**
	 * Generate words by changing one char and adding one char
	 * @param word
	 * @return List<ItemGenerate> No duplicate words
	 */
	public static Set<String> addOneChangeOne( String word ){
		
		//check permission
		if( word.length() < ADD_ONE_CHANGE_ONE_ACCESSIBLE ) 
			return new HashSet<String>();
		
		word = word.toLowerCase();
		StringBuilder processBuilder = new StringBuilder( word );
		int wordLength = processBuilder.length();
		Set<String> returnList = new HashSet<String>();
		
		//process change one then add one
		for (int i = 0; i < processBuilder.length(); i++) {
			for (String str1 : DOMAIN_NAME_CHAR) {
				String tmp1 = new StringBuilder( word ).replace( i , i+1 , str1).toString();
				for( int j = i+1 ; j < wordLength ; j ++ ){
					for (String str2 : DOMAIN_NAME_CHAR) {
						String tmp2 = new StringBuilder(tmp1).insert(j+1, str2).toString();
						//Domain format do not accept - and _ at start and end of string
						if( !tmp2.matches("^-.*") && !tmp2.matches("^\\_.*") 
								&& tmp2.indexOf("-") != (tmp2.length() -1) && tmp2.indexOf("_") != (tmp2.length() -1) )
							returnList.add(tmp2);
					}
				}
			}
		}
		
		//process add one then change one
		for (int i = 0; i < processBuilder.length(); i++) {
			for (String str1 : DOMAIN_NAME_CHAR) {
				String tmp1 = new StringBuilder( word ).insert( i , str1).toString();
				for( int j = i+1 ; j < wordLength ; j ++ ){
					for (String str2 : DOMAIN_NAME_CHAR) {
						String tmp2 = new StringBuilder(tmp1).replace(j, j+1, str2).toString();
						//Domain format do not accept - and _ at start and end of string
						if( !tmp2.matches("^-.*") && !tmp2.matches("^\\_.*") 
								&& tmp2.indexOf("-") != (tmp2.length() -1) && tmp2.indexOf("_") != (tmp2.length() -1) )
							returnList.add(tmp2);
					}
				}
			}
		}
		
		return returnList;
	}
	
	/**
	 * Subtract by one char or missing one char
	 * <p>This function copied form typos missing 
	 * @param word
	 * @return List<ItemGenerate> No duplicate words
	 */
	public static Set<String> subtractOne(String word){
		
		//check permission
		if( word.length() < SUBTRACT_ONE_ACCESSIBLE  ) return new HashSet<String>();
		
		word = word.toLowerCase();
		Set<String> typos = new HashSet<String>();
		StringBuffer wordBuffer = new StringBuffer(word);
		int length = wordBuffer.length();
		//check each character
		for( int i = 0; i < length; i ++ )
		{
			String tempWord = "";
			if( i == 0 )
			{
				// at first character
				tempWord = wordBuffer.substring( (i + 1 ) );

			} else if ( ( i + 1 ) == length ) {
				// at last character
				tempWord = wordBuffer.substring( 0,  i  ) ;

			} else {
				// in between
				tempWord = wordBuffer.substring( 0,  i  ).concat(wordBuffer.substring( i +1 ) ) ;


			}
			typos.add(tempWord);
		}

		return typos;
	}
	
	/**
	 * Subtract by one char or missing one char
	 * <p>This function copied form typos missing 
	 */
	public static Set<String> subtractTwo(String word){
		
		//check permission
		if( word.length() < SUBTRACT_TWO_ACCESSIBLE  ) return new HashSet<String>();
		
		word = word.toLowerCase();
		Set<String> typos = new HashSet<String>();
		StringBuffer wordBuffer = new StringBuffer(word);
		int length = wordBuffer.length();
		//check each character
		for( int i = 0; i < length -1 ; i ++ )
		{
			String tmp1 ;
			if( i == 0)
				tmp1 = wordBuffer.substring(i+1);
			else 
				tmp1 = new StringBuilder(word).delete(i, i+1).toString();
			
			for (int j = 0; j < tmp1.length() -1 ; j++) {
				String tempWord = new StringBuilder(tmp1).delete(j, j+1).toString();
				typos.add(tempWord);
			}
			
		}

		return typos;
	}
	
	/**
	 * Generate words by changing two character
	 * @param word
	 * @return List<ItemGenerate> No duplicate words
	 */
	public static Set<String> addTwoChar( String word ){
		
		//check permission
		if( word.length() < ADD_TWO_ACCESSIBLE  ) return new HashSet<String>();
		
		word = word.toLowerCase();
		StringBuilder processBuilder = new StringBuilder( word );
		int wordLength = processBuilder.length();
		Set<String> returnList = new HashSet<String>();
		
		//processing
		for (int i = 0; i < wordLength ; i++) {
			for (String str1 : DOMAIN_NAME_CHAR) {
				String tmp = new StringBuilder(word).insert(i, str1).toString();
				for( int j = i+1 ; j <= wordLength ; j ++ ){
					for (String str2 : DOMAIN_NAME_CHAR) {
						String tmp2 = new StringBuilder(tmp).insert(j+1, str2).toString();
						if( !tmp2.matches("^-.*") && !tmp2.matches("^\\_.*") 
								&& tmp2.indexOf("-") != (tmp2.length() -1) && tmp2.indexOf("_") != (tmp2.length() -1) )
							returnList.add(tmp2);
					}
				}
			}
		}
		
		
		return returnList;
	}
	
	
	/**
	 * Compare list of words with original words.
	 * <p>Just compare the necessary words
	 * @return Map<String, Integer> list word with score
	 */
	public static Map<String, Integer> runCompare( Set<String> list, String word ) {
		
		String[] array = list.toArray(new String[list.size()]);
		HashMap<String, Integer> returnList = CompareNIST.compareWithNIST(word , array , 80);
		
		//Remove the 100% similar item
		returnList.remove(word);
		
		return returnList ;
	}
	
	/**
	 * Compare list of words with original word.
	 * <p>Just compare the necessary words
	 * @return Map<String, Integer> list word with score
	 */
	public static Map<String, Integer> runCompare( Set<String> list, String word , int threshold, boolean isAll ) {

		String[] array = list.toArray(new String[list.size()]);
		
		HashMap<String, Integer> returnList = CompareNIST.compareWithNIST(word , array , threshold);
		
		//Remove the 100% similar item
		returnList.remove(word);
		
		return returnList ;
	}
	/**
	 * Override runCompare
	 * @param list
	 * @param word
	 * @return Map<String, Integer> list word with score
	 */
	public static Map<String, Integer> runCompare( Queue<String> list, String word ) {
		Set<String> set = new HashSet<String>();
		for( String str : list ){
			set.add(str);
		}
		return runCompare(set, word);
	}
	
	/**
	 * Generate All the case base on all these function
	 * @param isRunAll : is get all the generator from changeTwoChar function
	 * @return Map<String, Map<String, Integer>> include many maps base on type of SimilarGenerator 
	 * ( ex: changeOneChar, addOneChar, subtractOne ..)
	 */
	public static Map<String, Map<String, Integer>> getAll( String word ,int threshold, boolean isRunAll ){
		
		SimilarGenrator.threshold = threshold;
		
		Map<String, Map<String, Integer>> returnList = new HashMap<String, Map<String,Integer>>();
		
		Map<String, Integer> changeOneChar =  (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( changeOneChar(word) , word, threshold, isRunAll ) : new HashMap<String, Integer>();
		returnList.put("changeOneChar", changeOneChar);
		
		Map<String, Integer> addOneChar = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( addOneChar(word) , word, threshold, isRunAll ) : new HashMap<String, Integer>();
		returnList.put("addOneChar", addOneChar);
		
		Map<String, Integer> subtractOneChar = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( subtractOne(word) , word, threshold, isRunAll ) : new HashMap<String, Integer>();
		returnList.put("subtractOneChar", subtractOneChar);
		
		Map<String, Integer> addTwoChar = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( addTwoChar(word) , word, threshold, isRunAll ) : new HashMap<String, Integer>();
		returnList.put("addTwoChar", addTwoChar);
		
		Map<String, Integer> subtractTwo = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( subtractTwo(word) , word, threshold, isRunAll ) : new HashMap<String, Integer>();
		returnList.put("subtractTwo", subtractTwo);
		
		if( isRunAll ){
			
			Map<String, Integer> changeTwoChar = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( changeTwoChar(word) , word ) : new HashMap<String, Integer>();
			returnList.put("changeTwoChar", changeTwoChar);
			
			Map<String, Integer> addOneChangeOne = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( addOneChangeOne(word) , word ) : new HashMap<String, Integer>();
			returnList.put("addOneChangeOne", addOneChangeOne);
			
		}
		else{
			//GET SMALL lIST 

			Map<String, Integer> changeTwoChar = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( changeTwoChar(word), word, threshold, isRunAll ) : new HashMap<String, Integer>();
			returnList.put("changeTwoChar", changeTwoChar);
			
			Map<String, Integer> addOneChangeOne = (word.length() < MAX_LENGTH_ACCESSIBLE ) ? runCompare( addOneChangeOne(word), word, threshold , isRunAll ) : new HashMap<String, Integer>();
			returnList.put("addOneChangeOne", addOneChangeOne);
		}	
		
		
		return returnList;
	}

//	public static void main( String ... arg){
//		Map<String, Map<String, Integer>> list =  getAll("keith" , 50 , true) ;
//		for ( Map.Entry<String, Map<String, Integer>> item : list.entrySet() ) {
//			for ( Map.Entry<String, Integer> entry: item.getValue().entrySet() ) {
//				if( entry.getKey().contains("keith") ){
//					//System.out.print(entry.getKey() + " ");
//				}
//				else {
//					System.out.print(entry.getKey() + " ");
//				}
//			}
//		}
//	}
}
