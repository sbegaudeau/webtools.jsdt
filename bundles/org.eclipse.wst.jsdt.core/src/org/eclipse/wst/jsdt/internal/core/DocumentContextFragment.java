package org.eclipse.wst.jsdt.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IJavaElement;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.internal.core.PackageFragment;
import org.eclipse.wst.jsdt.internal.core.PackageFragmentRoot;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class DocumentContextFragment extends LibraryPackageFragment{
	
	private String[] filesInScope;

	protected DocumentContextFragment(PackageFragmentRoot root, String[] names) {
		super(root, new String[0]);
		filesInScope = names;
	}
	
	public IPath resolveRelativePath(String path) {
		IResource member = getRelativeAsResource(path);
		if(member!=null) return member.getLocation();
		return new Path(path).makeAbsolute();
	}
	
	public IResource getRelativeAsResource(String path) {
		IPath absolute = ((DocumentContextFragmentRoot)parent).resolveChildPath(path);
		IResource member = ((IContainer)parent.getResource()).findMember(absolute);
		
		return member;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragment#buildStructure(org.eclipse.wst.jsdt.internal.core.OpenableElementInfo, org.eclipse.core.runtime.IProgressMonitor, java.util.Map, org.eclipse.core.resources.IResource)
	 */
//	protected boolean buildStructure(OpenableElementInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource) throws JavaModelException {
//			IJavaElement[] children = info.getChildren();
//			for(int k = 0;k<children.length;k++) {
//				if(children[k] instanceof CompilationUnit) {
//					try {
//						CompilationUnitElementInfo compInfo = new CompilationUnitElementInfo();
//						IPath myPath = ((CompilationUnit)children[k]).getPath();
//						IContainer parent  = ((IContainer)getParent().getResource());
//						IResource me = parent.findMember(myPath);
//						//((CompilationUnit)children[k]).openWhenClosed(compInfo, pm);
//						//((CompilationUnit)children[k]).buildStructure(compInfo, pm, newElements, me);
//						
//					} catch (/*JavaModelException*/ Exception  ex) {
//						// TODO Auto-generated catch block
//						ex.printStackTrace();
//					}
//				}
//				info.addChild(children[k]);
//			}
//			return true;
//			
//	}

	protected boolean computeChildren(OpenableElementInfo info) {
		for(int i = 0;i<filesInScope.length;i++) {
			//ClassFile classFile = new ClassFile(this,resolvePath(filesInScope[i]).toOSString());
//		CompilationUnit cu= new CompilationUnit(this, this.getPackageFragmentRoot().getPath().toOSString(), DefaultWorkingCopyOwner.PRIMARY);
			IJavaElement[] children= new IJavaElement[]{getJavaElement(filesInScope[i])};
			for(int k=0;k<children.length;k++) {
				
				info.addChild(children[k]);
			}
		}
		return true;
	}
	
	public IJavaElement getJavaElement(String resource) {
		/* if resource exists in project, return compunit, else return class */
		if(true) return getClassFile(resolveRelativePath(resource).makeAbsolute().toOSString());
		IPath workspacePath = getPackageFragmentRoot().getJavaProject().getProject().getWorkspace().getRoot().getLocation();
		/* remove the file part of the path */
		IPath resourcePath = new Path(resource);
		//if(true) return getClassFile(resource);
		if(!resourcePath.isAbsolute() || workspacePath.isPrefixOf(resourcePath.removeLastSegments(1))) {
			try {
				//return createCompilationUnit(resource, null, true, new NullProgressMonitor());
				ICompilationUnit unit = getCompilationUnit(resource);
				((CompilationUnit)unit).buildStructure(new CompilationUnitElementInfo(), new NullProgressMonitor(), new HashMap(), ((IContainer)getParent().getResource()).findMember(resource));
				//unit.makeConsistent(new NullProgressMonitor());
				((JavaElement)unit).openWhenClosed(new CompilationUnitElementInfo(), new NullProgressMonitor());
				return unit;
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			return null;
		}else {
			return getClassFile(resource);
		}
		
	}
	
	
	public IClassFile[] getClassFiles() throws JavaModelException {
		IClassFile[] classFiles = new IClassFile[filesInScope.length];
		for(int i = 0;i<filesInScope.length;i++) {
			ClassFile classFile = new ClassFile(this,resolveRelativePath(filesInScope[i]).toOSString());
			classFiles[i] = classFile;
			
		}
		return classFiles;
	}
	


	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragment#getKind()
	 */
	public int getKind() throws JavaModelException {
		// TODO Auto-generated method stub
		System.out.println("Unimplemented method:DocumentContextFragment.getKind");
		return super.getKind();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragment#getResource()
	 */
	public IResource getResource() {
		if(filesInScope.length==0 ) return null;
		if(filesInScope.length!=1 )
			return parent.getResource();
		return getRelativeAsResource(new Path(filesInScope[0]).removeLastSegments(1).toString());
	}

	public IClassFile getClassFile(String classFileName) {
		return new ClassFile(this,classFileName);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragment#getCompilationUnit(java.lang.String)
	 */
	public ICompilationUnit getCompilationUnit(String cuName) {
		return  new CompilationUnit(this, cuName, DefaultWorkingCopyOwner.PRIMARY);	
	}

	public ICompilationUnit createCompilationUnit(String cuName, String contents, boolean force, IProgressMonitor monitor) throws JavaModelException {
		CreateCompilationUnitOperation op= new CreateCompilationUnitOperation(this, cuName, contents, force);
		op.runOperation(monitor);
		return new CompilationUnit(this, cuName, DefaultWorkingCopyOwner.PRIMARY);
	}
	
	public String getElementName() {
		if (this.names.length == 0)
			return DEFAULT_PACKAGE_NAME;
		
		return names[0];
		//return Util.concatWith(this.names, '/');
	}
	
	public boolean isDefaultPackage() {
		return true;
	}


	
}
