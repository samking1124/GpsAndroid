package org.gps.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.gps.activity.MonitorActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class MarkerOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private Map<String, ArrayList<GeoPoint>> lineMap = new HashMap<String, ArrayList<GeoPoint>>();
	private Context context;

	public MarkerOverlay(Context context, Drawable drawable) {
		super(boundCenterBottom(drawable));
		this.context = context;
		doPopulate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return getMarkers().get(i);
	}

	public void addMarker(Marker marker) {
		if(markers.size()>0){
			for (Marker old : markers) {
				if (marker.getTkNo().equals(old.getTkNo())) {
					markers.remove(old);
				}
			}
		}
		getMarkers().add(marker);
		if (lineMap.get(marker.getTkNo()) == null) {
			ArrayList<GeoPoint> pts = new ArrayList<GeoPoint>();
			pts.add(marker.getP());
			lineMap.put(marker.getTkNo(), pts);
		} else {
			lineMap.get(marker.getTkNo()).add(marker.getP());
		}

		doPopulate();
	}

	public void addMarkers(ArrayList<Marker> markers) {
		this.markers = markers;
		for (Marker marker : markers) {
			ArrayList<GeoPoint> pts = new ArrayList<GeoPoint>();
			pts.add(marker.getP());
			lineMap.put(marker.getTkNo(), pts);
		}
		doPopulate();
	}

	private void doPopulate() {
		setLastFocusedIndex(-1);
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		try {
			// 画线
			Projection projection = mapView.getProjection();
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setDither(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeWidth(3);

			for (ArrayList<GeoPoint> geoPoints : lineMap.values()) {
				Path path = new Path();
				for (int i = 0; i < geoPoints.size(); i++) {
					Point p = new Point();
					projection.toPixels(geoPoints.get(i), p);
					if (i == 0) {
						path.moveTo(p.x, p.y);
					} else {
						path.lineTo(p.x, p.y);
					}
				}
				canvas.drawPath(path, paint);
			}
			super.draw(canvas, mapView, false);
		} catch (Exception e) {
			Log.e("MarkerOverlay draw", e.getMessage());
		}
	}

	public void clear() {
		markers.clear();
		lineMap.clear();
		doPopulate();
	}

	@Override
	public int size() {
		return getMarkers().size();
	}

	public Context getContext() {
		return context;
	}

	public void setContext(MonitorActivity context) {
		this.context = context;
	}

	public void setMarkers(ArrayList<Marker> markers) {
		this.markers = markers;
	}

	public ArrayList<Marker> getMarkers() {
		return markers;
	}

}
