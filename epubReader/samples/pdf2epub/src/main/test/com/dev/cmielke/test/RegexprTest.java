package com.dev.cmielke.test;

import junit.framework.Assert;

import org.junit.Test;


public class RegexprTest {
	
	@Test
	public void checkPageNumber() {		
		String line = "Rückkehr zur Eißwelt - Zeitablauf Beta wird eingeleitet.";
		Assert.assertTrue("no match for line.", line.matches("[a-zA-Z_0-9|ü|Ü|ö|Ö|ä|Ä|ß|,|\\.| |-]*"));
		line = "Goshmo-Khan, Alaska Saedelaere und Mentro Kosum - Lapals Begleiter.";
		Assert.assertTrue("no match for line.", line.matches("[a-zA-Z_0-9|ü|Ü|ö|Ö|ä|Ä|ß|,|\\.| |-]*"));
	}

}
