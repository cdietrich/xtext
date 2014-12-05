/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.builder.ng.CompilationRequest;
import org.eclipse.xtext.builder.ng.ProjectDependencies;
import org.eclipse.xtext.builder.ng.XtextCompiler;
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@Singleton
@SuppressWarnings("all")
public class XtextCompilerJob extends Job {
  @Inject
  private XtextCompiler compiler;
  
  @Inject
  private IWorkspace workspace;
  
  @Inject
  private CompilationRequest.Factory requestFactory;
  
  private LinkedList<CompilationRequest> requests = CollectionLiterals.<CompilationRequest>newLinkedList();
  
  @Extension
  private ProjectDependencies projectDependencies;
  
  public XtextCompilerJob() {
    super("XtextCompilerJob");
  }
  
  public synchronized void enqueue(final Iterable<CompilationRequest> newRequests) {
    boolean _or = false;
    boolean _isEmpty = IterableExtensions.isEmpty(newRequests);
    if (_isEmpty) {
      _or = true;
    } else {
      final Function1<CompilationRequest, Boolean> _function = new Function1<CompilationRequest, Boolean>() {
        public Boolean apply(final CompilationRequest it) {
          return Boolean.valueOf(it.shouldCompile());
        }
      };
      boolean _exists = IterableExtensions.<CompilationRequest>exists(newRequests, _function);
      boolean _not = (!_exists);
      _or = _not;
    }
    if (_or) {
      return;
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Queueing:");
    _builder.newLine();
    _builder.append("\t");
    String _join = IterableExtensions.join(newRequests, "\n");
    _builder.append(_join, "\t");
    _builder.newLineIfNotEmpty();
    XtextCompilerConsole.log(_builder);
    final Function1<CompilationRequest, IProject> _function_1 = new Function1<CompilationRequest, IProject>() {
      public IProject apply(final CompilationRequest it) {
        return it.getProject();
      }
    };
    final Map<IProject, CompilationRequest> project2newRequest = IterableExtensions.<IProject, CompilationRequest>toMap(newRequests, _function_1);
    final LinkedHashMap<IProject, CompilationRequest> allProjects2request = CollectionLiterals.<IProject, CompilationRequest>newLinkedHashMap();
    IWorkspaceRoot _root = this.workspace.getRoot();
    IProject[] _projects = _root.getProjects();
    IWorkspace.ProjectOrder _computeProjectOrder = this.workspace.computeProjectOrder(_projects);
    for (final IProject project : _computeProjectOrder.projects) {
      {
        CompilationRequest _elvis = null;
        CompilationRequest _get = project2newRequest.get(project);
        if (_get != null) {
          _elvis = _get;
        } else {
          CompilationRequest _create = this.requestFactory.create(project);
          _elvis = _create;
        }
        final CompilationRequest request = _elvis;
        allProjects2request.put(project, request);
      }
    }
    IWorkspaceRoot _root_1 = this.workspace.getRoot();
    IProject[] _projects_1 = _root_1.getProjects();
    ProjectDependencies _projectDependencies = new ProjectDependencies((Iterable<IProject>)Conversions.doWrapArray(_projects_1));
    this.projectDependencies = _projectDependencies;
    final ArrayList<CompilationRequest> pendingRequests = this.drainRequests();
    final CompilationRequest currentRequest = IterableExtensions.<CompilationRequest>head(pendingRequests);
    boolean _notEquals = (!Objects.equal(currentRequest, null));
    if (_notEquals) {
      IProject _project = currentRequest.getProject();
      Iterable<IProject> _allUpstream = this.projectDependencies.getAllUpstream(_project);
      final Function1<IProject, Boolean> _function_2 = new Function1<IProject, Boolean>() {
        public Boolean apply(final IProject it) {
          CompilationRequest _get = project2newRequest.get(it);
          boolean _shouldCompile = false;
          if (_get!=null) {
            _shouldCompile=_get.shouldCompile();
          }
          return Boolean.valueOf(_shouldCompile);
        }
      };
      boolean _exists_1 = IterableExtensions.<IProject>exists(_allUpstream, _function_2);
      if (_exists_1) {
        this.cancel();
      }
    }
    boolean _isEmpty_1 = pendingRequests.isEmpty();
    boolean _not_1 = (!_isEmpty_1);
    if (_not_1) {
      for (final CompilationRequest pending : pendingRequests) {
        IProject _project_1 = pending.getProject();
        CompilationRequest _get = allProjects2request.get(_project_1);
        if (_get!=null) {
          _get.merge(pending);
        }
      }
    }
    /* this.requests; */
    synchronized (this.requests) {
      Collection<CompilationRequest> _values = allProjects2request.values();
      this.requests.addAll(_values);
    }
    int _state = this.getState();
    boolean _notEquals_1 = (_state != Job.RUNNING);
    if (_notEquals_1) {
      this.schedule();
    }
  }
  
  private ArrayList<CompilationRequest> drainRequests() {
    ArrayList<CompilationRequest> _xsynchronizedexpression = null;
    synchronized (this.requests) {
      ArrayList<CompilationRequest> _xblockexpression = null;
      {
        final ArrayList<CompilationRequest> result = new ArrayList<CompilationRequest>(this.requests);
        this.requests.clear();
        _xblockexpression = result;
      }
      _xsynchronizedexpression = _xblockexpression;
    }
    return _xsynchronizedexpression;
  }
  
  protected IStatus run(final IProgressMonitor monitor) {
    try {
      while (true) {
        {
          final CompilationRequest currentRequest = this.getNextCompilableRequest();
          boolean _equals = Objects.equal(currentRequest, null);
          if (_equals) {
            return Status.OK_STATUS;
          }
          currentRequest.setMonitor(monitor);
          final ImmutableList<IResourceDescription.Delta> deltas = this.compiler.compile(currentRequest);
          /* this.requests; */
          synchronized (this.requests) {
            boolean _isEmpty = this.requests.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              this.requests.removeFirst();
              boolean _isEmpty_1 = deltas.isEmpty();
              boolean _not_1 = (!_isEmpty_1);
              if (_not_1) {
                IProject _project = currentRequest.getProject();
                Iterable<IProject> _allDownstream = this.projectDependencies.getAllDownstream(_project);
                for (final IProject downstream : _allDownstream) {
                  final Function1<CompilationRequest, Boolean> _function = new Function1<CompilationRequest, Boolean>() {
                    public Boolean apply(final CompilationRequest it) {
                      IProject _project = it.getProject();
                      return Boolean.valueOf(Objects.equal(_project, downstream));
                    }
                  };
                  CompilationRequest _findFirst = IterableExtensions.<CompilationRequest>findFirst(this.requests, _function);
                  List<IResourceDescription.Delta> _upstreamFileChanges = null;
                  if (_findFirst!=null) {
                    _upstreamFileChanges=_findFirst.getUpstreamFileChanges();
                  }
                  if (_upstreamFileChanges!=null) {
                    _upstreamFileChanges.addAll(deltas);
                  }
                }
              }
            }
          }
        }
      }
    } catch (final Throwable _t) {
      if (_t instanceof OperationCanceledException) {
        final OperationCanceledException e = (OperationCanceledException)_t;
        return Status.CANCEL_STATUS;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  private CompilationRequest getNextCompilableRequest() {
    /* this.requests; */
    synchronized (this.requests) {
      {
        while ((!this.requests.isEmpty())) {
          {
            final CompilationRequest next = IterableExtensions.<CompilationRequest>head(this.requests);
            boolean _shouldCompile = next.shouldCompile();
            if (_shouldCompile) {
              return next;
            } else {
              this.requests.removeFirst();
            }
          }
        }
        return null;
      }
    }
  }
}
