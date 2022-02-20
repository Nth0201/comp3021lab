package base;
import java.util.Date;
//import java.text.SimpleDateFormat;

public class Note {
	Date date;
	private String title;

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
//		if (getClass() != obj.getClass()){
//			System.out.println(obj.getClass());
//			return false;
//		}

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
}
