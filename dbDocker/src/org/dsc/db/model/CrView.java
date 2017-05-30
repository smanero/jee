package org.dsc.db.model;

public class CrView {
	public String name;
	public String def;
	public String synonym;
	public String comment;
	
	public CrView(String canonicalTableName) {
	   name = canonicalTableName;
   }
	
	public String fullName() {
		String fname = this.name;
		if (null != synonym && !"".equals(synonym)) {
			fname += " - " + synonym;
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
}
