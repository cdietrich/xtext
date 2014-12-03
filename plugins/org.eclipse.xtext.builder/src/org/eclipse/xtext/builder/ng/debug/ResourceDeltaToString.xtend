/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder.ng.debug

import org.eclipse.core.resources.IResourceDelta
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.Path

import static org.eclipse.core.resources.IResourceDelta.*

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
class ResourceDeltaToString implements (IResourceDelta)=>String {

	private def String getKindString(IResourceDelta delta) {
		val kind = delta.kind
		val flags = delta.flags
		val result = <String>newArrayList()
		if(kind ** ADDED) result += "ADDED"
		if(kind ** REMOVED) result += "REMOVED"
		if(kind ** CHANGED) result += "CHANGED"
		if(kind ** ADDED_PHANTOM) result += "ADDED_PHANTOM"
		if(kind ** REMOVED_PHANTOM) result += "REMOVED_PHANTOM"
		if(flags ** CONTENT) result += "CONTENT"
		if(flags ** DERIVED_CHANGED) result += "DERIVED_CHANGED"
		if(flags ** DESCRIPTION) result += "DESCRIPTION"
		if(flags ** ENCODING) result += "ENCODING"
		if(flags ** LOCAL_CHANGED) result += "LOCAL_CHANGED"
		if(flags ** OPEN) result += "OPEN"
		if(flags ** MOVED_TO) result += "MOVED_TO"
		if(flags ** MOVED_FROM) result += "MOVED_FROM"
		if(flags ** COPIED_FROM) result += "COPIED_FROM"
		if(flags ** TYPE) result += "TYPE"
		if(flags ** SYNC) result += "SYNC"
		if(flags ** MARKERS) result += "MARKERS"
		if(flags ** REPLACED) result += "REPLACED"
		return result.join("|")
	}

	def boolean **(int bits, int bit) {
		bits.bitwiseAnd(bit) != 0
	}

	override apply(IResourceDelta delta) {
		delta.class.simpleName + " " + apply(new Path("/"), delta)
	}

	def private String apply(IPath parent, IResourceDelta delta) {
		val children = delta.affectedChildren
		if (children.size == 1)
			return apply(parent, children.head)
		val kind = delta.kindString
		val type = delta.resource?.class.simpleName
		val name = delta.fullPath.makeRelativeTo(parent).toString
		val body = '''«kind» «type» «name»'''
		if (children.isEmpty)
			return body
		return body + " {\n  " + children.map[apply(delta.fullPath, it)].join("\n").replace("\n", "\n  ") + "\n}"
	}
}