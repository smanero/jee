package de.wfink;

import org.junit.Test;

import com.dev.cmielke.gui.beans.Options;


public class OptionsTest {

	@Test
	public void readOptions() {
		System.out.println("Ignore missing Cover = "+Options.ignoreMissingCoverImage());
		System.out.println("Show BookDialog = "+Options.showBookMetaDataDialog());
		System.out.println("INPUT "+Options.getInputPath());
		System.out.println("OUTPUT "+Options.getOutputPath());
	}
}
