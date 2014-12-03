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

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@Accessors
class CompilationRequest {

	val Set<URI> toBeUpdated = newHashSet
	val Set<URI> toBeDeleted = newHashSet

	IProject project

//	val List<IResourceDescription.Delta> upstreamFileChanges = newArrayList
//	val List<IResourceDescription.Delta> upstreamStructuralFileChanges = newArrayList

	boolean computeAffected

	Provider<ResourceSet> resourceSetProvider

	IProgressMonitor monitor

//	def void addUpstreamChange(IResourceDescription.Delta change) {
//		if (change.haveEObjectDescriptionsChanged)
//			upstreamStructuralFileChanges += change
//		upstreamFileChanges += change
//	}

//	def void addUpstreamChanges(Iterable<IResourceDescription.Delta> changes) {
//		changes.forEach[addUpstreamChange]
//	}

	override toString() '''
		CompilationRequest: «project.name»:
		  delete: «FOR uri : toBeDeleted SEPARATOR ','»«uri?.lastSegment»«ENDFOR»
		  update: «FOR uri : toBeUpdated SEPARATOR ','»«uri?.lastSegment»«ENDFOR»
«««		  «upstreamFileChanges.size» upstreamFileChanges 
«««		  «upstreamStructuralFileChanges.size» upstreamStructuralFileChanges 
	'''

	def boolean shouldCompile() {
		computeAffected || !toBeDeleted.empty || !toBeUpdated.empty 
//		|| !upstreamFileChanges.empty
	}
}
