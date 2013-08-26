package com.tcl.smart.server.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcl.smart.server.bean.BaikeBean;
import com.tcl.smart.server.service.IBaikeSortService;
import com.tcl.smart.server.util.StringUtil;

public class BaikeSortByFeatureCount implements IBaikeSortService{

	@Override
	public List<BaikeBean> sort(List<BaikeBean> baikeBeans,
			String program_name, List<String> actorDirectors) {
		if(null == baikeBeans) return null;
		if(baikeBeans.size()<=1){
			return baikeBeans;
		}
		
		Map<BaikeBean, Integer> map = new HashMap<BaikeBean, Integer>();
		for(BaikeBean bean:baikeBeans){
			map.put(bean, countNumber(program_name,actorDirectors,bean));
		}
		
		List<Map.Entry<BaikeBean, Integer>> valuedItems =
			    new ArrayList<Map.Entry<BaikeBean, Integer>>(map.entrySet());
		
		Collections.sort(valuedItems, new Comparator<Map.Entry<BaikeBean, Integer>>() {   
		    public int compare(Map.Entry<BaikeBean, Integer> o1, Map.Entry<BaikeBean, Integer> o2) {
		    	double t1 = o1.getValue();
		    	double t2 = o2.getValue();
				return t1<t2 ? 1 : t1>t2 ? -1 : 0;
		    }
		});
		
		List<BaikeBean> results = new ArrayList<BaikeBean>();
		for (int i = 0; i < valuedItems.size(); i++) {
			results.add(valuedItems.get(i).getKey());
		}
		return results;
	}
	
	/**
	 * ����Ȩ�أ����������
	 * 1. ��program_name == bean.getName(),����ǰbaike��Ŀ�������ǽ�Ŀ����ʱ��ֻʹ��actorDirectors��������Ȩ�ء�
	 * 2. ��if program_name != bean.getName(),����ǰbaike��Ŀ����Ա���ݵȵ�����ʱ��program_name�������,actorDirectorsȥ����ǰ
	 * ��Ŀ���ټ�����ء�
	 * @param program_name
	 * @param actorDirectors ���в�����program_name
	 * @param bean
	 * @return
	 */
	private int countNumber(String program_name, List<String> actorDirectors,BaikeBean bean){
		int number=0;
		List<String> actorDirectorsList = new ArrayList<String>();
		actorDirectorsList.addAll(actorDirectors);
		String allText = bean.allToText();
		//program_name == bean.getName(), count the actors number
		if(bean.getName().equals(program_name)){
			for(String temp:actorDirectorsList){
				number += StringUtil.countStrAInStrB(temp, allText);
			}
		}
		
		// if program_name != bean.getName(); bean is a actor's baike;
		// program_name is important than other feature here. If bean has program_name in it,
		// it may be the correct one. So set the value of program_name to 10.
		if(!bean.getName().equals(program_name)){
			actorDirectorsList.remove(bean.getName());
			for(String temp:actorDirectorsList){
				number += StringUtil.countStrAInStrB(temp, allText);
			}
			number += 10*StringUtil.countStrAInStrB(program_name, allText);
		}
		
		return number;
		
	}
}
