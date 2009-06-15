/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.Map;

import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;

public class AssignmentTest extends AbstractRegressionTest {

	public AssignmentTest(String name) {
		super(name);
	}

	protected Map getCompilerOptions() {
		Map options = super.getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportNullReference,
				CompilerOptions.ERROR);
		options.put(CompilerOptions.OPTION_ReportNoEffectAssignment,
				CompilerOptions.ERROR);
		return options;
	}

	/*
	 * no effect assignment bug
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=27235
	 */
	public void test001() {
		this.runConformTest(
				new String[] {
						"X.js",
						"    var i;	\n" + "    function X(j) {	\n"
								+ "    	i = j;	\n" + "    }	\n"
								+ "       function B() {	\n"
								+ "            this.i =this.i;	\n"
								+ "        }	\n"
								+ "      function main( args) {	\n"
								+ "        X a = new X(3);	\n"
								+ "        print(a.i + \" \");	\n"
								+ "        print(a.new B().i);	\n" + "	}	\n"
								+ "}	\n", }, "3 3");
	}

	public void test002() {
		this
				.runNegativeTest(
						new String[] {
								"X.js",
								"	var a;	\n"
										+ "	var next;	\n"
										+ "	 function foo( arg){	\n"
										+ "	\n"
										+ "		zork = zork;	\n"
										+ "		arg = zork;	\n"
										+ "	\n"
										+ "		arg = arg;  // noop	\n"
										+ "		a = a;  // noop	\n"
										+ "		this.next = this.next; // noop	\n"
										+ "		this.next = next; // noop	\n"
										+ "	\n"
										+ "		next.a = next.a; // could raise NPE	\n"
										+ "		this.next.next.a = next.next.a; // could raise NPE	\n"
										+ "		a = next.a; // could raise NPE	\n"
										+ "		this. a = next.a; 	\n" + "	}	\n"
										+ "\n", },
						"----------\n"
								+ "2. ERROR in X.js (at line 5)\n"
								+ "	zork = zork;	\n"
								+ "	       ^^^^\n"
								+ "zork cannot be resolved\n"
								+ "----------\n"
								+ "3. ERROR in X.js (at line 6)\n"
								+ "	arg = zork;	\n"
								+ "	      ^^^^\n"
								+ "zork cannot be resolved\n"
								+ "----------\n"
								+ "4. ERROR in X.js (at line 8)\n"
								+ "	arg = arg;  // noop	\n"
								+ "	^^^^^^^^^\n"
								+ "The assignment to variable arg has no effect\n"
								+ "----------\n"
								+ "5. ERROR in X.js (at line 9)\n"
								+ "	a = a;  // noop	\n"
								+ "	^^^^^\n"
								+ "The assignment to variable a has no effect\n"
								+ "----------\n"
								+ "6. ERROR in X.js (at line 10)\n"
								+ "	this.next = this.next; // noop	\n"
								+ "	^^^^^^^^^^^^^^^^^^^^^\n"
								+ "The assignment to variable next has no effect\n"
								+ "----------\n"
								+ "7. ERROR in X.js (at line 11)\n"
								+ "	this.next = next; // noop	\n"
								+ "	^^^^^^^^^^^^^^^^\n"
								+ "The assignment to variable next has no effect\n"
								+ "----------\n");
	}

	// // final multiple assignment
	// public void test020() {
	// this.runNegativeTest(
	// new String[] {
	// "X.js",
	// "	function foo() {\n" +
	// "		 var v;\n" +
	// "		for (var i = 0; i < 10; i++) {\n" +
	// "			v = i;\n" +
	// "		}\n" +
	// "		v = 0;\n" +
	// "	}\n" +
	// "\n",
	// },
	// "----------\n" +
	// "1. ERROR in X.js (at line 4)\n" +
	// "	v = i;\n" +
	// "	^\n" +
	// "The final local variable v may already have been assigned\n" +
	// "----------\n" +
	// "2. ERROR in X.js (at line 6)\n" +
	// "	v = 0;\n" +
	// "	^\n" +
	// "The final local variable v may already have been assigned\n" +
	// "----------\n");
	// }

	// null part has been repeated into NullReferenceTest#test1033
	public void test033() {
		this
				.runNegativeTest(
						new String[] {
								"X.js",
								"	\n" + "	function foo() {\n" + "		var a,b;\n"
										+ "		do{\n" + "		   a=\"Hello \";\n"
										+ "		}while(a!=null);\n" + "				\n"
										+ "		if(a!=null)\n" + "		{\n"
										+ "		   b=\"World!\";\n" + "		}\n"
										+ "		println(a+b);\n" + "	}\n" + "\n", },
						"----------\n"
								+ "1. ERROR in X.js (at line 6)\n"
								+ "	}while(a!=null);\n"
								+ "	       ^\n"
								+ "The variable a cannot be null; it was either set to a non-null value or assumed to be non-null when last used\n"
								+ "----------\n"
								+ "2. ERROR in X.js (at line 8)\n"
								+ "	if(a!=null)\n"
								+ "	   ^\n"
								+ "The variable a can only be null; it was either set to null or checked for null when last used\n"
								+ "----------\n"
								+ "3. ERROR in X.js (at line 12)\n"
								+ "	println(a+b);\n"
								+ "	                     ^\n"
								+ "The local variable b may not have been initialized\n"
								+ "----------\n");
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=84215
	// TODO (philippe) should move to InitializationTest suite
	public void test034() {
		this.runConformTest(new String[] {
				"X.js",
				"public final class X \n" + "{\n" + "	  var vdg;\n"
						+ "	   var aa = null;\n" + "	   var a = 14;\n"
						+ "	   var b = 3;\n" + "	   var c = 12;\n"
						+ "	   var d = 2; \n" + "	   var e = 3; \n"
						+ "	   var f = 34; \n" + "	   var g = 35; \n"
						+ "	   var h = 36; \n" + "	   var j = 4;\n"
						+ "	   var k = 1;\n" + "	   var aba = 1;\n"
						+ "	   var as = 11;\n" + "	   var ad = 12;\n"
						+ "	   var af = 13;\n" + "	   var ag = 2;\n"
						+ "	   var ah = 21;\n" + "	   var aj = 22;\n"
						+ "	   var ak = 3;\n" + "	   var aaad = null;\n"
						+ "	   var aaaf = 1;\n" + "	   var aaag = 2;\n"
						+ "	   var aaha = 2;\n" + "	 var cxvvb = 1;\n"
						+ "	 var z = a;\n" + "	var asdff;\n" + "	  var ppfp;\n"
						+ "	  var ppfpged;\n" + "	boolean asfadf;\n"
						+ "	boolean cbxbx;\n" + "	  long tyt, rrky;\n"
						+ "	  var dgjt, ykjr6y;\n" + "	   var krykr = 1;\n"
						+ "	  var rykr5;\n" + "	  var dhfg;\n"
						+ "	  var dthj;\n" + "	  var fkffy;\n"
						+ "	  var fhfy;\n" + "	  var fhmf;\n"
						+ "	 var ryur6;\n" + "	 var dhdthd;\n"
						+ "	 var dth5;\n" + "	 var kfyk;\n" + "	 var ntd;\n"
						+ "	 var asdasdads;\n" + "	   var dntdr = 7;\n"
						+ "	   var asys = 1;\n" + "	   var djd5rwas = 11;\n"
						+ "	   var dhds45rjd = 12;\n"
						+ "	   var srws4jd = 13;\n" + "	   var s4ts = 2;\n"
						+ "	   var dshes4 = 21;\n"
						+ "	   var drthed56u = 22;\n"
						+ "	   var drtye45 = 23;\n" + "	   var xxbxrb = 3;\n"
						+ "	   var xfbxr = 31;\n" + "	   var asgw4y = 32;\n"
						+ "	   var hdtrhs5r = 33;\n" + "	   var dshsh = 34;\n"
						+ "	   var ds45yuwsuy = 4;\n"
						+ "	   var astgs45rys = 5;\n" + "	   var srgs4y = 6;\n"
						+ "	   var srgsryw45 = -6;\n"
						+ "	   var srgdtgjd45ry = -7;\n"
						+ "	   var srdjs43t = 1;\n"
						+ "	   var sedteued5y = 2;\n" + "	  var jrfd6u;\n"
						+ "	  var udf56u;\n" + "	 var jf6tu;\n"
						+ "	 var jf6tud;\n" + "	var bsrh;\n" + "	 X(var a)\n"
						+ "	{\n" + "	}\n" + "	 long sfhdsrhs;\n"
						+ "	 boolean qaafasdfs;\n" + "	 var sdgsa;\n"
						+ "	 long dgse4;\n" + "	long sgrdsrg;\n"
						+ "	 function gdsthsr()\n" + "	{\n" + "	}\n"
						+ "	 var hsrhs;\n" + "	 function hsrhsdsh()\n" + "	{\n"
						+ "	}\n" + "	 var dsfhshsr;\n"
						+ "	 function sfhsh4rsrh()\n" + "	{\n" + "	}\n"
						+ "	 function shsrhsh()\n" + "	{\n" + "	}\n"
						+ "	 function sfhstuje56u()\n" + "	{\n" + "	}\n"
						+ "	 function dhdrt6u()\n" + "	{\n" + "	}\n"
						+ "	 function hdtue56u()\n" + "	{\n" + "	}\n"
						+ "	 function htdws4()\n" + "	{\n" + "	}\n"
						+ "	var mfmgf;\n" + "	var mgdmd;\n" + "	var mdsrh;\n"
						+ "	var nmdr;\n" + "	 function oyioyio()\n" + "	{\n"
						+ "	}\n" + "	  long oyioyreye()\n" + "	{\n"
						+ "		return 0;\n" + "	}\n" + "	  long etueierh()\n"
						+ "	{\n" + "		return 0;\n" + "	}\n"
						+ "	  function sdfgsgs()\n" + "	{\n" + "	}\n"
						+ "	  function fhsrhsrh()\n" + "	{\n" + "	}\n" + "\n"
						+ "	long dcggsdg;\n" + "	var ssssssgsfh;\n"
						+ "	long ssssssgae;\n" + "	long ssssssfaseg;\n"
						+ "	 function zzzdged()\n" + "	{\n" + "	}\n" + "	\n"
						+ "	var t;\n" + "	 function xxxxxcbsg()\n" + "	{\n"
						+ "	}\n" + "\n" + "	\n" + "	 function vdg()\n" + "	{\n"
						+ "	}\n" + "	\n" + "	 int[] fffcvffffffasdfaef;\n"
						+ "	 int[] fffcffffffasdfaef;\n"
						+ "	 long[] ffcvfffffffasdfaef;\n"
						+ "	 var fffffghffffasdfaef; \n"
						+ "	 var fffffdffffasdfaef; \n"
						+ "	 var ffafffffffasdfaef;\n" + "	\n"
						+ "	 function fffffffffasdfaef()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 boolean aaaadgasrg;\n"
						+ "	 function ddddgaergnj()\n" + "	{\n" + "	}\n" + "\n"
						+ "	 function aaaadgaeg()\n" + "	{\n" + "	}\n" + "	\n"
						+ "	 function aaaaaaefadfgh()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 function addddddddafge()\n" + "	{\n"
						+ "	}\n" + "	\n" + "	 boolean aaaaaaaefae;\n"
						+ "	 function aaaaaaefaef()\n" + "	{\n" + "	}\n" + "\n"
						+ "	 function ggggseae()\n" + "	{\n" + "	}\n" + "\n"
						+ "	  function ggggggsgsrg()\n" + "	{\n" + "	}\n"
						+ "\n" + "	  synchronized function ggggggfsfgsr()\n"
						+ "	{\n" + "	}\n" + "\n" + "	 function aaaaaadgaeg()\n"
						+ "	{\n" + "	}\n" + "	\n"
						+ "	 function aaaaadgaerg()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 function bbbbbbsfryghs()\n" + "	{\n"
						+ "	}\n" + "	\n" + "	 function bfbbbbbbfssreg()\n"
						+ "	{\n" + "	}\n" + "\n" + "	 function bbbbbbfssfb()\n"
						+ "	{\n" + "	}\n" + "\n" + "	 function bbbbbbfssb()\n"
						+ "	{\n" + "	}\n" + "\n" + "	 function bbbbfdssb()\n"
						+ "	{\n" + "	}\n" + "	\n" + "	boolean dggggggdsg;\n"
						+ "\n" + "	 function hdfhdr()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 function dhdrtdrs()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 function dghdthtdhd()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 function dhdhdtdh()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 function fddhdsh()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 boolean sdffgsdg()\n" + "	{\n"
						+ "		return true;\n" + "	}\n" + "			\n"
						+ "	  boolean sdgsdg()\n" + "	{\n"
						+ "		return false;\n" + "	}\n" + "	\n"
						+ "	   function sfdgsg()\n" + "	{\n" + "	}\n" + "\n"
						+ "	 int[] fghtys;\n" + "\n" + "	   var sdsst = 1;\n"
						+ "	  X asdfahnr;\n"
						+ "	  var ssdsdbrtyrtdfhd, ssdsrtyrdbdfhd;\n"
						+ "	  var ssdsrtydbdfhd, ssdsrtydffbdfhd;\n"
						+ "	  var ssdrtyhrtysdbdfhd, ssyeghdsdbdfhd;\n"
						+ "	  var ssdsdrtybdfhd, ssdsdehebdfhd;\n"
						+ "	  var ssdthrtsdbdfhd, ssdshethetdbdfhd;\n"
						+ "	  var sstrdrfhdsdbdfhd;\n"
						+ "	  var ssdsdbdfhd, ssdsdethbdfhd;\n"
						+ "	  long ssdshdfhchddbdfhd;\n"
						+ "	  long ssdsdvbbdfhd;\n" + "	\n" + "	\n"
						+ "	  long ssdsdbdfhd()\n" + "	{\n" + "		return 0;\n"
						+ "	}\n" + "\n" + "	  long sdgsrsbsf()\n" + "	{\n"
						+ "		return 0;\n" + "	}\n" + "\n"
						+ "	  function sfgsfgssghr()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	  var sgsgsrg()\n" + "	{\n"
						+ "		return null;\n" + "	}\n" + "\n"
						+ "	  function sdgshsdygra()\n" + "	{\n" + "	}\n"
						+ "\n" + "	  var sdfsdfs()\n" + "	{\n"
						+ "		return null;\n" + "	}\n" + "\n"
						+ "	 boolean ryweyer;\n" + "\n"
						+ "	  function adfadfaghsfh()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	  function ghasghasrg()\n" + "	{\n"
						+ "	}\n" + "\n" + "	  function aadfadfaf()\n" + "	{\n"
						+ "	}\n" + "\n" + "	  function aadfadf()\n" + "	{\n"
						+ "	}\n" + "	\n" + "	  var fgsfhwr()\n" + "	{\n"
						+ "		return 0;\n" + "	}\n" + "\n"
						+ "	  var gdfgfgrfg()\n" + "	{\n" + "		return 0;\n"
						+ "	}\n" + "\n" + "	  var asdfsfs()\n" + "	{\n"
						+ "		return 0;\n" + "	}\n" + "\n" + "	  var sdgs;\n"
						+ "	  var sdfsh4e;\n" + "	   var gsregs = 0;\n" + "	\n"
						+ "	  var sgsgsd()\n" + "	{\n" + "		return null;\n"
						+ "	}\n" + "\n"
						+ "	 byte[] sdhqtgwsrh(String rsName, var id)\n"
						+ "	{\n" + "		var rs = null;\n" + "		try\n" + "		{\n"
						+ "			rs = \"\";\n" + "			return null;\n" + "		}\n"
						+ "		catch (Exception ex)\n" + "		{\n" + "		}\n"
						+ "		finally\n" + "		{\n" + "			if (rs != null)\n"
						+ "			{\n" + "				try\n" + "				{\n"
						+ "					rs.toString();\n" + "				}\n"
						+ "				catch (Exception ex)\n" + "				{\n" + "				}\n"
						+ "			}\n" + "		}\n" + "		return null;\n" + "	}\n"
						+ "\n" + "	 function dgagadga()\n" + "	{\n" + "	}\n"
						+ "	\n" + "	 var adsyasta;\n" + "}\n", }, "");
	}

	/*
	 * Check scenario: i = i++
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=84480 disabled:
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=111898
	 */
	public void test035() {
		this.runNegativeTest(new String[] {
				"X.js",
				"	var f;\n" + "	function foo( i) {\n" + "		i = i++;\n"
						+ "		i = ++i;\n" + "		f = f++;\n" + "		f = ++f;\n"
						+ "		var z;" + "	}\n" + "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 4)\n" + "	i = ++i;\n"
				+ "	^^^^^^^\n" + "The assignment to variable i has no effect\n"
				+ "----------\n" + "2. ERROR in X.js (at line 6)\n"
				+ "	f = ++f;\n" + "	^^^^^^^\n"
				+ "The assignment to variable f has no effect\n"
				+ "----------\n");
	}

	public void test036() {
		this
				.runNegativeTest(
						new String[] {
								"X.js",
								"\n" + "	function foo() {\n"
										+ "		var o = new Object();\n"
										+ "		do {\n" + "			o = null;\n"
										+ "		} while (o != null);\n"
										+ "		if (o == null) {\n"
										+ "			// throw new Exception();\n"
										+ "		}\n" + "	}\n" + "\n", },
						"----------\n"
								+ "1. ERROR in X.js (at line 6)\n"
								+ "	} while (o != null);\n"
								+ "	         ^\n"
								+ "The variable o can only be null; it was either set to null or checked for null when last used\n"
								+ "----------\n"
								+ "2. ERROR in X.js (at line 7)\n"
								+ "	if (o == null) {\n"
								+ "	    ^\n"
								+ "The variable o can only be null; it was either set to null or checked for null when last used\n"
								+ "----------\n");
	}

	// //https://bugs.eclipse.org/bugs/show_bug.cgi?id=93588
	// public void test037() {
	// this.runConformTest(
	// new String[] {
	// "X.js",
	// " class X extends Object implements Runnable {\n" +
	// "	var interval = 5;\n" +
	// "	 function run() {\n" +
	// "		try {\n" +
	// "			Thread.sleep(interval = interval + 100);\n" +
	// "			Thread.sleep(interval += 100);\n" +
	// "		} catch (InterruptedException e) {\n" +
	// "			e.printStackTrace();\n" +
	// "		}\n" +
	// "	}\n" +
	// "\n" +
	// "	  function main( args) {\n" +
	// "		new X().run();\n" +
	// "	}\n" +
	// "}\n",
	// },
	// "");
	// }
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=111703
	// public void test038() {
	// this.runNegativeTest(
	// new String[] {
	// "X.js",
	// "import java.awt.event.*;\n" +
	// "\n" +
	// "import javax.swing.*;\n" +
	// "import javax.swing.event.*;\n" +
	// "\n" +
	// " class X {\n" +
	// "    JButton myButton = new JButton();\n" +
	// "    JTree myTree = new JTree();\n" +
	// "    ActionListener action;\n" +
	// "    X() {\n" +
	// "        action = new ActionListener() {\n" +
	// "             function actionPerformed(ActionEvent e) {\n" +
	// "                if (true) {\n" +
	// "                    // unlock document\n" +
	// "                     Object document = new Object();\n" +
	// "                    myButton.addActionListener(new ActionListener() {\n"
	// +
	// "                          boolean selectionChanged;\n" +
	// "                         TreeSelectionListener list = new TreeSelectionListener() {\n"
	// +
	// "                             function valueChanged(TreeSelectionEvent e) {\n"
	// +
	// "                                selectionChanged = true;\n" +
	// "                            }\n" +
	// "                        };\n" +
	// "                       {\n" +
	// "                      myTree.addTreeSelectionListener(list);\n" +
	// "                      }\n" +
	// "                         function actionPerformed(ActionEvent e) {\n" +
	// "                            if(!selectionChanged)\n" +
	// "                            myButton.removeActionListener(this);\n" +
	// "                        }\n" +
	// "                    });\n" +
	// "                }\n" +
	// "            }\n" +
	// "        };\n" +
	// "    }\n" +
	// "      function main( args) {\n" +
	// "        new X();\n" +
	// "    }\n" +
	// "\n" +
	// "}",
	// },
	// "----------\n" +
	// "1. WARNING in X.js (at line 19)\n" +
	// "	 function valueChanged(TreeSelectionEvent e) {\n" +
	// "	                                            ^\n" +
	// "The parameter e is hiding another local variable defined in an enclosing type scope\n"
	// +
	// "----------\n" +
	// "2. ERROR in X.js (at line 23)\n" +
	// "	 {\n" +
	// "	       ^\n" +
	// "Cannot define  initializer in inner type new ActionListener(){}\n" +
	// "----------\n" +
	// "3. ERROR in X.js (at line 24)\n" +
	// "	myTree.addTreeSelectionListener(list);\n" +
	// "	^^^^^^\n" +
	// "Cannot make a  reference to the non- field myTree\n" +
	// "----------\n" +
	// "4. WARNING in X.js (at line 26)\n" +
	// "	 function actionPerformed(ActionEvent e) {\n" +
	// "	                                        ^\n" +
	// "The parameter e is hiding another local variable defined in an enclosing type scope\n"
	// +
	// "----------\n");
	// }
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=111898
	// public void test039() {
	// this.runConformTest(
	// new String[] {
	// "X.js",
	// " class X {\n" +
	// "	  function main( args) {\n" +
	// "		var a = 1;\n" +
	// "	    a = a++;\n" +
	// "		print(\"a=\"+a);\n" +
	// "		\n" +
	// "		var b = 1;\n" +
	// "		print(b = b++);\n" +
	// "		println(\"b=\"+b);\n" +
	// "	}\n" +
	// "}\n",
	// },
	// "a=11b=1");
	// }
	// warn upon parameter assignment
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	public void test040() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportParameterAssignment,
				CompilerOptions.ERROR);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"  function foo(b) {\n" + "    b = false;\n" + "  }\n"
								+ "\n", }, "----------\n"
						+ "1. ERROR in X.js (at line 2)\n" + "	b = false;\n"
						+ "	^\n" + "The parameter b should not be assigned\n"
						+ "----------\n", null, true, options);
	}

	// warn upon parameter assignment
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	// diagnose within fake reachable code
	public void test041() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportParameterAssignment,
				CompilerOptions.ERROR);
		this.runNegativeTest(new String[] {
				"X.js",
				"  function foo(b) {\n" + "    if (false) {\n"
						+ "      b = false;\n" + "    }\n" + "  }\n" + "\n", },
				"----------\n" + "1. ERROR in X.js (at line 3)\n"
						+ "	b = false;\n" + "	^\n"
						+ "The parameter b should not be assigned\n"
						+ "----------\n", null, true, options);
	}

	// warn upon parameter assignment
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	// diagnose within fake reachable code
	public void test042() {
		Map options = getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportParameterAssignment,
				CompilerOptions.ERROR);
		this.runNegativeTest(new String[] {
				"X.js",
				"  function foo(b) {\n" + "    if (true) {\n"
						+ "      return;\n" + "    }\n" + "    b = false;\n"
						+ "  }\n" + "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 5)\n" + "	b = false;\n" + "	^\n"
				+ "The parameter b should not be assigned\n" + "----------\n",
				null, true, options);
	}

	// // warn upon parameter assignment
	// // https://bugs.eclipse.org/bugs/show_bug.cgi?id=53773
	// // we only show the 'assignment to final' error here
	// public void test043() {
	// Map options = getCompilerOptions();
	// options.put(CompilerOptions.OPTION_ReportParameterAssignment,
	// CompilerOptions.ERROR);
	// this.runNegativeTest(
	// new String[] {
	// "X.js",
	// " class X {\n" +
	// "  function foo( boolean b) {\n" +
	// "    if (false) {\n" +
	// "      b = false;\n" +
	// "    }\n" +
	// "  }\n" +
	// "}\n",
	// },
	// "----------\n" +
	// "1. ERROR in X.js (at line 4)\n" +
	// "	b = false;\n" +
	// "	^\n" +
	// "The final local variable b cannot be assigned. It must be blank and not using a compound assignment\n"
	// +
	// "----------\n",
	// null, true, options);
	// }
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=100369
	public void test044() {
		this.runNegativeTest(new String[] {
				"X.js",
				"	var length1 = 0;\n" + "	{\n"
						+ "		length1 = length1; // already detected\n" + "	}\n"
						+ "	var length2 = length2 = 0; // not detected\n"
						+ "	var length3 = 0;\n" + "	{\n"
						+ "		length3 = length3 = 0; // not detected\n" + "	}\n"
						+ "	 function foo() {\n" + "		var length1 = 0;\n"
						+ "		length1 = length1; // already detected\n"
						+ "		var length2 = length2 = 0; // not detected\n"
						+ "		var length3 = 0;\n"
						+ "		length3 = length3 = 0; // not detected\n" + "	}\n"
						+ "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 3)\n"
				+ "	length1 = length1; // already detected\n"
				+ "	^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length1 has no effect\n"
				+ "----------\n" + "2. ERROR in X.js (at line 5)\n"
				+ "	var length2 = length2 = 0; // not detected\n"
				+ "	    ^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length2 has no effect\n"
				+ "----------\n" + "3. ERROR in X.js (at line 8)\n"
				+ "	length3 = length3 = 0; // not detected\n"
				+ "	^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length3 has no effect\n"
				+ "----------\n" + "4. ERROR in X.js (at line 12)\n"
				+ "	length1 = length1; // already detected\n"
				+ "	^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length1 has no effect\n"
				+ "----------\n" + "5. ERROR in X.js (at line 13)\n"
				+ "	var length2 = length2 = 0; // not detected\n"
				+ "	    ^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length2 has no effect\n"
				+ "----------\n" + "6. ERROR in X.js (at line 15)\n"
				+ "	length3 = length3 = 0; // not detected\n"
				+ "	^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length3 has no effect\n"
				+ "----------\n");
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=133351
	public void test045() {
		this.runNegativeTest(new String[] {
				"X.js",
				"	function foo() {\n"
						+ "		var length2 = length2 = 0; // first problem\n"
						+ "		var length3 = 0;\n"
						+ "		length3 = length3 = 0; // second problem\n"
						+ "	}\n" + "\n", }, "----------\n"
				+ "1. ERROR in X.js (at line 2)\n"
				+ "	var length2 = length2 = 0; // first problem\n"
				+ "	    ^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length2 has no effect\n"
				+ "----------\n" + "2. ERROR in X.js (at line 4)\n"
				+ "	length3 = length3 = 0; // second problem\n"
				+ "	^^^^^^^^^^^^^^^^^^^^^\n"
				+ "The assignment to variable length3 has no effect\n"
				+ "----------\n");
	}

	public static Class testClass() {
		return AssignmentTest.class;
	}
}
