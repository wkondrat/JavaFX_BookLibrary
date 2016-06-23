package com.capgemini.javafx.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.capgemini.javafx.dataprovider.DataProvider;
import com.capgemini.javafx.dataprovider.data.BookStatusVO;
import com.capgemini.javafx.dataprovider.data.BookVO;
import com.capgemini.javafx.model.BookStatus;
import com.capgemini.javafx.model.BooksLibrary;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;


public class BooksLibraryController {

	private static final Logger LOG = Logger.getLogger(BooksLibraryController.class);

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField titleField;
	
	@FXML
	private TextField authorsField;

	@FXML
	private ComboBox<BookStatus> bookStatusField;

	@FXML
	private Button searchButton;
	
	@FXML
	private Button addButton;

	@FXML
	private TableView<BookVO> resultTable;

	@FXML
	private TableColumn<BookVO, String> titleColumn;

	@FXML
	private TableColumn<BookVO, String> bookStatusColumn;

	@FXML
	private TableColumn<BookVO, String> authorsColumn;

	private final DataProvider dataProvider = DataProvider.INSTANCE;

	private final BooksLibrary model = new BooksLibrary();
	

	public BooksLibraryController() {
		LOG.debug("Constructor: titleField = " + titleField);
	}


	@FXML
	private void initialize() {
		LOG.debug("initialize(): titleField = " + titleField);

		initializeBookStatusField();

		initializeResultTable();

		/*
		 * Bind controls properties to model properties.
		 */
		titleField.textProperty().bindBidirectional(model.titleProperty());
		authorsField.textProperty().bindBidirectional(model.authorsProperty());
		bookStatusField.valueProperty().bindBidirectional(model.bookStatusProperty());
		resultTable.itemsProperty().bind(model.resultProperty());

		/*
		 * Preselect the default value for bookStatus.
		 */
		model.setBookStatus(BookStatus.ANY);
		model.setAuthors("");

		/*
		 * Make the Search button inactive when the Name field is empty.
		 */
		addButton.disableProperty().bind(titleField.textProperty().isEmpty().or(authorsField.textProperty().isEmpty()).or(bookStatusField.valueProperty().isEqualTo(BookStatus.ANY)));
		searchButton.disableProperty().bind(titleField.textProperty().isEmpty());
	}

	private void initializeBookStatusField() {
		/*
		 * Add items to the list in combo box.
		 */
		for (BookStatus bookStatus : BookStatus.values()) {
			bookStatusField.getItems().add(bookStatus);
		}

		/*
		 * Set cell factory to render internationalized texts for list items.
		 */
		bookStatusField.setCellFactory(new Callback<ListView<BookStatus>, ListCell<BookStatus>>() {

			@Override
			public ListCell<BookStatus> call(ListView<BookStatus> param) {
				return new ListCell<BookStatus>() {

					@Override
					protected void updateItem(BookStatus item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							return;
						}
						String text = getInternationalizedText(item);
						setText(text);
					}
				};
			}
		});

		/*
		 * Set converter to display internationalized text for selected value.
		 */
		bookStatusField.setConverter(new StringConverter<BookStatus>() {

			@Override
			public String toString(BookStatus object) {
				return getInternationalizedText(object);
			}

			@Override
			public BookStatus fromString(String string) {
				/*
				 * Not used, because combo box is not editable.
				 */
				return null;
			}
		});
	}

	private void initializeResultTable() {
		/*
		 * Define what properties of PersonVO will be displayed in different
		 * columns.
		 */
		titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		// nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		bookStatusColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<BookVO, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<BookVO, String> param) {
						BookStatusVO bookStatus = param.getValue().getBookStatus();
						String text = getInternationalizedText(BookStatus.fromBookStatusVO(bookStatus));
						return new ReadOnlyStringWrapper(text);
					}
				});
		authorsColumn
				.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthors()));

		/*
		 * Show specific text for an empty table. This can also be done in FXML.
		 */
		resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));

	}

	private String getInternationalizedText(BookStatus bookStatus) {
		return resources.getString("bookStatus." + bookStatus.name());
	}

	@FXML
	private void searchButtonAction(ActionEvent event) {
		LOG.debug("'Search' button clicked");

		searchButtonActionVersion2();	
	}

	private void searchButtonActionVersion2() {
		/*
		 * Use task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Collection<BookVO>> backgroundTask = new Task<Collection<BookVO>>() {

			@Override
			protected Collection<BookVO> call() throws Exception {
				LOG.debug("call() called");

				Collection<BookVO> result = new ArrayList<BookVO>();
				try {
					result = dataProvider.findBooksByParameters(
							model.getTitle(), //
							model.getAuthors(),
							model.getBookStatus().toBookStatusVO());
					// REV: lap tylko te wyjatki, ktorych sie spodziewasz
				} catch (Exception e) {
					// REV: logowanie bledow na poziomie error
					LOG.debug("HTTP GET error");
					// REV: uzytkownik powinien byc poinformowany o bledzie - hint: metoda Task.failed()
				}
				return result;
			}

			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");

				/*
				 * Get result of the task execution.
				 */
				Collection<BookVO> result = getValue();

				/*
				 * Copy the result to model.
				 */
				model.setResult(new ArrayList<BookVO>(result));

				/*
				 * Reset sorting in the result table.
				 */
				resultTable.getSortOrder().clear();
			}
		};
		
		
		/*
		 * Start the background task. In real life projects some framework
		 * manages background tasks. You should never create a thread on your
		 * own.
		 */
		new Thread(backgroundTask).start();
	}
	
	@FXML
	private void addButtonAction(ActionEvent event) {
		addBookAction(titleField, authorsField, bookStatusField);
	}
	
	private void addBookAction(TextField titleField, TextField authorsField,
			ComboBox<BookStatus> bookStatusField) {

		Task<Object> backgroundTask = new Task<Object>() {

			@Override
			protected Object call() throws Exception {

				// REV: dane powinny byc pobrane z modelu, a nie z kontrolek
				String title = titleField.getText();
				String authors = authorsField.getText();
				BookStatusVO bookStatus = bookStatusField.getValue().toBookStatusVO();

				try {
					dataProvider.addBook(title, authors, bookStatus);
				} catch (Exception e) {
					// REV: j.w.
					LOG.debug("HTTP POST error");
				}

				return new Object();
			}

			@Override
			protected void succeeded() {
				clearFields();
				LOG.debug("succeeded() called");
			}
		};

		new Thread(backgroundTask).start();
	}
	
	private void clearFields() {
		titleField.clear();
		authorsField.clear();
		bookStatusField.setValue(BookStatus.ANY);
	}
}