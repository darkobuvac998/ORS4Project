module EuroBank {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	exports application.controllers;
	exports application.server;
	exports application.server.controllers;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controllers to javafx.fxml;
	opens application.server.controllers to javafx.fxml;
	opens application.clients to javafx.base;
}
