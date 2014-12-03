/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.ng

import com.google.common.collect.ImmutableList
import com.google.inject.Inject
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IWorkspace
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtend.lib.annotations.Data
import org.eclipse.xtext.builder.IXtextBuilderParticipant
import org.eclipse.xtext.builder.IXtextBuilderParticipant.BuildType
import org.eclipse.xtext.builder.builderState.IBuilderState
import org.eclipse.xtext.builder.impl.BuildData
import org.eclipse.xtext.builder.impl.QueuedBuildData
import org.eclipse.xtext.builder.impl.RegistryBuilderParticipant
import org.eclipse.xtext.builder.impl.ToBeBuilt
import org.eclipse.xtext.resource.IResourceDescription.Delta
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
class XtextCompiler {

	@Inject IBuilderState builderState

	@Inject QueuedBuildData queuedBuildData

	@Inject RegistryBuilderParticipant participant

	@Inject IWorkspace workspace

	def compile(CompilationRequest request) {
		val buildType = BuildType.INCREMENTAL
		val indexingOnly = buildType == BuildType.RECOVERY
		val resourceSet = request.resourceSetProvider.get
		resourceSet.loadOptions.put(ResourceDescriptionsProvider.NAMED_BUILDER_SCOPE, true)
		val toBeBuilt = new ToBeBuilt
		toBeBuilt.toBeDeleted += request.toBeDeleted
		toBeBuilt.toBeUpdated += request.toBeUpdated
		val buildData = new BuildData(request.project.name, resourceSet, toBeBuilt, queuedBuildData, indexingOnly)
		val progress = new NullProgressMonitor
		val deltas = builderState.update(buildData, progress)
		if (participant != null && !indexingOnly) {
			workspace.run(
				[
					participant.build(new BuildContext(request.project, resourceSet, deltas, buildType), progress);
				], progress)
		} else {
			progress.worked(1);
		}
		deltas
	}

	@Data
	static class BuildContext implements IXtextBuilderParticipant.IBuildContext {

		val IProject builtProject 
		val ResourceSet resourceSet
		val ImmutableList<Delta> deltas
		val BuildType buildType
		
		override needRebuild() {
			// I suppose this is no longer needed
		}
	}

}
