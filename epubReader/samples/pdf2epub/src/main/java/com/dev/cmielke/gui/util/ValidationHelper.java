package com.dev.cmielke.gui.util;

import javax.swing.JTextField;

import com.dev.cmielke.gui.dialogs.components.DialogBox;

public final class ValidationHelper
{
	private ValidationHelper() {} // no instance allowed
	
	public static boolean validateNumber(DialogBox dialog, JTextField textfield)
	{
		boolean isValid = true;
		try
		{
			Integer.valueOf(textfield.getText());
			dialog.removeError(textfield);
			textfield.transferFocus();
		}
		catch(NumberFormatException nfe)
		{
			textfield.selectAll();
			isValid = false;
			dialog.addError(textfield, "Eingabe ["+ textfield.getName() +"] ist keine gültige Zahl, bitte Eingabe korrigieren!");
		}
		return isValid;
	}
	
	public static boolean validateString(DialogBox dialog, JTextField textfield)
	{
		boolean isValid = true;
		if(textfield.getText().matches("[a-zA-Z_0-9|ü|Ü|ö|Ö|ä|Ä|ß|,|\\.| |-]*"))
		{
			dialog.removeError(textfield);
			textfield.transferFocus();
		}
		else
		{
			textfield.selectAll();
			isValid = false;
			dialog.addError(textfield, "Eingabe ["+ textfield.getName() +"] enthält ungültige Zeichen, bitte Eingabe korrigieren!");
		}
		return isValid;
	}
	
	public static boolean validateMandatory(DialogBox dialog, JTextField textfield) {
		boolean isValid = !textfield.getText().isEmpty();
		if(!isValid) {
			dialog.addError(textfield, "Eingabe ["+ textfield.getName() + "] darf nicht leer bleiben!");
		}
		return isValid;
	}
}
