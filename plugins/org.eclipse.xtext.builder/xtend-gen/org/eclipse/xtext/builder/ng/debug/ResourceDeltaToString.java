/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng.debug;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("all")
public class ResourceDeltaToString implements Function1<IResourceDelta, String> {
  private String getKindString(final IResourceDelta delta) {
    final int kind = delta.getKind();
    final int flags = delta.getFlags();
    final ArrayList<String> result = CollectionLiterals.<String>newArrayList();
    boolean _power = this.operator_power(kind, IResourceDelta.ADDED);
    if (_power) {
      result.add("ADDED");
    }
    boolean _power_1 = this.operator_power(kind, IResourceDelta.REMOVED);
    if (_power_1) {
      result.add("REMOVED");
    }
    boolean _power_2 = this.operator_power(kind, IResourceDelta.CHANGED);
    if (_power_2) {
      result.add("CHANGED");
    }
    boolean _power_3 = this.operator_power(kind, IResourceDelta.ADDED_PHANTOM);
    if (_power_3) {
      result.add("ADDED_PHANTOM");
    }
    boolean _power_4 = this.operator_power(kind, IResourceDelta.REMOVED_PHANTOM);
    if (_power_4) {
      result.add("REMOVED_PHANTOM");
    }
    boolean _power_5 = this.operator_power(flags, IResourceDelta.CONTENT);
    if (_power_5) {
      result.add("CONTENT");
    }
    boolean _power_6 = this.operator_power(flags, IResourceDelta.DERIVED_CHANGED);
    if (_power_6) {
      result.add("DERIVED_CHANGED");
    }
    boolean _power_7 = this.operator_power(flags, IResourceDelta.DESCRIPTION);
    if (_power_7) {
      result.add("DESCRIPTION");
    }
    boolean _power_8 = this.operator_power(flags, IResourceDelta.ENCODING);
    if (_power_8) {
      result.add("ENCODING");
    }
    boolean _power_9 = this.operator_power(flags, IResourceDelta.LOCAL_CHANGED);
    if (_power_9) {
      result.add("LOCAL_CHANGED");
    }
    boolean _power_10 = this.operator_power(flags, IResourceDelta.OPEN);
    if (_power_10) {
      result.add("OPEN");
    }
    boolean _power_11 = this.operator_power(flags, IResourceDelta.MOVED_TO);
    if (_power_11) {
      result.add("MOVED_TO");
    }
    boolean _power_12 = this.operator_power(flags, IResourceDelta.MOVED_FROM);
    if (_power_12) {
      result.add("MOVED_FROM");
    }
    boolean _power_13 = this.operator_power(flags, IResourceDelta.COPIED_FROM);
    if (_power_13) {
      result.add("COPIED_FROM");
    }
    boolean _power_14 = this.operator_power(flags, IResourceDelta.TYPE);
    if (_power_14) {
      result.add("TYPE");
    }
    boolean _power_15 = this.operator_power(flags, IResourceDelta.SYNC);
    if (_power_15) {
      result.add("SYNC");
    }
    boolean _power_16 = this.operator_power(flags, IResourceDelta.MARKERS);
    if (_power_16) {
      result.add("MARKERS");
    }
    boolean _power_17 = this.operator_power(flags, IResourceDelta.REPLACED);
    if (_power_17) {
      result.add("REPLACED");
    }
    return IterableExtensions.join(result, "|");
  }
  
  public boolean operator_power(final int bits, final int bit) {
    return ((bits & bit) != 0);
  }
  
  public String apply(final IResourceDelta delta) {
    Class<? extends IResourceDelta> _class = delta.getClass();
    String _simpleName = _class.getSimpleName();
    String _plus = (_simpleName + " ");
    Path _path = new Path("/");
    String _apply = this.apply(_path, delta);
    return (_plus + _apply);
  }
  
  private String apply(final IPath parent, final IResourceDelta delta) {
    final IResourceDelta[] children = delta.getAffectedChildren();
    int _size = ((List<IResourceDelta>)Conversions.doWrapArray(children)).size();
    boolean _equals = (_size == 1);
    if (_equals) {
      IResourceDelta _head = IterableExtensions.<IResourceDelta>head(((Iterable<IResourceDelta>)Conversions.doWrapArray(children)));
      return this.apply(parent, _head);
    }
    final String kind = this.getKindString(delta);
    IResource _resource = delta.getResource();
    Class<? extends IResource> _class = null;
    if (_resource!=null) {
      _class=_resource.getClass();
    }
    final String type = _class.getSimpleName();
    IPath _fullPath = delta.getFullPath();
    IPath _makeRelativeTo = _fullPath.makeRelativeTo(parent);
    final String name = _makeRelativeTo.toString();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(kind, "");
    _builder.append(" ");
    _builder.append(type, "");
    _builder.append(" ");
    _builder.append(name, "");
    final String body = _builder.toString();
    boolean _isEmpty = ((List<IResourceDelta>)Conversions.doWrapArray(children)).isEmpty();
    if (_isEmpty) {
      return body;
    }
    final Function1<IResourceDelta, String> _function = new Function1<IResourceDelta, String>() {
      public String apply(final IResourceDelta it) {
        IPath _fullPath = delta.getFullPath();
        return ResourceDeltaToString.this.apply(_fullPath, it);
      }
    };
    List<String> _map = ListExtensions.<IResourceDelta, String>map(((List<IResourceDelta>)Conversions.doWrapArray(children)), _function);
    String _join = IterableExtensions.join(_map, "\n");
    String _replace = _join.replace("\n", "\n  ");
    String _plus = ((body + " {\n  ") + _replace);
    return (_plus + "\n}");
  }
}
