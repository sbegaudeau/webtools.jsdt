/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.search.matching;

import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemReporter;

class ImportMatchLocatorParser extends MatchLocatorParser {

protected ImportMatchLocatorParser(ProblemReporter problemReporter, MatchLocator locator) {
	super(problemReporter, locator);
}
protected void consumeSingleTypeImportDeclarationName() {
	super.consumeSingleTypeImportDeclarationName();
	this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
}
protected void consumeTypeImportOnDemandDeclarationName() {
	super.consumeTypeImportOnDemandDeclarationName();
	this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
}

}
