package org.pcdm.analizador.context.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pcdm.analizador.context.R02xConst;


public abstract class R02xNode implements Comparable<R02xNode> {
	protected final R02xNode parent;
	protected final File file;
	protected final String nodeType;
	protected String fqn;

	protected final List<File> files = new ArrayList<File>(0);
	protected final Map<String, R02xNode> childs = new HashMap<String, R02xNode>(0);
	private boolean covered = false;

	// stats process timing
	protected long processIniStamp = 0;
	protected long processEndStamp = 0;
	protected long processDuration = 0;

	// stats process counters
	protected final Map<String, Long> counters = new HashMap<String, Long>(0);

	// process messages
	protected final String tab;
	protected final List<String> messages = new ArrayList<String>(0);

	/**
	 * @param file
	 * @param nodeType
	 * @param level
	 */
	public R02xNode(final R02xNode parent, final File file, final String nodeType, int level) {
		this.parent = parent;
		this.file = file;
		this.nodeType = nodeType;
		this.fqn = "LOTE";
		if (R02xNodeType.FILE.equals(nodeType)) {
			// si es modulo o file
			String parentPathEx = parent.getAbsolutePath().replaceAll("//", "[.]").replace('\\', '.');
			String pathEx = getAbsolutePath().replaceAll("//", "[.]").replace('\\', '.');
			this.fqn = pathEx.replaceAll(parentPathEx, "").replaceFirst("[.]", "");
		} else if (R02xNodeType.PROFILE.equals(nodeType) || R02xNodeType.MODULE.equals(nodeType)) {
			// si es profile fqn sera el nombre del directorio = appCode
			this.fqn = getFileName();
		}

		StringBuffer stab = new StringBuffer();
		for (int i = 0; i < level; i++) {
			stab.append("   ");
		}
		this.tab = stab.toString() + "| ";
	}

	public R02xNode getParent() {
		return parent;
	}

	public File getFile() {
		return file;
	}

	public String getFileName() {
		return file.getName();
	}

	public String[] getNameExt() {
		String[] ext = new String[] { getFileName(), "" };
		if (R02xNodeType.FILE.equals(nodeType)) {
			ext[0] = getFileName().substring(0, getFileName().indexOf("."));
			ext[1] = getFileName().substring(getFileName().indexOf(".") + 1).toUpperCase();
			if (ext[1].equals("JPD") && ext[0].matches(".*Facade.*")) {
				ext[1] = "FCJPD";
			}
		}
		return ext;
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	public String getFqn() {
		return fqn;
	}

	public String getNodeType() {
		return nodeType;
	}

	public boolean isCovered() {
		return covered;
	}

	public Collection<File> getFiles() {
		return files;
	}

	public void addChild(final R02xNode child) {
		String key = child.getFqn();
		childs.put(key, child);
	}

	public Collection<R02xNode> getChilds() {
		List<R02xNode> lstChilds = new ArrayList<R02xNode>(childs.values());
		Collections.sort(lstChilds);
		return lstChilds;
	}

	public List<String> getMessages() {
		return messages;
	}

	public Map<String, Long> getCounters() {
		return counters;
	}

	public void report(final String msg) {
		messages.add(tab + msg);
	}

	public void reportIni(boolean stats) {
		// node analizado
		covered = true;

		StringBuffer sLog = new StringBuffer();
		sLog.append("\n").append(tab).append("* " + file.getName()).append(" process ini");
		if (stats) {
			processIniStamp = System.nanoTime();
			sLog.append("--st stamp: ").append(processIniStamp);
		}
		messages.add(tab + sLog.toString());
	}

	public void reportEnd(boolean stats) {
		StringBuffer sLog = new StringBuffer();
		sLog.append(file.getName()).append(" process end");
		if (stats) {
			// report
			processEndStamp = System.nanoTime();
			processDuration = processEndStamp - processIniStamp;
			sLog.append(" --st stamp: ").append(processEndStamp).append(" --st duration: ").append(formatDuration());
			// actualizacion contadores
			for (R02xNode child : getChilds()) {
				for (String counter : child.getCounters().keySet()) {
					count(counter, child.getCounters().get(counter));
				}
			}
			// report contadores
			for (String counter : counters.keySet()) {
				// report
				sLog.append("\n").append(tab).append("-st ").append(counter).append("=").append(counters.get(counter));
				// herencia de contadores
				if (null != parent) {
					parent.count(counter, counters.get(counter));
				}
			}
		}
		messages.add(tab + sLog.toString());
		if (null != parent) {
			// herencia bottom-top de messages
			parent.getMessages().addAll(messages);
		}
	}

	public void count(final String counter, final Long incCount) {
		// actualizacion
		Long count = counters.get(counter);
		if (null != count) {
			counters.put(counter, count + incCount);
		} else {
			counters.put(counter, incCount);
		}
	}

	protected String formatDuration() {
		double duration = processDuration / R02xConst.SEGS;
		String ret = duration + " segs";
		if (duration > 60.0) {
			duration = processDuration / R02xConst.MINS;
			ret = duration + " mins";
			if (duration > 60.0) {
				duration = processDuration / R02xConst.HOURS;
				ret = duration + " horas";
			}
			if (duration > 24.0) {
				duration = processDuration / R02xConst.DAYS;
				ret = duration + " dias";
			}
		}
		return ret;
	}

	@Override
	public int compareTo(R02xNode node) {
		return getFileName().compareTo(node.getFileName());
	}
}
