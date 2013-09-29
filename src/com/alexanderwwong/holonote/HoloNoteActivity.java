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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/*
 * This file is a heavily modified version of the LonelyTwitter file provided by the 
 * University of Alberta's CMPUT301 Fall 2013 lab, courtesy of Joshua Campbell. 
 * Anyone with access to the CMPUT301F13 eclass may find the referenced file 
 * at the provided link: https://eclass.srv.ualberta.ca/mod/resource/view.php?id=794448
 */

/**
 * This activity displays the log of all notes, as well as the note editing
 * field at the bottom of the screen. This activity handles all the ui elements,
 * but it also writes and reads from the file using the necessary input streams.
 */

public class HoloNoteActivity extends Activity {

	/* One true file location to store all the notes */
	public static final String FILENAME = "file.sav";

	/* Handling the UI */
	private EditText bodyText;
	private EditText subjectText;
	private EditText datetimeText;
	private TextView noteNumber;
	private Button clearButton;
	private Button saveButton;
	private ListView oldNotesList;
	private ArrayList<String> stringNotes;
	private ArrayAdapter<String> adapter;

	/* Handling the not UI */
	private static NoteLog notelog;
	private Menu mainmenu;

	/* Updates the date time hint 'automatically' */
	private final Handler handler = new Handler();
	private final Runnable runnable = new Runnable() {
		public void run() {
			datetimeText.setHint(new Date(System.currentTimeMillis())
					.toString());
		}
	};

	/* Editing values (During the editing) */
	private boolean editing;
	private int editInt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* setup all the form fields */
		subjectText = (EditText) findViewById(R.id.subject);
		bodyText = (EditText) findViewById(R.id.body);
		datetimeText = (EditText) findViewById(R.id.datetime);
		noteNumber = (TextView) findViewById(R.id.noteNum);
		saveButton = (Button) findViewById(R.id.save);
		clearButton = (Button) findViewById(R.id.clear);
		oldNotesList = (ListView) findViewById(R.id.oldNotesList);

		/* Create the Activity's one true notelog */
		notelog = new NoteLog();

		/* do not allow editing on startup */
		editing = false;

		/* setup the timer on startup, for the timehint */
		Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateTimeHint();
			}

			private void updateTimeHint() {
				handler.post(runnable);
			}
		}, 0, 1000);

		/** Save the current string note values in field */
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/*
				 * check the datetime string, assume user is pedantic if the
				 * parse was not done properly, simply use current time by
				 * default, the application 'automatically' enters in the
				 * current datetime
				 */
				setResult(RESULT_OK);
				String subject = subjectText.getText().toString();
				String note = bodyText.getText().toString();
				String sdatetime = datetimeText.getText().toString();
				Date datetime;
				if (sdatetime.equals("")) {
					datetime = new Date(System.currentTimeMillis());
				} else {
					try {
						datetime = new Date(sdatetime);
					} catch (Exception e) {
						datetime = new Date(System.currentTimeMillis());
					}
				}
				if (editing == true) {
					notelog.editNote(editInt, datetime, subject, note);
					editing = false;
				} else {
					notelog.addNote(datetime, subject, note);
				}
				syncList();
				clearFields();
			}
		});

		/** Clear the current values in the fields, or delete the editing note */
		clearButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				if (editing == true) {
					editing = false;
					notelog.removeNote(editInt);
				}
				syncList();
				clearFields();
			}
		});

		/** Check whether or not to edit this selection */
		oldNotesList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long myLong) {
				// check editing
				if (editing == false) {
					editing = true;
					editInt = myItemInt;
					Note editNote = notelog.getNote(editInt);
					// index by one for sake of non-programmers
					noteNumber.setText("Editing\nNote " + (editInt + 1));
					datetimeText.setText(editNote.getNoteDate().toString());
					subjectText.setText(editNote.getNoteSubject());
					bodyText.setText(editNote.getNoteBody());
					clearButton.setText(R.string.delete);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadFromFile();
		syncList();
	}

	@Override
	/** Show some basic stats, allow navigation to histogram/stats page */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(1, 1, 0, "Number of Notes: " + notelog.numNotes());
		menu.add(1, 2, 1, "Number of Words: " + notelog.numWords());
		menu.add(1, 3, 2, "Number of Chars: " + notelog.numChars());
		menu.add(1, 4, 3, "Histogram/Stats");
		mainmenu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			// Number of notes, do nothing
			return true;
		case 2:
			// Number of words, do nothing
			return true;
		case 3:
			// Number of characters, do nothing
			return true;
		case 4:
			// Do the histogram or cloud statistics stuff here
			Intent intent = new Intent(this, HoloStatsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause();
		notelog.clear();
	}

	/** load all the notes from the file, required here because of OIS */
	private void loadFromFile() {
		try {
			FileInputStream fis = openFileInput(FILENAME);
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

	/** Save all notes to the file, required here because of OOS */
	private void saveAllNotes() {
		ArrayList<Note> fullNotes = notelog.getFullNotes();
		try {
			FileOutputStream fos = openFileOutput(FILENAME, 0);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for (Note nte : fullNotes) {
				oos.writeObject(nte);
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Sync the stringNotes to the notelog notes */
	private void syncList() {
		notelog.sortNotes();
		saveAllNotes();
		stringNotes = notelog.getStringNotes();
		adapter = new ArrayAdapter<String>(this, R.layout.list_item,
				stringNotes);
		oldNotesList.setAdapter(adapter);
	}

	/** Set all the edit text fields to the empty state */
	private void clearFields() {
		datetimeText.setText("");
		subjectText.setText("");
		bodyText.setText("");
		noteNumber.setText("");
		clearButton.setText(R.string.clear);
		onCreateOptionsMenu(mainmenu);
	}
}