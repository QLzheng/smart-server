package com.tcl.smart.server.dao;

import java.util.List;

import com.tcl.smart.server.bean.VideoUrlInfo;

public interface IVideoDao {
	public List<String> findAllPics();

	public List<VideoUrlInfo> findAllVideoInfosByVideos();

	public List<VideoUrlInfo> findAllVideoInfos();

	public void updateOrAddVideoUrlInfo(VideoUrlInfo videoInfo);

	public VideoUrlInfo findVideoUrlInfoById(String id);
}
