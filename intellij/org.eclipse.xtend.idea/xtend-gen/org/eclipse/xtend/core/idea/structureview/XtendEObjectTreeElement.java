/**
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtend.core.idea.structureview;

import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.idea.structureview.EObjectTreeElement;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class XtendEObjectTreeElement extends EObjectTreeElement {
  private boolean isStatic;
  
  @Accessors
  private int inheritanceDepth;
  
  public void setStatic(final boolean isStatic) {
    this.isStatic = isStatic;
  }
  
  public boolean isStatic() {
    return this.isStatic;
  }
  
  @Pure
  public int getInheritanceDepth() {
    return this.inheritanceDepth;
  }
  
  public void setInheritanceDepth(final int inheritanceDepth) {
    this.inheritanceDepth = inheritanceDepth;
  }
}
