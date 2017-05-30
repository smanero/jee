package com.dev.cmielke.gui.dialogs.components;

import java.awt.event.*; 

import javax.swing.JPopupMenu; 
 
public class PopupMenuMouseListener extends MouseAdapter 
{ 
  private final JPopupMenu popmen; 
 
  public PopupMenuMouseListener( JPopupMenu popmen ) 
  { 
    this.popmen = popmen; 
  } 
 
  @Override 
  public void mouseReleased( MouseEvent me ) { 
    if ( me.isPopupTrigger() ) 
      popmen.show( me.getComponent(), me.getX(), me.getY() ); 
  } 
}
