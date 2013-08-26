package com.tcl.smart.server.bean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import com.tcl.smart.server.util.Constants;

public class MovieModel implements Serializable {
	private static final long serialVersionUID = -7040384204255513174L;
	private static final int MAX_CONTENT_LEN = 240;

	private ObjectId _id;
	private Integer wiki_id;
	private Integer admin_id;
	private String comment_tags;
	private String content;
	private String country;
	private String cover;
	private Date created_at;
	@Indexed
	private List<String> director;
	private Integer dislike_num;
	private String episodes;
	private Integer has_video;
	private String html_cache;
	private String language;
	private Integer like_num;
	private String model;
	private String released;
	private List<String> screenshots;
	private String slug;
	@Indexed
	private List<String> starring;
	@Indexed
	private List<String> tags;
	private String title;
	private Date updated_at;
	private Integer watched_num;
	private List<String> writer;
	private List<String> host;
	private List<String> guest;
	private String team;

	@Override
	public String toString() {
		String s = "id = " + _id + "\n";
		s = s + "model = " + model + "\n";
		s = s + "title = " + title + "\n";
		s = s + "country = " + country + "\n";
		s = s + "content = " + content + "\n";
		s = s + "cover = " + cover + "\n";
		s = s + "director = " + director + "\n";
		s = s + "language = " + language + "\n";
		s = s + "tags = " + tags + "\n";
		s = s + "released = " + released + "\n";
		s = s + "starring = " + starring + "\n";
		s = s + "writer = " + writer + "\n";
		s = s + "updated_at = " + updated_at + "\n";
		s = s + "created_at = " + created_at + "\n";
		s = s + "screenshots = " + screenshots + "\n";
		return s;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Integer getWiki_id() {
		return wiki_id;
	}

	public void setWiki_id(Integer wiki_id) {
		this.wiki_id = wiki_id;
	}

	public Integer getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}

	public String getComment_tags() {
		return comment_tags;
	}

	public void setComment_tags(String comment_tags) {
		this.comment_tags = comment_tags;
	}

	public String getContent() {
		return content;
	}

	public String getContentEtc() {
		if (content == null)
			return null;
		if (content.length() > MAX_CONTENT_LEN) {
			return content.substring(0, MAX_CONTENT_LEN) + "..";
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public List<String> getDirector() {
		return director;
	}

	public void setDirector(List<String> director) {
		this.director = director;
	}

	public Integer getDislike_num() {
		return dislike_num;
	}

	public void setDislike_num(Integer dislike_num) {
		this.dislike_num = dislike_num;
	}

	public String getEpisodes() {
		return episodes;
	}

	public void setEpisodes(String episodes) {
		this.episodes = episodes;
	}

	public Integer getHas_video() {
		return has_video;
	}

	public void setHas_video(Integer has_video) {
		this.has_video = has_video;
	}

	public String getHtml_cache() {
		return html_cache;
	}

	public void setHtml_cache(String html_cache) {
		this.html_cache = html_cache;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getLike_num() {
		return like_num;
	}

	public void setLike_num(Integer like_num) {
		this.like_num = like_num;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	public List<String> getScreenshots() {
		return screenshots;
	}

	public void setScreenshots(List<String> screenshots) {
		this.screenshots = screenshots;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public List<String> getStarring() {
		return starring;
	}

	public void setStarring(List<String> starring) {
		this.starring = starring;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Integer getWatched_num() {
		return watched_num;
	}

	public void setWatched_num(Integer watched_num) {
		this.watched_num = watched_num;
	}

	public List<String> getShowTags() {
		HashSet<String> showTagsSet = new HashSet<String>();
		if (comment_tags != null && !comment_tags.trim().equals(""))
			showTagsSet.add(comment_tags);
		showTagsSet.addAll(director == null ? new ArrayList<String>() : director);
		showTagsSet.addAll(starring == null ? new ArrayList<String>() : starring);
		showTagsSet.addAll(tags == null ? new ArrayList<String>() : tags);
		if (language != null && !language.trim().equals(""))
			showTagsSet.add(language);
		if (country != null && !country.trim().equals(""))
			showTagsSet.add(country);
		if (released != null && !released.trim().equals(""))
			showTagsSet.add(released);
		List<String> showTags = new ArrayList<String>();
		showTags.addAll(showTagsSet);
		return showTags;
	}

	public boolean equals(Object obj) {
		if (obj instanceof MovieModel) {
			MovieModel other = (MovieModel) obj;
			return _id.equals(other.get_id());
		}
		return false;
	}

	public int hashCode() {
		return _id.hashCode();
	}

	public int compareTo(Object obj) {
		if (obj instanceof MovieModel) {
			MovieModel other = (MovieModel) obj;
			return _id.compareTo(other.get_id());
		}
		return -1;
	}

	public ObjectId getMovieId() {
		return _id;
	}

	// all images are accessible as:
	// http://image.5i.tv/thumb/100/150/1326781962756.jpg and
	// http://image.5i.tv/18/035/8788/1312878803518.jpg
	public String getImage() {
		return "http://image.5i.tv/thumb/100/150/" + cover;
	}

	public String getPicUrl1() {
		if (cover == null || cover.trim().equals("")) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		File pic = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator
				+ Constants.getProperties().getRecommendPicFolder1() + cover);
		if (!pic.exists() || !pic.isFile()) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicFolder1() + cover;
	}

	public String getPicUrl2() {
		if (cover == null || cover.trim().equals("")) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		File pic = new File(System.getProperty("user.dir") + File.separator + ".." + File.separator + "webapps" + File.separator
				+ Constants.getProperties().getRecommendPicFolder2() + cover);
		if (!pic.exists() || !pic.isFile()) {
			return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicDefault();
		}
		return Constants.getProperties().getRecommendPicWebPrefix() + Constants.getProperties().getRecommendPicFolder2() + cover;
	}

	public String getDescription() {
		return getContent();
	}

	public List<String> getLanguages() {
		List<String> ls = new ArrayList<String>();
		ls.add(getLanguage());
		return ls;
	}

	public List<String> getActors() {
		return getStarring();
	}

	public List<String> getDirectors() {
		return getDirector();
	}

	public List<String> getCountrys() {
		List<String> cs = new ArrayList<String>();
		cs.add(getCountry());
		return cs;
	}

	public Date getReleaseDate() {
		if (released == null)
			return null;
		Integer year = Integer.valueOf(released);
		Calendar ca = Calendar.getInstance();
		ca.set(year, 1, 1);
		return ca.getTime();
	}

	public String getSynopsis() {
		return content;
	}

	public List<String> getWriter() {
		return writer;
	}

	public void setWriter(List<String> writer) {
		this.writer = writer;
	}

	public List<String> getHost() {
		return host;
	}

	public void setHost(List<String> host) {
		this.host = host;
	}

	public List<String> getGuest() {
		return guest;
	}

	public void setGuest(List<String> guest) {
		this.guest = guest;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public List<String> getKeyQueryItems() {
		List<String> queryStrs = new ArrayList<String>();
		queryStrs.add(getTitle());
		List<String> directors = getDirector();
		if (directors != null && directors.size() > 0) {
			for (String director : directors)
				queryStrs.add(director);
		}
		List<String> stars = getStarring();
		if (stars != null && stars.size() > 0) {
			for (String star : stars)
				queryStrs.add(star);
		}
		List<String> writers = getWriter();
		if (writers != null && writers.size() > 0) {
			for (String writer : writers)
				queryStrs.add(writer);
		}
		List<String> hosts = getHost();
		if (hosts != null && hosts.size() > 0) {
			for (String host : hosts)
				queryStrs.add(host);
		}
		List<String> guests = getGuest();
		if (guests != null && guests.size() > 0) {
			for (String guest : guests)
				queryStrs.add(guest);
		}
		if (team != null && !team.trim().equals(""))
			queryStrs.add(team);
		return queryStrs;
	}
}
