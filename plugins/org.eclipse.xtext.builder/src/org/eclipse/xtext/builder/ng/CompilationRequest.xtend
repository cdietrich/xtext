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

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@Accessors
class CompilationRequest {

	val Set<URI> toBeUpdated = newHashSet
	val Set<URI> toBeDeleted = newHashSet

	IProject project

	val List<IResourceDescription.Delta> upstreamFileChanges = newArrayList

	boolean computeAffected

	Provider<ResourceSet> resourceSetProvider

	IProgressMonitor monitor
	
	boolean forceBuild
	
	override toString() '''
		CompilationRequest: «project.name»:
		  delete: «FOR uri : toBeDeleted SEPARATOR ','»«uri?.lastSegment»«ENDFOR»
		  update: «FOR uri : toBeUpdated SEPARATOR ','»«uri?.lastSegment»«ENDFOR»
		  «upstreamFileChanges.size» upstreamFileChanges 
	'''

	def boolean shouldCompile() {
		forceBuild || computeAffected || !toBeDeleted.empty || !toBeUpdated.empty || !upstreamFileChanges.empty
	}
}
