/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.ng

import com.google.inject.Inject
import java.util.ArrayList
import java.util.LinkedList
import java.util.List
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.OperationCanceledException
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.resources.IWorkspace

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
class CompilerJob extends Job {
	
	@Inject XtextCompiler compiler
	
	@Inject IWorkspace workspace
	
	LinkedList<CompilationRequest> requests = newLinkedList
	
	extension ProjectDependencies projectDependencies
	
	new() {
		super("XtextCompilerJob")
	}
	
	synchronized def setCompilationRequests(List<CompilationRequest> newRequests) {
		if(newRequests.empty || !newRequests.exists[shouldCompile])
			return
		val pendingRequests = drainRequests
		val currentRequest = pendingRequests.head
		projectDependencies = new ProjectDependencies(workspace.root.projects)
		if(currentRequest != null) {
			if(currentRequest.project.dependsOn(newRequests.findFirst[shouldSchedule].project)) 
				cancel
		}
		if(!pendingRequests.empty) {
			val project2request = newRequests.toMap[project]
			for(pending: pendingRequests) {
				val newRequest = project2request.get(pending.project)
				newRequest.toBeDeleted += pending.toBeDeleted
				newRequest.toBeUpdated += pending.toBeUpdated
			}
		}
		synchronized(requests) {
			requests.addAll(newRequests)
		}
		if(state != RUNNING)
			schedule		
	}
	
	private def drainRequests() {
		synchronized(requests) {
			val result = new ArrayList(requests)
			requests.clear
			result
		}
	}  
	
	override protected run(IProgressMonitor monitor) {
		try {
			while(true) {
				val currentRequest = nextCompilableRequest
				if(currentRequest == null)
					return Status.OK_STATUS
				currentRequest.monitor = monitor
				val deltas = compiler.compile(currentRequest)
				synchronized (requests) {
					if(!requests.empty) {
						requests.removeFirst
						if(!deltas.empty) {
							for(downstream: currentRequest.project.allDownstream) 
								requests.findFirst[project == downstream]?.upstreamFileChanges?.addAll(deltas)
						}					
					}
				}
			} 
		} catch(OperationCanceledException e) {
			return Status.CANCEL_STATUS
		}
	}
	
	private def getNextCompilableRequest() {
		synchronized (requests) {
			while(!requests.empty) {
				val next = requests.head
				if(next.shouldCompile)
					return next
				else
					requests.removeFirst
			}
			return null
		}
	}
}