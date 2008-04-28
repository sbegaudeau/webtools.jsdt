package org.eclipse.wst.jsdt.internal.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.CompletionRequestor;
import org.eclipse.wst.jsdt.core.IBuffer;
import org.eclipse.wst.jsdt.core.IBufferFactory;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.ICodeCompletionRequestor;
import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.ICompletionRequestor;
import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IJavaElement;
import org.eclipse.wst.jsdt.core.IMethod;
import org.eclipse.wst.jsdt.core.IProblemRequestor;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.internal.core.util.MementoTokenizer;
import org.eclipse.wst.jsdt.internal.core.util.Util;
import org.eclipse.wst.jsdt.internal.oaametadata.IOAAMetaDataConstants;
import org.eclipse.wst.jsdt.internal.oaametadata.LibraryAPIs;
import org.eclipse.wst.jsdt.internal.oaametadata.MetadataReader;
import org.eclipse.wst.jsdt.internal.oaametadata.MetadataSourceElementNotifier;
import org.xml.sax.InputSource;

public class MetadataFile extends Openable implements 
	IClassFile,	org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit, IVirtualParent
	{
	
	protected String name;
	protected IPath filePath;
	private static final IField[] NO_FIELDS = new IField[0];
	private static final IMethod[] NO_METHODS = new IMethod[0];
	private LibraryAPIs apis=null;
	
	
	protected MetadataFile(PackageFragment parent, String path) {
		super(parent);
		this.filePath = Path.fromOSString(path);
		if (filePath.getFileExtension()!=null)
		{
			String lastSegment = filePath.lastSegment();
			this.name=lastSegment.substring(0,lastSegment.length()-(filePath.getFileExtension().length()+1));
		}
		else
			this.name=path;
	}

	protected boolean buildStructure(OpenableElementInfo info,
			IProgressMonitor pm, Map newElements, IResource underlyingResource)
			throws JavaModelException {
		if (underlyingResource != null && !underlyingResource.isAccessible()) {
			throw newNotPresentException();
		}



		CompilationUnitElementInfo unitInfo = new CompilationUnitElementInfo();

		// get buffer contents

		// generate structure and compute syntax problems if needed
		CompilationUnitStructureRequestor requestor = new CompilationUnitStructureRequestor(this, unitInfo, newElements);

		boolean createAST;
//		HashMap problems;
		if (info instanceof ASTHolderCUInfo) {
			ASTHolderCUInfo astHolder = (ASTHolderCUInfo) info;
			createAST = astHolder.astLevel != ICompilationUnit.NO_AST;
//			problems = astHolder.problems;
		} else {
			createAST = false;
//			problems = null;
		}


			new MetadataSourceElementNotifier(getAPIs(),requestor).notifyRequestor();

 

		// update timestamp (might be IResource.NULL_STAMP if original does not exist)
		if (underlyingResource == null) {
			underlyingResource = getResource();
		}
		// underlying resource is null in the case of a working copy on a class file in a jar
		if (underlyingResource != null)
			unitInfo.timestamp = ((IFile)underlyingResource).getModificationStamp();

		// compute other problems if needed
//		CompilationUnitDeclaration compilationUnitDeclaration = null;
		info.setChildren(unitInfo.children);
		try {

			if (createAST) {
//				int astLevel = ((ASTHolderCUInfo) info).astLevel;
//				org.eclipse.wst.jsdt.core.dom.CompilationUnit cu = AST.convertCompilationUnit(astLevel, unit, contents, options, computeProblems, this, pm);
//				((ASTHolderCUInfo) info).ast = cu;
				throw new RuntimeException("Implement this"); //$NON-NLS-1$
			}
		} finally {

		}
		
		return true;

	}
	
	public LibraryAPIs getAPIs() 
	{
		if (apis==null)
		{
			IFile file = (IFile) getResource();
				try {
					apis = MetadataReader.readAPIsFromStream(new InputSource(file.getContents()));
					apis.fileName=file.getFullPath().toPortableString();
				} catch (CoreException e) {
					Util.log(e, "error reading metadata");
					apis=new LibraryAPIs();
				}
		}
		return apis;
	}

	public IJavaElement getHandleFromMemento(String token,
			MementoTokenizer memento, WorkingCopyOwner owner) {
		switch (token.charAt(0)) {
		case JEM_TYPE:
			if (!memento.hasMoreTokens()) return this;
			String typeName = memento.nextToken();
			JavaElement type = new BinaryType(this, typeName);
			return type.getHandleFromMemento(memento, owner);
	}
	return null;
	}

	protected char getHandleMementoDelimiter() {
		return JavaElement.JEM_METADATA;

	}

	public ICompilationUnit becomeWorkingCopy(
			IProblemRequestor problemRequestor, WorkingCopyOwner owner,
			IProgressMonitor monitor) throws JavaModelException {
		return null;
	}

	public byte[] getBytes() throws JavaModelException {
		IFile file = (IFile) getResource();
		return Util.getResourceContentsAsByteArray(file);
	}

	public IType getType() {
		return null;
	}

	public IType[] getTypes() throws JavaModelException {
		ArrayList list = getChildrenOfType(TYPE);
		IType[] array= new IType[list.size()];
		list.toArray(array);
		return array;
	}

	public IJavaElement getWorkingCopy(IProgressMonitor monitor,
			IBufferFactory factory) throws JavaModelException {
		return null;
	}

	public boolean isClass() throws JavaModelException {
		return true;
	}

	public boolean isInterface() throws JavaModelException {
		return false;
	}

	public IType findPrimaryType() {
		return null;
	}

	public IJavaElement getElementAt(int position) throws JavaModelException {
		return null;
	}

	public ICompilationUnit getWorkingCopy(WorkingCopyOwner owner,
			IProgressMonitor monitor) throws JavaModelException {
		return null;
	}

	public int getElementType() {
		return CLASS_FILE;
	}

	public IPath getPath() {
		return this.filePath;
	}

	public IResource getResource() {
		return ((IContainer)this.getParent().getResource()).getFile(new Path(this.getElementName()));
	}

	public String getSource() throws JavaModelException {
		IBuffer buffer = super.getBuffer();
		if (buffer == null) {
			return null;
		}
		return buffer.getContents();
	}

	public ISourceRange getSourceRange() throws JavaModelException {
		return null;
	}

	public void codeComplete(int offset, ICodeCompletionRequestor requestor)
			throws JavaModelException {

	}

	public void codeComplete(int offset, ICompletionRequestor requestor)
			throws JavaModelException {

	}

	public void codeComplete(int offset, CompletionRequestor requestor)
			throws JavaModelException {

	}

	public void codeComplete(int offset, ICompletionRequestor requestor,
			WorkingCopyOwner owner) throws JavaModelException {

	}

	public void codeComplete(int offset, CompletionRequestor requestor,
			WorkingCopyOwner owner) throws JavaModelException {

	}

	public IJavaElement[] codeSelect(int offset, int length)
			throws JavaModelException {
		return null;
	}

	public IJavaElement[] codeSelect(int offset, int length,
			WorkingCopyOwner owner) throws JavaModelException {
		return null;
	}

	public IField getField(String fieldName) {
		return new SourceField(this, fieldName);
	}

	public IField[] getFields() throws JavaModelException {
		ArrayList list = getChildrenOfType(FIELD);
		int size;
		if ((size = list.size()) == 0) {
			return NO_FIELDS;
		} else {
			IField[] array= new IField[size];
			list.toArray(array);
			return array;
		}
	}

	public IMethod getMethod(String selector, String[] parameterTypeSignatures) {
		return new SourceMethod(this, selector, parameterTypeSignatures);
	}

	public IMethod[] getMethods() throws JavaModelException {
		ArrayList list = getChildrenOfType(METHOD);
		int size;
		if ((size = list.size()) == 0) {
			return NO_METHODS;
		} else {
			IMethod[] array= new IMethod[size];
			list.toArray(array);
			return array;
		}
	}

	public IType getType(String typeName) {
		return new SourceType(this, typeName);
	}

	public char[] getContents() {
		char [] chars=null;
		try {
			chars=org.eclipse.wst.jsdt.internal.compiler.util.Util.getFileCharContent(new File(filePath.toOSString()),null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return chars;
	}

	public String getInferenceID() {
		return null;
	}

	public char[] getMainTypeName() {
		return null;
	}

	public char[][] getPackageName() {
		return new char[][] {getParent().getElementName().toCharArray()};
	}

	public char[] getFileName() {
		return this.filePath!=null?this.filePath.toString().toCharArray():getElementName().toCharArray();
	}

	public JsGlobalScopeContainerInitializer getContainerInitializer() {
		JsGlobalScopeContainerInitializer init = ((IVirtualParent)parent).getContainerInitializer();
		return init;
	}

	public String getElementName() {
		return IOAAMetaDataConstants.METADATA_FILE;
	}

}
