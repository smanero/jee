package com.dev.wfink;

import junit.framework.Assert;

import org.junit.Test;


public class RegexprTest {
	
	@Test
	public void checkPageNumber() {
		String line = " 4";
		Assert.assertTrue("no match for line with trailing blanc", line.matches("\\s[0-9]*"));
	}

}
