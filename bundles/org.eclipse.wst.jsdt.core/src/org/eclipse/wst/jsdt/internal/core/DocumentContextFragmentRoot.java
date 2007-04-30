package org.eclipse.wst.jsdt.internal.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
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
		//filesInScope.add(targetScope.getFullPath().toOSString());
	}
	
	public void addFileToScope(String fileName) {
		if(fileName!=null && !filesInScope.contains(fileName)) filesInScope.add(fileName);
	}
	
	public void addFilesToScope(String[] fileNames) {
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
		
		DocumentContextFragment packFrag=  new DocumentContextFragment(this, (String[])filesInScope.toArray(new String[filesInScope.size()]));
		LibraryPackageFragmentInfo fragInfo= new LibraryPackageFragmentInfo();
		packFrag.computeChildren(fragInfo);
		newElements.put(packFrag, fragInfo);
		IJavaElement[] children= new IJavaElement[]{packFrag};
		info.setChildren(children);
		return true;
	}
	
	public IPath resolveChildPath(String childPath) {
		
		IPath myPath = this.libraryPath;
		IPath newPath = myPath.append(childPath);
		IPath resolvedPath = newPath.makeAbsolute();
		return resolvedPath;
		
	}
	
	public IPath getPath() {
		return new Path("");
		//return getFileRootPath(getJavaProject().getProject(),fTargetScope.getFullPath());
	}
	
	public PackageFragment getPackageFragment(String[] filesInFragment) {
		return  new DocumentContextFragment(this, filesInFragment);
	}
	
	public PackageFragment getDefaultPackageFragment() {
		return  new DocumentContextFragment(this, (String[])filesInScope.toArray(new String[filesInScope.size()]));
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
		//return "";
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

				JavaModelManager manager = JavaModelManager.getJavaModelManager();
				Object info = manager.getInfo(this);
				if (info != null) return (LookupScopeElementInfo)info;
				return (LookupScopeElementInfo)openWhenClosed(createElementInfo(),new NullProgressMonitor());
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return null;
	}
	
	
	protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource) throws JavaModelException {
	
		//((PackageFragmentRootInfo) info).setRootKind(IPackageFragmentRoot.K_BINARY);
		return computeChildren(info, newElements);
	}
	
	public int getKind() throws JavaModelException {
		return IPackageFragmentRoot.K_BINARY;
	}
	
	public IResource getResource() {
		return (IResource)JavaModel.getTarget(ResourcesPlugin.getWorkspace().getRoot(), fTargetScope.getProjectRelativePath().removeLastSegments(1), false);
	}
}
