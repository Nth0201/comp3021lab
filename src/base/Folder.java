package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Folder implements Comparable<Folder>{
	private ArrayList<Note> notes;
	private String name;

	public Folder(String name){
		this.name = name;
		notes = new ArrayList<Note>();
	}

	public void addNote(Note note){
		notes.add(note);
	}
	public String getName(){
		return name;
	}
	public ArrayList<Note> getNotes(){
		return notes;
	}

	public void sortNotes(){
		Collections.sort(notes);
	}

	public List<Note> searchNotes(String keyword){
		String[] splitStr = keyword.trim().split("\\s+");
		ArrayList<Note> CurrentSearch = new ArrayList<Note>();
		ArrayList<Note> SearchRange = notes;
		boolean or_flag = false;
		boolean updateRange = false;
		boolean notexistSame = true;
		for(String searchWord : splitStr){
			if(searchWord.equals("OR")||searchWord.equals("or")){
				or_flag = true;
			}else{

					// doing And operation
					//update search range
					if(or_flag){
						or_flag = false;
					} else if(updateRange){
						SearchRange = CurrentSearch;
						CurrentSearch = new ArrayList<Note>();

					} else {
						updateRange = true;
					}

					for(Note o: SearchRange){
						if(o instanceof TextNote){
							TextNote other = (TextNote)o;
							if(other.getTitle().toUpperCase().contains(searchWord) || other.content.toUpperCase().contains(searchWord)){
								notexistSame = true;
								for(Note m: CurrentSearch){
									if(m.equals(o)){
										notexistSame = false;
									}
								}
								//not exist same note then add it to CurrentSearch
								if(notexistSame){
									CurrentSearch.add(o);
								}
							}
						} else {
							if(o.getTitle().toUpperCase().contains(searchWord)){
								notexistSame = true;
								for(Note m: CurrentSearch){
									if(m.equals(o)){
										notexistSame = false;
									}
								}

								//not exist same note then add it to CurrentSearch
								if(notexistSame){
									CurrentSearch.add(o);
								}
							}
						}
					}
				}

		}
		Collections.sort(CurrentSearch);
		return CurrentSearch;
	}

	@Override
	public int compareTo(Folder o){
		int compare = this.name.compareTo(o.name);
		return compare;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		int nText = 0;
		int nImage = 0;
		ArrayList<Note> items = getNotes();
		for(Note item : items){
			if(item instanceof TextNote)
				nText = nText + 1;
			else
				nImage = nImage + 1;
		}

		return name + ":" + nText + ":" + nImage;
	}

}
