package com.org.kobito.kobitosimilarity.services;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vu Hoai Nam
 * <p>Create date: July 28, 2012
 * 
 * <p>This class translate the NIST from Python to Java.
 * <p>It keeping the same function and variables naming from NIST.
 * <p>And Adding some extra function to generate the words base on the compare algorithm of NIST
 * <p>For more information about NIST algorithm, please check <a href="http://hissa.nist.gov/~black/GTLD/tldVisualSimilarity.html">NIST</a>
 * 
 */
public class CompareNIST {

	private static Map<String, Set<String>> similarMap = new HashMap<String, Set<String>>();
	private static Map<String, Map<String, Double>> pointMap = new HashMap<String, Map<String, Double>>();

	
	/**
	 * Generate String using recursive.
	 * 
	 * @param original The original base word where processed to generate the similar words
	 * @param threshold The bottom limit of percentage.( The similar word will be from threshold to 99% )
	 * @return HashMap< String , Integer > 
	 * String is the key of no duplicate words , Integer is the score compared with the word
	 */
	public static HashMap< String , Integer > generateStr(String original, int threshold ) {
		
		original = original.toLowerCase();
		
		//create the collection of similar char
		ArrayList<Set<String>> listSimiliarChr = new ArrayList<Set<String>>();
		for (int i = 0; i < original.length(); i++) {
			String chr = String.valueOf(original.charAt(i));
			Set<String> setChr = new HashSet<String>();
			setChr.add(chr);
			Set<String> similarChar = similarMap.get(chr);
			
			if( similarChar !=null )
			setChr.addAll(similarChar);
			
			listSimiliarChr.add(setChr);
		}
		
		Queue<String> tmp = new ConcurrentLinkedQueue<String>();
		Queue<String> tmp2 = new ConcurrentLinkedQueue<String>();
		
		System.out.println(listSimiliarChr);
		
		//Check where recurse to go 
		int charAt = 0;
		
		StringBuffer doubleCharBuff = new StringBuffer(original);
		StringBuffer originalBuff = new StringBuffer(original);
		
		Set<String> doubleSimilar = null;
		
		//Recurse the char collection list
		for (Set<String> set : listSimiliarChr) {
			charAt++;
			if(tmp.size() > 0){
				
				//In case replace one char
				for (String string : set) {
						
					for (String tmpStr : tmp) {
						
						tmpStr = tmpStr.concat(string);
						
						StringBuilder builder = new  StringBuilder();
						
						builder.append(tmpStr);
						
						builder.append( originalBuff.substring((charAt)) );
						
						String tmpCompareStr  = builder.toString();
						
						int score = (int) ( compareShortStr( original , tmpCompareStr ) * 100);
						
						if( score >= threshold )
							tmp2.add(tmpStr);
					}
				}
				
				//In case from second charAt - search the double similar
				if(charAt > 1){
					doubleSimilar = similarMap.get(doubleCharBuff.substring( ( charAt -2 ), ( charAt )  ));
					//For double char replace
					if( doubleSimilar != null ){
						for (String similarCharWithDouble : doubleSimilar) {
							StringBuffer processCharBuff = new StringBuffer(original);
							processCharBuff.replace( ( charAt - 2 ), charAt ,  similarCharWithDouble );
							int score = (int) ( compareShortStr( original , processCharBuff.toString() ) * 100);
							if( score >= threshold )
								tmp2.add(processCharBuff.toString());
						}
					}
				}
				
				tmp.clear();
				tmp.addAll(tmp2);
			}
			else 
			 tmp.addAll(set);
		}
		
		
		HashMap< String , Integer > listResult = compareWithNIST(original, tmp2.toArray(new String[tmp2.size()]), threshold);
		
		//Remove the 100% similar item
		listResult.remove(original);
		
		return listResult ;
	}
	
	/**
	 * Create by Vu Hoai Nam
	 * <p>Create date: Aug 31, 2012
	 * @param left: is the basic word
	 * @param right: is the list of words be generated ( All words )
	 * @param threshold : the limit of percentage to add to list
	 * @return : HashMap of list compared word with percentage
	 * <p>Description: This function will compare the list of words with one word, then return 
	 * a map of list word with percentage 
	 */
	public static HashMap<String, Integer > compareWithNIST( String left, String[] right , int threshold ){
		
		CompareNIST.init();
		ArrayList<Double> resultList = new ArrayList<Double>();
		
		for (int i = 0; i < right.length; i++) {
			Double result = CompareNIST.howConfusableAre(left, right[i]);
			resultList.add(result);
		}
		
		HashMap< String , Integer > listResult = new HashMap<String, Integer>();

		//Populate the list result to HashMap
		if(resultList.size() == right.length)
		for (int i = 0; i < resultList.size(); i++) {
			int strScore = (int)( resultList.get(i)  * 100);
			if(strScore >= threshold )
			listResult.put(right[i], strScore);
		}

		return listResult ;
	}
	
	
	/**
	 * Compare the short string
	 * @param str1
	 * @param str2
	 * @return bouble The percent of similar between str1 and str2
	 */
	public static double compareShortStr( String str1, String str2 ){

		if ( !str1.equals(str2) ) {
			return howConfusableAre(str1, str2);
		}
		else
			return 1;
	}
	
	/**
	 * Compare the short string
	 * @param word The word be compared with list string ( lstStr )
	 * @param lstStr List String wish to compare with word.
	 * @return Set<SimilarWord> The list of lstStr with score compared to word
	 */
//	public static List<SimilarWord> compareWithList( String word, String[] lstStr ){
//
//		if( similarMap == null || pointMap == null ){
//			init();
//		}
//		ArrayList<SimilarWord> returnSet = new ArrayList<SimilarWord>();
//
//		for (int i = 0; i < lstStr.length; i++) {
//			System.out.println("LIne" + lstStr[i]);
//			if( lstStr[i] != null && !lstStr[i].equals("") ){
//
//				SimilarWord similarWord = new SimilarWord();
//				String str = lstStr[i].toLowerCase();
//				similarWord.setWord(str);
//				similarWord.setScore(howConfusableInt( word , str ));
//				similarWord.setTypos( new TypoGenerator().isTypos(word, str) );
//				returnSet.add(similarWord);
//
//			}
//		}
//		return returnSet;
//	}
	

	/**
	 * Init the similar Map
	 * <p>Parse the mapping similar char to Map<String,Set<String>>
	 * <p>And similar point to Map<String, Map<String, Double>>
	 */
	public static void init() {
		ClassLoader loader = CompareNIST.class.getClassLoader();

		//TODO - Make dynamic reading the mapping source when run on server on test on local
		//URL location = CompareNIST.class.getProtectionDomain().getCodeSource().getLocation();
		Scanner sc = new Scanner(loader.getResourceAsStream("mapping"));
		while (sc.hasNextLine()) {
			String ch1 = sc.next().toLowerCase();
			String ch2 = sc.next().toLowerCase();
			double point = Double.parseDouble(sc.next());

			Set<String> set1 = similarMap.get(ch1);
			if (set1 == null) {
				set1 = new HashSet<String>();
				similarMap.put(ch1, set1);
			}
			set1.add(ch2);

			Set<String> set2 = similarMap.get(ch2);
			if (set2 == null) {
				set2 = new HashSet<String>();
				similarMap.put(ch2, set2);
			}
			set2.add(ch1);

			Map<String, Double> map1 = pointMap.get(ch1);
			if (map1 == null) {
				map1 = new HashMap<String, Double>();
				pointMap.put(ch1, map1);
			}
			map1.put(ch2, point);

			Map<String, Double> map2 = pointMap.get(ch2);
			if (map2 == null) {
				map2 = new HashMap<String, Double>();
				pointMap.put(ch2, map2);
			}
			map2.put(ch1, point);
		}
	}

	/**
	 * Calculate the different between two words - Base on NIST code in Python
	 * @param str1
	 * @param str2
	 * @return bouble The percent of similar between str1 and str2
	 */
	public static double howConfusableAre(String str1, String str2) {
		double score;
		
		int maxlen = Math.max(str1.length(), str2.length());
		int lendiff = Math.abs(str1.length() - str2.length());
		if (maxlen == 0) {
			score = 1;
		} else {
			double levDist = levenshtein(str1, str2);
			score = (maxlen - levDist) / (maxlen + 3 * levDist + lendiff * levDist);
		}
		return score;
	}
	
	/**
	 * Calculate the different between two words - Base on NIST code in Python
	 * @param str1
	 * @param str2
	 * @return int The percent of similar between str1 and str2
	 */
	public static int howConfusableInt(String str1, String str2) {
		int score = (int)( howConfusableAre( str1, str2) * 100);
		return score;
	}

	/**
	 * The levernshtein calculate - Base on NIST code in Python
	 * @param s
	 * @param t
	 * @return bouble The distant between s and t
	 */
	private static double levenshtein(String s, String t) {
		s = s.toLowerCase();
		t = t.toLowerCase();

		int effInfinity = Integer.MAX_VALUE;

		int len_s = s.length();
		int len_t = t.length();
		int max_l = Math.max(len_s, len_t);

		double[][] d = new double[max_l + 1][];
		d[0] = new double[max_l + 1];
		for (int i = 0; i < d[0].length; i++) {
			d[0][i] = i;
		}
		for (int i = 1; i < len_s + 1; i++) {
			d[i] = new double[max_l + 1];
			d[i][0] = i;
		}

		for (int i = 0; i < len_s; i++) {
			for (int j = 0; j < len_t; j++) {
				// System.out.println(i + " " + j);
				double minCost = effInfinity;

				// delete
				minCost = Math.min(d[i][j + 1] + 1, minCost);

				// insert
				minCost = Math.min(d[i + 1][j] + 1, minCost);

				// insert after repeat
				double repiCost = repetitionInsert(s, i, t, j);
				if (repiCost >= 0) {
					minCost = Math.min(d[i + 1][j] + repiCost, minCost);
				}

				// delete after repeat
				double repdCost = repetitionInsert(t, j, s, i);
				if (repdCost >= 0) {
					minCost = Math.min(d[i][j + 1] + repdCost, minCost);
				}

				// substitute
				double subsCost = 1 - characterSimilarity(String.valueOf(s.charAt(i)), String.valueOf(t.charAt(j)));
				minCost = Math.min(d[i][j] + subsCost, minCost);

				// compute total costs of 2 for 1, 1 for 2, or 2 for 2
				// substitution
				if (i > 0) {
					double subs21Cost = 1 - characterSimilarity(s.substring(i - 1, i + 1), String.valueOf(t.charAt(j)));
					if (subs21Cost == 1) {
						subs21Cost = 2;
					}
					minCost = Math.min(d[i - 1][j] + subs21Cost, minCost);
				}

				if (j > 0) {
					double subs12Cost = 1 - characterSimilarity(String.valueOf(s.charAt(i)), t.substring(j - 1, j + 1));
					if (subs12Cost == 1) {
						subs12Cost = 2;
					}
					minCost = Math.min(d[i][j - 1] + subs12Cost, minCost);
				}

				if (i > 0 && j > 0) {
					double subs22Cost = 1 - characterSimilarity(s.substring(i - 1, i + 1), t.substring(j - 1, j + 1));
					if (subs22Cost == 1) {
						subs22Cost = 2;
					}
					minCost = Math.min(d[i - 1][j - 1] + subs22Cost, minCost);

					if (s.charAt(i - 1) == t.charAt(j) && s.charAt(i) == t.charAt(j - 1)) {
						double transpCost = 1 - characterSimilarity(String.valueOf(s.charAt(i)), String.valueOf(t.charAt(j)));
						minCost = Math.min(d[i - 1][j - 1] + transpCost, minCost);
					}
				}

				d[i + 1][j + 1] = minCost;
			}
		}

		return d[len_s][len_t];
	}

	/**
	 * repetitionInsert - Base on NIST code in Python
	 * @param s
	 * @param i
	 * @param t
	 * @param j
	 * @return double the repetitionInsert between s and t
	 */
	private static double repetitionInsert(String s, int i, String t, int j) {
		if (i < 1 || j < 2 || !s.substring(i - 1, i + 1).equals(t.substring(j - 2, j)) || !t.substring(j - 2, j).equals(t.charAt(j) + t.charAt(j))) {
			return -1;
		}

		int back = 2;
		char mc = t.charAt(j);

		while (back <= i && back < j && s.charAt(i - back) == mc && t.charAt(j - back - 1) == mc) {
			back += 1;
		}

		double cost = Math.max(0, 1.7 - 0.4 * back);

		return cost;
	}

	public static double characterSimilarity(String ch1, String ch2) {
		double point = 0;
		if (ch1.equals(ch2)) {
			point = 1;
		} else {
			if (pointMap.containsKey(ch1)) {
				Double p = pointMap.get(ch1).get(ch2);
				if (p != null) {
					point = p;
				}
			}
		}
		return point;
	}

}
