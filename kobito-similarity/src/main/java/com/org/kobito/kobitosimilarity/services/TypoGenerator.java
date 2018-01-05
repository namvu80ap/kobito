/**
 * 
 */
package com.org.kobito.kobitosimilarity.services;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vu Hoai Nam
 * This class translate from PHP TypoGenerator to Java
 */
@Service
public class TypoGenerator {

	/**
	 * This table copy from VisialSimilarity charSimilarity
	 */
	private static final Map<String, String[]> SIMILAR_CHAR_TABLE =  Collections.unmodifiableMap(new HashMap<String, String[]>(){
        {
        	/**
        	 * THIS IS THE LIST OF SIMILAR TYPO
        	 * BASE ON THE PHP TYPO GENERATOR
        	 */
            put("1", new String[]{"2", "q"});
            put("2" , new String[]{ "1", "q", "w", "3" });
            put("3" , new String[]{ "2", "w", "e", "4" });
            put("4" , new String[]{ "3", "e", "r", "5" });
            put("5" , new String[]{ "4", "r", "t", "6" });
            put("6" , new String[]{ "5", "t", "y", "7" });
            put("7" , new String[]{ "6", "y", "u", "8" });
            put("8" , new String[]{ "7", "u", "i", "9" });
            put("9" , new String[]{ "8", "i", "o", "0" });
            put("0" , new String[]{ "9", "o", "p", "-" });
            put("-" , new String[]{ "0", "p" });
            
            // 2nd from top
            put("q" , new String[]{ "1", "2", "w", "a" } );
            put("w" , new String[]{ "q", "a", "s", "e", "3", "2" } );
            put("e" , new String[]{ "w", "s", "d", "r", "4", "3" } );
            put("r" , new String[]{ "e", "d", "f", "t", "5", "4" } );
            put("t" , new String[]{ "r", "f", "g", "y", "6", "5" } );	
            put("y" , new String[]{ "t", "g", "h", "u", "7", "6" } );
            put("u" , new String[]{ "y", "h", "j", "i", "8", "7" } );
            put("i" , new String[]{ "u", "j", "k", "o", "9", "8" } );
            put("o" , new String[]{ "i", "k", "l", "p", "0", "9" } );
            put("p" , new String[]{ "o", "l", "-", "0", "q" } ); // How p and q become typos ??
            
    		// home row
            put("a" , new String[]{ "z", "s" , "w", "q" } );
            put("s" , new String[]{ "a", "z", "x", "d", "e", "w" } );
            put("d" , new String[]{ "s", "x", "c", "f", "r", "e" } );
            put("f" , new String[]{ "d", "c", "v", "g", "t", "r" } );
            put("g" , new String[]{ "f", "v", "b", "h", "y", "t" } );
            put("h" , new String[]{ "g", "b", "n", "j", "u", "y" } );
            put("j" , new String[]{ "h", "n", "m", "k", "i", "u" } );
            put("k" , new String[]{ "j", "m", "l", "o", "i" } );
            put("l" , new String[]{ "k", "p", "o", "i" } ); // changed 1 to i or just remove i??
            
    		// bottom row
            put("z" , new String[]{ "x", "s", "a" } );
            put("x" , new String[]{ "z", "c", "d", "s" } );
            put("c" , new String[]{ "x", "v", "f", "d" } );
            put("v" , new String[]{ "c", "b", "g", "f" } );
            put("b" , new String[]{ "v", "n", "h", "g" } );
            put("n" , new String[]{ "b", "m", "j", "h" } );
            put("m" , new String[]{ "n", "k", "j" });
            
        }
    });
	
	public Map<String, Map<String, Integer>> getAllTypos(String word){
		
		 
		Map<String, Integer> lstWrongKeyTypos = getWrongKeyTypos(word);
		Map<String, Integer> lstMissedCharTypos = getMissedCharTypos(word);
		Map<String, Integer> lstTransposedTypos = getTransposedCharTypos(word);
		Map<String, Integer> lstDoubleCharTypos = getDoubleCharTypos(word);
		
		Map<String, Map<String, Integer>> listAll = new HashMap<String, Map<String, Integer>>();
		
		listAll.put("lstWrongKeyTypos", lstWrongKeyTypos);
		listAll.put("lstMissedCharTypos", lstMissedCharTypos);
		listAll.put("lstTransposedTypos", lstTransposedTypos);
		listAll.put("lstDoubleCharTypos", lstDoubleCharTypos);
		
		return listAll;
	}
	
	/**
	 * 
	 * Create by Vu Hoai Nam
	 * Create date: Aug 29, 2012
	 * @param word
	 * @return
	 * <p>Description: Get all Typos into one list all
	 */
	public Map<String, Integer> getAllToOne(String word){
		
		Map<String, Integer> listAll = getWrongKeyTypos(word);
		listAll.putAll( getMissedCharTypos(word) );
		listAll.putAll( getTransposedCharTypos(word) );
		listAll.putAll( getDoubleCharTypos(word) );
		
		return listAll;
	}
	
	
	/**
	 * Create by Vu Hoai Nam
	 * Description: Generate the typos WrongKey
	 * @param word
	 * @return HashMap<String, Integer> list word with score
	 */
	public HashMap<String, Integer> getWrongKeyTypos(String word){
		word = word.toLowerCase();
		HashMap<String, Integer> typos = new HashMap<String, Integer>();
		StringBuffer wordBuffer = new StringBuffer(word);
		int length = wordBuffer.length();

		//check each character
		for( int i = 0; i < length; i++ )
		{
			//if character has replacements then create all replacements
			String[] charList = SIMILAR_CHAR_TABLE.get( ""+wordBuffer.charAt(i) );
			if(  charList != null )
			{
				//temp word for manipulating
				StringBuffer tempWord = new StringBuffer(word);
				
				for (int j = 0; j < charList.length; j++) {
					tempWord.replace(i,i+1,charList[j]);
					typos.put( tempWord.toString(), CompareNIST.howConfusableInt( word , tempWord.toString()) );					
				}
			}
		}
		
		return typos;
	}
	
	/**
	 * Create by Vu Hoai Nam
	 * Description: Generate the typos Missed
	 * @param word
	 * @return HashMap<String, Integer> list word with score
	 */
	public HashMap<String, Integer> getMissedCharTypos(String word){
		word = word.toLowerCase();
		HashMap<String, Integer> typos = new HashMap<String, Integer>();
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
			typos.put( tempWord.toString(), CompareNIST.howConfusableInt( word , tempWord.toString()) );
		}

		return typos;
	}
	
	/**
	 * Create by Vu Hoai Nam
	 * Description: Generate the typos transposed
	 * @param word
	 * @return HashMap<String, Integer> list word with score
	 * 
	 */
	public HashMap<String, Integer> getTransposedCharTypos(String word){
		word = word.toLowerCase();
		StringBuffer worfBuffer = new StringBuffer(word);
		HashMap<String, Integer> typos = new HashMap<String, Integer>();
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
				
				typos.put( tempWord.toString(), CompareNIST.howConfusableInt( word , tempWord.toString()) );
			}
		}
		
		return typos;
	}
	
	
	/**
	 * accepts a string returns array of likely double entered character typos 
	 * arrays contain only characters that are valid domain names
	 * @param word
	 * @return HashMap<String, Integer> list word with score
	 */
	public HashMap<String, Integer> getDoubleCharTypos(String word){
		word = word.toLowerCase();
		StringBuffer wordBuffer = new StringBuffer(word);
		HashMap<String, Integer> typos = new HashMap<String, Integer>(); 
		for (int i = 0; i < wordBuffer.length(); i++) {
			StringBuffer tempWord = new StringBuffer( wordBuffer.substring(0,i+1) );
			tempWord.append( wordBuffer.charAt(i) );
			
			if( i == ( wordBuffer.length() -1 ) ){}
			else 
				tempWord.append( wordBuffer.substring(i+1) );
			
			typos.put( tempWord.toString(), CompareNIST.howConfusableInt( word , tempWord.toString()) );
		}
		
		return typos;
	}
	
	/**
	 * <p>Description: Check whether firstWord is typos of secondWord.
	 * @param str1 : First word
	 * @param str2 : Second word
	 * @return True = yes ; False = no
	 */
	public boolean isTypos( String str1, String str2 ){
		
		Map<String, Integer> lstWrongKeyTypos = getWrongKeyTypos(str1);
		if( lstWrongKeyTypos.get(str2) != null ){
			return true;
		}
		
		Map<String, Integer> lstMissedCharTypos = getMissedCharTypos(str1);
		if( lstMissedCharTypos.get(str2) != null ){
			return true;
		}
		
		Map<String, Integer> lstTransposedTypos = getTransposedCharTypos(str1);
		if( lstTransposedTypos.get(str2) != null ){
			return true;
		}
		
		Map<String, Integer> lstDoubleCharTypos = getDoubleCharTypos(str1);
		if( lstDoubleCharTypos.get(str2) != null ){
			return true;
		}
		
		return false ;
	}
	
//	public static void main( String[] args ){
//		TypoGenerator typeos = new TypoGenerator();
//		boolean check = typeos.isTypos("namvu", "namvu");
//		System.out.print( check );
//	}

}
