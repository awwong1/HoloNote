Copyright 2013 Alexander Wong
   
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

--------------------------------------------------------------------------

CMPUT301 Fall 2013
Assignment 1
Student: Alexander Wong, awwong1
Application: HoloNote

HoloNote is an application designed for quick note taking, and basic
aggregate statistics. It is named based on the android Holo theme.

My philosophy behind Assignment 1 was keep it simple, stupid. I did not
use SQLite, but opted for the serialized object approach, saving to disk.
The following is a list of the assignment specifications, and my take on 
them. Granted, some of the assumptions that I made were more than liberal.

View the log entries (most recently added appearing first):
 - Done, it is sorted by datetime, most recent appearing at the top.
 - NOTE: spec says yyyy-mm-dd. I didn't do that. I used the default
 values from the java.util.date toString() method. It is more detailed,
 going beyond the detail of the spec values.
 
Add a new entry (appends to the log):
 - The newest entries appear on the top of the log (with the time as well)
 
Select and delete an existing entry:
Select and edit an existing entry:
 - Selecting a list in the log allows a user to edit/delete that log.
 - Users must either delete that log or commit their changes. (or leave 
 the app)
 
See the total number of characters typed for all entries:
See the total number of words typed for all entries:
See the total number of log entries created:
 - Pressing the menu button on the phone will show all these statistics.
 - These statistics update when the values change
 
See a list of the top 100 most common words:
See a word cloud or a word histogram of common words:
 - Navigating from the menu bar, clicking "Histogram/Stats" will show a 
 histogram of all words and their appropriate count length. 
 - NOTE: I assumed that the 100 most common words limit was due to the 
 word cloud, and that it would be messy to print out all the words in 
 cloud format. With my list format, I displayed all words sorted by their 
 count.
 
Always save and load the log automatically for the user:
 - The application is concurrent and will always write/read from the 
 savefile when available.
 
The application should assist the user in proper and consistent data entry.
 - When entering a new note, the application assists the user in entering 
 the correct time. When editing, the time value is displayed as the raw
 date.toString(). It will be parsed back and forth through the java util.
 - If the time field is left blank, the system will put in the current 
 time. I believe this is sufficient for system assisting the user.
 - NOTE: The user can still enter a custom date/time, so long as the
 proper format is maintained. 