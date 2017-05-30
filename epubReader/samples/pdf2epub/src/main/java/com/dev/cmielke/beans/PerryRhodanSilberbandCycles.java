package com.dev.cmielke.beans;

import java.util.ArrayList;

import com.dev.cmielke.datatype.Collection;
import com.dev.cmielke.datatype.PRCycle;

public class PerryRhodanSilberbandCycles
{
	private PerryRhodanSilberbandCycles() { }
	
	public static final PRCycle DIE_DRITTE_MACHT = Collection.PRCycle(1, 6, "Die Dritte Macht");
	public static final PRCycle ATALN_UND_ARKON = Collection.PRCycle(7, 12, "Atlan und Arkon");
	public static final PRCycle DIE_POSBIS = Collection.PRCycle(13, 17, "Die Posbis");
	public static final PRCycle DAS_ZWEITE_IMPERIUM = Collection.PRCycle(18, 20, "Das zweite Imperium");
	public static final PRCycle DIE_MEISTER_DER_INSEL = Collection.PRCycle(21, 32, "Die Meister der Insel");
	public static final PRCycle M_87 = Collection.PRCycle(33, 44, "M 87");
	public static final PRCycle DIE_CAPPINS = Collection.PRCycle(45, 54, "Die Cappins");
	public static final PRCycle DER_SCHWARM = Collection.PRCycle(55, 63, "Der Schwarm");
	public static final PRCycle DIE_ALTMUTANTEN = Collection.PRCycle(64, 67, "Die Altmutanten");
	public static final PRCycle DAS_KOSMISCHE_SCHACHSPIEL = Collection.PRCycle(68, 73, "Das kosmische Schachspiel");
	public static final PRCycle DAS_KONZIL = Collection.PRCycle(74, 80, "Das Konzil");
	public static final PRCycle APHILIE = Collection.PRCycle(81, 93, "Aphilie");
	public static final PRCycle BARDIOC = Collection.PRCycle(94, 101, "BARDIOC");
	public static final PRCycle PAN_THAU_RA = Collection.PRCycle(102, 105, "PAN-THAU-RA");
	public static final PRCycle DIE_KOSMISCHEN_BURGEN = Collection.PRCycle(106, 110, "Die kosmischen Burgen");	
	
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
		System.out.println(PerryRhodanSilberbandCycles.getCycleName(100));
	}
}
