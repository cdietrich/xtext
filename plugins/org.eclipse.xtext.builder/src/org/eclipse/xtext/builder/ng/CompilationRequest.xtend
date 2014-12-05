/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.ng

import com.google.inject.Provider
import java.util.Set
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtext.resource.IResourceDescription
import java.util.List
import com.google.inject.Inject
import org.eclipse.xtext.ui.resource.IResourceSetProvider

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@Accessors
class CompilationRequest {

	static class Factory {
		@Inject IResourceSetProvider resourceSetProvider
		
		def create(IProject project) {
			new CompilationRequest => [ request |
				request.project = project
				request.resourceSetProvider = [
					resourceSetProvider.get(project)
				]
			]
		}
	}
	
	protected new() {}

	val Set<URI> toBeUpdated = newHashSet
	val Set<URI> toBeDeleted = newHashSet

	IProject project

	val List<IResourceDescription.Delta> upstreamFileChanges = newArrayList

	boolean computeAffected

	Provider<ResourceSet> resourceSetProvider

	IProgressMonitor monitor
	
	override toString() '''
		CompilationRequest: «project.name»:
		  computeAffected: «computeAffected»
		  delete: «toBeDeleted.map[lastSegment].join(',')»
		  update: «toBeUpdated.map[lastSegment].join(',')»
		  «upstreamFileChanges.size» upstreamFileChanges 
	'''

	def boolean shouldCompile() {
		computeAffected || !toBeDeleted.empty || !toBeUpdated.empty || !upstreamFileChanges.empty
	}
	
	def merge(CompilationRequest other) {
		toBeDeleted += other.toBeDeleted
		toBeUpdated += other.toBeUpdated
		computeAffected = computeAffected || other.computeAffected
		upstreamFileChanges += other.upstreamFileChanges
	}
}
