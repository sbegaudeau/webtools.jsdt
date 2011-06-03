/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


#pragma once

#include "activdbg.h"
#include <vector>

#include "CrossfireEvent.h"
#include "CrossfireRequest.h"
#include "CrossfireResponse.h"
#include "JSEvalCallback.h"
#include "IECrossfireServer.h"
#include "IBreakpointTarget.h"
#include "Value.h"
#include "Logger.h"

class CrossfireContext; // forward declaration
#include "PendingScriptLoad.h"
#include "CrossfireServer.h"
#include "IEDebugger.h"

class CrossfireContext : IBreakpointTarget {

public:
	CrossfireContext(DWORD threadId, CrossfireServer* server);
	~CrossfireContext();
	virtual wchar_t* getHref();
	virtual wchar_t* getName();
	virtual void installBreakpoints(std::vector<Value*>* breakpoints);
	virtual bool performRequest(CrossfireRequest* request);
	virtual bool scriptLoaded(std::wstring* url, IDebugApplicationNode *applicationNode, bool isRetry);
	virtual void sendEvent(CrossfireEvent* eventObj);
	virtual void setHref(wchar_t* value);
	virtual void setName(wchar_t* value);
	virtual void setRunning(bool value);

	/* IBreakpointTarget methods */
	virtual bool clearBreakpoint(unsigned int handle);
	virtual bool getBreakpoint(unsigned int handle, CrossfireBreakpoint** _value);
	virtual bool getBreakpoints(CrossfireBreakpoint*** ___values);
	virtual bool setBreakpointCondition(unsigned int handle, std::wstring* condition);
	virtual bool setBreakpointEnabled(unsigned int handle, bool enabled);
	virtual bool setLineBreakpoint(CrossfireLineBreakpoint* breakpoint, bool isRetry);

private:
	struct JSObject {
		IDebugProperty* debugProperty;
		IDebugStackFrame* stackFrame;
		bool isFunction;
		wchar_t* name;
		std::map<wchar_t*, unsigned int>* objects;
		unsigned int parentHandle;
	};

	virtual void clearObjects();
	virtual bool createValueForFrame(IDebugStackFrame* stackFrame, unsigned int frameIndex, bool includeScopes, Value** _value);
	virtual bool createValueForObject(JSObject* object, Value** _value);
	virtual bool createValueForScript(IDebugApplicationNode* node, bool includeSource, Value** _value);
	virtual bool findNode(wchar_t* name, IDebugApplicationNode* startNode, IDebugApplicationNode** _value);
	virtual bool getDebugApplication(IRemoteDebugApplication** _value);
	virtual bool getDebugApplicationThread(IRemoteDebugApplicationThread** _value);
	virtual bool hookDebugger();
	virtual bool unhookDebugger();

	std::map<unsigned int, CrossfireBreakpoint*>* m_breakpoints;
	DWORD m_cpcApplicationNodeEvents;
	IDebugApplicationNode* m_currentScriptNode;
	IRemoteDebugApplicationThread* m_debugApplicationThread;
	IIEDebugger* m_debugger;
	bool m_debuggerHooked;
	wchar_t* m_href;
	wchar_t* m_name;
	unsigned int m_nextObjectHandle;
	unsigned int m_nextUnnamedUrlIndex;
	std::map<unsigned int, JSObject*>* m_objects;
	std::vector<PendingScriptLoad*>* m_pendingScriptLoads;
	bool m_running;
	CrossfireServer* m_server;
	DWORD m_threadId;

	/* command: backtrace */
	static const wchar_t* COMMAND_BACKTRACE;
	static const wchar_t* KEY_FRAMES;
	static const wchar_t* KEY_FROMFRAME;
	static const wchar_t* KEY_TOFRAME;
	static const wchar_t* KEY_TOTALFRAMECOUNT;
	virtual bool commandBacktrace(Value* arguments, Value** _responseBody);

	/* command: continue */
	static const wchar_t* COMMAND_CONTINUE;
	virtual bool commandContinue(Value* arguments, Value** _responseBody);

	/* command: evaluate */
	static const wchar_t* COMMAND_EVALUATE;
	static const wchar_t* KEY_EXPRESSION;
	virtual bool commandEvaluate(Value* arguments, unsigned int requestSeq, Value** _responseBody);

	/* command: frame */
	static const wchar_t* COMMAND_FRAME;
	static const wchar_t* KEY_FRAME;
	static const wchar_t* KEY_INDEX;
	virtual bool commandFrame(Value* arguments, Value** _responseBody);

	/* command: inspect */
	static const wchar_t* COMMAND_INSPECT;

	/* command: lookup */
	static const wchar_t* COMMAND_LOOKUP;
	virtual bool commandLookup(Value* arguments, Value** _responseBody);

	/* command: scope */
	static const wchar_t* COMMAND_SCOPE;
	static const wchar_t* KEY_SCOPE;
	static const wchar_t* KEY_SCOPEINDEX;
	virtual bool commandScope(Value* arguments, Value** _responseBody);

	/* command: scopes */
	static const wchar_t* COMMAND_SCOPES;
	static const wchar_t* KEY_FROMSCOPE;
	static const wchar_t* KEY_SCOPES;
	static const wchar_t* KEY_TOSCOPE;
	static const wchar_t* KEY_TOTALSCOPECOUNT;
	virtual bool commandScopes(Value* arguments, Value** _responseBody);

	/* command: script */
	static const wchar_t* COMMAND_SCRIPT;
	static const wchar_t* KEY_SCRIPT;
	virtual bool commandScript(Value* arguments, Value** _responseBody);

	/* command: scripts */
	static const wchar_t* COMMAND_SCRIPTS;
	static const wchar_t* KEY_SCRIPTS;
	virtual bool commandScripts(Value* arguments, Value** _responseBody);
	virtual void addScriptsToArray(IDebugApplicationNode* node, bool includeSource, Value* arrayValue);

	/* command: suspend */
	static const wchar_t* COMMAND_SUSPEND;
	static const wchar_t* KEY_STEPACTION;
	static const wchar_t* VALUE_IN;
	static const wchar_t* VALUE_NEXT;
	static const wchar_t* VALUE_OUT;
	virtual bool commandSuspend(Value* arguments, Value** _responseBody);

	/* event: onScript */
	static const wchar_t* EVENT_ONSCRIPT;

	/* event: onToggleBreakpoint */
	static const wchar_t* EVENT_ONTOGGLEBREAKPOINT;
	static const wchar_t* KEY_SET;

	/* shared */
	static const wchar_t* KEY_BREAKPOINT;
	static const wchar_t* KEY_CONTEXTID;
	static const wchar_t* KEY_FRAMEINDEX;
	static const wchar_t* KEY_HANDLE;
	static const wchar_t* KEY_INCLUDESCOPES;
	static const wchar_t* KEY_INCLUDESOURCE;
	static const wchar_t* KEY_LINE;
	static const wchar_t* KEY_TYPE;

	/* breakpoint objects */
	static const wchar_t* BPTYPE_LINE;
	static const wchar_t* KEY_LOCATION;

	/* frame objects */
	static const wchar_t* KEY_FUNCTIONNAME;
	static const wchar_t* KEY_SCRIPTID;

	/* object objects */
	static const wchar_t* JSVALUE_BOOLEAN;
	static const wchar_t* JSVALUE_FUNCTION;
	static const wchar_t* JSVALUE_NUMBER;
	static const wchar_t* JSVALUE_NULL;
	static const wchar_t* JSVALUE_STRING;
	static const wchar_t* JSVALUE_TRUE;
	static const wchar_t* JSVALUE_UNDEFINED;
	static const wchar_t* KEY_LOCALS;
	static const wchar_t* KEY_THIS;
	static const wchar_t* KEY_VALUE;
	static const wchar_t* VALUE_BOOLEAN;
	static const wchar_t* VALUE_FUNCTION;
	static const wchar_t* VALUE_NUMBER;
	static const wchar_t* VALUE_OBJECT;
	static const wchar_t* VALUE_STRING;
	static const wchar_t* VALUE_UNDEFINED;

	/* script objects */
	static const wchar_t* KEY_COLUMNOFFSET;
	static const wchar_t* KEY_COMPILATIONTYPE;
	static const wchar_t* KEY_ID;
	static const wchar_t* KEY_LINECOUNT;
	static const wchar_t* KEY_LINEOFFSET;
	static const wchar_t* KEY_SOURCE;
	static const wchar_t* KEY_SOURCESTART;
	static const wchar_t* KEY_SOURCELENGTH;
	static const wchar_t* VALUE_TOPLEVEL;
};
