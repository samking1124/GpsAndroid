package org.gps.service;

import com.google.android.maps.GeoPoint;

public class History {
	private GeoPoint geoPoint;
	private GeoPoint orgiGeoPoint;
	public History(String lat, String lng, String orgiLat, String orgiLng) {
		this.geoPoint = new GeoPoint(getLatLngE6(lat), getLatLngE6(lng));
		this.orgiGeoPoint = new GeoPoint(getLatLngE6(orgiLat), getLatLngE6(orgiLat));
	}
	
	private int getLatLngE6(String latLng){
		return (int) ((Double.parseDouble(latLng))*1E6);
	}
	
	public GeoPoint getCurrentGeoPoint(){
		return geoPoint;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public GeoPoint getOrgiGeoPoint() {
		return orgiGeoPoint;
	}

	public void setOrgiGeoPoint(GeoPoint orgiGeoPoint) {
		this.orgiGeoPoint = orgiGeoPoint;
	}
	
}
