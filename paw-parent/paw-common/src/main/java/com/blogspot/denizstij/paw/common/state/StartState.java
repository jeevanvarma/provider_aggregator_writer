package com.blogspot.denizstij.paw.common.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class StartState implements IState {	
	private static final Logger logger = LoggerFactory.getLogger(StartState.class);	
	private Controllable controllable;
	
	public StartState(Controllable controllable){
		this.controllable=controllable;
	}
	
	public void start(){		
		logger.warn("Already started :"+controllable.getName());			
	}
	
	public void stop() {
		logger.info("Stopping :"+controllable.getName());
		controllable.handleStop();
		controllable.setHandle(new StopState(controllable));
		logger.info("Stopped :"+controllable.getName());
	}

	public boolean isStarted(){
		return true;
	}	
}
