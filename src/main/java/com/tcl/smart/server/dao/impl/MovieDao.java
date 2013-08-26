package com.tcl.smart.server.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tcl.smart.server.bean.MovieModel;
import com.tcl.smart.server.bean.cmd.Info;
import com.tcl.smart.server.bean.cmd.Media;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAck;
import com.tcl.smart.server.bean.cmd.TvWikInfoRequestAckData;
import com.tcl.smart.server.dao.IMovieDao;

public class MovieDao implements IMovieDao {

	private static final String HUAN_MOVIE_ORI_ID = "_id";
	// private static final String HUAN_MOVIE_ADMIN_ID = "admin_id";
	// private static final String HUAN_MOVIE_COMMENT_TAGS = "comment_tags";
	private static final String HUAN_MOVIE_CONTENT = "content";
	private static final String HUAN_MOVIE_COUNTRY = "country";
	private static final String HUAN_MOVIE_IMAGE = "cover";
	private static final String HUAN_MOVIE_CREATED_AT = "created_at";
	private static final String HUAN_MOVIE_DIRECTOR = "director";
	private static final String HUAN_MOVIE_DISLIKE_NUM = "dislike_num";
	// private static final String HUAN_MOVIE_EPISODES = "episodes";
	private static final String HUAN_MOVIE_HAS_VIDEO = "has_video";
	// private static final String HUAN_MOVIE_HTML_CACHE = "html_cache";
	private static final String HUAN_MOVIE_LANGUAGE = "language";
	private static final String HUAN_MOVIE_LIKE_NUM = "like_num";
	private static final String HUAN_MOVIE_MODEL = "model";
	private static final String HUAN_MOVIE_RELEASED = "released";
	private static final String HUAN_MOVIE_SCREENSHOTS = "sreenshots";
	// private static final String HUAN_MOVIE_SLUG = "slug";
	private static final String HUAN_MOVIE_STAR = "starring";
	private static final String HUAN_MOVIE_TAG = "tags";
	private static final String HUAN_MOVIE_TITLE = "title";
	private static final String HUAN_MOVIE_UPDATED_AT = "updated_at";
	// private static final String HUAN_MOVIE_WATCHED_NUM = "watch_num";
	// private static final String HUAN_MOVIE_WIKI_ID = "wiki_id";
	// private static final String HUAN_MOVIE_WRITER = "writer";

	@Autowired
	@Value("${db.video}")
	private String COL_MOVIE_ID;

	private DBCollection col;

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	public void init() {
		col = mongoTemplate.getCollection(COL_MOVIE_ID);
		col.ensureIndex(new BasicDBObject().append(HUAN_MOVIE_ORI_ID, 1));
	}

	@Override
	public MovieModel getMovieById(String movieId) {
		ObjectId id;
		try {
			id = new ObjectId(movieId);
		} catch (IllegalArgumentException e) {
			return null;
		}
		// DBObject query = new BasicDBObject();
		// query.put(HUAN_MOVIE_ORI_ID, id);
		Query query = new Query(Criteria.where("_id").is(id));
		MovieModel movie = mongoTemplate.findOne(query, MovieModel.class, COL_MOVIE_ID); // .findOne(query,
																							// MovieModel.class,
																							// COL_MOVIE_ID);
		// return DBObjectToMovie(col.findOne(query));
		return cleanData(movie);
	}

	@Override
	public void updateMovie(TvWikInfoRequestAck movie) {
		if (movie == null)
			return;
		String id = movie.getData().getMedia().getId();

		if (id == null)
			return;

		/*
		 * If the movie hasn't exist in the database then add the movie into the
		 * database
		 */
		if (getMovieById(id) == null) {
			DBObject obj = MovieToDBObject(movie);
			if (obj != null)
				col.save(obj);
		}
	}

	@Override
	public List<String> getAllMovieId() {

		ArrayList<String> ids = new ArrayList<String>();
		DBCursor cur = col.find();
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			if (obj != null)
				ids.add(obj.get(HUAN_MOVIE_ORI_ID).toString());
		}
		return ids;
	}

	private DBObject MovieToDBObject(TvWikInfoRequestAck movie) {
		if (movie == null) {
			return null;
		}
		TvWikInfoRequestAckData data = movie.getData();
		Media media = data.getMedia();
		Info info = media.getInfo();

		Date date = new Date(System.currentTimeMillis());

		ObjectId id;
		try {
			id = new ObjectId(media.getId());
		} catch (IllegalArgumentException e) {
			return null;
		}

		DBObject obj = new BasicDBObject();
		obj.put(HUAN_MOVIE_ORI_ID, id);
		obj.put(HUAN_MOVIE_CONTENT, media.getDescription());
		obj.put(HUAN_MOVIE_COUNTRY, info.getArea());
		obj.put(HUAN_MOVIE_IMAGE, media.getPosters().getCover());
		obj.put(HUAN_MOVIE_CREATED_AT, date);

		String[] directors = info.getDirector().split(",");
		List<String> director = new ArrayList<String>();
		for (String s : directors)
			director.add(s);
		obj.put(HUAN_MOVIE_DIRECTOR, director);

		obj.put(HUAN_MOVIE_DISLIKE_NUM, Integer.parseInt(info.getDispraise()));
		if (info.getSource() != null)
			obj.put(HUAN_MOVIE_HAS_VIDEO, 1);
		obj.put(HUAN_MOVIE_LANGUAGE, info.getLanguage());
		obj.put(HUAN_MOVIE_LIKE_NUM, Integer.parseInt(info.getPraise()));
		obj.put(HUAN_MOVIE_MODEL, media.getModel());
		obj.put(HUAN_MOVIE_RELEASED, info.getPlaydate());
		obj.put(HUAN_MOVIE_SCREENSHOTS, media.getScreens().getScreenCovers());
		obj.put(HUAN_MOVIE_TITLE, media.getTitle());
		obj.put(HUAN_MOVIE_UPDATED_AT, date);

		String[] stars = info.getActors().split(",");
		List<String> starring = new ArrayList<String>();
		for (String s : stars)
			starring.add(s);
		obj.put(HUAN_MOVIE_STAR, starring);

		String[] tags = info.getType().split(",");
		List<String> tagging = new ArrayList<String>();
		for (String s : tags)
			tagging.add(s);
		obj.put(HUAN_MOVIE_TAG, tagging);

		return obj;
	}

	private MovieModel cleanData(MovieModel movie) {
		if (movie == null)
			return null;
		/* Clean release date data */
		String release = movie.getReleased();
		if (release != null) {
			release = release.trim();
			if (release.isEmpty()) {
				release = null;
			} else {
				try {
					Integer.parseInt(release);
				} catch (Exception e) {
					Pattern p = Pattern.compile("(\\d+)(\\D+\\S*)?");
					Matcher m = p.matcher(release);
					if (m.find()) {
						release = m.group(1);
					} else {
						e.printStackTrace();
					}
				}
			}
			movie.setReleased(release);
		}

		/* Clean actor data */
		List<String> actors = movie.getActors();
		if (actors != null) {
			ArrayList<String> cleanedActors = new ArrayList<String>(actors.size());
			for (String actor : actors) {
				if (actor != null) {
					actor = actor.trim();
					if (!actor.isEmpty()) {
						cleanedActors.add(actor);
					}
				}
			}
			movie.setStarring(cleanedActors);
		}

		/* Clean director data */
		List<String> directors = movie.getDirectors();
		if (directors != null) {
			ArrayList<String> cleanedDirectors = new ArrayList<String>(directors.size());
			for (String director : directors) {
				if (director != null) {
					director = director.trim();
					if (!director.isEmpty()) {
						cleanedDirectors.add(director);
					}
				}
			}
			movie.setDirector(cleanedDirectors);
		}

		/* Clean tag data */
		List<String> tags = movie.getTags();
		if (tags != null) {
			ArrayList<String> cleanedTags = new ArrayList<String>(tags.size());
			for (String tag : tags) {
				if (tag != null) {
					tag = tag.trim();
					if (!tag.isEmpty()) {
						cleanedTags.add(tag);
					}
				}
			}
			movie.setTags(cleanedTags);
		}

		/* Clean country data */
		String country = movie.getCountry();
		if (country != null) {
			country = country.trim();
			if (country.isEmpty()) {
				movie.setCountry(null);
			} else {
				movie.setCountry(country);
			}
		}

		/* Clean writer data */
		List<String> writers = movie.getWriter();
		if (writers != null) {
			ArrayList<String> cleanedWriters = new ArrayList<String>(writers.size());
			for (String writer : writers) {
				if (writer != null) {
					writer = writer.trim();
					if (!writer.isEmpty()) {
						cleanedWriters.add(writer);
					}
				}
			}
			movie.setWriter(cleanedWriters);
		}
		return movie;
	}

}
