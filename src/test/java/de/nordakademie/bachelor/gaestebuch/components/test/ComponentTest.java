package de.nordakademie.bachelor.gaestebuch.components.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

public class ComponentTest {
	private static Routes routes;
	
	protected final TestView testLayout = new TestView();
	
	public ComponentTest () {
		//empty no args constructor
	}
	
	@BeforeAll
	public static void initBeforeAllTests() {
		routes = new Routes().autoDiscoverViews("de.nordakademie.bachelor.components.test");
	}
	
	@BeforeEach
	public void initBeforeEachTest() {
		MockVaadin.setup(routes);
		UI.getCurrent().navigate("TestUI");
	}
	
	@Route("TestUI")
	protected static class TestView extends VerticalLayout {
		public TestView () {}
	}
}
