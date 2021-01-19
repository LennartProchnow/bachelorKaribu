package de.nordakademie.bachelor.gaestebuch.integration.test;

import static com.github.mvysny.kaributesting.v10.GridKt._get;
import static com.github.mvysny.kaributesting.v10.GridKt._getFormattedRow;
import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._setValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import de.nordakademie.bachelor.gaestebuch.MainView;
import de.nordakademie.bachelor.gaestebuch.model.Post;

public class MainViewIntegrationTest {

	private static Routes routes;
	private MainView view;
	private TextField textFieldName;
	private TextField textFieldRating;
	private Button saveButton;
	private Grid<Post> grid;
	private TextField sliderNameField;
	private TextField sliderRatingField;
	private Button sliderSaveButton;

	@BeforeAll
	public static void initBeforeAllTests() {
		routes = new Routes().autoDiscoverViews("de.nordakademie.bachelor.gaestebuch");
	}

	@BeforeEach
	public void initBeforeEachTest() {
		MockVaadin.setup(routes);
		UI.getCurrent().navigate("");
		this.view = _get(MainView.class);
		this.textFieldName = _get(view, TextField.class, spec -> spec.withId("textFieldName"));
		this.textFieldRating = _get(view, TextField.class, spec -> spec.withId("textFieldRating"));
		this.saveButton = _get(view, Button.class, spec -> spec.withId("saveButton"));
		this.grid = _get(view, Grid.class);
	}

	@Test
	public void testAriaLabelTextFieldName() {
		assertEquals("Dieses Textfeld ist mit dem Namen des Kommentierenden zu füllen", textFieldName.getElement().getAttribute("aria-label"));
	}

	@Test
	public void testAriaLabelRatingField() {
		assertEquals("Dieses Textfeld ist mit dem Gästebucheintrag zu füllen", textFieldRating.getElement().getAttribute("aria-label"));
	}

	@Test
	public void testAlphanumericValidationTextFieldName() {
		_setValue(this.textFieldName, "a1a");
		assertTrue(this.textFieldName.isInvalid());
		assertTrue(this.textFieldName.getErrorMessage().equals("Hier sind nur Buchstaben erlaubt"));
	}

	@Test
	public void testIsRequiredValidationInput() {
		_setValue(this.textFieldName, "a");
		_setValue(this.textFieldName, "");
		_setValue(this.textFieldRating, "a");
		_setValue(this.textFieldRating, "");
		assertTrue(this.textFieldName.isInvalid());
		assertTrue(this.textFieldRating.isInvalid());
		assertTrue(this.textFieldName.getErrorMessage().equals("Dieses Feld darf nicht leergelassen werden"));
		assertTrue(this.textFieldRating.getErrorMessage().equals("Dieses Feld darf nicht leergelassen werden"));
	}
	
	@Test
	public void testDisabledSaveButtonAtInit () {
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void testDisabledSaveButtonInvalidTextFieldName () {
		_setValue(this.textFieldName,"a1");
		_setValue(this.textFieldRating, "a");
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void testDisabledSaveButtonInvalidTextFieldRating () {
		_setValue(this.textFieldName,"a");
		_setValue(this.textFieldRating,"a");
		_setValue(this.textFieldRating,"");
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void testDisabledSaveButtonInvalidBothTextFields () {
		_setValue(this.textFieldName,"a1");
		_setValue(this.textFieldRating,"a");
		_setValue(this.textFieldRating,"");
		assertFalse(this.saveButton.isEnabled());
	}
	
	@Test
	public void addAPostIntoGrid() {
		makeAValidPost();
		assertEquals(Arrays.asList("Lennart","Der Aufenthalt war sehr schön."), _getFormattedRow(grid, 0));
	}
	
	@Test
	public void testSliderComponentsDoNotExistsYet() {
		assertEquals(0,_find(view,TextField.class, spec -> spec.withId("sliderNameField")).size());
	}
	
	@Test
	public void testSliderFillUp() {
		fillUpSliderComponents();
		initSliderComponents();
		assertEquals("Lennart", this.sliderNameField.getValue());
		assertEquals("Der Aufenthalt war sehr schön.", this.sliderRatingField.getValue());
	}
	
	@Test
	public void testValidationSliderBeanValidInput() {
		fillUpSliderComponents();
		initSliderComponents();
		this.sliderRatingField.setValue("Der Aufenthalt war echt cool.");
		assertFalse(this.textFieldRating.isInvalid());
		assertTrue(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationSliderRatingTextRequired() {
		fillUpSliderComponents();
		initSliderComponents();
		_setValue(this.sliderRatingField, "");
		assertTrue(this.sliderRatingField.isInvalid());
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationSliderNameTextRequired() {
		fillUpSliderComponents();
		initSliderComponents();
		_setValue(this.sliderNameField, "");
		assertTrue(this.sliderNameField.isInvalid());
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationSliderNameTextAlphanumerics() {
		fillUpSliderComponents();
		initSliderComponents();
		_setValue(this.sliderNameField,"a1a");
		assertTrue(this.sliderNameField.isInvalid());
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationSliderBeanBothRequired() {
		fillUpSliderComponents();
		initSliderComponents();
		_setValue(this.sliderNameField,"");
		_setValue(this.sliderRatingField, "");
		assertTrue(this.sliderNameField.isInvalid());
		assertTrue(this.sliderRatingField.isInvalid());
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void testValidationBothTextFieldsSliderBean() {
		fillUpSliderComponents();
		initSliderComponents();
		_setValue(this.sliderNameField,"a1a");
		_setValue(this.sliderRatingField,"a");
		_setValue(this.sliderRatingField,"");
		assertTrue(this.sliderNameField.isInvalid());
		assertTrue(this.sliderRatingField.isInvalid());
		assertFalse(this.sliderSaveButton.isEnabled());
	}
	
	@Test
	public void addAnotherPost() {
		makeAValidPost();
		this.textFieldName.setValue("Hans");
		this.textFieldRating.setValue("Ne so schön fand ich es nicht.");
		this.saveButton.click();
		assertEquals(Arrays.asList("Hans","Ne so schön fand ich es nicht."), _getFormattedRow(grid, 1));
	}
	
	@Test
	public void addADuplicatedPost() {
		makeAValidPost();
		makeAValidPost();
		assertEquals(Arrays.asList("Lennart","Der Aufenthalt war sehr schön."), _getFormattedRow(grid, 1));
	}

	@Test
	public void editAPostInGrid() {
		fillUpSliderComponents();
		initSliderComponents();
		_setValue(this.sliderRatingField, "Der Aufenthalt war wirklich sehr schön.");
		_click(this.sliderSaveButton);
		assertEquals(Arrays.asList("Lennart","Der Aufenthalt war wirklich sehr schön."), _getFormattedRow(grid, 0));
	}
	
	private void makeAValidPost() {
		_setValue(this.textFieldName, "Lennart");
		_setValue(this.textFieldRating, "Der Aufenthalt war sehr schön.");
		_click(this.saveButton);
	}
	
	private void fillUpSliderComponents() {
		makeAValidPost();
		this.grid.select(_get(grid,0));
	}
	
	private void initSliderComponents() {
		this.sliderNameField = _get(view, TextField.class, spec -> spec.withId("sliderNameField"));
		this.sliderRatingField = _get(view, TextField.class, spec -> spec.withId("sliderRatingField"));
		this.sliderSaveButton = _get(view, Button.class, spec -> spec.withId("sliderButton"));
	}
}
