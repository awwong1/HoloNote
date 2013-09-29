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

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/*
 * This file is a heavily modified version of the LonelyTweet file provided by the 
 * University of Alberta's CMPUT301 Fall 2013 lab, courtesy of Joshua Campbell. 
 * Anyone with access to the CMPUT301F13 eclass may find the referenced file 
 * at the provided link: https://eclass.srv.ualberta.ca/mod/resource/view.php?id=794448
 */

/**
 * This class object holds the note data. It stores the note as an object
 * combination of Date, String (subject) and String (body). The object has the
 * appropriate getters and setters. There is a toString function that will
 * visualize the empty string as "<null>", as well as format the string nicely
 * for viewing purposes.
 */

public class Note implements Serializable {

	private static final long serialVersionUID = 609756716977307762L;
	private Date noteDate;
	private String noteSubject;
	private String noteBody;

	public Note(Date date, String subject, String body) {
		super();
		this.noteDate = date;
		this.noteSubject = subject;
		this.noteBody = body;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(noteDate);
		out.writeObject(noteSubject);
		out.writeObject(noteBody);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		noteDate = (Date) in.readObject();
		noteSubject = (String) in.readObject();
		noteBody = (String) in.readObject();
	}

	@Override
	public String toString() {
		String tnoteSubject = noteSubject;
		String tnoteBody = noteBody;
		if (noteSubject.equals("")) {
			tnoteSubject = "<null>";
		}
		if (noteBody.equals("")) {
			tnoteBody = "<null>";
		}
		return "Date: " + noteDate.toString() + "\nSubject: " + tnoteSubject
				+ "\n" + tnoteBody + "";
	}

	public Date getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(Date noteDate) {
		this.noteDate = noteDate;
	}

	public String getNoteSubject() {
		return noteSubject;
	}

	public void setNoteSubject(String noteSubject) {
		this.noteSubject = noteSubject;
	}

	public String getNoteBody() {
		return noteBody;
	}

	public void setNoteBody(String noteBody) {
		this.noteBody = noteBody;
	}

}
