package com.blogspot.denizstij.paw.writer.file;

import com.blogspot.denizstij.paw.common.state.Controllable;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface IWriter extends Controllable{
	void onEvent(String event);

}
