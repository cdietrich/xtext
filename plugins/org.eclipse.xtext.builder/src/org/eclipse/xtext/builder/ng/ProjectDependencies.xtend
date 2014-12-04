/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.ng

import java.util.Collection
import java.util.List
import java.util.Map
import java.util.Set
import org.eclipse.core.resources.IProject
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
class ProjectDependencies {

	Map<IProject, ProjectNode> nodeMap = newHashMap

	new(Iterable<IProject> projects) {
		for (project : projects)
			nodeMap.put(project, new ProjectNode(project))
		for (project : projects) {
			val currentNode = nodeMap.get(project)
			for (dependency : project.referencedProjects) {
				val dependencyNode = nodeMap.get(dependency)
				dependencyNode.downstream += currentNode
				currentNode.upstream += dependencyNode
			}
		}
		XtextCompilerConsole.log(nodeMap.values.map['''
			«toString»
			«project.allUpstream.map[name].join(', ')»
			«project.allDownstream.map[name].join(', ')»
		'''].join('\n'))
	}

	def getUpstream(IProject project) {
		nodeMap.get(project)?.upstream?.map[project] ?: #[]
	}

	def getDownstream(IProject project) {
		nodeMap.get(project)?.downstream?.map[project] ?: #[]
	}

	def getAllUpstream(IProject project) {
		nodeMap.get(project)?.transitiveHull[upstream]?.map[it.project] ?: #[]
	}

	def getAllDownstream(IProject project) {
		nodeMap.get(project)?.transitiveHull[downstream]?.map[it.project] ?: #[]
	}

	def dependsOn(IProject upstream, IProject downstream) {
		val upstreamNode = nodeMap.get(upstream)
		val downstreamNode = nodeMap.get(downstream)
		if (upstreamNode != null && downstreamNode != null)
			return containsRecursive(upstreamNode, downstreamNode, newHashSet)
		else
			return false
	}

	protected def boolean containsRecursive(ProjectNode upstream, ProjectNode downstream, Set<ProjectNode> seen) {
		val candidates = upstream.downstream
		if (candidates.contains(downstream))
			return true
		seen += upstream
		candidates.filter[!seen.contains(it)].exists[containsRecursive(downstream, seen)]
	}

	protected def <T> Set<T> transitiveHull(T start, (T)=>Iterable<T> reachable) {
		val collector = newLinkedHashSet
		for (r : reachable.apply(start))
			transitiveHull(r, reachable, collector)
		return collector
	}

	protected def <R extends Collection<? super T>, T> R transitiveHull(T start, (T)=>Iterable<T> reachable, R collector) {
		if (collector.add(start))
			for (r : reachable.apply(start))
				transitiveHull(r, reachable, collector)
		return collector
	}
	
	@Accessors
	@FinalFieldsConstructor
	protected static class ProjectNode {
		val IProject project
		val List<ProjectNode> upstream = newArrayList()
		val List<ProjectNode> downstream = newArrayList()
		
		override toString() '''
			«project.name»
			upstream: «upstream.map[project.name].join(', ')»
			downstream: «downstream.map[project.name].join(', ')»
		'''
		
	}
}

