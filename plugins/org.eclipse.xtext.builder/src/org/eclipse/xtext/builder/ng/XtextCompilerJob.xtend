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
import java.util.ArrayList
import java.util.LinkedList
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IWorkspace
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.OperationCanceledException
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.xtext.builder.ng.debug.XtextCompilerConsole

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @author Moritz Eysholdt
 */
@Singleton
class XtextCompilerJob extends Job {

	@Inject XtextCompiler compiler

	@Inject IWorkspace workspace

	@Inject CompilationRequest.Factory requestFactory

	LinkedList<CompilationRequest> requests = newLinkedList

	extension ProjectDependencies projectDependencies

	new() {
		super("XtextCompilerJob")
	}

	synchronized def enqueue(Iterable<CompilationRequest> newRequests) {
		if (newRequests.empty || !newRequests.exists[shouldCompile])
			return;
		XtextCompilerConsole.log('''
			Queueing:
				«newRequests.join('\n')»
		''')
		// build a map of all projects and requests in the correct build order 	
		val project2newRequest = newRequests.toMap[project]
		val allProjects2request = <IProject, CompilationRequest>newLinkedHashMap
		for (project : workspace.computeProjectOrder(workspace.root.projects).projects) {
			val request = project2newRequest.get(project) ?: requestFactory.create(project)
			allProjects2request.put(project, request)
		}

		projectDependencies = new ProjectDependencies(workspace.root.projects)
		val pendingRequests = drainRequests

		// cancel the current compilation if its project depends on a project that should be compiled now
		val currentRequest = pendingRequests.head
		if (currentRequest != null) {
			if (currentRequest.project.allUpstream.exists[project2newRequest.get(it)?.shouldCompile])
				cancel
		}
		
		// merge remaining requests
		if (!pendingRequests.empty) {
			for (pending : pendingRequests) {
				allProjects2request
					.get(pending.project)
					?.merge(pending)
			}
		}
		synchronized (requests) {
			requests.addAll(allProjects2request.values)
		}
		if (state != RUNNING)
			schedule
	}

	private def drainRequests() {
		synchronized (requests) {
			val result = new ArrayList(requests)
			requests.clear
			result
		}
	}

	override protected run(IProgressMonitor monitor) {
		try {
			while (true) {
				val currentRequest = nextCompilableRequest
				if (currentRequest == null)
					return Status.OK_STATUS
				currentRequest.monitor = monitor
				val deltas = compiler.compile(currentRequest)
				synchronized (requests) {
					if (!requests.empty) {
						requests.removeFirst
						if (!deltas.empty) {
							for (downstream : currentRequest.project.allDownstream)
								requests.findFirst[project == downstream]?.upstreamFileChanges?.addAll(deltas)
						}
					}
				}
			}
		} catch (OperationCanceledException e) {
			return Status.CANCEL_STATUS
		}
	}

	private def getNextCompilableRequest() {
		synchronized (requests) {
			while (!requests.empty) {
				val next = requests.head
				if (next.shouldCompile)
					return next
				else
					requests.removeFirst
			}
			return null
		}
	}
}
