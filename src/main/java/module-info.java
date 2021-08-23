module mycraft {
	exports com.cbapps.javafx.mycraft.main;
	exports com.cbapps.javafx.mycraft.objects;
	exports com.cbapps.javafx.mycraft.states;

	requires java.prefs;
	requires javafx.controls;
	requires transitive gamo;
}