package com.cbapps.javafx.mycraft;

import com.cbapps.javafx.gamo.objects.GameObjectBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TextObject extends GameObjectBase {

	private StringProperty text = new SimpleStringProperty();

	public TextObject() {
		Label t = new Label();
		t.textProperty().bind(text);
		t.setFont(Font.font("Monospaced"));
		t.setTextFill(Color.WHITE);
		t.setBackground(new Background(new BackgroundFill(new Color(0, 0, 0, 0.3), null, null)));
		t.setPadding(new Insets(5));

		setNode(t);
	}

	public StringProperty textProperty() {
		return text;
	}
}
