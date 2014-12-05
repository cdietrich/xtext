/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.builder.ng.BuilderSwitch;
import org.eclipse.xtext.builder.ng.CompilationRequest;
import org.eclipse.xtext.builder.ng.XtextCompilerJob;
import org.eclipse.xtext.builder.ng.debug.ResourceChangeEventToString;
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole;
import org.eclipse.xtext.ui.resource.IStorage2UriMapper;
import org.eclipse.xtext.ui.resource.UriValidator;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@Singleton
@SuppressWarnings("all")
public class XtextWorkspaceListener implements IResourceChangeListener {
  @Inject
  private IStorage2UriMapper storage2UriMapper;
  
  @Inject
  private XtextCompilerJob compilerJob;
  
  @Inject
  private CompilationRequest.Factory requestFactory;
  
  @Inject
  private UriValidator uriValidator;
  
  private IWorkspace workspace;
  
  @Inject
  public void register(final IWorkspace workspace) {
    this.workspace = workspace;
    workspace.addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
  }
  
  public void deregister() {
    this.workspace.removeResourceChangeListener(this);
  }
  
  public void resourceChanged(final IResourceChangeEvent event) {
    boolean _isUseNewCompiler = BuilderSwitch.isUseNewCompiler();
    boolean _not = (!_isUseNewCompiler);
    if (_not) {
      return;
    }
    try {
      ResourceChangeEventToString _resourceChangeEventToString = new ResourceChangeEventToString();
      String _apply = _resourceChangeEventToString.apply(event);
      XtextCompilerConsole.log(_apply);
      final HashMap<IProject, CompilationRequest> project2request = CollectionLiterals.<IProject, CompilationRequest>newHashMap();
      IResourceDelta _delta = event.getDelta();
      final IResourceDeltaVisitor _function = new IResourceDeltaVisitor() {
        public boolean visit(final IResourceDelta delta) throws CoreException {
          int _flags = delta.getFlags();
          boolean _notEquals = (_flags != IResourceDelta.MARKERS);
          if (_notEquals) {
            final IResource resource = delta.getResource();
            boolean _matched = false;
            if (!_matched) {
              if (resource instanceof IStorage) {
                int _kind = delta.getKind();
                boolean _equals = (_kind == IResourceDelta.REMOVED);
                if (_equals) {
                  _matched=true;
                  final Function1<CompilationRequest, Set<URI>> _function = new Function1<CompilationRequest, Set<URI>>() {
                    public Set<URI> apply(final CompilationRequest it) {
                      return it.getToBeDeleted();
                    }
                  };
                  XtextWorkspaceListener.this.addToCompilationRequest(resource, project2request, _function);
                }
              }
            }
            if (!_matched) {
              if (resource instanceof IStorage) {
                boolean _or = false;
                int _kind = delta.getKind();
                boolean _equals = (_kind == IResourceDelta.ADDED);
                if (_equals) {
                  _or = true;
                } else {
                  int _kind_1 = delta.getKind();
                  boolean _equals_1 = (_kind_1 == IResourceDelta.CHANGED);
                  _or = _equals_1;
                }
                if (_or) {
                  _matched=true;
                  final Function1<CompilationRequest, Set<URI>> _function = new Function1<CompilationRequest, Set<URI>>() {
                    public Set<URI> apply(final CompilationRequest it) {
                      return it.getToBeUpdated();
                    }
                  };
                  XtextWorkspaceListener.this.addToCompilationRequest(resource, project2request, _function);
                }
              }
            }
            return true;
          }
          return false;
        }
      };
      _delta.accept(_function);
      Collection<CompilationRequest> _values = project2request.values();
      this.compilerJob.enqueue(_values);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception exc = (Exception)_t;
        XtextCompilerConsole.log(exc);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  protected Boolean addToCompilationRequest(final IResource resource, final Map<IProject, CompilationRequest> project2request, final Function1<? super CompilationRequest, ? extends Set<URI>> uriList) {
    boolean _xifexpression = false;
    boolean _isBinaryJavaResource = this.isBinaryJavaResource(resource);
    if (_isBinaryJavaResource) {
      IProject _project = resource.getProject();
      CompilationRequest _createOrGet = this.createOrGet(project2request, _project);
      _createOrGet.setComputeAffected(true);
    } else {
      boolean _xifexpression_1 = false;
      if ((resource instanceof IStorage)) {
        boolean _xblockexpression = false;
        {
          final URI uri = this.storage2UriMapper.getUri(((IStorage)resource));
          boolean _xifexpression_2 = false;
          boolean _and = false;
          boolean _notEquals = (!Objects.equal(uri, null));
          if (!_notEquals) {
            _and = false;
          } else {
            boolean _canBuild = this.uriValidator.canBuild(uri, ((IStorage)resource));
            _and = _canBuild;
          }
          if (_and) {
            IProject _project_1 = resource.getProject();
            CompilationRequest _createOrGet_1 = this.createOrGet(project2request, _project_1);
            Set<URI> _apply = uriList.apply(_createOrGet_1);
            _xifexpression_2 = _apply.add(uri);
          }
          _xblockexpression = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression;
      }
      _xifexpression = _xifexpression_1;
    }
    return Boolean.valueOf(_xifexpression);
  }
  
  protected CompilationRequest createOrGet(final Map<IProject, CompilationRequest> project2request, final IProject project) {
    CompilationRequest _elvis = null;
    CompilationRequest _get = project2request.get(project);
    if (_get != null) {
      _elvis = _get;
    } else {
      CompilationRequest _xblockexpression = null;
      {
        final CompilationRequest request = this.requestFactory.create(project);
        project2request.put(project, request);
        _xblockexpression = request;
      }
      _elvis = _xblockexpression;
    }
    return _elvis;
  }
  
  protected boolean isBinaryJavaResource(final IResource resource) {
    String _fileExtension = resource.getFileExtension();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_fileExtension, "jar")) {
        _matched=true;
      }
      if (!_matched) {
        if (Objects.equal(_fileExtension, "class")) {
          _matched=true;
        }
      }
      if (_matched) {
        return true;
      }
    }
    return false;
  }
}
