package com.tcl.smart.server.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author fanjie
 * @date 2013-3-11
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
	public static final String dbUser = "";
	public static final String dbPwd = "";

	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		props.setProperty("db.user", dbUser);
		props.setProperty("db.password", dbPwd);
		super.processProperties(beanFactory, props);
	}
}