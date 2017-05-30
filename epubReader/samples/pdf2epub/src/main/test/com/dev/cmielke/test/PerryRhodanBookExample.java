package com.dev.cmielke.test;

import com.dev.cmielke.beans.BookType;
import com.dev.cmielke.beans.PerryRhodanBook;
import com.dev.cmielke.beans.PerryRhodanCycles;

public class PerryRhodanBookExample extends PerryRhodanBook
{
	private static PerryRhodanBookExample instance = null;
	
	private PerryRhodanBookExample()
	{
		String introduction = "Auf Terra und den anderen Menschheitswelten schreibt man Mitte Juni des Jahres 3457. Im Solaren Imperium und "+
		"bei den anderen Völkern der Galaxis herrscht Ruhe. Von der PAD-Seuche, die noch vor kurzem alles Leben in der "+
		"Galaxis zu vernichten drohte, gibt es keine Spur mehr. "+
		"Daß die Menschen und die übrigen Völker noch leben, verdanken sie, ohne es in ihrer Gesamtheit zu wissen, "+
		"einem Zeitparadoxon und einer Zeitkorrektur. "+
		"Perry Rhodan ist der Mann, der die Zeitkorrektur vornahm, doch die Person, die das Zeitparadoxon "+
		"herbeiführte, aufgrund dessen die rettende Korrektur erst bewerkstelligt werden konnte, ist Markhor de Lapal, "+
		"der bei seinem Auftreten auf der todgeweihten Erde unter dem Namen Kol Mimo agierte. "+
		"Die über die Rettungsaktion informierten Personen sind natürlich bereit, Markhor de Lapal Dank und "+
		"Anerkennung zu zollen, doch der geheimnisvolle Mann will keinen Dank - er will einen Plan durchführen, dem "+
		"Perry Rhodan um der Menschheit willen niemals zustimmen kann. "+
		"Da greift Lapal zu einer List - und die phantastische Reise eines Gehirns beginnt. Es wird zum GEHIRN IN "+
		"FESSELN...";
		
		setType(BookType.HEFTROMAN);
		setTitle("Die Zeitkorrektur");
		setSubtitle("Rückkehr zur Eiswelt - Zeitablauf Beta wird eingeleitet");
		setBookNumber(621);
		setCycle(PerryRhodanCycles.getCycleName(getBookNumber()));
		setIntroduction(introduction);
		setAuthors("H.G. Wevers");
		
		String ca = "Perry Rhodan - Der Großadministrator kämpft mit seinem negativen Ich.";
		String cb = "Atlan - Perry Rhodans Kampfgefährte.";
		String cc = "Perry Rhodan II und Roi Danton II - Der Diktator und sein Sohn werden gejagt.";
		String cd = "Markhor da Lapal - Schöpfer eines Zeitparodoxons.";
		String ce = "Goshmo-Khan, Alaska Saedelaere und Mentro Kosum - Lapals Begleiter.";
		
		getCharacters().add(ca);
		getCharacters().add(cb);
		getCharacters().add(cc);
		getCharacters().add(cd);
		getCharacters().add(ce);
	}
	
	public static PerryRhodanBook getInstance()
	{
		if(instance == null)
		{
			instance = new PerryRhodanBookExample();
		}
		return instance;
	}
}
