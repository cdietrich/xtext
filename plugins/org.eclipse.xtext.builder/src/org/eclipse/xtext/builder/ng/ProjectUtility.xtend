/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.ng

import java.util.Set
import org.eclipse.core.resources.IProject

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
class ProjectUtility {
	
	static def dependsOn(IProject project, IProject dependency) {
		containsRecursive(project, dependency, [referencedProjects.toSet], newHashSet)
	}
	
	private static def boolean containsRecursive(IProject project, IProject toBeFound, (IProject)=>Set<IProject> lambda, Set<IProject> seen) {
		val candidates = lambda.apply(project)
		if(candidates.contains(toBeFound)) 
			return true
		seen += project
		candidates
			.filter[!seen.contains(it)]
			.exists[containsRecursive(toBeFound, lambda, seen)]
	} 
}