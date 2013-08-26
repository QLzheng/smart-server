package com.tcl.smart.server.dao;

import java.util.List;

import com.tcl.smart.server.bean.NewsClassification;

public interface INewsClassificationDao {
	public List<NewsClassification> findAllNewsClassifications();

	public NewsClassification findNewsClassificationById(String newsClassificationId);

	public void updateOrInsertNewsClassification(NewsClassification newsClassification);
}
