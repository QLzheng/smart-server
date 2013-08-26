package com.tcl.smart.server.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class GetFlexigridJson {

	public static <T> String getJson(int page, List<T> object) {
		String result = "";
		int total = object.size();
		List<FlexigridRows<T>> frlist = new ArrayList<FlexigridRows<T>>();
		for (int i = 0; i < total; i++) {
			FlexigridRows<T> fr = new FlexigridRows<T>(i, object.get(i));
			frlist.add(fr);
		}
		FlexigridJson<T> fj = new FlexigridJson<T>(total, page, frlist);
		Gson gson = new Gson();
		result = gson.toJson(fj);
		return result;
	}
}