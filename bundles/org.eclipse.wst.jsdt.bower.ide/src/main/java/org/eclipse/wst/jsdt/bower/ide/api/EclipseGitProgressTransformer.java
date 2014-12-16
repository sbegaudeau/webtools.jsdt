/*******************************************************************************
 * Copyright (C) 2008, Shawn O. Pearce <spearce@spearce.org>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Shawn O. Pearce - initial API and implementation
 *     Obeo - Checkstyle
 *******************************************************************************/
package org.eclipse.wst.jsdt.bower.ide.api;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jgit.lib.ProgressMonitor;

/**
 * An EGit to Eclipse progress monitor.
 *
 * @author <a href="mailto:spearce@spearce.org">Shawn O. Pearce</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class EclipseGitProgressTransformer implements ProgressMonitor {
	/**
	 * Empty string.
	 */
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * The original progress monitor.
	 */
	private final IProgressMonitor root;

	/**
	 * The progress monitor of the subtasks.
	 */
	private IProgressMonitor task;

	/**
	 * The message.
	 */
	private String message;

	/**
	 * The last worked.
	 */
	private int lastWorked;

	/**
	 * The total work.
	 */
	private int totalWork;

	/**
	 * Create a new progress monitor.
	 *
	 * @param eclipseMonitor
	 *            the Eclipse monitor we update.
	 */
	public EclipseGitProgressTransformer(final IProgressMonitor eclipseMonitor) {
		root = eclipseMonitor;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jgit.lib.ProgressMonitor#start(int)
	 */
	@Override
	public void start(final int totalTasks) {
		root.beginTask(EMPTY_STRING, totalTasks * 1000);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jgit.lib.ProgressMonitor#beginTask(java.lang.String, int)
	 */
	@Override
	public void beginTask(final String name, final int total) {
		endTask();
		message = name;
		lastWorked = 0;
		totalWork = total;
		task = new SubProgressMonitor(root, 1000);
		if (totalWork == UNKNOWN) {
			task.beginTask(EMPTY_STRING, IProgressMonitor.UNKNOWN);
		} else {
			task.beginTask(EMPTY_STRING, totalWork);
		}
		task.subTask(message);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jgit.lib.ProgressMonitor#update(int)
	 */
	@Override
	public void update(final int work) {
		if (task == null) {
			return;
		}

		final int cmp = lastWorked + work;
		if (totalWork == UNKNOWN && cmp > 0) {
			if (lastWorked != cmp) {
				task.subTask(message + ", " + cmp); //$NON-NLS-1$
			}
		} else if (totalWork <= 0) {
			// Do nothing to update the task.
		} else if (cmp * 100 / totalWork != lastWorked * 100 / totalWork) {
			final StringBuilder m = new StringBuilder();
			m.append(message);
			m.append(": "); //$NON-NLS-1$
			while (m.length() < 25) {
				m.append(' ');
			}

			final String twstr = String.valueOf(totalWork);
			String cmpstr = String.valueOf(cmp);
			while (cmpstr.length() < twstr.length()) {
				cmpstr = " " + cmpstr; //$NON-NLS-1$
			}
			final int pcnt = cmp * 100 / totalWork;
			if (pcnt < 100) {
				m.append(' ');
			}
			if (pcnt < 10) {
				m.append(' ');
			}
			m.append(pcnt);
			m.append("% ("); //$NON-NLS-1$
			m.append(cmpstr);
			m.append("/"); //$NON-NLS-1$
			m.append(twstr);
			m.append(")"); //$NON-NLS-1$

			task.subTask(m.toString());
		}
		lastWorked = cmp;
		task.worked(work);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jgit.lib.ProgressMonitor#endTask()
	 */
	@Override
	public void endTask() {
		if (task != null) {
			try {
				task.done();
			} finally {
				task = null;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jgit.lib.ProgressMonitor#isCancelled()
	 */
	@Override
	public boolean isCancelled() {
		if (task != null) {
			return task.isCanceled();
		}
		return root.isCanceled();
	}
}
