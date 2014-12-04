/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@SuppressWarnings("all")
public class ProjectDependencies {
  @Accessors
  @FinalFieldsConstructor
  protected static class ProjectNode {
    private final IProject project;
    
    private final List<ProjectDependencies.ProjectNode> upstream = CollectionLiterals.<ProjectDependencies.ProjectNode>newArrayList();
    
    private final List<ProjectDependencies.ProjectNode> downstream = CollectionLiterals.<ProjectDependencies.ProjectNode>newArrayList();
    
    public String toString() {
      StringConcatenation _builder = new StringConcatenation();
      String _name = this.project.getName();
      _builder.append(_name, "");
      _builder.newLineIfNotEmpty();
      _builder.append("upstream: ");
      final Function1<ProjectDependencies.ProjectNode, String> _function = new Function1<ProjectDependencies.ProjectNode, String>() {
        public String apply(final ProjectDependencies.ProjectNode it) {
          return it.project.getName();
        }
      };
      List<String> _map = ListExtensions.<ProjectDependencies.ProjectNode, String>map(this.upstream, _function);
      String _join = IterableExtensions.join(_map, ", ");
      _builder.append(_join, "");
      _builder.newLineIfNotEmpty();
      _builder.append("downstream: ");
      final Function1<ProjectDependencies.ProjectNode, String> _function_1 = new Function1<ProjectDependencies.ProjectNode, String>() {
        public String apply(final ProjectDependencies.ProjectNode it) {
          return it.project.getName();
        }
      };
      List<String> _map_1 = ListExtensions.<ProjectDependencies.ProjectNode, String>map(this.downstream, _function_1);
      String _join_1 = IterableExtensions.join(_map_1, ", ");
      _builder.append(_join_1, "");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
    
    public ProjectNode(final IProject project) {
      super();
      this.project = project;
    }
    
    @Pure
    public IProject getProject() {
      return this.project;
    }
    
    @Pure
    public List<ProjectDependencies.ProjectNode> getUpstream() {
      return this.upstream;
    }
    
    @Pure
    public List<ProjectDependencies.ProjectNode> getDownstream() {
      return this.downstream;
    }
  }
  
  private Map<IProject, ProjectDependencies.ProjectNode> nodeMap = CollectionLiterals.<IProject, ProjectDependencies.ProjectNode>newHashMap();
  
  public ProjectDependencies(final Iterable<IProject> projects) {
    try {
      for (final IProject project : projects) {
        ProjectDependencies.ProjectNode _projectNode = new ProjectDependencies.ProjectNode(project);
        this.nodeMap.put(project, _projectNode);
      }
      for (final IProject project_1 : projects) {
        {
          final ProjectDependencies.ProjectNode currentNode = this.nodeMap.get(project_1);
          IProject[] _referencedProjects = project_1.getReferencedProjects();
          for (final IProject dependency : _referencedProjects) {
            {
              final ProjectDependencies.ProjectNode dependencyNode = this.nodeMap.get(dependency);
              dependencyNode.downstream.add(currentNode);
              currentNode.upstream.add(dependencyNode);
            }
          }
        }
      }
      Collection<ProjectDependencies.ProjectNode> _values = this.nodeMap.values();
      final Function1<ProjectDependencies.ProjectNode, String> _function = new Function1<ProjectDependencies.ProjectNode, String>() {
        public String apply(final ProjectDependencies.ProjectNode it) {
          StringConcatenation _builder = new StringConcatenation();
          String _string = it.toString();
          _builder.append(_string, "");
          _builder.newLineIfNotEmpty();
          Iterable<IProject> _allUpstream = ProjectDependencies.this.getAllUpstream(it.project);
          final Function1<IProject, String> _function = new Function1<IProject, String>() {
            public String apply(final IProject it) {
              return it.getName();
            }
          };
          Iterable<String> _map = IterableExtensions.<IProject, String>map(_allUpstream, _function);
          String _join = IterableExtensions.join(_map, ", ");
          _builder.append(_join, "");
          _builder.newLineIfNotEmpty();
          Iterable<IProject> _allDownstream = ProjectDependencies.this.getAllDownstream(it.project);
          final Function1<IProject, String> _function_1 = new Function1<IProject, String>() {
            public String apply(final IProject it) {
              return it.getName();
            }
          };
          Iterable<String> _map_1 = IterableExtensions.<IProject, String>map(_allDownstream, _function_1);
          String _join_1 = IterableExtensions.join(_map_1, ", ");
          _builder.append(_join_1, "");
          _builder.newLineIfNotEmpty();
          return _builder.toString();
        }
      };
      Iterable<String> _map = IterableExtensions.<ProjectDependencies.ProjectNode, String>map(_values, _function);
      String _join = IterableExtensions.join(_map, "\n");
      XtextCompilerConsole.log(_join);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public List<IProject> getUpstream(final IProject project) {
    List<IProject> _elvis = null;
    ProjectDependencies.ProjectNode _get = this.nodeMap.get(project);
    List<ProjectDependencies.ProjectNode> _upstream = null;
    if (_get!=null) {
      _upstream=_get.upstream;
    }
    List<IProject> _map = null;
    if (_upstream!=null) {
      final Function1<ProjectDependencies.ProjectNode, IProject> _function = new Function1<ProjectDependencies.ProjectNode, IProject>() {
        public IProject apply(final ProjectDependencies.ProjectNode it) {
          return project;
        }
      };
      _map=ListExtensions.<ProjectDependencies.ProjectNode, IProject>map(_upstream, _function);
    }
    if (_map != null) {
      _elvis = _map;
    } else {
      _elvis = Collections.<IProject>unmodifiableList(CollectionLiterals.<IProject>newArrayList());
    }
    return _elvis;
  }
  
  public List<IProject> getDownstream(final IProject project) {
    List<IProject> _elvis = null;
    ProjectDependencies.ProjectNode _get = this.nodeMap.get(project);
    List<ProjectDependencies.ProjectNode> _downstream = null;
    if (_get!=null) {
      _downstream=_get.downstream;
    }
    List<IProject> _map = null;
    if (_downstream!=null) {
      final Function1<ProjectDependencies.ProjectNode, IProject> _function = new Function1<ProjectDependencies.ProjectNode, IProject>() {
        public IProject apply(final ProjectDependencies.ProjectNode it) {
          return project;
        }
      };
      _map=ListExtensions.<ProjectDependencies.ProjectNode, IProject>map(_downstream, _function);
    }
    if (_map != null) {
      _elvis = _map;
    } else {
      _elvis = Collections.<IProject>unmodifiableList(CollectionLiterals.<IProject>newArrayList());
    }
    return _elvis;
  }
  
  public Iterable<IProject> getAllUpstream(final IProject project) {
    Iterable<IProject> _elvis = null;
    ProjectDependencies.ProjectNode _get = this.nodeMap.get(project);
    Set<ProjectDependencies.ProjectNode> _transitiveHull = null;
    if (_get!=null) {
      final Function1<ProjectDependencies.ProjectNode, Iterable<ProjectDependencies.ProjectNode>> _function = new Function1<ProjectDependencies.ProjectNode, Iterable<ProjectDependencies.ProjectNode>>() {
        public Iterable<ProjectDependencies.ProjectNode> apply(final ProjectDependencies.ProjectNode it) {
          return it.upstream;
        }
      };
      _transitiveHull=this.<ProjectDependencies.ProjectNode>transitiveHull(_get, _function);
    }
    Iterable<IProject> _map = null;
    if (_transitiveHull!=null) {
      final Function1<ProjectDependencies.ProjectNode, IProject> _function_1 = new Function1<ProjectDependencies.ProjectNode, IProject>() {
        public IProject apply(final ProjectDependencies.ProjectNode it) {
          return it.project;
        }
      };
      _map=IterableExtensions.<ProjectDependencies.ProjectNode, IProject>map(_transitiveHull, _function_1);
    }
    if (_map != null) {
      _elvis = _map;
    } else {
      _elvis = Collections.<IProject>unmodifiableList(CollectionLiterals.<IProject>newArrayList());
    }
    return _elvis;
  }
  
  public Iterable<IProject> getAllDownstream(final IProject project) {
    Iterable<IProject> _elvis = null;
    ProjectDependencies.ProjectNode _get = this.nodeMap.get(project);
    Set<ProjectDependencies.ProjectNode> _transitiveHull = null;
    if (_get!=null) {
      final Function1<ProjectDependencies.ProjectNode, Iterable<ProjectDependencies.ProjectNode>> _function = new Function1<ProjectDependencies.ProjectNode, Iterable<ProjectDependencies.ProjectNode>>() {
        public Iterable<ProjectDependencies.ProjectNode> apply(final ProjectDependencies.ProjectNode it) {
          return it.downstream;
        }
      };
      _transitiveHull=this.<ProjectDependencies.ProjectNode>transitiveHull(_get, _function);
    }
    Iterable<IProject> _map = null;
    if (_transitiveHull!=null) {
      final Function1<ProjectDependencies.ProjectNode, IProject> _function_1 = new Function1<ProjectDependencies.ProjectNode, IProject>() {
        public IProject apply(final ProjectDependencies.ProjectNode it) {
          return it.project;
        }
      };
      _map=IterableExtensions.<ProjectDependencies.ProjectNode, IProject>map(_transitiveHull, _function_1);
    }
    if (_map != null) {
      _elvis = _map;
    } else {
      _elvis = Collections.<IProject>unmodifiableList(CollectionLiterals.<IProject>newArrayList());
    }
    return _elvis;
  }
  
  public boolean dependsOn(final IProject upstream, final IProject downstream) {
    final ProjectDependencies.ProjectNode upstreamNode = this.nodeMap.get(upstream);
    final ProjectDependencies.ProjectNode downstreamNode = this.nodeMap.get(downstream);
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(upstreamNode, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(downstreamNode, null));
      _and = _notEquals_1;
    }
    if (_and) {
      HashSet<ProjectDependencies.ProjectNode> _newHashSet = CollectionLiterals.<ProjectDependencies.ProjectNode>newHashSet();
      return this.containsRecursive(upstreamNode, downstreamNode, _newHashSet);
    } else {
      return false;
    }
  }
  
  protected boolean containsRecursive(final ProjectDependencies.ProjectNode upstream, final ProjectDependencies.ProjectNode downstream, final Set<ProjectDependencies.ProjectNode> seen) {
    boolean _xblockexpression = false;
    {
      final List<ProjectDependencies.ProjectNode> candidates = upstream.downstream;
      boolean _contains = candidates.contains(downstream);
      if (_contains) {
        return true;
      }
      seen.add(upstream);
      final Function1<ProjectDependencies.ProjectNode, Boolean> _function = new Function1<ProjectDependencies.ProjectNode, Boolean>() {
        public Boolean apply(final ProjectDependencies.ProjectNode it) {
          boolean _contains = seen.contains(it);
          return Boolean.valueOf((!_contains));
        }
      };
      Iterable<ProjectDependencies.ProjectNode> _filter = IterableExtensions.<ProjectDependencies.ProjectNode>filter(candidates, _function);
      final Function1<ProjectDependencies.ProjectNode, Boolean> _function_1 = new Function1<ProjectDependencies.ProjectNode, Boolean>() {
        public Boolean apply(final ProjectDependencies.ProjectNode it) {
          return Boolean.valueOf(ProjectDependencies.this.containsRecursive(it, downstream, seen));
        }
      };
      _xblockexpression = IterableExtensions.<ProjectDependencies.ProjectNode>exists(_filter, _function_1);
    }
    return _xblockexpression;
  }
  
  protected <T extends Object> Set<T> transitiveHull(final T start, final Function1<? super T, ? extends Iterable<T>> reachable) {
    final LinkedHashSet<T> collector = CollectionLiterals.<T>newLinkedHashSet();
    Iterable<T> _apply = reachable.apply(start);
    for (final T r : _apply) {
      this.<LinkedHashSet<T>, T>transitiveHull(r, reachable, collector);
    }
    return collector;
  }
  
  protected <R extends Collection<? super T>, T extends Object> R transitiveHull(final T start, final Function1<? super T, ? extends Iterable<T>> reachable, final R collector) {
    boolean _add = collector.add(start);
    if (_add) {
      Iterable<T> _apply = reachable.apply(start);
      for (final T r : _apply) {
        this.<R, T>transitiveHull(r, reachable, collector);
      }
    }
    return collector;
  }
}
