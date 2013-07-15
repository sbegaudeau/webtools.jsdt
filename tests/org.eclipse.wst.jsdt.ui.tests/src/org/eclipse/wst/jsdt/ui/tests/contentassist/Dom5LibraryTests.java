/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

public class Dom5LibraryTests extends TestCase {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test HTML5 APIs";

	/**
	 * <p>
	 * Test project setup for this test.
	 * </p>
	 */
	private static TestProjectSetup fTestProjectSetup;
	
	/**
	 * <p>
	 * Default constructor
	 * <p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @see #suite()
	 */
	public Dom5LibraryTests() {
		super(TEST_NAME);
	}

	/**
	 * <p>
	 * Constructor that takes a test name.
	 * </p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @param name
	 *            The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public Dom5LibraryTests(String name) {
		super(name);
	}

	/**
	 * <p>
	 * Use this method to add these tests to a larger test suite so set up and tear down can be
	 * performed
	 * </p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this class
	 *         with set up and tear down.
	 */

	public static Test suite() {
		TestSuite ts = new TestSuite(Dom5LibraryTests.class, TEST_NAME);
		
		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);
		
		return fTestProjectSetup;
	}

	public void testNavigatorDotG() throws Exception {
		String[][] expectedProposals = new String[][] { {"geolocation : Geolocation - Navigator"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 0, 11, expectedProposals);
	}
	
	public void testGeolocation() throws Exception {
		String[][] expectedProposals = new String[][] { {"clearWatch(Number watchId) - Geolocation",
				"getCurrentPosition(Function successCallback, Function errorCallback, PositionOptions options) - Geolocation",
				"watchPosition(Function successCallback, Function errorCallback, PositionOptions options) : Number - Geolocation"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 2, 22, expectedProposals);
	}
	
	public void testPosition() throws Exception {
		String[][] expectedProposals = new String[][] { {"coords : Coordinates - Position",
				"timestamp : Number - Position"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 9, 8, expectedProposals);
	}
	
	public void testCoordinates() throws Exception {
		String[][] expectedProposals = new String[][] { {"accuracy : Number - Coordinates",
				"altitude : Number - Coordinates",
				"altitudeAccuracy : Number - Coordinates",
				"heading : Number - Coordinates",
				"latitude : Number - Coordinates",
				"longitude : Number - Coordinates",
				"speed : Number - Coordinates"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 10, 15, expectedProposals);
	}
	
	public void testPositionError() throws Exception {
		String[][] expectedProposals = new String[][] { {"code : Number - PositionError",
			"message : String - PositionError",
			"PERMISSION_DENIED : Number - PositionError",
			"POSITION_UNAVAILABLE : Number - PositionError",
			"TIMEOUT : Number - PositionError"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 18, 10, expectedProposals);
	}
	
	public void testPositionOptions() throws Exception {
		String[][] expectedProposals = new String[][] { {"enableHighAccuracy : Boolean - PositionOptions",
			"maximumAge : Number - PositionOptions",
			"timeout : Number - PositionOptions"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 26, 12, expectedProposals);
	}
	
	public void testHistory() throws Exception {
		String[][] expectedProposals = new String[][] { {"back() - History",
			"forward() - History",
			"go(arg) - History",
			"pushState(Object data, String title, String url) - History",
			"replaceState(Object data, String title, String url) - History",
			"state : Object - History"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 29, 8, expectedProposals);
	}
	
	public void testSessionStorage() throws Exception {
		String[][] expectedProposals = new String[][] { {"clear() - Storage",
			"getItem(String key) : String - Storage",
			"key(Number index) : String - Storage",
			"removeItem(String key) - Storage",
			"setItem(String key, String value) - Storage",
			"length : Number - Storage",
			"prototype - Storage"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 31, 15, expectedProposals);
	}
	
	public void testLocalStorage() throws Exception {
		String[][] expectedProposals = new String[][] { {"clear() - Storage",
			"getItem(String key) : String - Storage",
			"key(Number index) : String - Storage",
			"removeItem(String key) - Storage",
			"setItem(String key, String value) - Storage",
			"length : Number - Storage",
			"prototype - Storage"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 33, 13, expectedProposals);
	}
	
	public void testWebSocket() throws Exception {
		String[][] expectedProposals = new String[][] { {"close(Number code, String reason) - WebSocket",
			"send(Object data) - WebSocket",
			"binaryType : String - WebSocket",
			"bufferedAmount : Number - WebSocket",
			"CLOSED : Number - WebSocket",
			"CLOSING : Number - WebSocket",
			"CONNECTING : Number - WebSocket",
			"extensions : String - WebSocket",
			"OPEN : Number - WebSocket",
			"protocol : String - WebSocket",
			"readyState : Number - WebSocket",
			"url : String - WebSocket"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 36, 7, expectedProposals);
	}
	
	public void testDocumentQuerySelector() throws Exception {
		String[][] expectedProposals = new String[][] { {"querySelector(String selectors) : Element - Document",
			"querySelectorAll(String selectors) : NodeList - Document"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 38, 10, expectedProposals);
	}
	
	public void testDocumentFragmentQuerySelector() throws Exception {
		String[][] expectedProposals = new String[][] { {"querySelector(String selectors) : Element - DocumentFragment",
			"querySelectorAll(String selectors) : NodeList - DocumentFragment"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 40, 35, expectedProposals);
	}
	
	public void testElementQuerySelector() throws Exception {
		String[][] expectedProposals = new String[][] { {"querySelector(String selectors) : Element - Element",
			"querySelectorAll(String selectors) : NodeList - Element"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 42, 36, expectedProposals);
	}
	
	public void testAudio() throws Exception {
		String[][] expectedProposals = new String[][] { { "canPlayType(String type) : String - HTMLMediaElement",
				"load() - HTMLMediaElement", "pause() - HTMLMediaElement", "play() - HTMLMediaElement",
				"autoplay : Boolean - HTMLMediaElement", "buffered : TimeRanges - HTMLMediaElement",
				"className : String - HTMLElement", "controls : Boolean - HTMLMediaElement",
				"crossOrigin : String - HTMLMediaElement", "currentSrc : String - HTMLMediaElement",
				"currentTime : Number - HTMLMediaElement", "defaultMuted : Boolean - HTMLMediaElement",
				"defaultPlaybackRate : Number - HTMLMediaElement", "duration : Number - HTMLMediaElement",
				"ended : Boolean - HTMLMediaElement", "HAVE_CURRENT_DATA : Number - HTMLMediaElement",
				"HAVE_ENOUGH_DATA : Number - HTMLMediaElement", "HAVE_FUTURE_DATA : Number - HTMLMediaElement",
				"HAVE_METADATA : Number - HTMLMediaElement", "HAVE_NOTHING : Number - HTMLMediaElement",
				"initialTime : Number - HTMLMediaElement", "loop : Boolean - HTMLMediaElement",
				"muted : Boolean - HTMLMediaElement", "NETWORK_EMPTY : Number - HTMLMediaElement",
				"NETWORK_IDLE : Number - HTMLMediaElement", "NETWORK_LOADING : Number - HTMLMediaElement",
				"NETWORK_NO_SOURCE : Number - HTMLMediaElement", "networkState : Number - HTMLMediaElement",
				"paused : Boolean - HTMLMediaElement", "playbackRate : Number - HTMLMediaElement",
				"played : TimeRanges - HTMLMediaElement", "preload : String - HTMLMediaElement",
				"readyState : Number - HTMLMediaElement", "seekable : TimeRanges - HTMLMediaElement",
				"seeking : Boolean - HTMLMediaElement", "src : String - HTMLMediaElement",
				"startOffsetTime : Date - HTMLMediaElement", "volume : Number - HTMLMediaElement" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 45, 6, expectedProposals);
	}
	
	public void testVideo() throws Exception {
		String[][] expectedProposals = new String[][] { { "canPlayType(String type) : String - HTMLMediaElement",
				"load() - HTMLMediaElement", "pause() - HTMLMediaElement", "play() - HTMLMediaElement",
				"autoplay : Boolean - HTMLMediaElement", "buffered : TimeRanges - HTMLMediaElement",
				"className : String - HTMLElement", "controls : Boolean - HTMLMediaElement",
				"crossOrigin : String - HTMLMediaElement", "currentSrc : String - HTMLMediaElement",
				"currentTime : Number - HTMLMediaElement", "defaultMuted : Boolean - HTMLMediaElement",
				"defaultPlaybackRate : Number - HTMLMediaElement", "duration : Number - HTMLMediaElement",
				"ended : Boolean - HTMLMediaElement", "HAVE_CURRENT_DATA : Number - HTMLMediaElement",
				"HAVE_ENOUGH_DATA : Number - HTMLMediaElement", "HAVE_FUTURE_DATA : Number - HTMLMediaElement",
				"HAVE_METADATA : Number - HTMLMediaElement", "HAVE_NOTHING : Number - HTMLMediaElement",
				"initialTime : Number - HTMLMediaElement", "loop : Boolean - HTMLMediaElement",
				"muted : Boolean - HTMLMediaElement", "NETWORK_EMPTY : Number - HTMLMediaElement",
				"NETWORK_IDLE : Number - HTMLMediaElement", "NETWORK_LOADING : Number - HTMLMediaElement",
				"NETWORK_NO_SOURCE : Number - HTMLMediaElement", "networkState : Number - HTMLMediaElement",
				"paused : Boolean - HTMLMediaElement", "playbackRate : Number - HTMLMediaElement",
				"played : TimeRanges - HTMLMediaElement", "preload : String - HTMLMediaElement",
				"readyState : Number - HTMLMediaElement", "seekable : TimeRanges - HTMLMediaElement",
				"seeking : Boolean - HTMLMediaElement", "src : String - HTMLMediaElement",
				"startOffsetTime : Date - HTMLMediaElement", "volume : Number - HTMLMediaElement",
				"height : Number - HTMLVideoElement", "poster : String - HTMLVideoElement",
				"videoHeight : Number - HTMLVideoElement", "videoWidth : Number - HTMLVideoElement",
				"width : Number - HTMLVideoElement" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestDom5Library_0.js", 52, 12, expectedProposals);
	}
	
	

}