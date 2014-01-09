public class X {
	void foo() {
		if (JavaModelManager.CP_RESOLVE_VERBOSE) {
			System.out
					.println("CPContainer SET  - setting container: [" + containerPath + "] for projects: {" //$NON-NLS-1$ //$NON-NLS-2$
							+ (org.eclipse.wst.jsdt.internal.compiler.util.Util
									.toString(
											affectedProjects,
											new org.eclipse.wst.jsdt.internal.compiler.util.Util.Displayable() {
												public String displayString(
														Object o) {
													return ((IJavaProject) o)
															.getElementName();
												}
											}))
							+ "} with values: " //$NON-NLS-1$
							+ (org.eclipse.wst.jsdt.internal.compiler.util.Util
									.toString(
											respectiveContainers,
											new org.eclipse.wst.jsdt.internal.compiler.util.Util.Displayable() {
												public String displayString(
														Object o) {
													return ((IJsGlobalScopeContainer) o)
															.getDescription();
												}
											})));
		}
	}
}