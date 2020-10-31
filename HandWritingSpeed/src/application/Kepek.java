package application;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

public class Kepek {
	ScrollPane scrollPane = WindowElements.createScrollPane();
	ImageView imageView = new ImageView();	
	DoubleProperty zoomProperty = new SimpleDoubleProperty();
	
	public Kepek() {
		scrollPane = new ScrollPane();
		scrollPane.pannableProperty().set(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		
		// nagyítás és kicsinyítés egér görgõvel
		scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (imageView.getImage() != null && event.getDeltaY() > 0
				/* && zoomPropertyOffline.get() <= offlineImage.getImage().getWidth() * 4 */) {
					zoomProperty.set(zoomProperty.get() * 1.1);
				} else if (imageView.getImage() != null && event.getDeltaY() < 0
				/* && zoomPropertyOffline.get() >= offlineImage.getImage().getWidth() / 3 */ ) {
					zoomProperty.set(zoomProperty.get() / 1.1);
				}
				event.consume();
			}
		});

		zoomProperty.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable o) {
				imageView.setFitWidth(zoomProperty.get());
			}
		});
	}

	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(ScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public DoubleProperty getZoomProperty() {
		return zoomProperty;
	}

	public void setZoomProperty(DoubleProperty zoomProperty) {
		this.zoomProperty = zoomProperty;
	}

	
}
