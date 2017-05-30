<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match='process'> 
package <xsl:value-of select='/migration/@package'/>;

import java.rmi.RemoteException;
import org.apache.log4j.Logger;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * @ejb.bean name="R02z<xsl:value-of select='/migration/@functional'/>EJB"
 *           jndi-name="r02z.r02z<xsl:value-of select='/migration/@functional'/>EJB"
 *           type="Stateless"
 *           view-type="remote"
 *           
 * @weblogic.enable-call-by-reference True
 * @weblogic.dispatch-policy r02.executeQueue.level3
 *
 * @integration: <xsl:value-of select='@name'/>EJB
 */
public class <xsl:value-of select='/migration/@className'/> implements SessionBean {
	private static final Logger logger = Logger.getLogger(<xsl:value-of select='/migration/@className'/>.class.getName());
   
	/** The session context */
	private SessionContext context;
	
	// private <xsl:value-of select='@name'/>Helper helper;

	public <xsl:value-of select='/migration/@className'/>() {
	   // helper = new <xsl:value-of select='@name'/>Helper();
	}
	
    <xsl:for-each select="//clientRequest">
    
    /**
     * <xsl:value-of select='@name'/>
     *
     * @ejb.interface-method
     * @ejb.transaction type="Required"
     */
    public String <xsl:value-of select='@method'/>() throws Exception {
        <xsl:value-of select='//@name'/>Helper helper = new <xsl:value-of select='//@name'/>Helper();
        helper.<xsl:value-of select='@method'/>();
        <xsl:for-each select="perform">helper.<xsl:value-of select='@method'/>();
        </xsl:for-each>
        return helper.<xsl:value-of select='@returnMethod'/>();
    }</xsl:for-each>
    
    public void ejbCreate() throws EJBException, RemoteException {
    }
    
    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }
    
    public void setSessionContext(SessionContext newContext) throws EJBException {
        context = newContext;
    }
}
</xsl:template>
<xsl:template match='clientRequest'>
    /**
     * <xsl:value-of select='@name'/>
     *
     * @ejb.interface-method
     * @ejb.transaction type="Required"
	 */
	public String <xsl:value-of select='@method'/>(String sSessionToken, ) throws Exception {
      helper.<xsl:value-of select='@method'/>();
      <xsl:for-each select="//clientRequest/*/perform">helper.<xsl:value-of select='@method'/>();</xsl:for-each>
      return helper.<xsl:value-of select='@returnMethod'/>();
   }
</xsl:template>
</xsl:stylesheet>
