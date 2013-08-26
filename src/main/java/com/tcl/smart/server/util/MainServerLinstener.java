package com.tcl.smart.server.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainServerLinstener implements ServletContextListener{

	private static Logger logger = LoggerFactory.getLogger(MainServerLinstener.class);

	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//execute while initiate
		logger.info("Server start!!!");
	}

	public static void executeByServer(Runnable task,long initialDelay, long period, TimeUnit unit){
		scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//execute while destroyed
		scheduler.shutdown();
		
	}
	
	public static void stopServer(){
		scheduler.shutdown();
	}
	
	public static boolean isStop(){
		return scheduler.isShutdown();
	}
}
