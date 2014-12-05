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
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.builder.IXtextBuilderParticipant;
import org.eclipse.xtext.builder.builderState.IBuilderState;
import org.eclipse.xtext.builder.impl.BuildData;
import org.eclipse.xtext.builder.impl.QueuedBuildData;
import org.eclipse.xtext.builder.impl.RegistryBuilderParticipant;
import org.eclipse.xtext.builder.impl.ToBeBuilt;
import org.eclipse.xtext.builder.ng.CompilationRequest;
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@SuppressWarnings("all")
public class XtextCompiler {
  @Data
  public static class BuildContext implements IXtextBuilderParticipant.IBuildContext {
    private final IProject builtProject;
    
    private final ResourceSet resourceSet;
    
    private final ImmutableList<IResourceDescription.Delta> deltas;
    
    private final IXtextBuilderParticipant.BuildType buildType;
    
    public void needRebuild() {
    }
    
    public BuildContext(final IProject builtProject, final ResourceSet resourceSet, final ImmutableList<IResourceDescription.Delta> deltas, final IXtextBuilderParticipant.BuildType buildType) {
      super();
      this.builtProject = builtProject;
      this.resourceSet = resourceSet;
      this.deltas = deltas;
      this.buildType = buildType;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.builtProject== null) ? 0 : this.builtProject.hashCode());
      result = prime * result + ((this.resourceSet== null) ? 0 : this.resourceSet.hashCode());
      result = prime * result + ((this.deltas== null) ? 0 : this.deltas.hashCode());
      result = prime * result + ((this.buildType== null) ? 0 : this.buildType.hashCode());
      return result;
    }
    
    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      XtextCompiler.BuildContext other = (XtextCompiler.BuildContext) obj;
      if (this.builtProject == null) {
        if (other.builtProject != null)
          return false;
      } else if (!this.builtProject.equals(other.builtProject))
        return false;
      if (this.resourceSet == null) {
        if (other.resourceSet != null)
          return false;
      } else if (!this.resourceSet.equals(other.resourceSet))
        return false;
      if (this.deltas == null) {
        if (other.deltas != null)
          return false;
      } else if (!this.deltas.equals(other.deltas))
        return false;
      if (this.buildType == null) {
        if (other.buildType != null)
          return false;
      } else if (!this.buildType.equals(other.buildType))
        return false;
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("builtProject", this.builtProject);
      b.add("resourceSet", this.resourceSet);
      b.add("deltas", this.deltas);
      b.add("buildType", this.buildType);
      return b.toString();
    }
    
    @Pure
    public IProject getBuiltProject() {
      return this.builtProject;
    }
    
    @Pure
    public ResourceSet getResourceSet() {
      return this.resourceSet;
    }
    
    @Pure
    public ImmutableList<IResourceDescription.Delta> getDeltas() {
      return this.deltas;
    }
    
    @Pure
    public IXtextBuilderParticipant.BuildType getBuildType() {
      return this.buildType;
    }
  }
  
  @Inject
  private IBuilderState builderState;
  
  @Inject
  private QueuedBuildData queuedBuildData;
  
  @Inject
  private RegistryBuilderParticipant participant;
  
  @Inject
  private IWorkspace workspace;
  
  public ImmutableList<IResourceDescription.Delta> compile(final CompilationRequest request) {
    try {
      ImmutableList<IResourceDescription.Delta> _xblockexpression = null;
      {
        XtextCompilerConsole.log(("Compiling " + request));
        final IXtextBuilderParticipant.BuildType buildType = IXtextBuilderParticipant.BuildType.INCREMENTAL;
        final boolean indexingOnly = Objects.equal(buildType, IXtextBuilderParticipant.BuildType.RECOVERY);
        Provider<ResourceSet> _resourceSetProvider = request.getResourceSetProvider();
        final ResourceSet resourceSet = _resourceSetProvider.get();
        Map<Object, Object> _loadOptions = resourceSet.getLoadOptions();
        _loadOptions.put(ResourceDescriptionsProvider.NAMED_BUILDER_SCOPE, Boolean.valueOf(true));
        final ToBeBuilt toBeBuilt = new ToBeBuilt();
        Set<URI> _toBeDeleted = toBeBuilt.getToBeDeleted();
        Set<URI> _toBeDeleted_1 = request.getToBeDeleted();
        Iterables.<URI>addAll(_toBeDeleted, _toBeDeleted_1);
        Set<URI> _toBeUpdated = toBeBuilt.getToBeUpdated();
        Set<URI> _toBeUpdated_1 = request.getToBeUpdated();
        Iterables.<URI>addAll(_toBeUpdated, _toBeUpdated_1);
        IProject _project = request.getProject();
        String _name = _project.getName();
        final BuildData buildData = new BuildData(_name, resourceSet, toBeBuilt, this.queuedBuildData, indexingOnly);
        final NullProgressMonitor progress = new NullProgressMonitor();
        final ImmutableList<IResourceDescription.Delta> deltas = this.builderState.update(buildData, progress);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Compiled ");
        final Function1<IResourceDescription.Delta, String> _function = new Function1<IResourceDescription.Delta, String>() {
          public String apply(final IResourceDescription.Delta it) {
            URI _uri = it.getUri();
            return _uri.lastSegment();
          }
        };
        List<String> _map = ListExtensions.<IResourceDescription.Delta, String>map(deltas, _function);
        String _join = IterableExtensions.join(_map, ", ");
        _builder.append(_join, "");
        _builder.newLineIfNotEmpty();
        XtextCompilerConsole.log(_builder);
        boolean _and = false;
        boolean _notEquals = (!Objects.equal(this.participant, null));
        if (!_notEquals) {
          _and = false;
        } else {
          _and = (!indexingOnly);
        }
        if (_and) {
          final IWorkspaceRunnable _function_1 = new IWorkspaceRunnable() {
            public void run(final IProgressMonitor it) throws CoreException {
              IProject _project = request.getProject();
              XtextCompiler.BuildContext _buildContext = new XtextCompiler.BuildContext(_project, resourceSet, deltas, buildType);
              XtextCompiler.this.participant.build(_buildContext, progress);
            }
          };
          this.workspace.run(_function_1, progress);
        } else {
          progress.worked(1);
        }
        _xblockexpression = deltas;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
