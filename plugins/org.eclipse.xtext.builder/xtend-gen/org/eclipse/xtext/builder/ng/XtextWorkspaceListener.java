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
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
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
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.builder.ng.BuilderSwitch;
import org.eclipse.xtext.builder.ng.CompilationRequest;
import org.eclipse.xtext.builder.ng.CompilerJob;
import org.eclipse.xtext.builder.ng.debug.ResourceChangeEventToString;
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.ui.resource.IStorage2UriMapper;
import org.eclipse.xtext.ui.resource.UriValidator;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@Singleton
@SuppressWarnings("all")
public class XtextWorkspaceListener implements IResourceChangeListener {
  @Inject
  private IStorage2UriMapper storage2UriMapper;
  
  @Inject
  private CompilerJob compilerJob;
  
  @Inject
  private IResourceSetProvider resourceSetProvider;
  
  @Inject
  private UriValidator uriValidator;
  
  private IWorkspace workspace;
  
  @Inject
  public void register(final IWorkspace workspace) {
    this.workspace = workspace;
    workspace.addResourceChangeListener(this);
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
      final ArrayList<CompilationRequest> requests = CollectionLiterals.<CompilationRequest>newArrayList();
      IWorkspaceRoot _root = this.workspace.getRoot();
      IProject[] _projects = _root.getProjects();
      IWorkspace.ProjectOrder _computeProjectOrder = this.workspace.computeProjectOrder(_projects);
      for (final IProject project : _computeProjectOrder.projects) {
        CompilationRequest _compilationRequest = new CompilationRequest();
        final Procedure1<CompilationRequest> _function = new Procedure1<CompilationRequest>() {
          public void apply(final CompilationRequest request) {
            request.setProject(project);
            final Provider<ResourceSet> _function = new Provider<ResourceSet>() {
              public ResourceSet get() {
                return XtextWorkspaceListener.this.resourceSetProvider.get(project);
              }
            };
            request.setResourceSetProvider(_function);
          }
        };
        CompilationRequest _doubleArrow = ObjectExtensions.<CompilationRequest>operator_doubleArrow(_compilationRequest, _function);
        requests.add(_doubleArrow);
      }
      final Function1<CompilationRequest, IProject> _function_1 = new Function1<CompilationRequest, IProject>() {
        public IProject apply(final CompilationRequest it) {
          return it.getProject();
        }
      };
      final Map<IProject, CompilationRequest> project2request = IterableExtensions.<IProject, CompilationRequest>toMap(requests, _function_1);
      IResourceDelta _delta = event.getDelta();
      final IResourceDeltaVisitor _function_2 = new IResourceDeltaVisitor() {
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
      _delta.accept(_function_2);
      this.compilerJob.setCompilationRequests(requests);
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
      CompilationRequest _get = project2request.get(_project);
      _get.setForceBuild(true);
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
            CompilationRequest _get_1 = project2request.get(_project_1);
            Set<URI> _apply = uriList.apply(_get_1);
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
  
  protected boolean isBinaryJavaResource(final IResource resource) {
    String _fileExtension = resource.getFileExtension();
    return Collections.<String>unmodifiableSet(CollectionLiterals.<String>newHashSet("class", "jar")).contains(_fileExtension);
  }
}
