package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;

public class Folder implements Comparable<Folder>, java.io.Serializable{
	private ArrayList<Note> notes;
	private static final long serialVersionUID = 1L;
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

	public void findFrequentWord(){
		Hashtable<String,Integer> countWord = new Hashtable<>();
		List<Entry<String,Integer>> freqWord = new ArrayList<>();
		int smallestKey = 2;
		for(Note o : notes){
			if(o instanceof TextNote){
				// ensure it is a instance of TextNote to do the content search
				String[] contents = o.getContent().trim().split("[\\p{Punct}\\s]+");
				for(String word: contents){
					if(countWord.containsKey(word) == true){
						// find the key of the word
						countWord.computeIfPresent(word, (k, v) -> v + 1);
					} else {
						countWord.put(word, 1);
					}
				}

			}
		}
		// after loop all the content of all note in this folder
		// print the most frequency word
		/*for(String key : countWord.keySet()){
			int tmp = countWord.get(key);
			if(freqWord.size() != 3 && tmp >= 2){
				freqWord.put(key, tmp);
				if(freqWord.size() == 1){
					smallestKey = tmp;
				} else if( tmp < smallestKey){
					smallestKey = tmp;
				}
				freqWord = sortByValue(freqWord);
			} else if(tmp > smallestKey){
				freqWord.remove(freqWord.keySet().toArray()[0],smallestKey);
				freqWord.put(key, tmp);
				freqWord = sortByValue(freqWord);
				smallestKey = (int) freqWord.values().toArray()[0];
			}
		}*/
		freqWord = sortByValue(countWord);
		//Set freqWord_set = freqWord.keySet();
		//int tar = freqWord.size();

		//for(int i = freqWord.size() - 1; i > tar ){

		//}

		System.out.println(freqWord);



	}

	public static <String, Integer extends Comparable<Integer>> List<Entry<String, Integer>> sortByValue(Map<String, Integer> map) {
        List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<String, Integer> tmp = new LinkedHashMap<>();
        List<Entry<String, Integer>> result = new ArrayList<>();
        for (Entry<String, Integer> entry : list) {

        	tmp.put(entry.getKey(), entry.getValue());
            if(map.size() - tmp.size() < 3 && (int)entry.getValue() > 1){
            	result.add(entry);
            	System.out.println(entry.getKey());
            	System.out.println(entry.getValue());
            }
        }

        return result;
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
	public boolean removeNotes(String title){
		for(Note o: notes){
			if(o.getTitle().equals(title)){
				notes.remove(o);
				return true;
			}
		}
		return false;
	}
}
