package com.tcl.smart.server.util;

import java.util.ArrayList;
import java.util.List;

import com.tcl.smart.server.bean.MovieModel;

public class KeywordTypeInMovie {
	
    public static List<KeyType> getKeyType(String key, MovieModel movie){
		
		String model = movie.getModel();
		List<KeyType> types = new ArrayList<KeyType>();
		if(key.equals(movie.getTitle()))
			types.add(KeyType.WIKI_TITLE);
		
		if (model.equals("teleplay") || model.equals("film")) {
			List<String> directors = movie.getDirector();
			if (directors != null && directors.size() > 0) {
				for (String director : directors)
					if(key.equals(director)){
						types.add(KeyType.DIRECTOR);
						break;
					}
			}
			List<String> actors = movie.getStarring();
			if (actors != null && actors.size() > 0) {
				for (String actor : actors)
					if(key.equals(actor)){
						types.add(KeyType.ACTOR);
						break;
					}
			}
			List<String> writers = movie.getWriter();
			if (writers != null && writers.size() > 0) {
				for (String writer : writers)
					if(key.equals(writer)){
						types.add(KeyType.WRITER);
						break;
					}
			}
			List<String> tags = movie.getTags();
			if (tags != null && tags.size() > 0) {
				for (String tag : tags)
					if(key.equals(tag)){
						types.add(KeyType.TAG);
						break;
					}
			}
		} else if (model.equals("television")) {
			List<String> hosts = movie.getHost();
			if (hosts != null && hosts.size() > 0) {
				for (String host : hosts)
					if(key.equals(host)){
						types.add(KeyType.HOST);
						break;
					}
			}
			List<String> guests = movie.getGuest();
			if (guests != null && guests.size() > 0) {
				for (String guest : guests)
					if(key.equals(guest)){
						types.add(KeyType.GUSET);
						break;
					}
			}
			List<String> tags = movie.getTags();
			if (tags != null && tags.size() > 0) {
				for (String tag : tags)
					if(key.equals(tag)){
						types.add(KeyType.TAG);
						break;
					}
			}
		}else if (model.equals("basketball_player")) {
			String team = movie.getTeam();
			if (team != null && !team.trim().equals(""))
				if(key.equals(team))
					types.add(KeyType.TEAM);
			
			List<String> tags = movie.getTags();
			if (tags != null && tags.size() > 0) {
				for (String tag : tags)
					if(key.equals(tag)){
						types.add(KeyType.TAG);
						break;
					}
			}
		} else
			;
		return types;
	}

}
