package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
/**
 *
 * NoteBook GUI with JAVAFX
 *
 * COMP 3021
 *
 *
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 *
	 * Combobox for selecting the folder
	 *
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";

	String currentNote = "";

	TextField searchfield = new TextField();

	Stage stage;
	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		// Use a border pane as the root for scene

		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
		this.stage = stage;
	}

	/**
	 * This create the top section
	 *
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load");
		buttonLoad.setPrefSize(100, 20);
		//buttonLoad.setDisable(true);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object");

            	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
            	fileChooser.getExtensionFilters().add(extFilter);

            	File file = fileChooser.showOpenDialog(stage);

            	if(file != null){
            		loadNoteBook(file);
            	}
            }
		});

		Button buttonSave = new Button("Save");
		buttonSave.setPrefSize(100, 20);
		//buttonSave.setDisable(true);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Please Choose The Path To Save");

            	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
            	fileChooser.getExtensionFilters().add(extFilter);
            	File file = fileChooser.showSaveDialog(stage);
            	if(file != null){
            		if(noteBook.save(file.getAbsolutePath())){
                		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    					alert.setTitle("Successfully saved");
    					alert.setContentText("Your file has been saved to file " + file.getName());
    					alert.showAndWait().ifPresent(rs -> {
    						if (rs == ButtonType.OK) {
    							System.out.println("Pressed OK.");
    						}
    					});
                	}
            	}
            }
		});


		searchfield.setPrefSize(150, 20);

		Button buttonsearch = new Button("Search");
		buttonsearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              	currentSearch = searchfield.getText();
              	//textAreNote.setText("");
				foldersComboBox.setValue("-----");
				currentNote = "";
				currentFolder = "";
				updateListView(noteBook.searchNotes(currentSearch));
            }
		});

		Button buttonClearSeacrh = new Button("Clear Search");
		buttonClearSeacrh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              	searchfield.setText("");
              	textAreNote.setText("");

            }
		});

		hbox.getChildren().addAll(buttonLoad, buttonSave);
		hbox.getChildren().add(new Label("Search : "));
		hbox.getChildren().add(searchfield);
		hbox.getChildren().add(buttonsearch);
		hbox.getChildren().add(buttonClearSeacrh);

		return hbox;
	}

	/**
	 * this create the section on the left
	 *
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		HBox hbox = new HBox();
		hbox.setSpacing(8); // Gap between nodes
		// add file name
		foldersComboBox.getItems().clear();
		ArrayList<Folder> allNotes = noteBook.getFolders();
		ArrayList<String> allNotesName = new ArrayList<>();
		for(Folder f : allNotes){
			allNotesName.add(f.getName());
		}
		foldersComboBox.getItems().addAll(allNotesName);
		//

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if(t1 != null){
					currentFolder = t1.toString();
					currentNote = "";
					// this contains the name of the folder selected
					// TODO update listview
					updateListView();

				}

			}

		});

		foldersComboBox.setValue("-----");

		Button AddFolder = new Button("Add a folder");
		//AddFolder.setPrefSize(100, 20);
		//buttonLoad.setDisable(true);
		AddFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	TextInputDialog dialog = new TextInputDialog("Add a Folder");
                dialog.setTitle("Input");
                dialog.setHeaderText("Add a new folder for your notebook:");
                dialog.setContentText("Please enter the name you want to create:");

                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                	if(result.get().isEmpty()){
                		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    					alert.setTitle("Warning");
    					alert.setContentText("Please input valid folder name");
    					alert.showAndWait().ifPresent(rs -> {
    						if (rs == ButtonType.OK) {
    							System.out.println("Pressed OK.");
    						}
    					});
                	} else {
                		boolean tmp = noteBook.getFolders().stream().anyMatch(e->e.getName().equals(result.get()));
                        if( tmp == true){
                    	   Alert alert = new Alert(Alert.AlertType.INFORMATION);
       						alert.setTitle("Warning");
       						alert.setContentText("You already have a folder name with Books");
       						alert.showAndWait().ifPresent(rs -> {
    	   						if (rs == ButtonType.OK) {
    	   							System.out.println("Pressed OK.");
    	   						}
       						});
                       } else {
      						noteBook.addFolder(result.get());
      						foldersComboBox.getItems().add(result.get());
                       }
                	}
                }
            }
		});


		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				ArrayList<Folder> allfolder = noteBook.getFolders();

				String content = "";
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				for(Folder f: allfolder){
					ArrayList<Note> allNote = f.getNotes();
					for(Note o : allNote){
						if(o.getTitle() == title){
							currentNote = title;
							content = o.getContent();
						}
					}
				};



				textAreNote.setText(content);
				textAreNote.setEditable(true);

			}
		});

		Button AddNote = new Button("Add a Note");
		//AddFolder.setPrefSize(100, 20);
		//buttonLoad.setDisable(true);
		AddNote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(currentFolder.equals("-----") || currentFolder.isEmpty()){
            		Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Choose a folder first!");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
            	} else {
            		TextInputDialog dialog = new TextInputDialog("Add a Folder");
                    dialog.setTitle("Input");
                    dialog.setHeaderText("Add a new note to your current folder:");
                    dialog.setContentText("Please enter the name of your note:");
                    Optional<String> result = dialog.showAndWait();
                    if(result.isPresent()){
                    	if(result.get().isEmpty()){
                    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        					alert.setTitle("Warning");
        					alert.setContentText("Please input valid note name!");
        					alert.showAndWait().ifPresent(rs -> {
        						if (rs == ButtonType.OK) {
        							System.out.println("Pressed OK.");
        						}
        					});
                    	} else {
                    		if(noteBook.createTextNote(currentFolder, result.get())){
                    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
            					alert.setTitle("Message");
            					alert.setContentText("Insert note " + result.get() + "to folder" + currentFolder + "successfully!");
            					alert.showAndWait().ifPresent(rs -> {
            						if (rs == ButtonType.OK) {
            							System.out.println("Pressed OK.");
            						}
            					});
            					for(Folder f: noteBook.getFolders()){
            						if(f.getName() == currentFolder){
            							updateListView(f.getNotes());
            							break;
            						}
            					};
                    		} else {
                    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
            					alert.setTitle("Warning");
            					alert.setContentText("Repeat Note!");
            					alert.showAndWait().ifPresent(rs -> {
            						if (rs == ButtonType.OK) {
            							System.out.println("Pressed OK.");
            						}
            					});
                    		}
                    	}
                    }
            	}
            }
		});

		vbox.getChildren().add(new Label("Choose folder: "));
		hbox.getChildren().addAll(foldersComboBox);
		hbox.getChildren().add(AddFolder);
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(AddNote);

		return vbox;
	}

	private void updateListView() {

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		ArrayList<Folder> allfolder = noteBook.getFolders();
		ArrayList<Note> allNote = new ArrayList<Note>();
		for(Folder f: allfolder){
			if(f.getName() == currentFolder){
				allNote = f.getNotes();
				break;
			}
		};

		updateListView(allNote);
	}

	private void updateListView(List<Note> notes) {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder

		for (Note o : notes) {
			list.add(o.getTitle());
		}
		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreNote.setText("");
	}
	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		HBox hbox = new HBox();
		hbox.setSpacing(10);
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);

		Button saveNote = new Button("Save Note");
		saveNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (currentFolder.equals("-----") || currentNote.isEmpty() || currentFolder.isEmpty()){
					// no folder or no note is selected
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note ");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				} else {
					for(Folder f: noteBook.getFolders()){
						if(f.getName() == currentFolder){
							for(Note o: f.getNotes()){
								if(o.getTitle() == currentNote){
									o.setContent(textAreNote.getText());
									break;
								}
							}
						}
					};
				}
			}
		});

		Button deleteNote = new Button("Delete Note");
		deleteNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (currentFolder.equals("-----") || currentNote.isEmpty()){
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note ");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				} else {
					for(Folder f: noteBook.getFolders()){
						if(f.getName() == currentFolder){
							if(f.removeNotes(currentNote)){
								updateListView(f.getNotes());
								currentNote = "";
								Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
								alert.setTitle("Succeed!");
								alert.setContentText("Your note has been successfully removed");
								alert.showAndWait().ifPresent(rs -> {
									if (rs == ButtonType.OK) {
										System.out.println("Pressed OK.");
									}
								});
							}
						}
					};
				}
			}
		});
		hbox.getChildren().add(saveView);
		hbox.getChildren().add(saveNote);
		hbox.getChildren().add(deleteNote);

		textAreNote.setEditable(false);
		textAreNote.setMaxSize(450, 400);
		textAreNote.setWrapText(true);
		textAreNote.setPrefWidth(450);
		textAreNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		grid.add(hbox,0,0);
		grid.add(textAreNote, 0, 1);

		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called �he most shocking play in NFL history�� and the Washington Redskins dubbed the �hrowback Special��: the November 1985 play in which the Redskins�� Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award�inning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything�ntil it wasn�. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant� part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether� Daddy Was a Number Runner and Dorothy Allison� Bastard Out of Carolina, Jacqueline Woodson� Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood�he promise and peril of growing up�nd exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}
	private void loadNoteBook(File file) {
		noteBook = new NoteBook(file.getAbsolutePath());
		searchfield.setText("");
		textAreNote.setText("");
		currentFolder = "";
		currentNote = "";
		currentSearch = "";
		foldersComboBox.getItems().clear();
		ArrayList<String> allNotesName = new ArrayList<>();
		for(Folder f : noteBook.getFolders()){
			allNotesName.add(f.getName());
		}
		foldersComboBox.getItems().addAll(allNotesName);

		updateListView();
	}

}
