package org.croquet.nabata;

//import javax.speech.*;
//import javax.speech.recognition.*;
//import javax.speech.synthesis.*;

public class SpeechRecog {
	
//	Engine engine;
//	{
//		engine = Central.createRecognizer();
//
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					engine.allocate();
//				}
//				catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//
//		// Do other stuff while allocation takes place
//		...
//
//		// Now wait until allocation is complete
//		engine.waitEngineState(Engine.ALLOCATED);
//		}
//	}
//
//	public static void main(String[] args) {
//		// Get a synthesizer for the default locale
//		Synthesizer synth = Central.createSynthesizer(null);
//		// Get a recognizer for the default locale
//		Recognizer rec = Central.createRecognizer(null);
//	}
//	
//	/** Get a dictation recognizer for the default locale */
//	Recognizer createDictationRecognizer()
//	{
//		
//		// Get the list of matching engine modes
//		EngineList list = Central.availableRecognizers(required);
//
//		// Test whether the list is empty - any suitable synthesizers?
//		if (list.isEmpty()) ...
//		
//		// Create a mode descriptor with all required features
//		RecognizerModeDesc required = new RecognizerModeDesc();
//		required.setDictationGrammarSupported(Boolean.TRUE);
//		return Central.createRecognizer(required);
//	}
//	
//	Recognizer getSpanishDictation(String name)
//	{
//		RecognizerModeDesc required = new RecognizerModeDesc();
//		required.setLocale(new Locale("es", ""));
//		required.setDictationGrammarSupported(Boolean.TRUE);
//
//		// Get a list of Spanish dictation recognizers
//		EngineList list = Central.availableRecognizers(required);
//
//		if (list.isEmpty()) return null; // nothing available
//
//		// Create a description for an engine trained for the speaker
//		SpeakerProfile profile = new SpeakerProfile(null, name, null);
//		RecognizerModeDesc requireSpeaker = new RecognizerModeDesc();
//		requireSpeaker.addSpeakerProfile(profile);
//
//		// Prune list if any recognizers have been trained for speaker
//		if (list.anyMatch(requireSpeaker))
//			list.requireMatch(requireSpeaker);
//
//		// Now try to create the recognizer
//		RecognizerModeDesc first = 
//						(RecognizerModeDesc)(list.firstElement());
//		try {
//			return Central.createRecognizer(first);
//		} catch (SpeechException e) {
//			return null;
//		}
//	}
//
//	
}
