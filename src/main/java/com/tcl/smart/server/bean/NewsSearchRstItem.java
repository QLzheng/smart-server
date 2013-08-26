package com.tcl.smart.server.bean;

import java.util.List;

import com.tcl.smart.server.util.Constants;
import com.tcl.smart.server.util.KeyType;

/**
 * 新闻条目搜索结果数据模型
 * 
 * @author fanjie
 * @date 2013-4-7
 */
public class NewsSearchRstItem extends BaseNewsItem {
	private static final long serialVersionUID = 630571730626561686L;
	private String program_name;
	private String program_wiki_id;
	private String program_wiki_name;
	private List<String> program_wiki_keys;
	private List<KeyType> keyType;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.parseTime(updateTime)).append(" ").append(title).append("     ").append(keyType).append("   ").append("(").append(link).append("): ")
				.append(description);
		if (program_wiki_name != null) {
			sb.append(" [").append(program_wiki_name).append(", ");
			if (program_wiki_keys != null && program_wiki_keys.size() > 0) {
				for (String key : program_wiki_keys) {
					sb.append(key).append(",");
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}

	public String getProgram_name() {
		return program_name;
	}

	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}

	public String getProgram_wiki_id() {
		return program_wiki_id;
	}

	public void setProgram_wiki_id(String program_wiki_id) {
		this.program_wiki_id = program_wiki_id;
	}

	public String getProgram_wiki_name() {
		return program_wiki_name;
	}

	public void setProgram_wiki_name(String program_wiki_name) {
		this.program_wiki_name = program_wiki_name;
	}

	public List<String> getProgram_wiki_keys() {
		return program_wiki_keys;
	}

	public List<String> getProgram_wiki_keys_etc() {
		if (program_wiki_keys != null && program_wiki_keys.size() > 5) {
			return program_wiki_keys.subList(0, 5);
		}
		return program_wiki_keys;
	}

	public void setProgram_wiki_keys(List<String> program_wiki_keys) {
		this.program_wiki_keys = program_wiki_keys;
	}

	public List<KeyType> getKeyType() {
		return keyType;
	}

	public void setKeyType(List<KeyType> keyType) {
		this.keyType = keyType;
	}
}
