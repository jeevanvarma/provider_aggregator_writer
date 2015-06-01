package com.blogspot.denizstij.paw.writer.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blogspot.denizstij.paw.common.state.IState;
import com.blogspot.denizstij.paw.common.state.StopState;
import com.blogspot.denizstij.paw.writer.file.sub.IAggregatorSubscriber;
/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class FileWriter implements IWriter {

	private static final Logger logger = LoggerFactory.getLogger(FileWriter.class);
	private IState state= new StopState(this);	
	private IAggregatorSubscriber aggregatorSubscriber;
	private String outputDir=System.getProperty("java.io.tmpdir") + "/paw-writer/";
	private int numOfWriterThread=3;
	private Executor executorService;
	private File outputDirFile;
	private ConcurrentHashMap<String, FileNameBean> fileNameMap= new ConcurrentHashMap<>();
	
	
	@Override
	public void onEvent(String event) {
		executorService.execute(new FileWriterRunnable(event));
	}	
		
	
	@Override
	public void start() {		
		state.start();
	}

	@Override
	public void stop() {
		state.stop();
	}

	@Override
	public String getName() {
		return "File Writer";
	}

	@Override
	public void setHandle(IState state) {
		this.state=state;
		
	}

	@Override
	public void handleStart() {
		createParentOutputDir();
		executorService= Executors.newFixedThreadPool(numOfWriterThread, new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {				
				return new Thread(r,"File writer thread");
			}
		});
		aggregatorSubscriber.setWriter(this);
		aggregatorSubscriber.start();
	}

	private void createParentOutputDir(){
		outputDirFile= new File(outputDir);
		if (!outputDirFile.exists()){
			boolean res = outputDirFile.mkdirs();
			if (!res) {			
				String msg="Can not create parent dir:"+outputDir;
				IllegalStateException ex = new IllegalStateException(msg);
				logger.error(msg,ex);
				throw ex;
			}
		}
	}
	
	@Override
	public void handleStop() {
		aggregatorSubscriber.stop();
	}

	public IAggregatorSubscriber getAggregatorSubscriber() {
		return aggregatorSubscriber;
	}

	public void setAggregatorSubscriber(IAggregatorSubscriber aggregatorSubscriber) {
		this.aggregatorSubscriber = aggregatorSubscriber;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	
	public int getNumOfWriterThread() {
		return numOfWriterThread;
	}

	public void setNumOfWriterThread(int numOfWriterThread) {
		this.numOfWriterThread = numOfWriterThread;
	}


	private class FileNameBean {
		private String fileName;
		private volatile AtomicInteger count= new AtomicInteger(0); 
		
		public FileNameBean(String event) {
			// event content is used as filename
			fileName=event.replace(',', '_').replace('\n', '_').replaceAll("\\\\s", "_");	
			fileName=fileName.substring(0, fileName.length()-1);
		}	

	}
	
	private class FileWriterRunnable implements Runnable{

		private String event;
		public FileWriterRunnable(String event) {
			this.event=event;
		}

		@Override
		public void run() {			
			String outputFileName;
			FileNameBean fileNameBean = fileNameMap.get(event);
			if (fileNameBean==null){
				fileNameBean= new FileNameBean(event);
				FileNameBean preVal = fileNameMap.putIfAbsent(event, fileNameBean);
				if (preVal!=null){
					fileNameBean=preVal;
				}
			}
			
			int curVal=fileNameBean.count.getAndIncrement();
			if (curVal==0){
				outputFileName = fileNameBean.fileName;
			} else {
				outputFileName = "__"+curVal+"_"+fileNameBean.fileName;
			}
			writeToFile(outputFileName);			
			fileNameBean.count.decrementAndGet();
		}

		protected void writeToFile(String outputFileName) {
			String fullName=outputDir+File.separator+outputFileName+".csv";
			BufferedWriter out =null;
			try {
				out=new BufferedWriter(new java.io.FileWriter(fullName));
				out.write("Instrument,Value");
				out.newLine();
				out.write(event);
				out.flush();
			} catch (Exception e ){
				logger.error("Can not write to file"+ event,e);
			} finally {
				if (out!=null){
					try {
						out.close();
					} catch (IOException e) {
						logger.error ("Can not close file",outputFileName);
					}
				}
			}			
		}		
	}
}
