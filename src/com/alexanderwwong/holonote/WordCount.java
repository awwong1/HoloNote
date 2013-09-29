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

/** 
 * Helper class object for unique word counting, word sorting 
 * Stores a count of the word as well as the word string itself.
 */

public class WordCount {
	public int count;
	public String word;

	protected WordCount(String tword) {
		this.count = 1;
		this.word = tword;
	}

	protected WordCount(int tcount, String tword) {
		this.count = tcount;
		this.word = tword;
	}

	protected int getCount() {
		return count;
	}

	protected String getCountSpace() {
		String countSpace = "";
		for (int icount = 0; icount < count; icount++) {
			countSpace = countSpace + " ";
		}
		return countSpace;
	}

	protected String getString() {
		return "\"" + word + "\" Frequency: " + count;
	}
}