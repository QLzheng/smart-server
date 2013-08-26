package com.tcl.smart.server.dao;


import java.util.List;


import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAck;

public interface IMovieDao {

	MovieModel getMovieById(String movieId);
	
	void updateMovie(TvWikInfoRequestAck movie);
	
	List<String> getAllMovieId();
}
