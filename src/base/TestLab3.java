package base;
import java.util.ArrayList;
import java.util.List;

import base.Folder;
import base.Note;
import base.NoteBook;

public class TestLab3 {
	public static void main(String args[]){

		NoteBook nb = new NoteBook();
		nb.createTextNote("Java", "COMP30213021 syllabus", "in. in in");
		nb.createTextNote("Java", "course information", "is");
		nb.createTextNote("Lab", "Lab requirement","Each lab has 2 credits, 1 for attendence the other is based the completeness of your lab.");

		nb.createImageNote("Course", "Time Tables");
		nb.createImageNote("Assignment", "Assignment Lists");
		nb.createImageNote("CSE", "Lab Session");
		nb.createTextNote("Java", "marking scheme", "The quizzes and lab grades will be given based on your attendance in quizze and lab, respectively");
		nb.createTextNote("Java", "marking scheme1", "The quizzes and lab grades will be given based on your attendance in quizze and lab, respectively  is is is");
		nb.createImageNote("Java", "java Attendance Checking");

		nb.sortFolders();
		int findex = 0;
		for (Folder folder : nb.getFolders()) {
			System.out.println("Folder " + findex++ + ":" + folder.toString());
			List<Note> notes = folder.getNotes();
			int nindex = 0;
			for (Note note : notes) {
				System.out.println("--" + nindex++ + ":" + note.toString());
			}
			folder.findFrequentWord();
		}

		/*List<Note> notes = nb.searchNotes("java or LAB attendance OR SESSION");
		System.out.println("Search Results:");
		if (notes == null || notes.size() == 0) {
			System.out.println("No Search Results");
		} else  {
			for (Note note : notes) {
				System.out.println(note.toString());
			}
		}*/


	}
}