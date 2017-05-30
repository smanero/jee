package org.dsc.db.model;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author bimalose
 *
 */
public class CrTable {
	public String name;
	public String synonym;
	public String comment;
	
	// rows volumetria
	public long numRows = 0;
	// size volumetria
	public long avlg = 0;

	//
	public HashMap<String, CrColumn> columns = new HashMap<String, CrColumn>();
	//	
	public HashMap<String, String> pks = new HashMap<String, String>();
	public HashMap<String, String> fks = new HashMap<String, String>();
	public HashMap<String, String> ixs = new HashMap<String, String>();
	public HashMap<String, String> cks = new HashMap<String, String>();
	//	
	public HashMap<String, List<String>> relations = new HashMap<String, List<String>>();
	public HashMap<String, List<String>> references = new HashMap<String, List<String>>();
	
	public CrTable(String canonicalTableName, long numRows) {
		this.name = canonicalTableName;
		this.numRows = numRows;
	}
	
	public String fullName() {
		String fname = this.name;
		if (null != synonym && !"".equals(synonym)) {
			fname += " - " + synonym;
		} else {
			synonym = "";
		}
		return fname;
	}
	
	// descripcion en bd de la tabla
	private String desc;

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	public void dimension() {
		for (CrColumn crcolumn : columns.values()) {
			avlg += crcolumn.lg;
		}
	}
}
