/*
Kevin Baron
4/7/13
CSE 143 Assignment #3
Letter Inventory
*/

import java.lang.Character;

public class LetterInventory {
	
	private int[] charCount;// an array to store how many of each letter
	private String cleanChars;// a not-necessarily-ordered String of all the letters accounted for in charCount
	
	//post: a LetterInventory object has been created
	public LetterInventory(String s) {
		s = s.toLowerCase();
		//remove the non-letter charcters to keep the 'clean' ones.
		cleanChars = "";
		for (int i = 0; i < s.length(); i++) {
			char current = s.charAt(i);
			if(current >= 'a' && current <= 'z')
				cleanChars += current;
		}//eo for
		//initialize charCount to hold all 26 letters
		//run through cleanChars to count all the letters
		charCount = new int[26];
		for (int i = 0; i < cleanChars.length(); i++)
			charCount[cleanChars.charAt(i) - 'a']++;
	}//eo LetterInventory constructor
	
	//pre : passed a valid letter. If not, throws IllegalArgumentException
	//post: the number of occurences of this letter has been returned
	public int get(char letter) {
		letter = Character.toLowerCase(letter);
		if (letter < 'a' || letter > 'z') 
			throw new IllegalArgumentException();
		else
			return charCount[letter - 'a'];
	}//eo get
	
	//pre : passed a valid letter and nonnegative value. If not, throws IllegalArgumentException
	//post: the number of occurences of this letter has been changed
	public void set(char letter, int value) {
		letter = Character.toLowerCase(letter);
		if (letter < 'a' || letter > 'z' || value < 0) 
			throw new IllegalArgumentException();
		else {
			//changes the array's data
			charCount[letter - 'a'] = value;
			//takes all occurences of the letter out of cleanChars
			//appends the correct number of occurences to the end of cleanChars
			cleanChars = cleanChars.replaceAll("" + letter, "");
			for (int i = 0; i < value; i++)
				cleanChars += letter;
		}//eo else
	}//eo set
	
	//post: the number of letters in the inventory has been returned
	public int size() {
		return cleanChars.length();
	}//eo size
	
	//post: returns whether or not the inventory has any letters in it
	public boolean isEmpty() {
		return size() == 0;
	}//eo isEmpty
	
	//post: returns all the letters in the inventory in sorted order enclosed by square brackets
	public String toString() {
		String result = "[";
		for (int i = 0; i < 26; i++)
			for (int j = 0; j < charCount[i]; j++)
				result += (char) ('a' + i);
		return result + "]";
		//Annother way to get the proper sorted String.
		//import java.util.Arrays in order to use
		/*
		char[] chAr = cleanChars.toCharArray();
		Arrays.sort(chAr);
		return "[" + new String(chAr) + "]";
		*/
	}//eo toString
	
	//post: A LetterInventory representing all of the letters in both constituent LetterInventorys has been returned
	//(neither constituent LetterInventory object has been altered)
	public LetterInventory add(LetterInventory other) {
		return new LetterInventory(this.cleanChars + other.cleanChars);
	}//eo add
	
	//pre : this LetterInventory object contains at least every letter from the other LetterInventory object
	//if not, returns null
	//post: A LetterInventory object representing all of the letters in this but not other has been returned
	//(neither this nor other has been altered)
	public LetterInventory subtract(LetterInventory other) {
		LetterInventory result = new LetterInventory("");
		for (int i = 0; i < 26; i++) {
			char letter = (char) ('a' + i);
			int value = this.get(letter) - other.get(letter);
			if (value < 0)
				return null;
			result.set(letter, value);
		}//eo for
		return result;
	}//eo subtract
	
	public boolean contains(LetterInventory other) {
		return this.subtract(other) != null;
	}//eo contains
	
}//eo LetterInventory class