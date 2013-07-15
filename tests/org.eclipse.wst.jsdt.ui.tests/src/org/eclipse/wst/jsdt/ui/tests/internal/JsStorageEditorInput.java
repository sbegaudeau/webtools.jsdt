/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.ui.tests.internal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

/**
 * @author nitin
 * 
 */
public class JsStorageEditorInput implements IStorageEditorInput {
	private static class JSStorage implements IStorage {
		private String fContents;
		private IPath fPath;

		/**
		 * 
		 */
		public JSStorage(String contents, IPath path) {
			fContents = contents;
			fPath = path;
		}

		/* (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class) */
		public Object getAdapter(Class adapter) {
			return Platform.getAdapterManager().getAdapter(this, adapter);
		}

		/* (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IStorage#getContents() */
		public InputStream getContents() throws CoreException {
			return new ByteArrayInputStream(fContents.getBytes());
		}

		/* (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IStorage#getFullPath() */
		public IPath getFullPath() {
			return fPath;
		}

		/* (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IStorage#getName() */
		public String getName() {
			return fPath.lastSegment();
		}

		/* (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IStorage#isReadOnly() */
		public boolean isReadOnly() {
			return true;
		}
	}

	IStorage fStorage;

	/**
	 * 
	 */
	public JsStorageEditorInput(IStorage storage) {
		fStorage = storage;
	}

	public JsStorageEditorInput(String contents, IPath path) {
		this(new JSStorage(contents, path));
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists() */
	public boolean exists() {
		return false;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class) */
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor() */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getName() */
	public String getName() {
		return fStorage.getName();
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable() */
	public IPersistableElement getPersistable() {
		return null;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStorageEditorInput#getStorage() */
	public IStorage getStorage() throws CoreException {
		return fStorage;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText() */
	public String getToolTipText() {
		return fStorage.getFullPath().toString();
	}
}
