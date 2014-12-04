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
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IResourceChangeEvent
import org.eclipse.core.resources.IResourceChangeListener
import org.eclipse.core.resources.IResourceDelta
import org.eclipse.core.resources.IStorage
import org.eclipse.core.resources.IWorkspace
import org.eclipse.jdt.core.ElementChangedEvent
import org.eclipse.jdt.core.IElementChangedListener
import org.eclipse.jdt.core.JavaCore
import org.eclipse.xtext.builder.ng.debug.ResourceChangeEventToString
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole
import org.eclipse.xtext.ui.resource.IResourceSetProvider
import org.eclipse.xtext.ui.resource.IStorage2UriMapper
import org.eclipse.xtext.ui.resource.UriValidator

import static org.eclipse.core.resources.IResourceDelta.*

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@Singleton
class XtextWorkspaceListener implements IResourceChangeListener, IElementChangedListener {

	@Inject IStorage2UriMapper storage2UriMapper

	@Inject CompilerJob compilerJob

	@Inject IResourceSetProvider resourceSetProvider

	@Inject UriValidator uriValidator
	
	IWorkspace workspace

	@Inject
	def register(IWorkspace workspace) {
		this.workspace = workspace
		workspace.addResourceChangeListener(this)
		JavaCore.addElementChangedListener(this, ElementChangedEvent.POST_CHANGE)
	}

	def deregister() {
		JavaCore.removeElementChangedListener(this)
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
				if(delta.flags != IResourceDelta.MARKERS) {
					val resource = delta.resource
					switch resource {
						IStorage case delta.kind == REMOVED: {
							if(isBinaryJavaResource(resource)) {
								project2request.get(resource.project).forceBuild = true
							} else {
								val uri = storage2UriMapper.getUri(resource)
								if(uri != null && uriValidator.canBuild(uri, resource))
									project2request.get(resource.project).toBeDeleted += uri
							}
						}
						IStorage case delta.kind == ADDED || delta.kind == CHANGED: {
							if(isBinaryJavaResource(resource)) {
								project2request.get(resource.project).forceBuild = true
							} else {
								val uri = storage2UriMapper.getUri(resource)
								if(uri != null && uriValidator.canBuild(uri, resource))
									project2request.get(resource.project).toBeUpdated += uri
							}
						}
					}
					return true
				}
			]
			compilerJob.compilationRequests = requests
		} catch (Exception exc) {
			XtextCompilerConsole.log(exc)
		}
		
	}
	
	def isBinaryJavaResource(IResource resource) {
		#{'class', 'jar'}.contains(resource.fileExtension)
	}
	
	override elementChanged(ElementChangedEvent event) {
		XtextCompilerConsole.log(event)
	}
	
}
