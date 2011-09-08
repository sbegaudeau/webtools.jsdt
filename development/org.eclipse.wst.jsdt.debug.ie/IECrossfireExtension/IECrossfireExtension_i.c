

/* this ALWAYS GENERATED file contains the IIDs and CLSIDs */

/* link this file in with the server and any clients */


 /* File created by MIDL compiler version 7.00.0555 */
/* at Thu Sep 08 00:09:45 2011
 */
/* Compiler settings for IECrossfireExtension.idl:
    Oicf, W1, Zp8, env=Win32 (32b run), target_arch=X86 7.00.0555 
    protocol : dce , ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
    VC __declspec() decoration level: 
         __declspec(uuid()), __declspec(selectany), __declspec(novtable)
         DECLSPEC_UUID(), MIDL_INTERFACE()
*/
/* @@MIDL_FILE_HEADING(  ) */

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

MIDL_DEFINE_GUID(IID, IID_IIECrossfireBHO,0x201244D7,0x94C6,0x4fb0,0x99,0x48,0x26,0x34,0x52,0x3A,0x47,0x5B);


MIDL_DEFINE_GUID(IID, IID_IBrowserContext,0xE4121804,0x5350,0x4DDD,0xBE,0x57,0x9C,0x5B,0x2A,0x13,0xEA,0x29);


MIDL_DEFINE_GUID(IID, IID_ICrossfireServer,0x031DB015,0xB1BE,0x4D39,0x84,0xD2,0xD7,0xF9,0x6D,0x2A,0xCB,0xFE);


MIDL_DEFINE_GUID(IID, IID_ICrossfireServerClass,0xF48260BB,0xC061,0x4410,0x9C,0xE1,0x4C,0x5C,0x76,0x02,0x69,0x0E);


MIDL_DEFINE_GUID(IID, IID_IExplorerBar,0x72BA7A37,0x6D18,0x439D,0x8A,0x42,0x5F,0x6A,0x4F,0x2C,0xD3,0xC3);


MIDL_DEFINE_GUID(IID, LIBID_IECrossfireExtensionLib,0xA8FFC284,0xCE2C,0x40B5,0x98,0xD1,0xD3,0x11,0x28,0x11,0xE9,0xD9);


MIDL_DEFINE_GUID(CLSID, CLSID_IECrossfireBHO,0xE8779887,0x5AF1,0x4071,0xB4,0xD4,0x61,0x35,0x15,0x7F,0x14,0x2C);


MIDL_DEFINE_GUID(CLSID, CLSID_ExplorerBar,0x34EF57F8,0x9295,0x483E,0xB6,0x56,0x4E,0xE1,0x54,0xB0,0xB3,0xA5);


MIDL_DEFINE_GUID(CLSID, CLSID_CrossfireServer,0x47836AF4,0x3E0C,0x4995,0x80,0x29,0xFF,0x93,0x1C,0x5A,0x43,0xFC);


MIDL_DEFINE_GUID(CLSID, CLSID_CrossfireServerClass,0x7C3C5D7A,0xAF4D,0x4F32,0xA3,0xC9,0x46,0x2B,0xFB,0xAF,0xDC,0x25);


MIDL_DEFINE_GUID(CLSID, CLSID_BrowserContext,0x2FA65B09,0x5063,0x45FA,0x91,0xF9,0x50,0xEB,0x7F,0x4A,0xF2,0xC6);

#undef MIDL_DEFINE_GUID

#ifdef __cplusplus
}
#endif



