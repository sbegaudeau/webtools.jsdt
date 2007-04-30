package org.eclipse.wst.jsdt.internal.core;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IJavaElement;
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
	
	public IPath resolvePath(String path) {
		return ((DocumentContextFragmentRoot)parent).resolveChildPath(path);
	}
	
	protected boolean computeChildren(OpenableElementInfo info) {
		for(int i = 0;i<filesInScope.length;i++) {
			ClassFile classFile = new ClassFile(this,resolvePath(filesInScope[i]).toOSString());
//		CompilationUnit cu= new CompilationUnit(this, this.getPackageFragmentRoot().getPath().toOSString(), DefaultWorkingCopyOwner.PRIMARY);
			IJavaElement[] children= new IJavaElement[]{classFile};
			for(int k=0;k<children.length;k++) {
				info.addChild(children[k]);
			}
		}
		return true;
	}
	
	
	public IClassFile[] getClassFiles() throws JavaModelException {
		IClassFile[] classFiles = new IClassFile[filesInScope.length];
		for(int i = 0;i<filesInScope.length;i++) {
			ClassFile classFile = new ClassFile(this,resolvePath(filesInScope[i]).toOSString());
			classFiles[i] = classFile;
			
		}
		return classFiles;
	}
	
	public IClassFile getClassFile(String classFileName) {
		return new ClassFile(this,resolvePath(classFileName).toOSString());
	}
	
	public ICompilationUnit createCompilationUnit(String cuName, String contents, boolean force, IProgressMonitor monitor) throws JavaModelException {
		CreateCompilationUnitOperation op= new CreateCompilationUnitOperation(this, cuName, contents, force);
		op.runOperation(monitor);
		return new CompilationUnit(this, cuName, DefaultWorkingCopyOwner.PRIMARY);
	}
	public String getElementName() {
		//if (this.names.length == 0)
			return DEFAULT_PACKAGE_NAME;
		//return Util.concatWith(this.names, '/');
	}
	
	public boolean isDefaultPackage() {
		return true;
	}
}
