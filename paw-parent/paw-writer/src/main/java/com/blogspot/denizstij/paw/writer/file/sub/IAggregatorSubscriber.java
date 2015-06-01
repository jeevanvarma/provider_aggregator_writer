package com.blogspot.denizstij.paw.writer.file.sub;


import com.blogspot.denizstij.paw.common.state.Controllable;
import com.blogspot.denizstij.paw.writer.file.IWriter;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public interface IAggregatorSubscriber extends  Controllable {
	void setWriter(IWriter fileWriter);	
}
