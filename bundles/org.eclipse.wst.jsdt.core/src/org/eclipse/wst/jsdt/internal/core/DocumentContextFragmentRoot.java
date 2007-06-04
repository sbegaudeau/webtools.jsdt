package org.eclipse.wst.jsdt.internal.core;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IJavaElement;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.ILookupScope;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.internal.core.ClassFile;
import org.eclipse.wst.jsdt.internal.core.JavaModel;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.jsdt.internal.core.LibraryFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.LibraryPackageFragment;
import org.eclipse.wst.jsdt.internal.core.LibraryPackageFragmentInfo;
import org.eclipse.wst.jsdt.internal.core.OpenableElementInfo;
import org.eclipse.wst.jsdt.internal.core.PackageFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.util.Util;


public class DocumentContextFragmentRoot extends LibraryFragmentRoot{
	
	private Object[] includedFiles;
	private Object[] timeStamps;
	
	private IFile fRelativeFile;
	
	private IResource absolutePath;
	private IPath webContext;
	
	public static final Boolean RETURN_CU = true;
	
	private boolean isDirty = true;

	public DocumentContextFragmentRoot(IJavaProject project,
									   IFile resourceRelativeFile,
									   IPath resourceAbsolutePath,
									   IPath webContext) {
		
		super(resourceAbsolutePath, (JavaProject)project);
		
		fRelativeFile = resourceRelativeFile ;
		this.includedFiles = timeStamps = new Object[0];
		this.absolutePath = ((IContainer)project.getResource()).findMember(resourceAbsolutePath);
		this.webContext=webContext;
		isDirty = true;
	}
	
	public DocumentContextFragmentRoot(IJavaProject project,
			   						   IFile resourceRelativeFile) {
		
			this(project,resourceRelativeFile, new Path(""), new Path(""));
	}
	
	public void removeFile(String fileName) {
		//includedFiles.remove(resolveChildPath(fileName));
	}
	
	public void setIncludedFiles(String[] fileNames) {
		Object[] includedFiles = new Object[fileNames.length];
		Object[] timeStamps = new Object[fileNames.length];
		int arrayLength = 0;
		for(int i = 0; i<fileNames.length;i++) {
			File importFile = isValidImport(fileNames[i]);
			if(importFile==null) continue;
			IPath importPath = resolveChildPath(fileNames[i]);	
			includedFiles[arrayLength] = importPath;
			timeStamps[arrayLength++] = new Long(importFile.lastModified());		
		}
		this.includedFiles = new Object[arrayLength];
		this.timeStamps = new Object[arrayLength];
		
		System.arraycopy(includedFiles, 0, this.includedFiles, 0, arrayLength);
		System.arraycopy(timeStamps, 0, this.timeStamps, 0, arrayLength);
		
		boolean localDirty = false;
		
		for(int i = 0;(!localDirty && i<includedFiles.length);i++) {
			IPath fileImport = (IPath)includedFiles[i];
			Long modified = (Long)timeStamps[i];
			localDirty = (modified != (new Long(fileImport.toFile().lastModified())));
		}
		
		isDirty = localDirty;
		
	}

	public IResource getRelativeAsResource(String path) {
		IPath absolute =    resolveChildPath(path);
		IResource member = ((IContainer)getResource()).findMember(absolute);
		
		return member;
	}
	
	protected Object createElementInfo() {
		return new LookupScopeElementInfo((JavaProject)getJavaProject(), new IPackageFragmentRoot[]{this});
	}
	
 
	
 
	public boolean isResourceContainer() {
 		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.LibraryFragmentRoot#computeChildren(org.eclipse.wst.jsdt.internal.core.OpenableElementInfo, java.util.Map)
	 */
	protected boolean computeChildren(OpenableElementInfo info, Map newElements) throws JavaModelException {
		
		//Enumeration files = includedFiles.keys();
//		IJavaElement[] children = new IJavaElement[includedFiles.size()];
//		for(int i=0; files.hasMoreElements(); i++ ) {
//			IPath fileImport = (IPath)files.nextElement();
//			String includeName = fileImport.toString();
//			DocumentContextFragment packFrag=  new DocumentContextFragment(this, new String[] {includeName});
//			LibraryPackageFragmentInfo fragInfo= new LibraryPackageFragmentInfo();
//			packFrag.computeChildren(fragInfo);
//			newElements.put(packFrag, fragInfo);
//			children[i] = packFrag;
//		}
		IJavaElement[] children = new IJavaElement[includedFiles.length];
		for(int i = 0;i<includedFiles.length;i++) {
			String includeName = ((IPath)includedFiles[i]).toString();
			DocumentContextFragment packFrag=  new DocumentContextFragment(this, new String[] {includeName});
			LibraryPackageFragmentInfo fragInfo= new LibraryPackageFragmentInfo();
			packFrag.computeChildren(fragInfo);
			newElements.put(packFrag, fragInfo);
			children[i] = packFrag;
		}
		info.setChildren(children);
		return true;
	}
	
	public IPath resolveChildPath(String childPathString) {
		/* relative paths:
		 * ./testfile.js  are relative to file scope
		 * absolute paths: /scripts/file.js are relative to absolutePath, and must be made relative to this resource
		 * if the file does not exist in context root, the path is the absolute path on the filesystem.
		 */
		if(childPathString==null) return null;
		if(childPathString.length()==0) return new Path("");
		IPath resolvedPath = null;
		IResource member;
		switch(childPathString.charAt(0)) {
			
			default:
				resolvedPath = new Path(childPathString);
			//if(resolvedPath.toFile()!=null && resolvedPath.toFile().exists()) break;
			
			member = ((IContainer)getResource()).findMember(resolvedPath);

			if(member!=null && member.exists()) break;
			case '/':
			case '\\':
				IPath childPath = new Path(childPathString);
				
				IPath newPath = childPath.removeFirstSegments(childPath.matchingFirstSegments(webContext));
				
				member = ((IContainer)getResource()).findMember(newPath);
				//if(member.exists()) return new Path(newPath);
				
				resolvedPath = newPath;
				break;				
			case '.':
				/* returns a new relative path thats relative to the resource */
				IPath relative = fRelativeFile.getProjectRelativePath().removeLastSegments(1);
				IPath relRes = getResource().getProjectRelativePath();
				if(relRes.isPrefixOf(relative)) {
					IPath amended = relative.removeFirstSegments(relRes.matchingFirstSegments(relative));
					resolvedPath = amended.append(childPathString);
				}
				break;		
			
			
		}
		
		return resolvedPath;
		
//		IPath childPath = new Path(childPathString);
//		
//		if(childPath.isAbsolute()) {
//			return childPath;
//		}
//		IPath newPath = getPath().append(childPath);
//		
//		return newPath;
		
	}
	
	public IPath getPath() {
		return fRelativeFile.getProjectRelativePath().removeLastSegments(1);
	}
	
	public PackageFragment getPackageFragment(String[] filesInFragment) {
		return new DocumentContextFragment(this, filesInFragment);
	}
	
	public PackageFragment getDefaultPackageFragment() {
		return  new DocumentContextFragment(this, (new String[0]));
	}
	
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof DocumentContextFragmentRoot) {
			DocumentContextFragmentRoot other= (DocumentContextFragmentRoot) o;
			return this.fRelativeFile.equals(other.fRelativeFile);
		}
		return false;
	}
	
	public String getElementName() {
		return this.fRelativeFile.getName();
	}
	
	
	public int hashCode() {
		return this.fRelativeFile.hashCode();
	}
	
	public boolean isExternal() {
		return false;
	}
	/**
	 * Jars and jar entries are all read only
	 */
	public boolean isReadOnly() {
		return true;
	}

	/**
 * Returns whether the corresponding resource or associated file exists
 */
	protected boolean resourceExists() {
		return true;
	}



	protected int determineKind(IResource underlyingResource) {
		if(getJavaProject().getProject().getLocation().isPrefixOf( underlyingResource.getLocation())) {
			return IPackageFragmentRoot.K_SOURCE;
		}else
			return IPackageFragmentRoot.K_BINARY;
	}

	public SearchableEnvironment newSearchableNameEnvironment(WorkingCopyOwner owner) throws JavaModelException {
		return new SearchableEnvironment((JavaProject)getJavaProject(),this, owner);
	}

	/*
	 * Returns a new name lookup. This name lookup first looks in the given working copies.
	 */
	public NameLookup newNameLookup(ICompilationUnit[] workingCopies) throws JavaModelException {
		return getElementInfo().newNameLookup( workingCopies);
	}

	/*
	 * Returns a new name lookup. This name lookup first looks in the working copies of the given owner.
	 */
	public NameLookup newNameLookup(WorkingCopyOwner owner) throws JavaModelException {
		
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		ICompilationUnit[] workingCopies = owner == null ? null : manager.getWorkingCopies(owner, true/*add primary WCs*/);
		return newNameLookup(workingCopies);
	}
	
	public LookupScopeElementInfo getElementInfo() {
		LookupScopeElementInfo fScopeElementInfo = null;
		
		try {
			LookupScopeElementInfo cachedInfo;
			 
			JavaModelManager manager = JavaModelManager.getJavaModelManager();
			Object info = manager.getInfo(this);
			if (info != null && !isDirty()) {
				fScopeElementInfo = (LookupScopeElementInfo)info;
//				cachedInfo = (LookupScopeElementInfo)info;
//				String[] rawImports = cachedInfo.getRawImportsFromCache();
//				String[] currentImports = getRawImports();
//				if(stringArraysEqual(rawImports,currentImports )) {
//					fScopeElementInfo = cachedInfo;
//				}else {
//					fScopeElementInfo = (LookupScopeElementInfo)openWhenClosed(createElementInfo(),new NullProgressMonitor());
//				}
//				
			}else {
				fScopeElementInfo = (LookupScopeElementInfo)openWhenClosed(createElementInfo(),new NullProgressMonitor());
			}
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		isDirty = false;
		return fScopeElementInfo;
	}
	
	public boolean isDirty() {
		return isDirty;
	}
	
	public File isValidImport(String importName) {
			
		File file = resolveChildPath(importName).toFile();
		if(file.isFile()) {
			return file;
		}else {
			IPath childPath = new Path(importName);
			IFile resolved =  ((IContainer)getResource()).getFile(new Path(file.getPath()));
			
			boolean exists =  resolved.exists();
			/* Special case for absolute paths specified with \ and / */
			if( importName.charAt(0)=='\\' || importName.charAt(0)=='/'){
				int seg = childPath.matchingFirstSegments(webContext); 
				exists = exists && (webContext!=new Path("") && seg >0);
			}
			if(exists) return new File(resolved.getFullPath().makeAbsolute().toString());
		}
		return null;
	}
	
	protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource) throws JavaModelException {

		((PackageFragmentRootInfo) info).setRootKind(determineKind(underlyingResource));
		boolean known =  computeChildren(info, newElements);
		if(isDirty()) JavaModelManager.getJavaModelManager().putInfos(this, newElements);
		return known;
	}

	
	public int getKind() throws JavaModelException {
		if(DocumentContextFragmentRoot.RETURN_CU)
			return IPackageFragmentRoot.K_SOURCE;
		else 
			return IPackageFragmentRoot.K_BINARY;
	}
	
	public IResource getResource() {
		return absolutePath;
	}
}
