package org.gps.service;

import java.io.IOException;
import java.util.List;

import org.gps.db.GpsDBOpenHelper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class Marker extends OverlayItem {
	private String name;
	private String tkNo;
	private String recordeTime;
	private String address = "查询中...";
	private int isGsm;
	private String title;
	private String snippet;
	private GeoPoint p;
	private boolean hasAdress = false;

	public Marker(GeoPoint p, String title, String snippet) {
		super(p, title, snippet);
		this.title = title;
		this.snippet = snippet;
	}

	public Marker(GeoPoint p, String tkNo, String time, int isGsm) {
		super(p, null, null);
		this.name = GpsDBOpenHelper.getInstance().getTrackerData().get(tkNo)
				.getName();
		this.tkNo = tkNo;
		this.recordeTime = time;
		this.isGsm = isGsm;
		this.title = name + "      " + (isGsm == 1 ? "GSM" : "GPS");
		this.p = p;
	}

	public void requestAddress(Context context) {
		Geocoder geocoder = new Geocoder(context);
		try {
			List<Address> addresses = geocoder.getFromLocation(p
					.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
					sb.append(address.getAddressLine(i));
				}
				this.address = sb.toString();
				this.hasAdress = true;
			} else {
				address = "未查询到位置信息！";
			}

		} catch (IOException e) {
			address = "查询位置信息超时！";
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTkNo() {
		return tkNo;
	}

	public void setTkNo(String tkNo) {
		this.tkNo = tkNo;
	}

	public String getRecordeTime() {
		return recordeTime;
	}

	public void setRecordeTime(String recordeTime) {
		this.recordeTime = recordeTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public GeoPoint getP() {
		return p;
	}

	public void setP(GeoPoint p) {
		this.p = p;
	}

	public String getSnippet() {
		if (snippet != null) {
			return snippet;
		}
		return "编号：" + tkNo + "\n时间：" + recordeTime + "\n地址：" + address;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public int getIsGsm() {
		return isGsm;
	}

	public void setIsGsm(int isGsm) {
		this.isGsm = isGsm;
	}

	public boolean hasAdress() {
		return hasAdress;
	}

}
