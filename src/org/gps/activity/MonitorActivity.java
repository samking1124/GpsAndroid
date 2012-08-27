package org.gps.activity;

import org.gps.service.ConfigUtil;
import org.gps.service.Marker;
import org.gps.service.MarkerOverlay;
import org.gps.service.MonitorClient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.ItemizedOverlay.OnFocusChangeListener;

public class MonitorActivity extends MapActivity {
	private final static String TAG = "MonitorActivity";
	private MapView monitorMapView;
	private MapController mapController;
	private MarkerOverlay markerOverlay;
	private View popView;
	private Drawable drawable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor);
		//初始化地图
		initMap();
		try {
			MonitorClient.getInstance(this).start();
		} catch (Exception e) {
			Log.e(TAG, "实时监控线程未关闭，不能再次启动！");
		}
		Button trackerButton = (Button) this.findViewById(R.id.trackerBt);
		Button historyButton = (Button) this.findViewById(R.id.historyBt);
		Button exitButton = (Button) this.findViewById(R.id.exitBt);
		
		trackerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MonitorActivity.this,
						TrackerActivity.class);
				startActivity(intent);
			}
		});

		historyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MonitorActivity.this,
						HistoryActivity.class);
				startActivity(intent);
			}
		});
		
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				finish();
			}
		});
	}

	private void initPopView() {
		popView = View.inflate(this, R.layout.popview, null);
		popView.setVisibility(View.GONE);
		monitorMapView.addView(popView,
				new MapView.LayoutParams(
				MapView.LayoutParams.FILL_PARENT,
				MapView.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM_CENTER));
	}

	private void initMap() {
		monitorMapView = (MapView) findViewById(R.id.monitorMapView);
		monitorMapView.setBuiltInZoomControls(true);
		monitorMapView.setSatellite(false);
		mapController = monitorMapView.getController();
		mapController.setZoom(ConfigUtil.INIT_ZOOM);
		mapController.animateTo(new GeoPoint(ConfigUtil.INIT_LAT,
				ConfigUtil.INIT_LNG));
		drawable = this.getResources().getDrawable(R.drawable.online);
		markerOverlay = new MarkerOverlay(this, drawable);
		monitorMapView.getOverlays().add(markerOverlay);
		markerOverlay.setOnFocusChangeListener(onFocusChangeListener);
		initPopView();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private final OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void onFocusChanged(ItemizedOverlay itemizedOverlay,
				OverlayItem overlayItem) {
			MapView.LayoutParams popParams = (MapView.LayoutParams) popView
					.getLayoutParams();
			if (overlayItem == null) {
				popView.setVisibility(View.GONE);
				return;
			}
			GeoPoint geoPoint = overlayItem.getPoint();
			popParams.point = geoPoint;
			popParams.x = -5;
			popParams.y = -drawable.getBounds().height();
			TextView title = (TextView) popView.findViewById(R.id.pop_title);
			title.setText(overlayItem.getTitle());
			TextView content = (TextView) popView
					.findViewById(R.id.pop_content);
			content.setText(overlayItem.getSnippet());
			popView.setVisibility(View.VISIBLE);
			monitorMapView.updateViewLayout(popView, popParams);
			// TODO 怎么解决先不更新问题
			Log.e(TAG, "001 set first finish");
			if (((Marker) overlayItem).hasAdress()) {
				Log.e(TAG, "002 has adress");
				return;
			}
			Log.e(TAG, "003 not has adress");
			((Marker) overlayItem).requestAddress(MonitorActivity.this);
			Log.e(TAG, "004 requets finish");
			content.setText(overlayItem.getSnippet());
			monitorMapView.updateViewLayout(popView, popParams);
			Log.e(TAG, "005 all finish");

		}
	};

	public MapView getMapView() {
		return monitorMapView;
	}

	public void setMapView(MapView mapView) {
		this.monitorMapView = mapView;
	}

	public MapController getMapController() {
		return mapController;
	}

	public void setPopView(View popView) {
		this.popView = popView;
	}

	public View getPopView() {
		return popView;
	}

	public void setMapController(MapController mapController) {
		this.mapController = mapController;
	}

	public MapView getMonitorMapView() {
		return monitorMapView;
	}

	public void setMonitorMapView(MapView monitorMapView) {
		this.monitorMapView = monitorMapView;
	}

	public MarkerOverlay getMarkerOverlay() {
		return markerOverlay;
	}

	public void setMarkerOverlay(MarkerOverlay markerOverlay) {
		this.markerOverlay = markerOverlay;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		popView.setVisibility(View.GONE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//退出系统，会关闭进程，包括此进程下的所有线程(主线程,实时监控线程)
		System.exit(0);
	}
}