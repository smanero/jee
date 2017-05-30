package org.pcdm.analizador.context.node;

import java.io.File;

public class R02xProfileFactory {

	public static R02xProfile get(final R02xLote lote, final File profilePath) throws Exception {
		R02xProfile profile = null;
		if (profilePath.getName().matches(R02xWls8Profile.APP_REGEX)) {
			return new R02xWls8Profile(lote, profilePath, "");

//		} else if (profilePath.getName().matches(R02xWliProfile.APP_REGEX)) {
//			return new R02xWliProfile(lote, profilePath, "");
		}
		return profile;
	}
}
