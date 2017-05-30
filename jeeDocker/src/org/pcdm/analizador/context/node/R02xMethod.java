package org.pcdm.analizador.context.node;

import java.util.ArrayList;
import java.util.List;

public class R02xMethod {
	
	public String returnType;
	public String methodName;
	public List<String> paramType = new ArrayList<String>();
	public List<String> paramName = new ArrayList<String>();
	
	public String returnType() {
		return returnType;
	}
	public String methodName() {
		return methodName;
	}
	public List<String> paramType() {
		return paramType;
	}
	public List<String> paramName() {
		return paramName;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(returnType).append(" ").append(methodName).append("(");
		for (int i=0; i<paramType.size(); i++) {
			if (i > 0 ) {
				sb.append(", ");
			}
			sb.append(paramType.get(i)).append(" ").append(paramName.get(i));
		}
		sb.append(")");
		return sb.toString();
	}
}
