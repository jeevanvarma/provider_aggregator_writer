package com.blogspot.denizstij.paw.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class MainProvider {
    private static final Logger logger = LoggerFactory.getLogger(MainProvider.class);
    public static final String MAIN_SPRING_CONFIG_FILE="/spring-context.xml";
    
	private ApplicationContext applicationContext;
	private ProviderManager providerManager;

	public static void main(String[] args)  {		
		MainProvider mainProvider = new MainProvider();
		mainProvider.start();
	}

	private void loadSpringBeans() {
		try {
			applicationContext = new ClassPathXmlApplicationContext(MAIN_SPRING_CONFIG_FILE);
			providerManager = applicationContext.getBean(ProviderManager.class);
		} catch (Exception e) {
			logger.error("Error during loading spring context file:" + MAIN_SPRING_CONFIG_FILE, e);
		}
		if (providerManager == null) {
			throw new IllegalStateException("No Provider Manager is defined in spring context file:" + MAIN_SPRING_CONFIG_FILE);
		}
		logger.info("Starting all providers...");
		providerManager.start();
		logger.info("Started all providers");
	}
	
	private void start() {
		loadSpringBeans();		
	}
}
