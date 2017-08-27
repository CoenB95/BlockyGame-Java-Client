import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class AngleBindings {

	public static BooleanBinding isBetween(ObservableDoubleValue observable, double min, double max) {
		while (min < 0) min += 360;
		while (min > 360) min -= 360;
		while (max < 0) max += 360;
		while (max > 360) max -= 360;
		final double fMin = min;
		final double fMax = max;
		return new BooleanBinding() {
			{
				super.bind(observable);
			}
			@Override
			protected boolean computeValue() {
				return fMin <= fMax ? observable.get() > fMin && observable.get() < fMax :
						(observable.get() > fMin && observable.get() <= 360) ||
								(observable.get() >= 0 && observable.get() < fMax);
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.singletonObservableList(observable);
			}
		};
	}

}
