/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng.debug;

import com.google.common.collect.Iterables;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.xtext.builder.ng.BuilderSwitch;
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole;
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsoleFactory;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@SuppressWarnings("all")
public class UseNewXtextCompilerHandler extends AbstractHandler {
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final boolean selected = UseNewXtextCompilerHandler.isSelected(event);
      BuilderSwitch.setUseNewCompiler(selected);
      if (selected) {
        XtextCompilerConsoleFactory _xtextCompilerConsoleFactory = new XtextCompilerConsoleFactory();
        _xtextCompilerConsoleFactory.openConsole();
        ConsolePlugin _default = ConsolePlugin.getDefault();
        final IConsoleManager consoleManager = _default.getConsoleManager();
        IConsole[] _consoles = consoleManager.getConsoles();
        Iterable<XtextCompilerConsole> _filter = Iterables.<XtextCompilerConsole>filter(((Iterable<?>)Conversions.doWrapArray(_consoles)), XtextCompilerConsole.class);
        final XtextCompilerConsole xtextCompilerConsole = IterableExtensions.<XtextCompilerConsole>head(_filter);
        consoleManager.showConsoleView(xtextCompilerConsole);
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
  
  private static boolean isSelected(final ExecutionEvent event) {
    Object _trigger = event.getTrigger();
    return ((MenuItem) ((Event) _trigger).widget).getSelection();
  }
}
