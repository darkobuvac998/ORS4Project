module EuroBank {
	requires javafx.controls;
	requires javafx.fxml;
	exports application.controllers;
	exports application.server;
	exports application.server.controllers;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controllers to javafx.fxml;
	opens application.server.controllers to javafx.fxml;
}
