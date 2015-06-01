package com.blogspot.denizstij.paw.common.state;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface Controllable {
	String getName();
	void setHandle(IState state);
	void start();
	void stop();
	void handleStart();
	void handleStop();
}
