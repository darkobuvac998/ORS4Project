module EuroBank {
	requires javafx.controls;
	requires javafx.fxml;
	exports application.controllers;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controllers to javafx.fxml;
}
