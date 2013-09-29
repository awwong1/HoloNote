/**
 * Copyright 2013 Alexander Wong
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexanderwwong.holonote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Maintains the array of Notes in the application. Complexity of sorting and
 * getting the statistical data of the words lay inside this class. Allows
 * editing/removing/adding notes, conversion of notes to strings. All
 * appropriate getters and setters have been set.
 */

public class NoteLog {
	private ArrayList<Note> fullNotes;

	public NoteLog() {
		this.fullNotes = new ArrayList<Note>();
	}

	public ArrayList<Note> getFullNotes() {
		return fullNotes;
	}

	public void setFullNotes(ArrayList<Note> fullNotes) {
		this.fullNotes.clear();
		this.fullNotes = fullNotes;
	}

	public void addNote(Note note) {
		this.fullNotes.add(note);
	}
	
	public void addNote(Date tdate, String tsubject, String tbody) {
		Note tempNote = new Note(tdate, tsubject, tbody);
		this.fullNotes.add(tempNote);
	}

	public Note getNote(int index) {
		return fullNotes.get(index);
	}

	public void removeNote(int index) {
		fullNotes.remove(index);
	}

	public void editNote(int index, Note note) {
		this.fullNotes.set(index, note);
	}
	
	public void editNote(int index, Date tdate, String tsubject, String tbody) {
		Note tempNote = new Note(tdate, tsubject, tbody);
		this.fullNotes.set(index, tempNote);
	}

	public void clear() {
		this.fullNotes.clear();
	}

	/** Comparator class for list sorting */
	private class CustomComparatorList implements Comparator<Note> {
		public int compare(Note o1, Note o2) {
			return o2.getNoteDate().compareTo(o1.getNoteDate());
		}
	}

	/** Doing sorting of the list */
	public void sortNotes() {
		Collections.sort(fullNotes, new CustomComparatorList());
	}

	/** Returns an ArrayList String of all notes */
	public ArrayList<String> getStringNotes() {
		ArrayList<String> allNotes = new ArrayList<String>();
		for (Note nte : fullNotes) {
			allNotes.add(nte.toString());
		}
		return allNotes;
	}

	/** Returns an ArrayList String of all unformatted words */
	private ArrayList<String> getRawWords() {
		ArrayList<String> allWords = new ArrayList<String>();
		for (Note nte : fullNotes) {
			if (!nte.getNoteSubject().equals("")) {
				allWords.addAll(Arrays.asList(nte.getNoteSubject()
						.split("\\s+")));
			}
			if (!nte.getNoteBody().equals("")) {
				allWords.addAll(Arrays.asList(nte.getNoteBody().split("\\s+")));
			}
		}
		return allWords;
	}

	/**
	 * Returns an ArrayList String of all formatted words By formatted, words
	 * have all punctuation stripped and are capitalized. Free floating
	 * punctuation will be a word "". This is valid.
	 */
	private ArrayList<String> getFormatWords() {
		ArrayList<String> allWords = getRawWords();
		ArrayList<String> allFormatWords = new ArrayList<String>();
		Locale locale = new Locale("ENGLISH");
		for (String word : allWords) {
			allFormatWords.add(word.replaceAll("\\W", "").toUpperCase(locale));
		}
		return allFormatWords;
	}

	/** Returns the number of words in all entries */
	public int numWords() {
		return getRawWords().size();
	}

	/** Returns the number of entries */
	public int numNotes() {
		return fullNotes.size();
	}

	/** Returns the number of characters in all entries */
	public int numChars() {
		int numchars = 0;
		for (Note nte : fullNotes) {
			if (!nte.getNoteSubject().equals("")) {
				numchars += nte.getNoteSubject().toCharArray().length;
			}
			if (!nte.getNoteBody().equals("")) {
				numchars += nte.getNoteBody().toCharArray().length;
			}
		}
		return numchars;
	}

	/** Comparator class for word sorting */
	private class CustomComparatorWord implements Comparator<WordCount> {
		public int compare(WordCount o1, WordCount o2) {
			return o2.getCount() - o1.getCount();
		}
	}

	/**
	 * Returns a sorted ArrayList String of the frequency of all words. Use the
	 * formatted words list. Formatted words criteria apply here. Words: all
	 * caps, no punctuation. Floating punctuation = "" Words are separated by
	 * spaces.
	 */
	public ArrayList<WordCount> getWordFrequency() {
		ArrayList<WordCount> wordCount = new ArrayList<WordCount>();
		ArrayList<String> wordFrequency = new ArrayList<String>();
		ArrayList<String> allWords = getFormatWords();
		// Get the list of common words
		for (String word : allWords) {
			if (wordFrequency.contains(word)) {
				int index = wordFrequency.indexOf(word);
				wordCount.set(index, new WordCount(wordCount.get(index)
						.getCount() + 1, word));
			} else {
				wordFrequency.add(word);
				wordCount.add(new WordCount(word));
			}
		}
		Collections.sort(wordCount, new CustomComparatorWord());
		return wordCount;
	}

	/**
	 * Returns an ArrayList String of word frequency texts Display as text style
	 * "<Word>" Frequency: <Frequency>
	 */
	public ArrayList<String> getStringWordFrequency() {
		ArrayList<WordCount> wordCount = getWordFrequency();
		ArrayList<String> stringWC = new ArrayList<String>();
		for (WordCount wc : wordCount) {
			stringWC.add(wc.getString());
		}
		return stringWC;
	}

	/**
	 * Returns an ArrayList String of spaces, matching the index of the word The
	 * idea here is to emulate a histogram bar with spaces as units
	 */
	public ArrayList<String> getSpaceWordFrequency() {
		ArrayList<WordCount> wordCount = getWordFrequency();
		ArrayList<String> stringWC = new ArrayList<String>();
		for (WordCount wc : wordCount) {
			stringWC.add(wc.getCountSpace());
		}
		return stringWC;
	}
}
