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
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IJavaElement;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.ILookupScope;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.core.search.IJavaSearchScope;
import org.eclipse.wst.jsdt.internal.core.ClassFile;
import org.eclipse.wst.jsdt.internal.core.JavaModel;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.jsdt.internal.core.LibraryFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.LibraryPackageFragment;
import org.eclipse.wst.jsdt.internal.core.LibraryPackageFragmentInfo;
import org.eclipse.wst.jsdt.internal.core.OpenableElementInfo;
import org.eclipse.wst.jsdt.internal.core.PackageFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.search.JavaSearchScope;
import org.eclipse.wst.jsdt.internal.core.util.Util;


public class DocumentContextFragmentRoot extends LibraryFragmentRoot{
	
	private IPath[] includedFiles;
	private Long[] timeStamps;
	private IFile fRelativeFile;
	private IResource absolutePath;
	private IPath webContext;
	private IClasspathEntry rawClassPathEntry;
	
	public static final boolean RETURN_CU = true;
	private static final boolean DEBUG = false;
	private boolean dirty;
	
	private static int instances=0;
	
	
	
	public DocumentContextFragmentRoot(IJavaProject project,
									   IFile resourceRelativeFile,
									   IPath resourceAbsolutePath,
									   IPath webContext, 
									   IClasspathEntry rawClassPath) {
		
		super(resourceAbsolutePath, (JavaProject)project);
		instances++;
		fRelativeFile = resourceRelativeFile ;
		this.includedFiles = new IPath[0];
		this.timeStamps = new Long[0];
		this.absolutePath = ((IContainer)project.getResource()).findMember(resourceAbsolutePath);
		this.webContext=webContext;
		this.rawClassPathEntry = rawClassPath;
		dirty = true;
		if(DEBUG) System.out.println("Creating instance of DocuContexFrag for total of:" + instances);
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public void finalize() {
		if(DEBUG) System.out.println("Destroying instance of DocuContexFrag for total of:" + instances);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragmentRoot#getRawClasspathEntry()
	 */
	public IClasspathEntry getRawClasspathEntry() throws JavaModelException {
		if(rawClassPathEntry!=null) return rawClassPathEntry;
		return super.getRawClasspathEntry();
	}



	public DocumentContextFragmentRoot(IJavaProject project,
			   IFile resourceRelativeFile,
			   IPath resourceAbsolutePath,
			   IPath webContext) {
		this(project,resourceRelativeFile,resourceAbsolutePath,webContext,null);
	}
	
	public DocumentContextFragmentRoot(IJavaProject project,
			   						   IFile resourceRelativeFile) {
		
			this(project,resourceRelativeFile, new Path(""), new Path(""));
	}
	
	public void removeFile(String fileName) {
		//includedFiles.remove(resolveChildPath(fileName));
	}
	
	public void setIncludedFiles(String[] fileNames) {
		
		IPath[] newImports = new IPath[fileNames.length];
		Long[] newTimestamps = new Long[fileNames.length];
		int arrayLength = 0;
		
		for(int i = 0; i<fileNames.length;i++) {
			File importFile = isValidImport(fileNames[i]);
			if(importFile==null) continue;
			IPath importPath = resolveChildPath(fileNames[i]);	
			newImports[arrayLength] = importPath;
			newTimestamps[arrayLength] = new Long(importFile.lastModified());	

			arrayLength++;
		}
		
		boolean equals =   this.includedFiles!=null && (newImports !=null) &&
		  				   this.includedFiles.length == newImports.length;
		

		if(!equals) dirty = true;
		
		/* try some more cases */
		
		for(int i = 0;!dirty && i<this.includedFiles.length;i++) {
			if(!(this.includedFiles[i].equals(newImports[i]))) {
				dirty = true;
				
			}
		}
		
		for(int i = 0;!dirty && i<newTimestamps.length;i++) {
			if(!(this.timeStamps[i].equals(newTimestamps[i]))) {
				dirty = true;
			}
		}
		
		if(!dirty) return;
		
		this.includedFiles = new IPath[arrayLength];
		this.timeStamps = new Long[arrayLength];
		System.arraycopy(newImports, 0, this.includedFiles, 0, arrayLength);
		System.arraycopy(newTimestamps, 0, this.timeStamps, 0, arrayLength);
	
		
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
			IPath includePath = (IPath)includedFiles[i];
			String includeName = includePath.toString();
			//String fileName = includePath.lastSegment();
			//IPath pathToFile = includePath.removeLastSegments(1);
			
			//.findMember(pathToFile);
		
			DocumentContextFragment packFrag=  new DocumentContextFragment(this,  includeName);
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
				if(member!=null && member.exists()) break;
							
			case '.':
				/* returns a new relative path thats relative to the resource */
				IPath relative = fRelativeFile.getFullPath().removeLastSegments(1);
				IPath relRes = getResource().getFullPath();
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
		return fRelativeFile.getFullPath().removeLastSegments(1);
	}
	
	public IPackageFragment getPackageFragment(String filesInFragment) {
		return new DocumentContextFragment(this, filesInFragment);
	}
	
	public PackageFragment getDefaultPackageFragment() {
		return  new DocumentContextFragment(this, "");
	}
	
	public boolean equals(Object o) {
//		if (this == o)
//			return true;
		if (!(o instanceof DocumentContextFragmentRoot)) return false;
		
		DocumentContextFragmentRoot other= (DocumentContextFragmentRoot) o;
		
		
		
		boolean equals = (this.fRelativeFile.equals(other.fRelativeFile)) && 
						  this.includedFiles!=null && (other.includedFiles !=null) &&
						  this.includedFiles.length == other.includedFiles.length;
		
		if(!equals) return equals;
		
		/* try some more cases */
		
		for(int i = 0;i<this.includedFiles.length;i++) {
			if(!(this.includedFiles[i].equals(other.includedFiles[i]))) return false;
		}
		
		for(int i = 0;i<this.timeStamps.length;i++) {
			if(!(this.timeStamps[i].equals( other.timeStamps[i]        )        )) return false;
		}
			
		
		return true;
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
		/* previously restricted searchable environment to 'this'.  But that removes library entries from search results so going back to global project */
		SearchableEnvironment env =  new SearchableEnvironment((JavaProject)getJavaProject(),this, owner);
		int includeMask = IJavaSearchScope.SOURCES | IJavaSearchScope.APPLICATION_LIBRARIES | IJavaSearchScope.SYSTEM_LIBRARIES | IJavaSearchScope.REFERENCED_PROJECTS;
		
		((JavaSearchScope)env.searchScope).add((JavaProject)getJavaProject(), includeMask, new HashSet(2));
		return env;
	}

	/*
	 * Returns a new name lookup. This name lookup first looks in the given working copies.
	 */
	public NameLookup newNameLookup(ICompilationUnit[] workingCopies) throws JavaModelException {
		return ((LookupScopeElementInfo)getElementInfo()).newNameLookup( workingCopies);
	}

	/*
	 * Returns a new name lookup. This name lookup first looks in the working copies of the given owner.
	 */
	public NameLookup newNameLookup(WorkingCopyOwner owner) throws JavaModelException {
		
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		ICompilationUnit[] workingCopies = owner == null ? null : manager.getWorkingCopies(owner, true/*add primary WCs*/);
		return newNameLookup(workingCopies);
	}
	
	public Object getElementInfo() {
		LookupScopeElementInfo fScopeElementInfo = null;
		
		try {
			LookupScopeElementInfo cachedInfo;
			 
			
			if (!isDirty()) {
				JavaModelManager manager = JavaModelManager.getJavaModelManager();
				fScopeElementInfo = (LookupScopeElementInfo)manager.getInfo(this);
				//fScopeElementInfo = (LookupScopeElementInfo)info;
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
				dirty = false;
			}
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return fScopeElementInfo;
	}
	
	public boolean isDirty() {
		if(dirty) {
			if(DEBUG) System.out.println("-----------------------------------DIRTY MODEL");
			return dirty;
		}
		
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		Object info = manager.getInfo(this);
		if(info==null) {
			if(DEBUG) System.out.println("-----------------------------------DIRTY MODEL");
			return true;
		}
		if(DEBUG) System.out.println("-------===CLEAN MODEL");
		return false;
//		LookupScopeElementInfo myInfo = ((LookupScopeElementInfo)info);
//		
//		/* should only be one */
//		IPackageFragmentRoot[] roots = myInfo.getAllRoots();
//		if(DEBUG) {
//			System.out.println("------------------------------ DIRTY CHECK -----------------------------\n");
//			System.out.println(this);
//			System.out.println("Comparing to others:");
//		}
//		
//		for(int i = 0;i<roots.length;i++) {
//			if(DEBUG && roots[i] instanceof DocumentContextFragmentRoot) {
//				System.out.println("---Start---------------------------");
//				System.out.println(roots[i]);
//				System.out.println("----End--------------------------");
//			}
//			if(sameScope(roots[i])) {
//				if(DEBUG) System.out.println("Returning NOT dirty "); 
//				return false;
//				
//				
//			}
//		}
//		
//		
//		return true;
		
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
				int seg  = resolved.getFullPath().matchingFirstSegments(webContext); 
				
				exists = exists && (webContext!=new Path("") && seg >0);
			}
			if(exists) return new File(resolved.getLocation().toString());
		}
		return null;
	}
	public boolean inScope(IPath importPath) {
		
		for(int i = 0;i<includedFiles.length;i++) {
			if(includedFiles[i].equals(importPath)) return true;
		}
		return false;
	}
	
	protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource) throws JavaModelException {

		((PackageFragmentRootInfo) info).setRootKind(determineKind(underlyingResource));
		boolean known =  computeChildren(info, newElements);
		if(isDirty()) JavaModelManager.getJavaModelManager().putInfos(this, newElements);
		dirty = false;
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

	public String toString() {
		StringBuffer me = new StringBuffer("Relative to: " + fRelativeFile.getName() + "\n");
		me.append("Absolute to: " + webContext + "\n");
		me.append("Included File\t\t\tLast Moddified\n");
		for(int i = 0;i<includedFiles.length;i++) {
			me.append(includedFiles[i] + "\t\t\t\t" + timeStamps[i].longValue() + "\n");
		}
		
		return me.toString();
	}
	
}
