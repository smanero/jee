package com.dev.cmielke.beans;

import java.util.ArrayList;

import com.dev.cmielke.datatype.Collection;
import com.dev.cmielke.datatype.PRCycle;

public class PerryRhodanCycles
{
	private PerryRhodanCycles() { }
	
	public static final PRCycle DIE_DRITTE_MACHT = Collection.PRCycle(1, 49, "Die Dritte Macht");
	public static final PRCycle ATALN_UND_ARKON = Collection.PRCycle(50, 99, "Atlan und Arkon");
	public static final PRCycle DIE_POSBIS = Collection.PRCycle(100, 149, "Die Posbis");
	public static final PRCycle DAS_ZWEITE_IMPERIUM = Collection.PRCycle(150, 199, "Das zweite Imperium");
	public static final PRCycle DIE_MEISTER_DER_INSEL = Collection.PRCycle(200, 299, "Die Meister der Insel");
	public static final PRCycle M_87 = Collection.PRCycle(300, 399, "M 87");
	public static final PRCycle DIE_CAPPINS = Collection.PRCycle(400, 499, "Die Cappins");
	public static final PRCycle DER_SCHWARM = Collection.PRCycle(500, 569, "Der Schwarm");
	public static final PRCycle DIE_ALTMUTANTEN = Collection.PRCycle(570, 599, "Die Altmutanten");
	public static final PRCycle DAS_KOSMISCHE_SCHACHSPIEL = Collection.PRCycle(600, 649, "Das kosmische Schachspiel");
	public static final PRCycle DAS_KONZIL = Collection.PRCycle(650, 699, "Das Konzil");
	public static final PRCycle APHILIE = Collection.PRCycle(700, 799, "Aphilie");
	public static final PRCycle BARDIOC = Collection.PRCycle(800, 867, "BARDIOC");
	public static final PRCycle PAN_THAU_RA = Collection.PRCycle(868, 899, "PAN-THAU-RA");
	public static final PRCycle DIE_KOSMISCHEN_BURGEN = Collection.PRCycle(900, 999, "Die kosmischen Burgen");
	public static final PRCycle DIE_KOSMISCHE_HANSE = Collection.PRCycle(1000, 1099, "Die kosmische Hanse");
	public static final PRCycle DIE_ENDLOSE_ARMADA = Collection.PRCycle(1100, 1199, "Die endlose Armada");
	public static final PRCycle DIE_CHRONOFOSSILIEN = Collection.PRCycle(1200, 1299, "Die Chronofossilien");
	public static final PRCycle DIE_GAENGER_DES_NETZES = Collection.PRCycle(1300, 1349, "Die G‰nger des Netzes");
	public static final PRCycle TARKAN = Collection.PRCycle(1350, 1399, "Tarkan");
	public static final PRCycle DIE_CANTAROS = Collection.PRCycle(1400, 1499, "Die Cantaros");
	public static final PRCycle DIE_LINGUIDEN = Collection.PRCycle(1500, 1599, "Die Linguiden");
	public static final PRCycle DIE_ENNOX = Collection.PRCycle(1600, 1649, "Die Ennox");
	public static final PRCycle DIE_GROSSE_LEERE = Collection.PRCycle(1650, 1699, "Die Groﬂe Leere");
	public static final PRCycle DIE_AYINDI = Collection.PRCycle(1700, 1749, "Die Ayindi");
	public static final PRCycle DIE_HAMAMESCH = Collection.PRCycle(1750, 1799, "Die Hamamesch");
	public static final PRCycle DIE_TOLKANDER = Collection.PRCycle(1800, 1875, "Die Tolkander");
	public static final PRCycle DIE_HELIOTISCHEN_BOLLWERKE = Collection.PRCycle(1876, 1899, "Die Heliotischen Bollwerke");
	public static final PRCycle DER_SECHSTE_BOTE = Collection.PRCycle(1900, 1949, "Der sechste Bote");
	public static final PRCycle MATERIA = Collection.PRCycle(1950, 1999, "MATERIA");
	public static final PRCycle DIE_SOLARE_RESIDENZ = Collection.PRCycle(2000, 2099, "Die Solare Residenz");
	public static final PRCycle DAS_REICH_TRADOM = Collection.PRCycle(2100, 2199, "Das Reich Tradom");
	public static final PRCycle STERNENOZEAN = Collection.PRCycle(2200, 2299, "Sternenozean");
	public static final PRCycle TERRANOVA = Collection.PRCycle(2300, 2399, "TERRANOVA");
	public static final PRCycle NEGASPHAERE = Collection.PRCycle(2400, 2499, "Negasph‰re");
	
	
	private static final ArrayList<PRCycle> cycleList = new ArrayList<PRCycle>();
	
	static
	{
		cycleList.add(DIE_DRITTE_MACHT);
		cycleList.add(ATALN_UND_ARKON);
		cycleList.add(DIE_POSBIS);
		cycleList.add(DAS_ZWEITE_IMPERIUM);
		cycleList.add(DIE_MEISTER_DER_INSEL);
		cycleList.add(M_87);
		cycleList.add(DIE_CAPPINS);
		cycleList.add(DER_SCHWARM);
		cycleList.add(DIE_ALTMUTANTEN);
		cycleList.add(DAS_KOSMISCHE_SCHACHSPIEL);
		cycleList.add(DAS_KONZIL);
		cycleList.add(APHILIE);
		cycleList.add(BARDIOC);
		cycleList.add(PAN_THAU_RA);
		cycleList.add(DIE_KOSMISCHEN_BURGEN);
		cycleList.add(DIE_KOSMISCHE_HANSE);
		cycleList.add(DIE_ENDLOSE_ARMADA);
		cycleList.add(DIE_CHRONOFOSSILIEN);
		cycleList.add(DIE_GAENGER_DES_NETZES);
		cycleList.add(TARKAN);
		cycleList.add(DIE_CANTAROS);
		cycleList.add(DIE_LINGUIDEN);
		cycleList.add(DIE_ENNOX);
		cycleList.add(DIE_GROSSE_LEERE);
		cycleList.add(DIE_AYINDI);
		cycleList.add(DIE_HAMAMESCH);
		cycleList.add(DIE_TOLKANDER);
		cycleList.add(DIE_HELIOTISCHEN_BOLLWERKE);
		cycleList.add(DER_SECHSTE_BOTE);
		cycleList.add(MATERIA);
		cycleList.add(DIE_SOLARE_RESIDENZ);
		cycleList.add(DAS_REICH_TRADOM);
		cycleList.add(STERNENOZEAN);
		cycleList.add(TERRANOVA);
		cycleList.add(NEGASPHAERE);
	}
	
	public static String getCycleName(int bookNumber)
	{
		String retValue = "";
		for(PRCycle cycle : cycleList)
		{
			if(cycle.contains(bookNumber))
			{
				retValue = cycle.getName();
				break;
			}
		}
		return retValue;
	}
	
	public static void main(String[] args)
	{
		System.out.println(PerryRhodanCycles.getCycleName(100));
	}
}
