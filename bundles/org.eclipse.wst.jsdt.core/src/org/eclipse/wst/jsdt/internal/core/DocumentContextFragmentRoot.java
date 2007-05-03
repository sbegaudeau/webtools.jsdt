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
	
	private ArrayList filesInScope;
	private IFile fTargetScope;
	private LookupScopeElementInfo fLookupScope;
	
	

	public DocumentContextFragmentRoot(IJavaProject project,IFile targetScope) {
		super(getFileRootPath(project.getProject(),targetScope), (JavaProject)project);
		fTargetScope = targetScope  ;
		filesInScope = new ArrayList();
	}
	
	public String[] getRawImports() {
		return (String[])filesInScope.toArray(new String[filesInScope.size()]);
	}
	
	public void addFileToScope(String fileName) {
		if(fileName!=null && !fileName.equals("") && !filesInScope.contains(fileName) && isValidImport(fileName)) filesInScope.add(fileName);
	}
	
	public void removeFileInScope(String fileName) {
		if(fileName!=null && !filesInScope.contains(fileName)) filesInScope.remove(fileName);
	}
	
	public void setScope(String[] fileNames) {
		filesInScope.clear();
		for(int i = 0;i<fileNames.length;i++) {
			addFileToScope(fileNames[i]);
		}
	}

	public static IPath getFileWorkLocation(IProject targetProject, IFile targetFile) {
		IPath workingRoot = targetProject.getProject().getWorkingLocation(JavaCore.PLUGIN_ID);
		if(workingRoot!=null && !workingRoot.toFile().exists())
			workingRoot.toFile().mkdir();
		IPath destination = workingRoot.append(targetFile.getProjectRelativePath().toString());
		if(destination!=null && !destination.toFile().exists()) {
			destination.toFile().mkdirs();
			
		}
		
		return destination;
	}
	
	public static IPath getFileRootPath(IProject targetProject, IFile targetFile) {
		
		return getFileRootPath(targetProject,targetFile.getLocation());
	}
	
	public static IPath getFileRootPath(IProject targetProject, IPath targetPath) {
		IPath location;
		if(targetPath.toFile().isFile()) {
			location = targetPath.removeLastSegments(1);
		}else {
			location = targetPath;
		}
		return location;
	}
	protected Object createElementInfo() {
		return new LookupScopeElementInfo((JavaProject)getJavaProject(), new IPackageFragmentRoot[]{this});
	}
	
	public boolean isArchive() {
		return  false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.LibraryFragmentRoot#computeChildren(org.eclipse.wst.jsdt.internal.core.OpenableElementInfo, java.util.Map)
	 */
	protected boolean computeChildren(OpenableElementInfo info, Map newElements) throws JavaModelException {
		IJavaElement[] children = new IJavaElement[filesInScope.size()];
		for(int i = 0;i<filesInScope.size();i++) {
			DocumentContextFragment packFrag=  new DocumentContextFragment(this, new String[] {(String)filesInScope.get(i)});
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
		 * absolute paths: /scripts/file.js are relative to web context root, and must be made relative to this resource
		 * if the file does not exist in context root, the path is the absolute path on the filesystem.
		 */
		IPath childPath = new Path(childPathString);
		
		if(childPath.isAbsolute()) {
			return childPath;
		}
		IPath newPath = getPath().append(childPath);
		
		return newPath;
		
	}
	
	public IPath getPath() {
		return fTargetScope.getProjectRelativePath().removeLastSegments(1);
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
			return this.fTargetScope.equals(other.fTargetScope);
		}
		return false;
	}
	
	public String getElementName() {
		return this.fTargetScope.getName();
	}
	
	
	public int hashCode() {
		return this.fTargetScope.hashCode();
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
		IPath rootPath = getFileRootPath(getJavaProject().getProject(), fTargetScope);
		IPath rPath = getFileRootPath(getJavaProject().getProject(), underlyingResource.getLocation());
		
		if(rootPath.equals(rPath)) {
			return IPackageFragmentRoot.K_SOURCE;
		}
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
		 try {
			LookupScopeElementInfo cachedInfo;
			 
			JavaModelManager manager = JavaModelManager.getJavaModelManager();
			Object info = manager.getInfo(this);
			if (info != null) {
				cachedInfo = (LookupScopeElementInfo)info;
				String[] rawImports = cachedInfo.getRawImportsFromCache();
				String[] currentImports = getRawImports();
				if(stringArraysEqual(rawImports,currentImports )) {
					fLookupScope = cachedInfo;
				}else {
					fLookupScope = (LookupScopeElementInfo)openWhenClosed(createElementInfo(),new NullProgressMonitor());
				}
				
			}else {
				fLookupScope = (LookupScopeElementInfo)openWhenClosed(createElementInfo(),new NullProgressMonitor());
			}
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return fLookupScope;
	}
	
	public boolean isValidImport(String importName) {
		File file = resolveChildPath(importName).toFile();
		if(file.isFile()) {
			return true;
		}else {
			IFile resolved =  ((IContainer)getResource()).getFile(new Path(file.getPath()));
			boolean exists =  resolved.exists();
			return exists;
		}
	}
	
	public static boolean stringArraysEqual(String[] a1, String[] a2) {
		if(a1.length!=a2.length) return false;
		return Arrays.asList(a1).containsAll(Arrays.asList(a1));
	}
	protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource) throws JavaModelException {
		
//		// check whether this pkg fragment root can be opened
//		IStatus status = validateOnClasspath();
//		if (!status.isOK()) throw newJavaModelException(status);
//		if (!resourceExists()) throw newNotPresentException();

		((PackageFragmentRootInfo) info).setRootKind(determineKind(underlyingResource));
		return computeChildren(info, newElements);
	}
//	protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource) throws JavaModelException {
//	//	Hashtable myChildren = new Hashtable();
//		computeChildren(info,  newElements);
//	//	Enumeration children = myChildren.keys();
//	//	while(children.hasMoreElements()){
//		//	Object obj = children.nextElement();
////			if(obj instanceof Openable) {
////				((Openable)obj).buildStructure((OpenableElementInfo)myChildren.get(obj), pm, newElements, null);
////			}
//			//info.addChild((IJavaElement)obj);
//		//}
//		return true;
//	}
	
	public int getKind() throws JavaModelException {
		return IPackageFragmentRoot.K_BINARY;
	}
	
	public IResource getResource() {
		/* resource relative to the scope */
		//IPath myPath =fTargetScope.getProjectRelativePath().removeLastSegments(1);
		//IResource myResource = getJavaProject().getProject().findMember(myPath);
		
		return getJavaProject().getResource();
	}
}
