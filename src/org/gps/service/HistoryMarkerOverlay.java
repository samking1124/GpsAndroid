package org.gps.service;

import org.gps.activity.R;

import android.content.Context;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HistoryMarkerOverlay extends ItemizedOverlay<OverlayItem> {
	private OverlayItem overlayItem;

	public HistoryMarkerOverlay(Context context) {
		super(boundCenter(context.getResources().getDrawable(
				R.drawable.history)));
		populate();
	}

	@Override
	protected OverlayItem createItem(int position) {
		return overlayItem;
	}

	@Override
	public int size() {
		return overlayItem == null ? 0 : 1;
	}

	public OverlayItem getOverlayItem() {
		return overlayItem;
	}

	public void setOverlayItem(GeoPoint geoPoint) {
		this.overlayItem = new OverlayItem(geoPoint, null, null);
		populate();
	}

}
