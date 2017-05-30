package org.dsc.db.model;

import java.util.List;

import schemacrawler.schema.Column;

public class CrColumn {
	public String name;
	// tipo de dato
	public String type;
	// atributos de la columna
	public String attrs;
	public boolean pk = false;
	public boolean fk = false;
	public boolean ix = false;
	public boolean nb = false;
	// comentario en bd
	public String comment;
	
	// size volumetria
	public long lg = 0;
	
	// relaciones definidas de la columna con otras tablas
	public List<String> relations;
	
	public CrColumn(final Column column, long lg) {
	   this.name = column.getName();
	   this.type = column.getType().toString() + "(" + lg + ")";
	   this.lg = lg;
   }
}
