/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.builder.ng;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@SuppressWarnings("all")
public class ProjectUtility {
  public static boolean dependsOn(final IProject project, final IProject dependency) {
    final Function1<IProject, Set<IProject>> _function = new Function1<IProject, Set<IProject>>() {
      public Set<IProject> apply(final IProject it) {
        try {
          IProject[] _referencedProjects = it.getReferencedProjects();
          return IterableExtensions.<IProject>toSet(((Iterable<IProject>)Conversions.doWrapArray(_referencedProjects)));
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    HashSet<IProject> _newHashSet = CollectionLiterals.<IProject>newHashSet();
    return ProjectUtility.containsRecursive(project, dependency, _function, _newHashSet);
  }
  
  private static boolean containsRecursive(final IProject project, final IProject toBeFound, final Function1<? super IProject, ? extends Set<IProject>> lambda, final Set<IProject> seen) {
    boolean _xblockexpression = false;
    {
      final Set<IProject> candidates = lambda.apply(project);
      boolean _contains = candidates.contains(toBeFound);
      if (_contains) {
        return true;
      }
      seen.add(project);
      final Function1<IProject, Boolean> _function = new Function1<IProject, Boolean>() {
        public Boolean apply(final IProject it) {
          boolean _contains = seen.contains(it);
          return Boolean.valueOf((!_contains));
        }
      };
      Iterable<IProject> _filter = IterableExtensions.<IProject>filter(candidates, _function);
      final Function1<IProject, Boolean> _function_1 = new Function1<IProject, Boolean>() {
        public Boolean apply(final IProject it) {
          return Boolean.valueOf(ProjectUtility.containsRecursive(it, toBeFound, lambda, seen));
        }
      };
      _xblockexpression = IterableExtensions.<IProject>exists(_filter, _function_1);
    }
    return _xblockexpression;
  }
}
