

/* this ALWAYS GENERATED file contains the proxy stub code */


 /* File created by MIDL compiler version 7.00.0555 */
/* at Thu Sep 08 00:09:27 2011
 */
/* Compiler settings for IECrossfireServer.idl:
    Oicf, W1, Zp8, env=Win32 (32b run), target_arch=X86 7.00.0555 
    protocol : dce , ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
    VC __declspec() decoration level: 
         __declspec(uuid()), __declspec(selectany), __declspec(novtable)
         DECLSPEC_UUID(), MIDL_INTERFACE()
*/
/* @@MIDL_FILE_HEADING(  ) */

#if !defined(_M_IA64) && !defined(_M_AMD64)


#pragma warning( disable: 4049 )  /* more than 64k source lines */
#if _MSC_VER >= 1200
#pragma warning(push)
#endif

#pragma warning( disable: 4211 )  /* redefine extern to static */
#pragma warning( disable: 4232 )  /* dllimport identity*/
#pragma warning( disable: 4024 )  /* array to pointer mapping*/
#pragma warning( disable: 4152 )  /* function/data pointer conversion in expression */
#pragma warning( disable: 4100 ) /* unreferenced arguments in x86 call */

#pragma optimize("", off ) 

#define USE_STUBLESS_PROXY


/* verify that the <rpcproxy.h> version is high enough to compile this file*/
#ifndef __REDQ_RPCPROXY_H_VERSION__
#define __REQUIRED_RPCPROXY_H_VERSION__ 440
#endif


#include "rpcproxy.h"
#ifndef __RPCPROXY_H_VERSION__
#error this stub requires an updated version of <rpcproxy.h>
#endif /* __RPCPROXY_H_VERSION__ */


#include "IECrossfireServer.h"

#define TYPE_FORMAT_STRING_SIZE   51                                
#define PROC_FORMAT_STRING_SIZE   355                               
#define EXPR_FORMAT_STRING_SIZE   1                                 
#define TRANSMIT_AS_TABLE_SIZE    0            
#define WIRE_MARSHAL_TABLE_SIZE   0            

typedef struct _IECrossfireServer_MIDL_TYPE_FORMAT_STRING
    {
    short          Pad;
    unsigned char  Format[ TYPE_FORMAT_STRING_SIZE ];
    } IECrossfireServer_MIDL_TYPE_FORMAT_STRING;

typedef struct _IECrossfireServer_MIDL_PROC_FORMAT_STRING
    {
    short          Pad;
    unsigned char  Format[ PROC_FORMAT_STRING_SIZE ];
    } IECrossfireServer_MIDL_PROC_FORMAT_STRING;

typedef struct _IECrossfireServer_MIDL_EXPR_FORMAT_STRING
    {
    long          Pad;
    unsigned char  Format[ EXPR_FORMAT_STRING_SIZE ];
    } IECrossfireServer_MIDL_EXPR_FORMAT_STRING;


static const RPC_SYNTAX_IDENTIFIER  _RpcTransferSyntax = 
{{0x8A885D04,0x1CEB,0x11C9,{0x9F,0xE8,0x08,0x00,0x2B,0x10,0x48,0x60}},{2,0}};


extern const IECrossfireServer_MIDL_TYPE_FORMAT_STRING IECrossfireServer__MIDL_TypeFormatString;
extern const IECrossfireServer_MIDL_PROC_FORMAT_STRING IECrossfireServer__MIDL_ProcFormatString;
extern const IECrossfireServer_MIDL_EXPR_FORMAT_STRING IECrossfireServer__MIDL_ExprFormatString;


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO IJSEvalCallback_ServerInfo;
extern const MIDL_STUBLESS_PROXY_INFO IJSEvalCallback_ProxyInfo;


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO IIEDebugger_ServerInfo;
extern const MIDL_STUBLESS_PROXY_INFO IIEDebugger_ProxyInfo;


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO IBrowserContext_ServerInfo;
extern const MIDL_STUBLESS_PROXY_INFO IBrowserContext_ProxyInfo;


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO ICrossfireServer_ServerInfo;
extern const MIDL_STUBLESS_PROXY_INFO ICrossfireServer_ProxyInfo;


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO ICrossfireServerClass_ServerInfo;
extern const MIDL_STUBLESS_PROXY_INFO ICrossfireServerClass_ProxyInfo;


extern const MIDL_STUB_DESC Object_StubDesc;


extern const MIDL_SERVER_INFO IPendingScriptLoad_ServerInfo;
extern const MIDL_STUBLESS_PROXY_INFO IPendingScriptLoad_ProxyInfo;



#if !defined(__RPC_WIN32__)
#error  Invalid build platform for this stub.
#endif

#if !(TARGET_IS_NT40_OR_LATER)
#error You need Windows NT 4.0 or later to run this stub because it uses these features:
#error   -Oif or -Oicf.
#error However, your C/C++ compilation flags indicate you intend to run this app on earlier systems.
#error This app will fail with the RPC_X_WRONG_STUB_VERSION error.
#endif


static const IECrossfireServer_MIDL_PROC_FORMAT_STRING IECrossfireServer__MIDL_ProcFormatString =
    {
        0,
        {

	/* Procedure navigate */

			0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/*  2 */	NdrFcLong( 0x0 ),	/* 0 */
/*  6 */	NdrFcShort( 0x3 ),	/* 3 */
/*  8 */	NdrFcShort( 0x10 ),	/* x86 Stack size/offset = 16 */
/* 10 */	NdrFcShort( 0x5 ),	/* 5 */
/* 12 */	NdrFcShort( 0x8 ),	/* 8 */
/* 14 */	0x6,		/* Oi2 Flags:  clt must size, has return, */
			0x3,		/* 3 */

	/* Parameter url */

/* 16 */	NdrFcShort( 0x10b ),	/* Flags:  must size, must free, in, simple ref, */
/* 18 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 20 */	NdrFcShort( 0x4 ),	/* Type Offset=4 */

	/* Parameter openNewTab */

/* 22 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 24 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 26 */	0x3,		/* FC_SMALL */
			0x0,		/* 0 */

	/* Return value */

/* 28 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 30 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 32 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure contextCreated */

/* 34 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 36 */	NdrFcLong( 0x0 ),	/* 0 */
/* 40 */	NdrFcShort( 0x7 ),	/* 7 */
/* 42 */	NdrFcShort( 0x10 ),	/* x86 Stack size/offset = 16 */
/* 44 */	NdrFcShort( 0x8 ),	/* 8 */
/* 46 */	NdrFcShort( 0x8 ),	/* 8 */
/* 48 */	0x6,		/* Oi2 Flags:  clt must size, has return, */
			0x3,		/* 3 */

	/* Parameter processId */

/* 50 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 52 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 54 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter url */

/* 56 */	NdrFcShort( 0x10b ),	/* Flags:  must size, must free, in, simple ref, */
/* 58 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 60 */	NdrFcShort( 0x4 ),	/* Type Offset=4 */

	/* Return value */

/* 62 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 64 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 66 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure RemoveServer */


	/* Procedure contextDestroyed */

/* 68 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 70 */	NdrFcLong( 0x0 ),	/* 0 */
/* 74 */	NdrFcShort( 0x8 ),	/* 8 */
/* 76 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 78 */	NdrFcShort( 0x8 ),	/* 8 */
/* 80 */	NdrFcShort( 0x8 ),	/* 8 */
/* 82 */	0x4,		/* Oi2 Flags:  has return, */
			0x2,		/* 2 */

	/* Parameter windowHandle */


	/* Parameter processId */

/* 84 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 86 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 88 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Return value */


	/* Return value */

/* 90 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 92 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 94 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure contextLoaded */

/* 96 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 98 */	NdrFcLong( 0x0 ),	/* 0 */
/* 102 */	NdrFcShort( 0x9 ),	/* 9 */
/* 104 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 106 */	NdrFcShort( 0x8 ),	/* 8 */
/* 108 */	NdrFcShort( 0x8 ),	/* 8 */
/* 110 */	0x4,		/* Oi2 Flags:  has return, */
			0x2,		/* 2 */

	/* Parameter processId */

/* 112 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 114 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 116 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Return value */

/* 118 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 120 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 122 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure getPort */

/* 124 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 126 */	NdrFcLong( 0x0 ),	/* 0 */
/* 130 */	NdrFcShort( 0xa ),	/* 10 */
/* 132 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 134 */	NdrFcShort( 0x0 ),	/* 0 */
/* 136 */	NdrFcShort( 0x24 ),	/* 36 */
/* 138 */	0x4,		/* Oi2 Flags:  has return, */
			0x2,		/* 2 */

	/* Parameter value */

/* 140 */	NdrFcShort( 0x2150 ),	/* Flags:  out, base type, simple ref, srv alloc size=8 */
/* 142 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 144 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Return value */

/* 146 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 148 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 150 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure getState */

/* 152 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 154 */	NdrFcLong( 0x0 ),	/* 0 */
/* 158 */	NdrFcShort( 0xb ),	/* 11 */
/* 160 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 162 */	NdrFcShort( 0x0 ),	/* 0 */
/* 164 */	NdrFcShort( 0x24 ),	/* 36 */
/* 166 */	0x4,		/* Oi2 Flags:  has return, */
			0x2,		/* 2 */

	/* Parameter value */

/* 168 */	NdrFcShort( 0x2150 ),	/* Flags:  out, base type, simple ref, srv alloc size=8 */
/* 170 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 172 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Return value */

/* 174 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 176 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 178 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure registerBrowser */

/* 180 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 182 */	NdrFcLong( 0x0 ),	/* 0 */
/* 186 */	NdrFcShort( 0xc ),	/* 12 */
/* 188 */	NdrFcShort( 0x10 ),	/* x86 Stack size/offset = 16 */
/* 190 */	NdrFcShort( 0x8 ),	/* 8 */
/* 192 */	NdrFcShort( 0x8 ),	/* 8 */
/* 194 */	0x6,		/* Oi2 Flags:  clt must size, has return, */
			0x3,		/* 3 */

	/* Parameter processId */

/* 196 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 198 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 200 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter browser */

/* 202 */	NdrFcShort( 0xb ),	/* Flags:  must size, must free, in, */
/* 204 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 206 */	NdrFcShort( 0xa ),	/* Type Offset=10 */

	/* Return value */

/* 208 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 210 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 212 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure removeBrowser */

/* 214 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 216 */	NdrFcLong( 0x0 ),	/* 0 */
/* 220 */	NdrFcShort( 0xd ),	/* 13 */
/* 222 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 224 */	NdrFcShort( 0x8 ),	/* 8 */
/* 226 */	NdrFcShort( 0x8 ),	/* 8 */
/* 228 */	0x4,		/* Oi2 Flags:  has return, */
			0x2,		/* 2 */

	/* Parameter processId */

/* 230 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 232 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 234 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Return value */

/* 236 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 238 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 240 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure setCurrentContext */

/* 242 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 244 */	NdrFcLong( 0x0 ),	/* 0 */
/* 248 */	NdrFcShort( 0xe ),	/* 14 */
/* 250 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 252 */	NdrFcShort( 0x8 ),	/* 8 */
/* 254 */	NdrFcShort( 0x8 ),	/* 8 */
/* 256 */	0x4,		/* Oi2 Flags:  has return, */
			0x2,		/* 2 */

	/* Parameter processId */

/* 258 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 260 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 262 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Return value */

/* 264 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 266 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 268 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure start */

/* 270 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 272 */	NdrFcLong( 0x0 ),	/* 0 */
/* 276 */	NdrFcShort( 0xf ),	/* 15 */
/* 278 */	NdrFcShort( 0x10 ),	/* x86 Stack size/offset = 16 */
/* 280 */	NdrFcShort( 0x10 ),	/* 16 */
/* 282 */	NdrFcShort( 0x8 ),	/* 8 */
/* 284 */	0x4,		/* Oi2 Flags:  has return, */
			0x3,		/* 3 */

	/* Parameter port */

/* 286 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 288 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 290 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Parameter debugPort */

/* 292 */	NdrFcShort( 0x48 ),	/* Flags:  in, base type, */
/* 294 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 296 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Return value */

/* 298 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 300 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 302 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure stop */

/* 304 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 306 */	NdrFcLong( 0x0 ),	/* 0 */
/* 310 */	NdrFcShort( 0x10 ),	/* 16 */
/* 312 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 314 */	NdrFcShort( 0x0 ),	/* 0 */
/* 316 */	NdrFcShort( 0x8 ),	/* 8 */
/* 318 */	0x4,		/* Oi2 Flags:  has return, */
			0x1,		/* 1 */

	/* Return value */

/* 320 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 322 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 324 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

	/* Procedure GetServer */

/* 326 */	0x33,		/* FC_AUTO_HANDLE */
			0x6c,		/* Old Flags:  object, Oi2 */
/* 328 */	NdrFcLong( 0x0 ),	/* 0 */
/* 332 */	NdrFcShort( 0x7 ),	/* 7 */
/* 334 */	NdrFcShort( 0xc ),	/* x86 Stack size/offset = 12 */
/* 336 */	NdrFcShort( 0x0 ),	/* 0 */
/* 338 */	NdrFcShort( 0x8 ),	/* 8 */
/* 340 */	0x5,		/* Oi2 Flags:  srv must size, has return, */
			0x2,		/* 2 */

	/* Parameter _value */

/* 342 */	NdrFcShort( 0x13 ),	/* Flags:  must size, must free, out, */
/* 344 */	NdrFcShort( 0x4 ),	/* x86 Stack size/offset = 4 */
/* 346 */	NdrFcShort( 0x1c ),	/* Type Offset=28 */

	/* Return value */

/* 348 */	NdrFcShort( 0x70 ),	/* Flags:  out, return, base type, */
/* 350 */	NdrFcShort( 0x8 ),	/* x86 Stack size/offset = 8 */
/* 352 */	0x8,		/* FC_LONG */
			0x0,		/* 0 */

			0x0
        }
    };

static const IECrossfireServer_MIDL_TYPE_FORMAT_STRING IECrossfireServer__MIDL_TypeFormatString =
    {
        0,
        {
			NdrFcShort( 0x0 ),	/* 0 */
/*  2 */	
			0x11, 0x8,	/* FC_RP [simple_pointer] */
/*  4 */	
			0x25,		/* FC_C_WSTRING */
			0x5c,		/* FC_PAD */
/*  6 */	
			0x11, 0xc,	/* FC_RP [alloced_on_stack] [simple_pointer] */
/*  8 */	0x8,		/* FC_LONG */
			0x5c,		/* FC_PAD */
/* 10 */	
			0x2f,		/* FC_IP */
			0x5a,		/* FC_CONSTANT_IID */
/* 12 */	NdrFcLong( 0xe4121804 ),	/* -468576252 */
/* 16 */	NdrFcShort( 0x5350 ),	/* 21328 */
/* 18 */	NdrFcShort( 0x4ddd ),	/* 19933 */
/* 20 */	0xbe,		/* 190 */
			0x57,		/* 87 */
/* 22 */	0x9c,		/* 156 */
			0x5b,		/* 91 */
/* 24 */	0x2a,		/* 42 */
			0x13,		/* 19 */
/* 26 */	0xea,		/* 234 */
			0x29,		/* 41 */
/* 28 */	
			0x11, 0x10,	/* FC_RP [pointer_deref] */
/* 30 */	NdrFcShort( 0x2 ),	/* Offset= 2 (32) */
/* 32 */	
			0x2f,		/* FC_IP */
			0x5a,		/* FC_CONSTANT_IID */
/* 34 */	NdrFcLong( 0x31db015 ),	/* 52277269 */
/* 38 */	NdrFcShort( 0xb1be ),	/* -20034 */
/* 40 */	NdrFcShort( 0x4d39 ),	/* 19769 */
/* 42 */	0x84,		/* 132 */
			0xd2,		/* 210 */
/* 44 */	0xd7,		/* 215 */
			0xf9,		/* 249 */
/* 46 */	0x6d,		/* 109 */
			0x2a,		/* 42 */
/* 48 */	0xcb,		/* 203 */
			0xfe,		/* 254 */

			0x0
        }
    };


/* Object interface: IUnknown, ver. 0.0,
   GUID={0x00000000,0x0000,0x0000,{0xC0,0x00,0x00,0x00,0x00,0x00,0x00,0x46}} */


/* Object interface: IJSEvalCallback, ver. 0.0,
   GUID={0x31DC44C3,0x7C98,0x4737,{0x8D,0x62,0x0E,0x78,0xFC,0xCC,0x50,0x3C}} */

#pragma code_seg(".orpc")
static const unsigned short IJSEvalCallback_FormatStringOffsetTable[] =
    {
    0
    };

static const MIDL_STUBLESS_PROXY_INFO IJSEvalCallback_ProxyInfo =
    {
    &Object_StubDesc,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IJSEvalCallback_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };


static const MIDL_SERVER_INFO IJSEvalCallback_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IJSEvalCallback_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0};
CINTERFACE_PROXY_VTABLE(3) _IJSEvalCallbackProxyVtbl = 
{
    0,
    &IID_IJSEvalCallback,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy
};

const CInterfaceStubVtbl _IJSEvalCallbackStubVtbl =
{
    &IID_IJSEvalCallback,
    &IJSEvalCallback_ServerInfo,
    3,
    0, /* pure interpreted */
    CStdStubBuffer_METHODS
};


/* Object interface: IIEDebugger, ver. 0.0,
   GUID={0x0F9DDD31,0x746A,0x4EBC,{0x87,0x3D,0x7A,0x98,0x8B,0x5E,0xE0,0x88}} */

#pragma code_seg(".orpc")
static const unsigned short IIEDebugger_FormatStringOffsetTable[] =
    {
    0
    };

static const MIDL_STUBLESS_PROXY_INFO IIEDebugger_ProxyInfo =
    {
    &Object_StubDesc,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IIEDebugger_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };


static const MIDL_SERVER_INFO IIEDebugger_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IIEDebugger_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0};
CINTERFACE_PROXY_VTABLE(3) _IIEDebuggerProxyVtbl = 
{
    0,
    &IID_IIEDebugger,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy
};

const CInterfaceStubVtbl _IIEDebuggerStubVtbl =
{
    &IID_IIEDebugger,
    &IIEDebugger_ServerInfo,
    3,
    0, /* pure interpreted */
    CStdStubBuffer_METHODS
};


/* Object interface: IBrowserContext, ver. 0.0,
   GUID={0xE4121804,0x5350,0x4DDD,{0xBE,0x57,0x9C,0x5B,0x2A,0x13,0xEA,0x29}} */

#pragma code_seg(".orpc")
static const unsigned short IBrowserContext_FormatStringOffsetTable[] =
    {
    0
    };

static const MIDL_STUBLESS_PROXY_INFO IBrowserContext_ProxyInfo =
    {
    &Object_StubDesc,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IBrowserContext_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };


static const MIDL_SERVER_INFO IBrowserContext_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IBrowserContext_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0};
CINTERFACE_PROXY_VTABLE(4) _IBrowserContextProxyVtbl = 
{
    &IBrowserContext_ProxyInfo,
    &IID_IBrowserContext,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy ,
    (void *) (INT_PTR) -1 /* IBrowserContext::navigate */
};

const CInterfaceStubVtbl _IBrowserContextStubVtbl =
{
    &IID_IBrowserContext,
    &IBrowserContext_ServerInfo,
    4,
    0, /* pure interpreted */
    CStdStubBuffer_METHODS
};


/* Object interface: IDispatch, ver. 0.0,
   GUID={0x00020400,0x0000,0x0000,{0xC0,0x00,0x00,0x00,0x00,0x00,0x00,0x46}} */


/* Object interface: ICrossfireServer, ver. 0.0,
   GUID={0x031DB015,0xB1BE,0x4D39,{0x84,0xD2,0xD7,0xF9,0x6D,0x2A,0xCB,0xFE}} */

#pragma code_seg(".orpc")
static const unsigned short ICrossfireServer_FormatStringOffsetTable[] =
    {
    (unsigned short) -1,
    (unsigned short) -1,
    (unsigned short) -1,
    (unsigned short) -1,
    34,
    68,
    96,
    124,
    152,
    180,
    214,
    242,
    270,
    304
    };

static const MIDL_STUBLESS_PROXY_INFO ICrossfireServer_ProxyInfo =
    {
    &Object_StubDesc,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &ICrossfireServer_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };


static const MIDL_SERVER_INFO ICrossfireServer_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &ICrossfireServer_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0};
CINTERFACE_PROXY_VTABLE(17) _ICrossfireServerProxyVtbl = 
{
    &ICrossfireServer_ProxyInfo,
    &IID_ICrossfireServer,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy ,
    0 /* IDispatch::GetTypeInfoCount */ ,
    0 /* IDispatch::GetTypeInfo */ ,
    0 /* IDispatch::GetIDsOfNames */ ,
    0 /* IDispatch_Invoke_Proxy */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::contextCreated */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::contextDestroyed */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::contextLoaded */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::getPort */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::getState */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::registerBrowser */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::removeBrowser */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::setCurrentContext */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::start */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServer::stop */
};


static const PRPC_STUB_FUNCTION ICrossfireServer_table[] =
{
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2,
    NdrStubCall2
};

CInterfaceStubVtbl _ICrossfireServerStubVtbl =
{
    &IID_ICrossfireServer,
    &ICrossfireServer_ServerInfo,
    17,
    &ICrossfireServer_table[-3],
    CStdStubBuffer_DELEGATING_METHODS
};


/* Object interface: ICrossfireServerClass, ver. 0.0,
   GUID={0xF48260BB,0xC061,0x4410,{0x9C,0xE1,0x4C,0x5C,0x76,0x02,0x69,0x0E}} */

#pragma code_seg(".orpc")
static const unsigned short ICrossfireServerClass_FormatStringOffsetTable[] =
    {
    (unsigned short) -1,
    (unsigned short) -1,
    (unsigned short) -1,
    (unsigned short) -1,
    326,
    68
    };

static const MIDL_STUBLESS_PROXY_INFO ICrossfireServerClass_ProxyInfo =
    {
    &Object_StubDesc,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &ICrossfireServerClass_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };


static const MIDL_SERVER_INFO ICrossfireServerClass_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &ICrossfireServerClass_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0};
CINTERFACE_PROXY_VTABLE(9) _ICrossfireServerClassProxyVtbl = 
{
    &ICrossfireServerClass_ProxyInfo,
    &IID_ICrossfireServerClass,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy ,
    0 /* IDispatch::GetTypeInfoCount */ ,
    0 /* IDispatch::GetTypeInfo */ ,
    0 /* IDispatch::GetIDsOfNames */ ,
    0 /* IDispatch_Invoke_Proxy */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServerClass::GetServer */ ,
    (void *) (INT_PTR) -1 /* ICrossfireServerClass::RemoveServer */
};


static const PRPC_STUB_FUNCTION ICrossfireServerClass_table[] =
{
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    NdrStubCall2,
    NdrStubCall2
};

CInterfaceStubVtbl _ICrossfireServerClassStubVtbl =
{
    &IID_ICrossfireServerClass,
    &ICrossfireServerClass_ServerInfo,
    9,
    &ICrossfireServerClass_table[-3],
    CStdStubBuffer_DELEGATING_METHODS
};


/* Object interface: IPendingScriptLoad, ver. 0.0,
   GUID={0x56C1AD62,0x3E69,0x46BA,{0xBE,0x6D,0xE1,0xD2,0xA0,0xD8,0x8A,0xB5}} */

#pragma code_seg(".orpc")
static const unsigned short IPendingScriptLoad_FormatStringOffsetTable[] =
    {
    (unsigned short) -1,
    (unsigned short) -1,
    (unsigned short) -1,
    (unsigned short) -1,
    0
    };

static const MIDL_STUBLESS_PROXY_INFO IPendingScriptLoad_ProxyInfo =
    {
    &Object_StubDesc,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IPendingScriptLoad_FormatStringOffsetTable[-3],
    0,
    0,
    0
    };


static const MIDL_SERVER_INFO IPendingScriptLoad_ServerInfo = 
    {
    &Object_StubDesc,
    0,
    IECrossfireServer__MIDL_ProcFormatString.Format,
    &IPendingScriptLoad_FormatStringOffsetTable[-3],
    0,
    0,
    0,
    0};
CINTERFACE_PROXY_VTABLE(7) _IPendingScriptLoadProxyVtbl = 
{
    0,
    &IID_IPendingScriptLoad,
    IUnknown_QueryInterface_Proxy,
    IUnknown_AddRef_Proxy,
    IUnknown_Release_Proxy ,
    0 /* IDispatch::GetTypeInfoCount */ ,
    0 /* IDispatch::GetTypeInfo */ ,
    0 /* IDispatch::GetIDsOfNames */ ,
    0 /* IDispatch_Invoke_Proxy */
};


static const PRPC_STUB_FUNCTION IPendingScriptLoad_table[] =
{
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION,
    STUB_FORWARDING_FUNCTION
};

CInterfaceStubVtbl _IPendingScriptLoadStubVtbl =
{
    &IID_IPendingScriptLoad,
    &IPendingScriptLoad_ServerInfo,
    7,
    &IPendingScriptLoad_table[-3],
    CStdStubBuffer_DELEGATING_METHODS
};

static const MIDL_STUB_DESC Object_StubDesc = 
    {
    0,
    NdrOleAllocate,
    NdrOleFree,
    0,
    0,
    0,
    0,
    0,
    IECrossfireServer__MIDL_TypeFormatString.Format,
    1, /* -error bounds_check flag */
    0x20000, /* Ndr library version */
    0,
    0x700022b, /* MIDL Version 7.0.555 */
    0,
    0,
    0,  /* notify & notify_flag routine table */
    0x1, /* MIDL flag */
    0, /* cs routines */
    0,   /* proxy/server info */
    0
    };

const CInterfaceProxyVtbl * const _IECrossfireServer_ProxyVtblList[] = 
{
    ( CInterfaceProxyVtbl *) &_IBrowserContextProxyVtbl,
    ( CInterfaceProxyVtbl *) &_ICrossfireServerProxyVtbl,
    ( CInterfaceProxyVtbl *) &_IIEDebuggerProxyVtbl,
    ( CInterfaceProxyVtbl *) &_IPendingScriptLoadProxyVtbl,
    ( CInterfaceProxyVtbl *) &_ICrossfireServerClassProxyVtbl,
    ( CInterfaceProxyVtbl *) &_IJSEvalCallbackProxyVtbl,
    0
};

const CInterfaceStubVtbl * const _IECrossfireServer_StubVtblList[] = 
{
    ( CInterfaceStubVtbl *) &_IBrowserContextStubVtbl,
    ( CInterfaceStubVtbl *) &_ICrossfireServerStubVtbl,
    ( CInterfaceStubVtbl *) &_IIEDebuggerStubVtbl,
    ( CInterfaceStubVtbl *) &_IPendingScriptLoadStubVtbl,
    ( CInterfaceStubVtbl *) &_ICrossfireServerClassStubVtbl,
    ( CInterfaceStubVtbl *) &_IJSEvalCallbackStubVtbl,
    0
};

PCInterfaceName const _IECrossfireServer_InterfaceNamesList[] = 
{
    "IBrowserContext",
    "ICrossfireServer",
    "IIEDebugger",
    "IPendingScriptLoad",
    "ICrossfireServerClass",
    "IJSEvalCallback",
    0
};

const IID *  const _IECrossfireServer_BaseIIDList[] = 
{
    0,
    &IID_IDispatch,
    0,
    &IID_IDispatch,
    &IID_IDispatch,
    0,
    0
};


#define _IECrossfireServer_CHECK_IID(n)	IID_GENERIC_CHECK_IID( _IECrossfireServer, pIID, n)

int __stdcall _IECrossfireServer_IID_Lookup( const IID * pIID, int * pIndex )
{
    IID_BS_LOOKUP_SETUP

    IID_BS_LOOKUP_INITIAL_TEST( _IECrossfireServer, 6, 4 )
    IID_BS_LOOKUP_NEXT_TEST( _IECrossfireServer, 2 )
    IID_BS_LOOKUP_NEXT_TEST( _IECrossfireServer, 1 )
    IID_BS_LOOKUP_RETURN_RESULT( _IECrossfireServer, 6, *pIndex )
    
}

const ExtendedProxyFileInfo IECrossfireServer_ProxyFileInfo = 
{
    (PCInterfaceProxyVtblList *) & _IECrossfireServer_ProxyVtblList,
    (PCInterfaceStubVtblList *) & _IECrossfireServer_StubVtblList,
    (const PCInterfaceName * ) & _IECrossfireServer_InterfaceNamesList,
    (const IID ** ) & _IECrossfireServer_BaseIIDList,
    & _IECrossfireServer_IID_Lookup, 
    6,
    2,
    0, /* table of [async_uuid] interfaces */
    0, /* Filler1 */
    0, /* Filler2 */
    0  /* Filler3 */
};
#pragma optimize("", on )
#if _MSC_VER >= 1200
#pragma warning(pop)
#endif


#endif /* !defined(_M_IA64) && !defined(_M_AMD64)*/

