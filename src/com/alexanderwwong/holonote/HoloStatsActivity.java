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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity for displaying the words and their respective counts, and the
 * histrogram bars. Will never write to the notelog, only read. No writing to
 * file is necessary here.
 */

public class HoloStatsActivity extends Activity {
	private ListView statsListView;
	private static NoteLog notelog;
	private ArrayList<WordCount> countlist;
	private ArrayAdapter<WordCount> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		statsListView = (ListView) findViewById(R.id.statslist);
		/* Create the Activity's one true notelog */
		notelog = new NoteLog();
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadFromFile();
		countlist = notelog.getWordFrequency();
		/* Show this baby on the screen */
		adapter = new WordCountAdapter(this, R.layout.stats_item, countlist);
		statsListView.setAdapter(adapter);

	}

	@Override
	public void onPause() {
		super.onPause();
		notelog.clear();
	}

	/**
	 * load all the notes from the file, required here because of OIS No need to
	 * save to file, this activity never modifies data
	 */
	private void loadFromFile() {
		try {
			FileInputStream fis = openFileInput(HoloNoteActivity.FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			while (true) {
				Note val = (Note) ois.readObject();
				notelog.addNote(val);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** Required for UI sublist view (histogram text/bar) */
	public class WordCountAdapter extends ArrayAdapter<WordCount> {
		private ArrayList<WordCount> items;
		private WordCountHolder holder;

		private class WordCountHolder {
			TextView wcString;
			TextView wcBar;
		}

		public WordCountAdapter(Context context, int tvResId,
				ArrayList<WordCount> items) {
			super(context, tvResId, items);
			this.items = items;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.stats_item, null);
				holder = new WordCountHolder();
				holder.wcString = (TextView) v.findViewById(R.id.stat_item);
				holder.wcBar = (TextView) v.findViewById(R.id.stat_bar);
				v.setTag(holder);
			} else {
				holder = (WordCountHolder) v.getTag();
			}
			WordCount wc = items.get(pos);
			if (wc != null) {
				holder.wcString.setText(wc.getString());
				holder.wcBar.setText(wc.getCountSpace());
			}
			return v;
		}
	}
}
