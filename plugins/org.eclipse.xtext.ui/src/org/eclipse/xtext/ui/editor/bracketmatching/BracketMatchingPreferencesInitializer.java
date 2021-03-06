/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.bracketmatching;

import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreInitializer;

/**
 * @author Sven Efftinge - Initial contribution and API
 */
public class BracketMatchingPreferencesInitializer implements IPreferenceStoreInitializer {
	
	public final static String IS_ACTIVE_KEY = "CharacterMatching";
	public final static String COLOR_KEY = "CharacterMatching.Color";

	@Override
	public void initialize(IPreferenceStoreAccess access) {
		access.getWritablePreferenceStore().setDefault(IS_ACTIVE_KEY, true);
		access.getWritablePreferenceStore().setDefault(COLOR_KEY, getDefautColor());
	}
	
	public String getDefautColor() {
		return "192,192,192"; // grey
	}

}
