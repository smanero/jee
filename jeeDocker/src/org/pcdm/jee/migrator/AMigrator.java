package org.pcdm.jee.migrator;

import org.pcdm.analizador.context.R02xContext;

public abstract class AMigrator implements IMigrator {

	protected R02xContext ctx;

	public AMigrator(final R02xContext ctx) {
		this.ctx = ctx;
	}
}
