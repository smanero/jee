package org.dsc.db.model;

import java.util.HashMap;

public class CrSchema {
	public String name;
//	<String, CrTable>
	public HashMap<String, CrTable> tables = new HashMap<String, CrTable>();
//	<String, CrView>
	public HashMap<String, CrView> views = new HashMap<String, CrView>();

	public CrSchema(String schemaName) {
		name = schemaName;
	}
}
