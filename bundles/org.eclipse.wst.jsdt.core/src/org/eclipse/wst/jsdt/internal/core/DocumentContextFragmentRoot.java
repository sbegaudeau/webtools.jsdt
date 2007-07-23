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
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
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
import org.eclipse.wst.jsdt.internal.compiler.env.AccessRestriction;
import org.eclipse.wst.jsdt.internal.core.ClassFile;
import org.eclipse.wst.jsdt.internal.core.JavaModel;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.eclipse.wst.jsdt.internal.core.LibraryFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.LibraryPackageFragment;
import org.eclipse.wst.jsdt.internal.core.LibraryPackageFragmentInfo;
import org.eclipse.wst.jsdt.internal.core.OpenableElementInfo;
import org.eclipse.wst.jsdt.internal.core.PackageFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.search.IRestrictedAccessBindingRequestor;
import org.eclipse.wst.jsdt.internal.core.search.JavaSearchScope;
import org.eclipse.wst.jsdt.internal.core.util.Util;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;


public class DocumentContextFragmentRoot extends PackageFragmentRoot{
	
	private String[] includedFiles;
	//private Long[] timeStamps;
	private IFile fRelativeFile;
	private IResource absolutePath;
	private IPath webContext;
	private IClasspathEntry rawClassPathEntry;
	
	//public static final boolean RETURN_CU = true;
	private static final boolean DEBUG = false;
	//private boolean dirty;
	
	private static int instances=0;
	private ICompilationUnit[] workingCopies;
	private String[] fSystemFiles;
	private RestrictedDocumentBinding importPolice;
	
	class RestrictedDocumentBinding implements IRestrictedAccessBindingRequestor {
		
		private String foundPath;
		
		public void reset() {
			foundPath=null;
		}
		
		public boolean acceptBinding(int type,int modifiers, char[] packageName,char[] simpleTypeName, String path, AccessRestriction access) {
			
			
			for (int i = 0; workingCopies!=null && i < workingCopies.length; i++) {
				if (workingCopies[i].getPath().toString().equals(path)) {
					if(DEBUG) System.out.println("DocumentContextFragmentRoot ====>" +"REJECTING binding..\n\t" + new String(simpleTypeName) + " in " + path + "\n\tfor file " + fRelativeFile.toString());
					if(DEBUG) System.out.println("\tType is in WorkingCopies ");
					return false;
				}
			}
			
			for(int i = 0;i<includedFiles.length;i++) {
				if(Util.isSameResourceString(path, includedFiles[i])) {
					if(DEBUG) System.out.println("DocumentContextFragmentRoot ====>" + "Accepting binding.. " + new String(simpleTypeName) + " in " + path + "\n\tfor file " + fRelativeFile.toString());
					this.foundPath=path;
					return true;
				}
			}
			
			String systemFiles[] = getProjectSystemFiles();
			
			for(int i = 0;i<systemFiles.length;i++) {
				if(Util.isSameResourceString(path, systemFiles[i])) {
					if(DEBUG) System.out.println("DocumentContextFragmentRoot ====>" + "Accepting binding.. " + new String(simpleTypeName) + " in " + path + " \n\tfor file " + fRelativeFile.toString());
					this.foundPath=path;
					return true;
				}
			}
			if(DEBUG) System.out.println("DocumentContextFragmentRoot ====>" +"REJECTING binding..\n\t" + new String(simpleTypeName) + " in " + path + " \n\tfor file " + fRelativeFile.toString());
			if(DEBUG) System.out.println("\t(relative) page includes = : " );
			if(DEBUG) {
				for(int i = 0;i<includedFiles.length;i++) {
					System.out.println("\t\t" + includedFiles[i]);
				}
			}
			//this.foundPath=null;
			return false;
		}
		
		public String getFoundPath() {
			return foundPath;
		}
	} 
	
	public String[] getProjectSystemFiles() {
		
		if(fSystemFiles!=null) return fSystemFiles;
		
		IJavaProject javaProject = getJavaProject();
		int lastGood = 0;
		IPackageFragmentRoot[]  projectRoots = null;
		
		try {
			projectRoots = javaProject.getPackageFragmentRoots();
			for(int i =0;i<projectRoots.length;i++) {
				if(projectRoots[i].isLanguageRuntime()) {
					projectRoots[lastGood++]=projectRoots[i];
				}
			}
		} catch (JavaModelException ex) {
			projectRoots = new IPackageFragmentRoot[0];
		}
		
		fSystemFiles = new String[lastGood ]; 
		for(int i = 0;i<fSystemFiles.length;i++) {
			fSystemFiles[i] = projectRoots[i].getPath().toString().intern();
		}
		return fSystemFiles;
	}
	
	
	public void classpathChange() {
		fSystemFiles=null;
	}


	
	public DocumentContextFragmentRoot(IJavaProject project,
									   IFile resourceRelativeFile,
									   IPath resourceAbsolutePath,
									   IPath webContext, 
									   IClasspathEntry rawClassPath) {
		
		super(resourceRelativeFile, (JavaProject)project);
		
		fRelativeFile = resourceRelativeFile ;
	//	this.includedFiles = new IPath[0];
		//this.timeStamps = new Long[0];
		this.absolutePath = ((IContainer)project.getResource()).findMember(resourceAbsolutePath);
		this.webContext=webContext;
		this.rawClassPathEntry = rawClassPath;
		//dirty = true;
		if(DEBUG) System.out.println("DocumentContextFragmentRoot ====>" + "Creating instance for total of:>>" + ++instances + "<<.  \n\tRelative file:" + fRelativeFile.toString());
		
	
	}

	public void finalize() {
		
		if(DEBUG) System.out.println("DocumentContextFragmentRoot ====>" + "finalize() for a  total of:>>" + --instances + "<<.  \n\tRelative file:" + fRelativeFile!=null?null:fRelativeFile.toString());
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragmentRoot#getRawClasspathEntry()
	 */
	public IClasspathEntry getRawClasspathEntry() throws JavaModelException {
		if(rawClassPathEntry!=null) return rawClassPathEntry;
		return super.getRawClasspathEntry();
	}

	protected  RestrictedDocumentBinding getRestrictedAccessRequestor() {
		 if(importPolice==null) {
			 importPolice = new RestrictedDocumentBinding();
		 }
		importPolice.reset();
		return importPolice;
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
	
	
	public void setIncludedFiles(String[] fileNames) {
		
		this.includedFiles = new String[0];
		
		String[] newImports = new String[fileNames.length];
		//Long[] newTimestamps = new Long[fileNames.length];
		int arrayLength = 0;
		
		for(int i = 0; i<fileNames.length;i++) {
			File importFile = isValidImport(fileNames[i]);
			if(importFile==null) continue;
			IPath importPath = resolveChildPath(fileNames[i]);	
			newImports[arrayLength] = importPath.toString();
			//newTimestamps[arrayLength] = new Long(importFile.lastModified());	

			arrayLength++;
		}
		
//		boolean equals =   this.includedFiles!=null && (newImports !=null) &&
//		  				   this.includedFiles.length == newImports.length;
//		
//
//		if(!equals) dirty = true;
//		
//		/* try some more cases */
//		
//		for(int i = 0;!dirty && i<this.includedFiles.length;i++) {
//			if(!(this.includedFiles[i].equals(newImports[i]))) {
//				dirty = true;
//				
//			}
//		}
//		
//		for(int i = 0;!dirty && i<newTimestamps.length;i++) {
//			if(!(this.timeStamps[i].equals(newTimestamps[i]))) {
//				dirty = true;
//			}
//		}
//		
//		if(!dirty) return;
		
		this.includedFiles = new String[arrayLength];
	//	this.timeStamps = new Long[arrayLength];
		System.arraycopy(newImports, 0, this.includedFiles, 0, arrayLength);
	//	System.arraycopy(newTimestamps, 0, this.timeStamps, 0, arrayLength);
	
		
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
				IPath relative=null;
				try {
					relative = fRelativeFile.getFullPath().removeLastSegments(1);
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				IPath relRes = getResource().getFullPath();
				if(relRes.isPrefixOf(relative)) {
					IPath amended = relative.removeFirstSegments(relRes.matchingFirstSegments(relative));
					resolvedPath = amended.append(childPathString);
				}
				break;		
			
			
		}
		
		return resolvedPath;

	}
	
	public IPath getPath() {
		return fRelativeFile.getFullPath().removeLastSegments(1);
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
		
//		for(int i = 0;i<this.timeStamps.length;i++) {
//			if(!(this.timeStamps[i].equals( other.timeStamps[i]        )        )) return false;
//		}
			
		
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
		return false;
	}

	/**
 * Returns whether the corresponding resource or associated file exists
 */
	protected boolean resourceExists() {
		return true;
	}


	public SearchableEnvironment newSearchableNameEnvironment(WorkingCopyOwner owner) throws JavaModelException {
		/* previously restricted searchable environment to 'this'.  But that removes library entries from search results so going back to global project */
		SearchableEnvironment env =  super.newSearchableNameEnvironment(owner);//new SearchableEnvironment((JavaProject)getJavaProject(),this, owner);
		int includeMask = IJavaSearchScope.SOURCES | IJavaSearchScope.APPLICATION_LIBRARIES | IJavaSearchScope.SYSTEM_LIBRARIES | IJavaSearchScope.REFERENCED_PROJECTS;
		env.nameLookup.setRestrictedAccessRequestor(getRestrictedAccessRequestor());
		((JavaSearchScope)env.searchScope).add((JavaProject)getJavaProject(), includeMask, new HashSet(2));
		return env;
	}

	/*
	 * Returns a new name lookup. This name lookup first looks in the given working copies.
	 */
	public NameLookup newNameLookup(ICompilationUnit[] workingCopies) throws JavaModelException {
		this.workingCopies = workingCopies;
		NameLookup lookup = super.newNameLookup(this.workingCopies);
		lookup.setRestrictedAccessRequestor(getRestrictedAccessRequestor());
		return lookup;
		//return ((LookupScopeElementInfo)getElementInfo()).newNameLookup( workingCopies);
	}

	/*
	 * Returns a new name lookup. This name lookup first looks in the working copies of the given owner.
	 */
	public NameLookup newNameLookup(WorkingCopyOwner owner) throws JavaModelException {
		
		NameLookup lookup =  super.newNameLookup(owner);
		lookup.setRestrictedAccessRequestor(getRestrictedAccessRequestor());
		return lookup;
//		
//		JavaModelManager manager = JavaModelManager.getJavaModelManager();
//		ICompilationUnit[] workingCopies = owner == null ? null : manager.getWorkingCopies(owner, true/*add primary WCs*/);
//		return newNameLookup(workingCopies);
	}
	
	public File isValidImport(String importName) {		
		IPath filePath = resolveChildPath(importName);
		if(filePath==null) return null;
		File file=file = filePath.toFile();
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
	
	public int getKind() throws JavaModelException {
			return IPackageFragmentRoot.K_SOURCE;
	}
	
	
	public String toString() {
		StringBuffer me = new StringBuffer("Relative to: " + fRelativeFile.getName() + "\n");
		me.append("Absolute to: " + webContext + "\n");
		me.append("Included File\t\t\tLast Moddified\n");
		for(int i = 0;i<includedFiles.length;i++) {
			me.append(includedFiles[i] /*+ "\t\t\t\t" + timeStamps[i].longValue()*/ + "\n");
		}
		
		return me.toString();
	}

	public IResource getResource() {
		return absolutePath;
	}
	

}
