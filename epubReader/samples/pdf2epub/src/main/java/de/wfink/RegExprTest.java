package de.wfink;

import static junit.framework.Assert.*;

import org.junit.Test;

public class RegExprTest {

	@Test
	public void checkRegExpr() {
		// Kapitelfilterung (einfach)
		checkTrue("1.", "[0-9]*\\.");
		checkTrue("1. ", "[0-9]*\\.[ ]*");
		checkTrue("1..", "[0-9]*[\\.]{0,2}[ ]*");
		checkTrue("1 ", "[0-9]*[\\.]{0,2}[ ]*");

		// Erweiterte Kapitelfilterung mit �berschrift
		checkTrue("12. �berschirft", "[0-9]*[\\.]{0,2}[ ]*.{0,32}");
		checkFalse("12.35 Uhr", "[0-9]*[\\.]{0,2}[ ]+.{0,32}");
	}

	private void checkTrue(String s, String regexpr) {
		assertTrue("String '" + s + "' not match by '" + regexpr + "]", s.matches(regexpr));
	}

	private void checkFalse(String s, String regexpr) {
		assertFalse("String '" + s + "' matched by '" + regexpr + "]", s.matches(regexpr));
	}

	@Test
	public void checkIndexOf() {
		String test = "alles %1.% ist %*% mist";
		String x = "1.";
		System.out.println(test.replace("%" + x + "%", "-- 1. --"));
	}

	@Test
	public void checkChapterMark() {
		assertTrue("'1.' must be a chapter mark",isChapterMark("1."));
		assertTrue("'1. ' must be a chapter mark",isChapterMark("1. "));
		assertTrue("'123.' must be a chapter mark",isChapterMark("123."));
		assertTrue("'123. Das ist eine Überschrift' must be a chapter mark",isChapterMark("123. Das ist eine Überschrift"));
		assertFalse("'50 000' is not chapter mark",isChapterMark("50 000"));
	}

	private boolean isChapterMark(String line) {
		return line != null &&
		// Filterung nach Kapiteln welche zus�tzlich zu der Kapitelnummer eine
		// �berschrift besitzen.
				// Diese Filterung ist charakterisiert durch das zwingende Vorhandensein
				// eines Leerzeichens
				// nach dem Kapitel-Punkt '.' (siehe 1. RegEx )
				(line.matches("[0-9][0-9]*[\\.]{1,2}[ ]+.{0,32}") ||
				// Ist die Filterung nach 1.) nicht erfolgreich, bleiben nur noch 2
				// M�glichkeiten offen
				// a.) Der String ist ganz anders als die RegEx., dann handelt es sich
				// auch nicht um ein Kapitel, oder
				// b.) Der String ist einfaches Kapitel ohne �berschrift und enth�lt
				// nach dem Kapitel-Punkt keine weiteren
				// Zeichen (dies wird mittels der 2. RegEx abgepr�ft).
				line.matches("[0-9][0-9]*[\\.]{1,2}[ ]*"));
	}

}
