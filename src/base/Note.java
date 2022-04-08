package base;
import java.util.Date;
//import java.text.SimpleDateFormat;

public class Note implements Comparable<Note>, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return date.toString() + "\t" +title;
	}

	Date date;
	private String title;

	@Override
	public int compareTo(Note o){
		if (this.date.compareTo(o.date) >= 0){
            return -1;
        } else {
            return 1;
        }
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Note))
			return false;
		Note other = (Note) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public Note(String title){
		this.title = title;
		date = new Date();
		//SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
		//String stringDate= DateFor.format(this.date);
	}

	public String getTitle(){
		return title;
	}

	public String getContent(){
		return "";
	}

	public void setContent(String text) {
		// TODO Auto-generated method stub

	}
}
