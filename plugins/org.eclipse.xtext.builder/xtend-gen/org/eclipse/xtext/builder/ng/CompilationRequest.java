/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.inject.Provider;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@Accessors
@SuppressWarnings("all")
public class CompilationRequest {
  private final Set<URI> toBeUpdated = CollectionLiterals.<URI>newHashSet();
  
  private final Set<URI> toBeDeleted = CollectionLiterals.<URI>newHashSet();
  
  private IProject project;
  
  private boolean computeAffected;
  
  private Provider<ResourceSet> resourceSetProvider;
  
  private IProgressMonitor monitor;
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("CompilationRequest: ");
    String _name = this.project.getName();
    _builder.append(_name, "");
    _builder.append(":");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("delete: ");
    {
      boolean _hasElements = false;
      for(final URI uri : this.toBeDeleted) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "  ");
        }
        String _lastSegment = null;
        if (uri!=null) {
          _lastSegment=uri.lastSegment();
        }
        _builder.append(_lastSegment, "  ");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("update: ");
    {
      boolean _hasElements_1 = false;
      for(final URI uri_1 : this.toBeUpdated) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
        } else {
          _builder.appendImmediate(",", "  ");
        }
        String _lastSegment_1 = null;
        if (uri_1!=null) {
          _lastSegment_1=uri_1.lastSegment();
        }
        _builder.append(_lastSegment_1, "  ");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public boolean shouldCompile() {
    boolean _or = false;
    boolean _or_1 = false;
    if (this.computeAffected) {
      _or_1 = true;
    } else {
      boolean _isEmpty = this.toBeDeleted.isEmpty();
      boolean _not = (!_isEmpty);
      _or_1 = _not;
    }
    if (_or_1) {
      _or = true;
    } else {
      boolean _isEmpty_1 = this.toBeUpdated.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      _or = _not_1;
    }
    return _or;
  }
  
  @Pure
  public Set<URI> getToBeUpdated() {
    return this.toBeUpdated;
  }
  
  @Pure
  public Set<URI> getToBeDeleted() {
    return this.toBeDeleted;
  }
  
  @Pure
  public IProject getProject() {
    return this.project;
  }
  
  public void setProject(final IProject project) {
    this.project = project;
  }
  
  @Pure
  public boolean isComputeAffected() {
    return this.computeAffected;
  }
  
  public void setComputeAffected(final boolean computeAffected) {
    this.computeAffected = computeAffected;
  }
  
  @Pure
  public Provider<ResourceSet> getResourceSetProvider() {
    return this.resourceSetProvider;
  }
  
  public void setResourceSetProvider(final Provider<ResourceSet> resourceSetProvider) {
    this.resourceSetProvider = resourceSetProvider;
  }
  
  @Pure
  public IProgressMonitor getMonitor() {
    return this.monitor;
  }
  
  public void setMonitor(final IProgressMonitor monitor) {
    this.monitor = monitor;
  }
}
