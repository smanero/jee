package org.croquet.nabata;

import java.io.InputStream;
import java.net.URL;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;

public class Sphinx4Recogniser {
	
	public static final String RES_MODEL_BASE = "resource:/sphinx4/es-es";

	public static void main(String[] args) throws Exception {

		Configuration cfg = new Configuration();

		String amPath = RES_MODEL_BASE + "/model";
		String dictPath = RES_MODEL_BASE + "/words.dict";
		String lmPath = RES_MODEL_BASE + "/es-es.lm";
		cfg.setAcousticModelPath(amPath);
		cfg.setDictionaryPath(dictPath);
		cfg.setLanguageModelPath(lmPath);

		URL url = Sphinx4Recogniser.class.getResource("/test.wav");
		SpeechAligner aligner = new SpeechAligner(amPath, dictPath, null);
		aligner.align(url, "nabata");

		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(cfg);
		InputStream stream = Sphinx4Recogniser.class.getResourceAsStream("/test.wav");
		// new FileInputStream(new File("/assets/recording.m4u"));

		recognizer.startRecognition(stream);
		SpeechResult result = recognizer.getResult();
		System.out.format("Hypothesis: %s\n", result.getHypothesis());

		// Get individual words and their times.
		for (WordResult r : result.getWords()) {
			System.out.println(r);
		}

		recognizer.stopRecognition();
	}
	
}