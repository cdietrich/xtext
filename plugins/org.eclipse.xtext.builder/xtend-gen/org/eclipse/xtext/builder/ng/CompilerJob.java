/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.builder.ng.CompilationRequest;
import org.eclipse.xtext.builder.ng.ProjectUtility;
import org.eclipse.xtext.builder.ng.XtextCompiler;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@SuppressWarnings("all")
public class CompilerJob extends Job {
  @Inject
  private XtextCompiler compiler;
  
  private LinkedList<CompilationRequest> requests = CollectionLiterals.<CompilationRequest>newLinkedList();
  
  public CompilerJob() {
    super("XtextCompilerJob");
  }
  
  public synchronized void setCompilationRequests(final List<CompilationRequest> newRequests) {
    try {
      boolean _or = false;
      boolean _isEmpty = newRequests.isEmpty();
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
      final ArrayList<CompilationRequest> pendingRequests = this.drainRequests();
      final CompilationRequest currentRequest = IterableExtensions.<CompilationRequest>head(pendingRequests);
      boolean _notEquals = (!Objects.equal(currentRequest, null));
      if (_notEquals) {
        IProject _project = currentRequest.getProject();
        CompilationRequest _head = IterableExtensions.<CompilationRequest>head(newRequests);
        IProject _project_1 = _head.getProject();
        boolean _dependsOn = ProjectUtility.dependsOn(_project, _project_1);
        if (_dependsOn) {
          this.cancel();
        }
      }
      this.join();
      boolean _isEmpty_1 = pendingRequests.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        final Function1<CompilationRequest, IProject> _function_1 = new Function1<CompilationRequest, IProject>() {
          public IProject apply(final CompilationRequest it) {
            return it.getProject();
          }
        };
        final Map<IProject, CompilationRequest> project2request = IterableExtensions.<IProject, CompilationRequest>toMap(newRequests, _function_1);
        for (final CompilationRequest pending : pendingRequests) {
          {
            IProject _project_2 = pending.getProject();
            final CompilationRequest newRequest = project2request.get(_project_2);
            Set<URI> _toBeDeleted = newRequest.getToBeDeleted();
            Set<URI> _toBeDeleted_1 = pending.getToBeDeleted();
            Iterables.<URI>addAll(_toBeDeleted, _toBeDeleted_1);
            Set<URI> _toBeUpdated = newRequest.getToBeUpdated();
            Set<URI> _toBeUpdated_1 = pending.getToBeUpdated();
            Iterables.<URI>addAll(_toBeUpdated, _toBeUpdated_1);
          }
        }
      }
      /* this.requests; */
      synchronized (this.requests) {
        this.requests.addAll(newRequests);
      }
      this.schedule();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
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
          this.compiler.compile(currentRequest);
          /* this.requests; */
          synchronized (this.requests) {
            boolean _isEmpty = this.requests.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              this.requests.removeFirst();
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
