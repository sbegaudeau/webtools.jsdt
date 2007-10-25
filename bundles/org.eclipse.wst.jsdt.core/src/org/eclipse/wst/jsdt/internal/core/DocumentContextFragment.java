package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IJavaElement;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaModelException;

public class DocumentContextFragment extends PackageFragment{

	private String fileInScope;

	protected DocumentContextFragment(PackageFragmentRoot root, String names) {
		super(root, new String[] {names});
		this.names  =new String[] {names};
		fileInScope = names;
	}
//	protected DocumentContextFragment(PackageFragmentRoot root, IPath you, String[] names) {
//		super(root, new String[0]);
//		filesInScope = names;
//		this.names = names;
//		me = you;
//	}
//
//	public IPath resolveRelativePath(String path) {
//		IResource member = getRelativeAsResource(path);
//		if(member!=null) return member.getLocation();
//		return ((DocumentContextFragmentRoot)parent).resolveRelativePath(path);
//	}
//
//	public IResource getRelativeAsResource(String path) {
//
//		return ((DocumentContextFragmentRoot)parent).getRelativeAsResource(path);
//	}

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
		//for(int i = 0;i<filesInScope.length;i++) {
			//ClassFile classFile = new ClassFile(this,resolvePath(filesInScope[i]).toOSString());
//		CompilationUnit cu= new CompilationUnit(this, this.getPackageFragmentRoot().getPath().toOSString(), DefaultWorkingCopyOwner.PRIMARY);
			IJavaElement[] children= new IJavaElement[]{getJavaElement(fileInScope)};
			for(int k=0;k<children.length;k++) {

				info.addChild(children[k]);
			}
		//}
		return true;
	}

	public IJavaElement getJavaElement(String resource) {
		/* if resource exists in project, return compunit, else return class */
		//if(!DocumentContextFragmentRoot.RETURN_CU) return getClassFile(resource);
		IPath workspacePath = getPackageFragmentRoot().getJavaProject().getProject().getWorkspace().getRoot().getLocation();
		/* remove the file part of the path */
		IResource elementResource=null;
		if(getResource() instanceof IContainer) {
			elementResource = ((IContainer)getResource()).findMember(resource);
		}
		//if(true) return getClassFile(resource);

		if(elementResource!=null && elementResource.exists()) {
			try {
				//return createCompilationUnit(resource, null, true, new NullProgressMonitor());
				ICompilationUnit unit = getCompilationUnit(resource);
				//((CompilationUnit)unit).buildStructure(new CompilationUnitElementInfo(), new NullProgressMonitor(), new HashMap(), ((IContainer)getParent().getResource()).findMember(resource));
				//unit.makeConsistent(new NullProgressMonitor());
				//((JavaElement)unit).openWhenClosed(new CompilationUnitElementInfo(), new NullProgressMonitor());
				boolean unitExists = unit.exists();
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
//		IClassFile[] classFiles = new IClassFile[filesInScope.length];
//		for(int i = 0;i<filesInScope.length;i++) {
//			ClassFile classFile = new ClassFile(this,filesInScope[i]);
//			classFiles[i] = classFile;
//
//		}
//		return classFiles;
		return new IClassFile[] { new ClassFile(this,fileInScope) };
	}



	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragment#getKind()
	 */
	public int getKind() throws JavaModelException {
		if(hasSource()) return IPackageFragmentRoot.K_SOURCE;
		return super.getKind();
	}

	public boolean hasSource() {
		//if(DocumentContextFragmentRoot.RETURN_CU /*&& filesInScope.length>0*/) {
			IResource file = ((IContainer)parent.getResource()).findMember(fileInScope);
			if(file!=null && file.exists()) return true;
	//	}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.PackageFragment#getResource()
	 */
	public IResource getResource() {
			IPath resourcePath = new Path(fileInScope);
			return ((IContainer)parent.getResource()).findMember(resourcePath.removeLastSegments(1));

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
		return DEFAULT_PACKAGE_NAME;
	}

	public boolean isDefaultPackage() {
		return true;
	}



}
