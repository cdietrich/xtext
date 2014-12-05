/** 
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng.debug

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.MenuItem
import org.eclipse.ui.console.ConsolePlugin
import org.eclipse.xtext.builder.ng.BuilderSwitch

/** 
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
class UseNewXtextCompilerHandler extends AbstractHandler {
	
	override Object execute(ExecutionEvent event) throws ExecutionException {
		val selected = event.isSelected
		BuilderSwitch.setUseNewCompiler(selected)
		if(selected) {
			// Our consoles hasn't been registered yet, so we have to make sure it's registered first
			// Is this really meant to be used that way?
			new XtextCompilerConsoleFactory().openConsole
			val consoleManager = ConsolePlugin.^default.consoleManager
			val xtextCompilerConsole = consoleManager.consoles.filter(XtextCompilerConsole).head
			consoleManager.showConsoleView(xtextCompilerConsole)
		}
		null
	}

	def private static isSelected(ExecutionEvent event) {
		(((event.trigger) as Event).widget as MenuItem).selection
	}
}
