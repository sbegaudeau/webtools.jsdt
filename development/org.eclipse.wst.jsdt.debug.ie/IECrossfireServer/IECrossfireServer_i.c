

/* this ALWAYS GENERATED file contains the IIDs and CLSIDs */

/* link this file in with the server and any clients */


 /* File created by MIDL compiler version 6.00.0366 */
/* at Fri Mar 11 10:39:23 2011
 */
/* Compiler settings for .\IECrossfireServer.idl:
    Oicf, W1, Zp8, env=Win32 (32b run)
    protocol : dce , ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
    VC __declspec() decoration level: 
         __declspec(uuid()), __declspec(selectany), __declspec(novtable)
         DECLSPEC_UUID(), MIDL_INTERFACE()
*/
//@@MIDL_FILE_HEADING(  )

#pragma warning( disable: 4049 )  /* more than 64k source lines */


#ifdef __cplusplus
extern "C"{
#endif 


#include <rpc.h>
#include <rpcndr.h>

#ifdef _MIDL_USE_GUIDDEF_

#ifndef INITGUID
#define INITGUID
#include <guiddef.h>
#undef INITGUID
#else
#include <guiddef.h>
#endif

#define MIDL_DEFINE_GUID(type,name,l,w1,w2,b1,b2,b3,b4,b5,b6,b7,b8) \
        DEFINE_GUID(name,l,w1,w2,b1,b2,b3,b4,b5,b6,b7,b8)

#else // !_MIDL_USE_GUIDDEF_

#ifndef __IID_DEFINED__
#define __IID_DEFINED__

typedef struct _IID
{
    unsigned long x;
    unsigned short s1;
    unsigned short s2;
    unsigned char  c[8];
} IID;

#endif // __IID_DEFINED__

#ifndef CLSID_DEFINED
#define CLSID_DEFINED
typedef IID CLSID;
#endif // CLSID_DEFINED

#define MIDL_DEFINE_GUID(type,name,l,w1,w2,b1,b2,b3,b4,b5,b6,b7,b8) \
        const type name = {l,w1,w2,{b1,b2,b3,b4,b5,b6,b7,b8}}

#endif !_MIDL_USE_GUIDDEF_

MIDL_DEFINE_GUID(IID, IID_IJSEvalCallback,0x31DC44C3,0x7C98,0x4737,0x8D,0x62,0x0E,0x78,0xFC,0xCC,0x50,0x3C);


MIDL_DEFINE_GUID(IID, IID_IIEDebugger,0x0F9DDD31,0x746A,0x4EBC,0x87,0x3D,0x7A,0x98,0x8B,0x5E,0xE0,0x88);


MIDL_DEFINE_GUID(IID, IID_ICrossfireServer,0x031DB015,0xB1BE,0x4D39,0x84,0xD2,0xD7,0xF9,0x6D,0x2A,0xCB,0xFE);


MIDL_DEFINE_GUID(IID, IID_ICrossfireServerClass,0xF48260BB,0xC061,0x4410,0x9C,0xE1,0x4C,0x5C,0x76,0x02,0x69,0x0E);


MIDL_DEFINE_GUID(IID, IID_IPendingBreakpoint,0x56C1AD62,0x3E69,0x46BA,0xBE,0x6D,0xE1,0xD2,0xA0,0xD8,0x8A,0xB5);


MIDL_DEFINE_GUID(IID, LIBID_IECrossfireServerLib,0xA8FFC284,0xCE2C,0x40B5,0x98,0xD1,0xD3,0x11,0x28,0x11,0xE9,0xD9);


MIDL_DEFINE_GUID(CLSID, CLSID_JSEvalCallback,0x4EC15DD3,0x9E2F,0x43A7,0xA6,0x86,0x3F,0xA9,0x0E,0x22,0xAB,0xB7);


MIDL_DEFINE_GUID(CLSID, CLSID_IEDebugger,0xB63BD92F,0x6C66,0x4AB2,0xA2,0x43,0xD5,0xAF,0xCC,0xF4,0xB5,0x87);


MIDL_DEFINE_GUID(CLSID, CLSID_CrossfireServer,0x47836AF4,0x3E0C,0x4995,0x80,0x29,0xFF,0x93,0x1C,0x5A,0x43,0xFC);


MIDL_DEFINE_GUID(CLSID, CLSID_CrossfireServerClass,0x7C3C5D7A,0xAF4D,0x4F32,0xA3,0xC9,0x46,0x2B,0xFB,0xAF,0xDC,0x25);


MIDL_DEFINE_GUID(CLSID, CLSID_PendingBreakpoint,0x88E7C480,0x7B7A,0x47C5,0x83,0x29,0xCE,0x1F,0xDF,0x41,0x55,0x27);

#undef MIDL_DEFINE_GUID

#ifdef __cplusplus
}
#endif



