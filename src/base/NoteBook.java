package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoteBook implements java.io.Serializable{
	private ArrayList<Folder> folders;
	private static final long serialVersionUID = 1L;

	public NoteBook(){
		folders = new ArrayList<Folder>();
	}
	public boolean createTextNote(String folderName, String title){
		TextNote note = new TextNote(title);
		return insertNote(folderName,note);
	}

	public boolean createTextNote(String folderName, String title, String content){
		TextNote note = new TextNote(title,content);
		return insertNote(folderName,note);
	}
	public boolean createImageNote(String folderName, String title){
		ImageNote note = new ImageNote(title);
		return insertNote(folderName,note);
	}
	public ArrayList<Folder> getFolders(){
		return folders;
	}
	public boolean insertNote(String folderName, Note note){
		Folder f = null;
		Folder tmp = new Folder(folderName);
		for(Folder f1:folders){
			if(tmp.equals(f1)){
				f = f1;
			}
		}
		if(f==null){
			f = tmp;
			folders.add(f);
		}
		//step 2
		ArrayList<Note> notes = f.getNotes();
		if(notes.size() != 0){
			for(Note n1:notes){
				if(n1.equals(note)){
					System.out.println("Creating note " + note.getTitle() + " under folder " + f.getName() + " failed.");
					return false;
				}
			}
		}
		f.addNote(note);
		return true;
	}
	public void sortFolders(){
		for(Folder o: folders){
			o.sortNotes();
		}
		Collections.sort(folders);
	}
	public List<Note> searchNotes(String keyword){
		ArrayList<Note> result = new ArrayList<Note>();
		for(Folder o: folders){
			result.addAll(o.searchNotes(keyword.toUpperCase()));
		}
		return result;
	}

	public boolean save(String file){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch(Exception e) {
		    return false;
		}
		return true;
	}

	public NoteBook(String file){
		FileInputStream fis = null;
	    ObjectInputStream in = null;
	    try{
	    	fis = new FileInputStream(file);
            in = new ObjectInputStream(fis);
	    	NoteBook n = (NoteBook)in.readObject();
	    	folders = n.folders;
	    	in.close();
	    } catch (Exception e){
	    	 e.printStackTrace();
	    }

	}
}
