package com.blogspot.denizstij.paw.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class MainAggregator {
    private static final Logger logger = LoggerFactory.getLogger(MainAggregator.class);
    public static final String MAIN_SPRING_CONFIG_FILE="/spring-context.xml";
    
	private ApplicationContext applicationContext;
	private IAggregator aggregator;

	public static void main(String[] args)  {		
		MainAggregator appMain = new MainAggregator();
		appMain.start();
	}

	private void init() {
		try {
			applicationContext = new ClassPathXmlApplicationContext(MAIN_SPRING_CONFIG_FILE);
			aggregator = applicationContext.getBean(Aggregator.class);
		} catch (Exception e) {
			logger.error("Error during loading spring context file:" + MAIN_SPRING_CONFIG_FILE, e);
		}
		if (aggregator == null) {
			throw new IllegalStateException("No Aggregagor is defined in spring context file:" + MAIN_SPRING_CONFIG_FILE);
		}
		aggregator.start();
	}
	
	private void start() {
		init();		
	}
}
