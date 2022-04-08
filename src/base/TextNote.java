package base;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class TextNote extends Note {
	String content;
	private static final long serialVersionUID = 1L;

	public TextNote(String title){
		super(title);
	}

	public TextNote(String title, String content){
		super(title);
		this.content = content;
	}

	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}

	private String getTextFromFile(String absolutePath) {
		String result = "";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader breader = null;
		try{
			fis = new FileInputStream(absolutePath);
			isr = new InputStreamReader(fis);
			breader = new BufferedReader(isr);
			String tar = null;
			while((tar = breader.readLine()) != null){
				result.concat(tar);
	        	result.concat("\n");
			}
			fis.close();
			isr.close();
			breader.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public void exportTextToFile(String pathFolder) {
		File file;
		String name = getTitle().replaceAll(" ", "_");
		FileWriter fw;
		BufferedWriter bw;
		try{
			if(pathFolder.equals("")){
				pathFolder = ".";
			}
			file = new File( pathFolder + File.separator + name + ".txt");

			if(file.exists() == false){
				file.createNewFile();
			}
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content);
			fw.flush();
			bw.close();
		} catch (Exception e){
            e.printStackTrace();
        }
	}
	@Override
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
}
