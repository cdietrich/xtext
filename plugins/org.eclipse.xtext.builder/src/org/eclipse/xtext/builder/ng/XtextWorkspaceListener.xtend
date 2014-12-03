/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.ng

import com.google.inject.Inject
import com.google.inject.Singleton
import org.eclipse.core.resources.IResourceChangeEvent
import org.eclipse.core.resources.IResourceChangeListener
import org.eclipse.core.resources.IStorage
import org.eclipse.core.resources.IWorkspace
import org.eclipse.xtext.builder.ng.debug.ResourceChangeEventToString
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole
import org.eclipse.xtext.ui.resource.IResourceSetProvider
import org.eclipse.xtext.ui.resource.IStorage2UriMapper

import static org.eclipse.core.resources.IResourceDelta.*

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@Singleton
class XtextWorkspaceListener implements IResourceChangeListener {

	@Inject IStorage2UriMapper storage2UriMapper

	@Inject CompilerJob compilerJob

	@Inject IResourceSetProvider resourceSetProvider

	IWorkspace workspace

	@Inject
	def register(IWorkspace workspace) {
		this.workspace = workspace
		workspace.addResourceChangeListener(this)
	}

	def deregister() {
		workspace.removeResourceChangeListener(this)
	}

	override resourceChanged(IResourceChangeEvent event) {
		if(!BuilderSwitch.isUseNewCompiler)
			return;
		try {
			XtextCompilerConsole.log(new ResourceChangeEventToString().apply(event))
			val requests = <CompilationRequest>newArrayList
			for (project : workspace.computeProjectOrder(workspace.root.projects).projects) {
				requests.add(new CompilationRequest => [ request |
					request.project = project
					request.resourceSetProvider = [
						resourceSetProvider.get(project)
					]
				])
			}
			val project2request = requests.toMap[project]
			event.delta.accept [ delta |
				val resource = delta.resource
				switch resource {
					IStorage case delta.kind == REMOVED:
						project2request.get(resource.project).toBeDeleted += storage2UriMapper.getUri(resource)
					IStorage case delta.kind == ADDED || delta.kind == CHANGED:
						project2request.get(resource.project).toBeUpdated += storage2UriMapper.getUri(resource)
				}
				return true
			]
			compilerJob.compilationRequests = requests	
		} catch (Exception exc) {
			XtextCompilerConsole.log(exc)
		}
	}
}
