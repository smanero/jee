<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match='process'> 
package <xsl:value-of select='/migration/@package'/>;

import org.apache.log4j.Logger;

/**
 * @integration: <xsl:value-of select='@functional'/>
 */
public class <xsl:value-of select='/migration/@className'/>Jpd {
	private static final Logger logger = Logger.getLogger(<xsl:value-of select='/migration/@className'/>Jpd.class.getName());
   
	
	// private <xsl:value-of select='/migration/@className'/>Helper helper;

	public <xsl:value-of select='/migration/@className'/>Jpd() {
	   // helper = new <xsl:value-of select='/migration/@className'/>Helper();
	}
	
    <xsl:for-each select="//clientRequest">
    
    /**
     * <xsl:value-of select='@name'/>
     */
    public String <xsl:value-of select='@method'/>() throws Exception {
        <xsl:value-of select='/migration/@className'/>Helper helper = new <xsl:value-of select='/migration/@className'/>Helper();
        helper.<xsl:value-of select='@method'/>();
        <xsl:for-each select="perform">helper.<xsl:value-of select='@method'/>();
        </xsl:for-each>
        return helper.<xsl:value-of select='@returnMethod'/>();
    }</xsl:for-each>
}
</xsl:template>
<!--<xsl:template match='clientRequest'>-->
<!---->
<!--	public String <xsl:value-of select='@name'/>(String sSessionToken, ) throws Q99qIntException {-->
<!--      helper.<xsl:value-of select='@method'/>();-->
<!--      <xsl:for-each select="//clientRequest/*/perform">helper.<xsl:value-of select='@method'/>();</xsl:for-each>-->
<!--      return helper.<xsl:value-of select='@returnMethod'/>();-->
<!--   }-->
<!--</xsl:template>-->
</xsl:stylesheet>
