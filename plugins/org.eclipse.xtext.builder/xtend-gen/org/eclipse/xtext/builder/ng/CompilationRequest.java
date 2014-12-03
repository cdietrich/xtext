/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.inject.Provider;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@Accessors
@SuppressWarnings("all")
public class CompilationRequest {
  private final Set<URI> toBeUpdated = CollectionLiterals.<URI>newHashSet();
  
  private final Set<URI> toBeDeleted = CollectionLiterals.<URI>newHashSet();
  
  private String projectName;
  
  private final List<IResourceDescription.Delta> upstreamFileChanges = CollectionLiterals.<IResourceDescription.Delta>newArrayList();
  
  private final List<IResourceDescription.Delta> upstreamStructuralFileChanges = CollectionLiterals.<IResourceDescription.Delta>newArrayList();
  
  private boolean computeAffected;
  
  private Provider<ResourceSet> resourceSetProvider;
  
  private CancelIndicator monitor;
  
  public void addUpstreamChange(final IResourceDescription.Delta change) {
    boolean _haveEObjectDescriptionsChanged = change.haveEObjectDescriptionsChanged();
    if (_haveEObjectDescriptionsChanged) {
      this.upstreamStructuralFileChanges.add(change);
    }
    this.upstreamFileChanges.add(change);
  }
  
  public void addUpstreamChanges(final Iterable<IResourceDescription.Delta> changes) {
    final Procedure1<IResourceDescription.Delta> _function = new Procedure1<IResourceDescription.Delta>() {
      public void apply(final IResourceDescription.Delta it) {
        CompilationRequest.this.addUpstreamChange(it);
      }
    };
    IterableExtensions.<IResourceDescription.Delta>forEach(changes, _function);
  }
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("CompilationRequest: ");
    _builder.append(this.projectName, "");
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
    _builder.append("  ");
    int _size = this.upstreamFileChanges.size();
    _builder.append(_size, "  ");
    _builder.append(" upstreamFileChanges ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    int _size_1 = this.upstreamStructuralFileChanges.size();
    _builder.append(_size_1, "  ");
    _builder.append(" upstreamStructuralFileChanges ");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public boolean shouldCompile() {
    boolean _or = false;
    boolean _or_1 = false;
    boolean _or_2 = false;
    if (this.computeAffected) {
      _or_2 = true;
    } else {
      boolean _isEmpty = this.toBeDeleted.isEmpty();
      boolean _not = (!_isEmpty);
      _or_2 = _not;
    }
    if (_or_2) {
      _or_1 = true;
    } else {
      boolean _isEmpty_1 = this.toBeUpdated.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      _or_1 = _not_1;
    }
    if (_or_1) {
      _or = true;
    } else {
      boolean _isEmpty_2 = this.upstreamFileChanges.isEmpty();
      boolean _not_2 = (!_isEmpty_2);
      _or = _not_2;
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
  public String getProjectName() {
    return this.projectName;
  }
  
  public void setProjectName(final String projectName) {
    this.projectName = projectName;
  }
  
  @Pure
  public List<IResourceDescription.Delta> getUpstreamFileChanges() {
    return this.upstreamFileChanges;
  }
  
  @Pure
  public List<IResourceDescription.Delta> getUpstreamStructuralFileChanges() {
    return this.upstreamStructuralFileChanges;
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
  public CancelIndicator getMonitor() {
    return this.monitor;
  }
  
  public void setMonitor(final CancelIndicator monitor) {
    this.monitor = monitor;
  }
}
