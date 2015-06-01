package com.blogspot.denizstij.aggregator.datasource;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.blogspot.denizstij.paw.common.MarketValue;
import com.blogspot.denizstij.paw.provider.datasource.RandomMarketValueDataSource;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-context.xml")
public class RandomMarketValueDataSourceTest {
	
	private HashSet<String> polarisInstrumentSet;
	@Resource(name="polarisRandomMarketValueDataSource")
	private RandomMarketValueDataSource polarisRandomMarketValueDataSource =null;

	private HashSet<String> orionInstrumentSet;
	@Resource(name="orionRandomMarketValueDataSource")
	private RandomMarketValueDataSource orionRandomMarketValueDataSource =null;
	
	@Before
	public void setUp(){
		orionInstrumentSet= new HashSet<String>();
		orionInstrumentSet.add("AAA");
		orionInstrumentSet.add("BBB");
		orionInstrumentSet.add("CCC");
		orionInstrumentSet.add("DDD");
		orionInstrumentSet.add("EEE");
		orionInstrumentSet.add("FFF");
		
		polarisInstrumentSet= new HashSet<String>();
		polarisInstrumentSet.add("AAA");
		polarisInstrumentSet.add("BBB");
		polarisInstrumentSet.add("CCC");
		polarisInstrumentSet.add("GGG");
		polarisInstrumentSet.add("HHH");
		polarisInstrumentSet.add("III");
		polarisInstrumentSet.add("JJJ");
	}	

	@Test
	public void testPolarisNameValueRangeIsBetween5_5and10_5() {
		testValueRangeIsBetween5_5and10_5(polarisRandomMarketValueDataSource,polarisInstrumentSet);	
	}

	@Test
	public void testOrionNameValueRangeIsBetween5_5and10_5() {
		testValueRangeIsBetween5_5and10_5(orionRandomMarketValueDataSource,orionInstrumentSet);	
	}

	
	private void testValueRangeIsBetween5_5and10_5(RandomMarketValueDataSource randomMarketValueDataSource, HashSet<String> instrumentSet) {
		int maxSample=10000;
		for (int i=0;i<maxSample;i++){
			MarketValue marketValue = randomMarketValueDataSource.next();
			// value should be between 5.5 and 10.5 
			assertTrue(marketValue.getValue()>=5.5 && marketValue.getValue()<10.5 );
			assertTrue(instrumentSet.contains(marketValue.getName()));
		}
	}
}
