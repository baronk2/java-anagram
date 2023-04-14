/*
Kevin Baron
4/20/13
CSE 143 Assignment #6
Anagram Solver
*/

import java.util.List;
import java.util.Map;
import java.util.LinkedList;//for dictionaries
import java.util.HashMap;//for associating words with their letter inventories
import java.util.Iterator;//for searching dictionaries

public class AnagramSolver {
	
	private List<String> dictionary;//copies the dictionary given by the client
	private List<String> relevantDictionary;//a reduced form of the given dictionary with only words that could be found in a given phrase
	private List<String> collected;//stores anagram words as they are put together and gets printed if a solution is found
	private Map<String, LetterInventory> lIDict;//stores the LetterInventory objects associated with each dictionary word
	private int max;//limit on number of words in anagram combinations
	
	//post: all fields have been initialized. the given list has been copied
	//      and all of it's words have been mapped to LetterInventory counterparts
	public AnagramSolver(List<String> list) {
		relevantDictionary = new LinkedList<String>();
		collected = new LinkedList<String>();
		max = 0;
		//copy the list
		dictionary = list;
		lIDict = new HashMap<String, LetterInventory>();
		Iterator<String> i = dictionary.iterator();
		//map each entry in the dictionary to its LetterInventory equivalent for quick access later
		while (i.hasNext()) {
			String s = i.next();
			lIDict.put(s, new LetterInventory(s));
		}//eo while
	}//eo AnagramSolver constructor
	
	//pre : max must be non-negative. throws IllegalArgumentException if not
	//post: every possible anagram of the given phrase available in the dictionary has been printed.
	//      nothing happends if no anagrams are found
	public void print(String s, int max) {
		if (max < 0)
			throw new IllegalArgumentException("max is less than 0: " + max);
		//store max as a field so that it does not need to be passed as a parameter for each recursion
		this.max = max;
		//obtain the LetterInventory of the given phrase (s).
		LetterInventory lI = new LetterInventory(s);
		//relevantDictionary is already cleared on the first time through, but print can be called
		//multiple times on the same object. start with a clean slate every time a new call on print is made
		relevantDictionary.clear();
		Iterator<String> i = dictionary.iterator();
		//build up relevantDictionary using only words that are contained by the given phrase
		while (i.hasNext()) {
			String word = i.next();
			//checking to see if the subtraction is null is the same as checking to see if the first 
			//LetterInventory contains the second because .subtract(...) returns null if any of the
			//resulting letter counts are negative. see LetterInventory.java
			if (lI.subtract(lIDict.get(word)) != null)
				relevantDictionary.add(word);
		}//eo while
		i = relevantDictionary.iterator();
		//enter the recursion for every word in relevantDictionary
		while (i.hasNext())
			findAmbigrams(lI, i.next());
	}//eo print
	
	//pre : the LetterInventory passed in has not been corrupted or turned null.
	//      the String passed in has been mapped to a LetterInventory in lIDict
	//post: all possible anagrams are printed. nothing happens if no anagrams are found
	private void findAmbigrams(LetterInventory original, String word) {
		//use the Map lIDict to quickly summon the words 
		LetterInventory wordLI = lIDict.get(word);
		//check to see if the remaining available letters (original) contain all the letters of thevgiven word
		if (original.subtract(wordLI) != null) {
			//if so, remove the word's letters from the remaining available letters and add the word to the collected list
			original = original.subtract(wordLI);
			collected.add(word);
			//base case: an anagram has been found and there are exactly zero remaining available letters. print the anagram
			if (original.size() == 0)
				System.out.println(collected);
			//recursive case: check to see if the max has been reached (excepted if max is 0).
			//if not, keep searching relevantDictionary for a solution, starting from the top
			else if (collected.size() < max || max == 0) {
				Iterator<String> i = relevantDictionary.iterator();
				while (i.hasNext()) {
					findAmbigrams(original, i.next());
				}//eo while
			}//eo else if
			//CRITICAL to recursive backtracking: undo what was done at the beginning of the recursive call.
			//put the letters taken out of the remaining available letters back in
			//and take the word off of the end of the collected list
			original.add(wordLI);
			collected.remove(collected.lastIndexOf(word));
		}//eo if
	}//eo findAmbigrams
	
}//eo AnagramSolver class