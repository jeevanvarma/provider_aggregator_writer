package com.blogspot.denizstij.paw.writer.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class MainWriter {
    private static final Logger logger = LoggerFactory.getLogger(MainWriter.class);
    public static final String MAIN_SPRING_CONFIG_FILE="/spring-context.xml";
    
	private ApplicationContext applicationContext;
	private IWriter writer;

	public static void main(String[] args)  {		
		MainWriter appMain = new MainWriter();
		appMain.start();
	}

	private void init() {
		try {
			applicationContext = new ClassPathXmlApplicationContext(MAIN_SPRING_CONFIG_FILE);
			writer = applicationContext.getBean(IWriter.class);
		} catch (Exception e) {
			logger.error("Error during loading spring context file:" + MAIN_SPRING_CONFIG_FILE, e);
		}
		if (writer == null) {
			throw new IllegalStateException("No File Writer is defined in spring context file:" + MAIN_SPRING_CONFIG_FILE);
		}
		writer.start();
	}
	
	private void start() {
		init();		
	}
}
