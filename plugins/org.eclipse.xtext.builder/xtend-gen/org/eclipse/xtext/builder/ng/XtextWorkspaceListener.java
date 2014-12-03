/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.util.ArrayList;
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
          final IResource resource = delta.getResource();
          boolean _matched = false;
          if (!_matched) {
            if (resource instanceof IStorage) {
              int _kind = delta.getKind();
              boolean _equals = (_kind == IResourceDelta.REMOVED);
              if (_equals) {
                _matched=true;
                IProject _project = resource.getProject();
                CompilationRequest _get = project2request.get(_project);
                Set<URI> _toBeDeleted = _get.getToBeDeleted();
                URI _uri = XtextWorkspaceListener.this.storage2UriMapper.getUri(((IStorage)resource));
                _toBeDeleted.add(_uri);
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
                IProject _project = resource.getProject();
                CompilationRequest _get = project2request.get(_project);
                Set<URI> _toBeUpdated = _get.getToBeUpdated();
                URI _uri = XtextWorkspaceListener.this.storage2UriMapper.getUri(((IStorage)resource));
                _toBeUpdated.add(_uri);
              }
            }
          }
          return true;
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
}
