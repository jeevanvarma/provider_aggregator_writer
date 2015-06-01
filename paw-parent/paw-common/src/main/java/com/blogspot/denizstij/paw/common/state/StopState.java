package com.blogspot.denizstij.paw.common.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class StopState implements IState {	
	private static final Logger logger = LoggerFactory.getLogger(StopState.class);	
	private Controllable controllable;
	
	public StopState(Controllable controllable){
		this.controllable=controllable;
	}
	
	public void start(){		
		logger.info("Starting :"+controllable.getName());
		controllable.setHandle(new StartState(controllable));
		controllable.handleStart();		
		logger.info("Started :"+controllable.getName());
	}
	
	public void stop() {
		logger.warn("Already stopped :"+controllable.getName());
	}

	public boolean isStarted(){
		return false;
	}	
}
