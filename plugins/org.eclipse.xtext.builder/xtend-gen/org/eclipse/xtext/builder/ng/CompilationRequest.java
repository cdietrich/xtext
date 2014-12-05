/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@Accessors
@SuppressWarnings("all")
public class CompilationRequest {
  public static class Factory {
    @Inject
    private IResourceSetProvider resourceSetProvider;
    
    public CompilationRequest create(final IProject project) {
      CompilationRequest _compilationRequest = new CompilationRequest();
      final Procedure1<CompilationRequest> _function = new Procedure1<CompilationRequest>() {
        public void apply(final CompilationRequest request) {
          request.project = project;
          final Provider<ResourceSet> _function = new Provider<ResourceSet>() {
            public ResourceSet get() {
              return Factory.this.resourceSetProvider.get(project);
            }
          };
          request.resourceSetProvider = _function;
        }
      };
      return ObjectExtensions.<CompilationRequest>operator_doubleArrow(_compilationRequest, _function);
    }
  }
  
  protected CompilationRequest() {
  }
  
  private final Set<URI> toBeUpdated = CollectionLiterals.<URI>newHashSet();
  
  private final Set<URI> toBeDeleted = CollectionLiterals.<URI>newHashSet();
  
  private IProject project;
  
  private final List<IResourceDescription.Delta> upstreamFileChanges = CollectionLiterals.<IResourceDescription.Delta>newArrayList();
  
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
    _builder.append("computeAffected: ");
    _builder.append(this.computeAffected, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("delete: ");
    final Function1<URI, String> _function = new Function1<URI, String>() {
      public String apply(final URI it) {
        return it.lastSegment();
      }
    };
    Iterable<String> _map = IterableExtensions.<URI, String>map(this.toBeDeleted, _function);
    String _join = IterableExtensions.join(_map, ",");
    _builder.append(_join, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("update: ");
    final Function1<URI, String> _function_1 = new Function1<URI, String>() {
      public String apply(final URI it) {
        return it.lastSegment();
      }
    };
    Iterable<String> _map_1 = IterableExtensions.<URI, String>map(this.toBeUpdated, _function_1);
    String _join_1 = IterableExtensions.join(_map_1, ",");
    _builder.append(_join_1, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    int _size = this.upstreamFileChanges.size();
    _builder.append(_size, "  ");
    _builder.append(" upstreamFileChanges ");
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
  
  public boolean merge(final CompilationRequest other) {
    boolean _xblockexpression = false;
    {
      Iterables.<URI>addAll(this.toBeDeleted, other.toBeDeleted);
      Iterables.<URI>addAll(this.toBeUpdated, other.toBeUpdated);
      this.computeAffected = (this.computeAffected || other.computeAffected);
      _xblockexpression = Iterables.<IResourceDescription.Delta>addAll(this.upstreamFileChanges, other.upstreamFileChanges);
    }
    return _xblockexpression;
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
  public List<IResourceDescription.Delta> getUpstreamFileChanges() {
    return this.upstreamFileChanges;
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
