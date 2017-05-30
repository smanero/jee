package com.dev.cmielke.util;

import static com.dev.cmielke.util.ApplicationContants.*;
import java.io.File;

import org.apache.log4j.Logger;

import com.dev.cmielke.gui.beans.Options;

public class ExternalZipTool {
	private static Logger log = Logger.getLogger(ExternalZipTool.class);

	public static void buildEPubFile(String filename) {
		log.debug("> buildEPubFile");

		try {
			File outputFile = new File(Options.getOutputPath(), filename + ".epub");
			log.debug("Calling external ZIP-application");

			log.debug("1. Call: Adding only mimetype");
			log.debug("ProcessBuilder-String [cmd, /c, start, ../extCmd/zip.exe, -Xr9D, " + outputFile.getCanonicalPath() + ", mimetype]");
			if (!Options.getOutputPath().exists()) {
				Options.getOutputPath().mkdirs();
			} else if (outputFile.exists()) {
				log.trace("Delete existing file: " + outputFile.getCanonicalPath());
				outputFile.delete();
			}
			ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "start", "../extCmd/zip.exe", "-Xr9D", outputFile.getCanonicalPath(),
					"mimetype");
			log.debug("Starting-Directory: [" + OUTPUT_DIRECTORY + "]");
			builder.directory(new File(OUTPUT_DIRECTORY));
			Process p = builder.start();

			int returnCode = p.waitFor();
			if (returnCode == 0) {
				log.trace("Process return - OK !");
			} else {
				log.fatal("Process return with ReturnCode [" + returnCode + "] - FAILURE !  Terminating Application!");
				System.exit(1);
			}
			log.debug("1. Call: complete !");

			log.debug("2. Call: Adding all other files");
			log.debug("ProcessBuilder-String [cmd, /c, start, ../extCmd/zip.exe, -Xr9D, " + outputFile.getCanonicalPath() + ", *]");
			builder = new ProcessBuilder("cmd", "/c", "start", "../extCmd/zip.exe", "-Xr9D", outputFile.getCanonicalPath(), "*");
			builder.directory(new File("generatedFiles"));
			p = builder.start();

			returnCode = p.waitFor();
			if (returnCode == 0) {
				log.trace("Process return - OK !");
			} else {
				log.fatal("Process return with ReturnCode [" + returnCode + "] - FAILURE !  Terminating Application!");
				System.exit(1);
			}
			log.debug("2. Call: complete !");

		} catch (Exception e) {
			throw new RuntimeException("EPub file '"+filename+"' konnte nicht geschrieben werden!",e);
		}
		log.debug("< buildEPubFile");
		return;
	}

	public static void main(String[] args) {
		LoggingUtils.configureLog4J();
		ExternalZipTool.buildEPubFile("PerryRhodan_Band474");
	}
}
