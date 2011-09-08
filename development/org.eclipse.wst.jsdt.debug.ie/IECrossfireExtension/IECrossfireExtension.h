

/* this ALWAYS GENERATED file contains the definitions for the interfaces */


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


/* verify that the <rpcndr.h> version is high enough to compile this file*/
#ifndef __REQUIRED_RPCNDR_H_VERSION__
#define __REQUIRED_RPCNDR_H_VERSION__ 440
#endif

#include "rpc.h"
#include "rpcndr.h"

#ifndef __RPCNDR_H_VERSION__
#error this stub requires an updated version of <rpcndr.h>
#endif // __RPCNDR_H_VERSION__

#ifndef COM_NO_WINDOWS_H
#include "windows.h"
#include "ole2.h"
#endif /*COM_NO_WINDOWS_H*/

#ifndef __IECrossfireExtension_h__
#define __IECrossfireExtension_h__

#if defined(_MSC_VER) && (_MSC_VER >= 1020)
#pragma once
#endif

/* Forward Declarations */ 

#ifndef __IIECrossfireBHO_FWD_DEFINED__
#define __IIECrossfireBHO_FWD_DEFINED__
typedef interface IIECrossfireBHO IIECrossfireBHO;
#endif 	/* __IIECrossfireBHO_FWD_DEFINED__ */


#ifndef __IBrowserContext_FWD_DEFINED__
#define __IBrowserContext_FWD_DEFINED__
typedef interface IBrowserContext IBrowserContext;
#endif 	/* __IBrowserContext_FWD_DEFINED__ */


#ifndef __ICrossfireServer_FWD_DEFINED__
#define __ICrossfireServer_FWD_DEFINED__
typedef interface ICrossfireServer ICrossfireServer;
#endif 	/* __ICrossfireServer_FWD_DEFINED__ */


#ifndef __ICrossfireServerClass_FWD_DEFINED__
#define __ICrossfireServerClass_FWD_DEFINED__
typedef interface ICrossfireServerClass ICrossfireServerClass;
#endif 	/* __ICrossfireServerClass_FWD_DEFINED__ */


#ifndef __IExplorerBar_FWD_DEFINED__
#define __IExplorerBar_FWD_DEFINED__
typedef interface IExplorerBar IExplorerBar;
#endif 	/* __IExplorerBar_FWD_DEFINED__ */


#ifndef __IECrossfireBHO_FWD_DEFINED__
#define __IECrossfireBHO_FWD_DEFINED__

#ifdef __cplusplus
typedef class IECrossfireBHO IECrossfireBHO;
#else
typedef struct IECrossfireBHO IECrossfireBHO;
#endif /* __cplusplus */

#endif 	/* __IECrossfireBHO_FWD_DEFINED__ */


#ifndef __ExplorerBar_FWD_DEFINED__
#define __ExplorerBar_FWD_DEFINED__

#ifdef __cplusplus
typedef class ExplorerBar ExplorerBar;
#else
typedef struct ExplorerBar ExplorerBar;
#endif /* __cplusplus */

#endif 	/* __ExplorerBar_FWD_DEFINED__ */


#ifndef __CrossfireServer_FWD_DEFINED__
#define __CrossfireServer_FWD_DEFINED__

#ifdef __cplusplus
typedef class CrossfireServer CrossfireServer;
#else
typedef struct CrossfireServer CrossfireServer;
#endif /* __cplusplus */

#endif 	/* __CrossfireServer_FWD_DEFINED__ */


#ifndef __CrossfireServerClass_FWD_DEFINED__
#define __CrossfireServerClass_FWD_DEFINED__

#ifdef __cplusplus
typedef class CrossfireServerClass CrossfireServerClass;
#else
typedef struct CrossfireServerClass CrossfireServerClass;
#endif /* __cplusplus */

#endif 	/* __CrossfireServerClass_FWD_DEFINED__ */


#ifndef __BrowserContext_FWD_DEFINED__
#define __BrowserContext_FWD_DEFINED__

#ifdef __cplusplus
typedef class BrowserContext BrowserContext;
#else
typedef struct BrowserContext BrowserContext;
#endif /* __cplusplus */

#endif 	/* __BrowserContext_FWD_DEFINED__ */


/* header files for imported files */
#include "oaidl.h"
#include "ocidl.h"

#ifdef __cplusplus
extern "C"{
#endif 


#ifndef __IIECrossfireBHO_INTERFACE_DEFINED__
#define __IIECrossfireBHO_INTERFACE_DEFINED__

/* interface IIECrossfireBHO */
/* [unique][helpstring][nonextensible][oleautomation][uuid][object] */ 


EXTERN_C const IID IID_IIECrossfireBHO;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("201244D7-94C6-4fb0-9948-2634523A475B")
    IIECrossfireBHO : public IUnknown
    {
    public:
    };
    
#else 	/* C style interface */

    typedef struct IIECrossfireBHOVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            IIECrossfireBHO * This,
            /* [in] */ REFIID riid,
            /* [annotation][iid_is][out] */ 
            __RPC__deref_out  void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            IIECrossfireBHO * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            IIECrossfireBHO * This);
        
        END_INTERFACE
    } IIECrossfireBHOVtbl;

    interface IIECrossfireBHO
    {
        CONST_VTBL struct IIECrossfireBHOVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define IIECrossfireBHO_QueryInterface(This,riid,ppvObject)	\
    ( (This)->lpVtbl -> QueryInterface(This,riid,ppvObject) ) 

#define IIECrossfireBHO_AddRef(This)	\
    ( (This)->lpVtbl -> AddRef(This) ) 

#define IIECrossfireBHO_Release(This)	\
    ( (This)->lpVtbl -> Release(This) ) 


#endif /* COBJMACROS */


#endif 	/* C style interface */




#endif 	/* __IIECrossfireBHO_INTERFACE_DEFINED__ */


#ifndef __IBrowserContext_INTERFACE_DEFINED__
#define __IBrowserContext_INTERFACE_DEFINED__

/* interface IBrowserContext */
/* [unique][helpstring][nonextensible][dual][oleautomation][uuid][object] */ 


EXTERN_C const IID IID_IBrowserContext;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("E4121804-5350-4DDD-BE57-9C5B2A13EA29")
    IBrowserContext : public IUnknown
    {
    public:
        virtual HRESULT STDMETHODCALLTYPE navigate( 
            /* [string][in] */ OLECHAR *url,
            /* [in] */ boolean openNewTab) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct IBrowserContextVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            IBrowserContext * This,
            /* [in] */ REFIID riid,
            /* [annotation][iid_is][out] */ 
            __RPC__deref_out  void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            IBrowserContext * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            IBrowserContext * This);
        
        HRESULT ( STDMETHODCALLTYPE *navigate )( 
            IBrowserContext * This,
            /* [string][in] */ OLECHAR *url,
            /* [in] */ boolean openNewTab);
        
        END_INTERFACE
    } IBrowserContextVtbl;

    interface IBrowserContext
    {
        CONST_VTBL struct IBrowserContextVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define IBrowserContext_QueryInterface(This,riid,ppvObject)	\
    ( (This)->lpVtbl -> QueryInterface(This,riid,ppvObject) ) 

#define IBrowserContext_AddRef(This)	\
    ( (This)->lpVtbl -> AddRef(This) ) 

#define IBrowserContext_Release(This)	\
    ( (This)->lpVtbl -> Release(This) ) 


#define IBrowserContext_navigate(This,url,openNewTab)	\
    ( (This)->lpVtbl -> navigate(This,url,openNewTab) ) 

#endif /* COBJMACROS */


#endif 	/* C style interface */




#endif 	/* __IBrowserContext_INTERFACE_DEFINED__ */


#ifndef __ICrossfireServer_INTERFACE_DEFINED__
#define __ICrossfireServer_INTERFACE_DEFINED__

/* interface ICrossfireServer */
/* [unique][helpstring][nonextensible][dual][oleautomation][uuid][object] */ 


EXTERN_C const IID IID_ICrossfireServer;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("031DB015-B1BE-4D39-84D2-D7F96D2ACBFE")
    ICrossfireServer : public IDispatch
    {
    public:
        virtual HRESULT STDMETHODCALLTYPE contextCreated( 
            /* [in] */ DWORD processId,
            /* [string][in] */ OLECHAR *url) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE contextDestroyed( 
            /* [in] */ DWORD processId) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE contextLoaded( 
            /* [in] */ DWORD processId) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE getPort( 
            /* [out] */ unsigned int *value) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE getState( 
            /* [out] */ int *value) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE registerBrowser( 
            /* [in] */ DWORD processId,
            /* [in] */ IBrowserContext *browser) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE removeBrowser( 
            /* [in] */ DWORD processId) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE setCurrentContext( 
            /* [in] */ DWORD processId) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE start( 
            /* [in] */ unsigned int port,
            /* [in] */ unsigned int debugPort) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE stop( void) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct ICrossfireServerVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            ICrossfireServer * This,
            /* [in] */ REFIID riid,
            /* [annotation][iid_is][out] */ 
            __RPC__deref_out  void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            ICrossfireServer * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            ICrossfireServer * This);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfoCount )( 
            ICrossfireServer * This,
            /* [out] */ UINT *pctinfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfo )( 
            ICrossfireServer * This,
            /* [in] */ UINT iTInfo,
            /* [in] */ LCID lcid,
            /* [out] */ ITypeInfo **ppTInfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetIDsOfNames )( 
            ICrossfireServer * This,
            /* [in] */ REFIID riid,
            /* [size_is][in] */ LPOLESTR *rgszNames,
            /* [range][in] */ UINT cNames,
            /* [in] */ LCID lcid,
            /* [size_is][out] */ DISPID *rgDispId);
        
        /* [local] */ HRESULT ( STDMETHODCALLTYPE *Invoke )( 
            ICrossfireServer * This,
            /* [in] */ DISPID dispIdMember,
            /* [in] */ REFIID riid,
            /* [in] */ LCID lcid,
            /* [in] */ WORD wFlags,
            /* [out][in] */ DISPPARAMS *pDispParams,
            /* [out] */ VARIANT *pVarResult,
            /* [out] */ EXCEPINFO *pExcepInfo,
            /* [out] */ UINT *puArgErr);
        
        HRESULT ( STDMETHODCALLTYPE *contextCreated )( 
            ICrossfireServer * This,
            /* [in] */ DWORD processId,
            /* [string][in] */ OLECHAR *url);
        
        HRESULT ( STDMETHODCALLTYPE *contextDestroyed )( 
            ICrossfireServer * This,
            /* [in] */ DWORD processId);
        
        HRESULT ( STDMETHODCALLTYPE *contextLoaded )( 
            ICrossfireServer * This,
            /* [in] */ DWORD processId);
        
        HRESULT ( STDMETHODCALLTYPE *getPort )( 
            ICrossfireServer * This,
            /* [out] */ unsigned int *value);
        
        HRESULT ( STDMETHODCALLTYPE *getState )( 
            ICrossfireServer * This,
            /* [out] */ int *value);
        
        HRESULT ( STDMETHODCALLTYPE *registerBrowser )( 
            ICrossfireServer * This,
            /* [in] */ DWORD processId,
            /* [in] */ IBrowserContext *browser);
        
        HRESULT ( STDMETHODCALLTYPE *removeBrowser )( 
            ICrossfireServer * This,
            /* [in] */ DWORD processId);
        
        HRESULT ( STDMETHODCALLTYPE *setCurrentContext )( 
            ICrossfireServer * This,
            /* [in] */ DWORD processId);
        
        HRESULT ( STDMETHODCALLTYPE *start )( 
            ICrossfireServer * This,
            /* [in] */ unsigned int port,
            /* [in] */ unsigned int debugPort);
        
        HRESULT ( STDMETHODCALLTYPE *stop )( 
            ICrossfireServer * This);
        
        END_INTERFACE
    } ICrossfireServerVtbl;

    interface ICrossfireServer
    {
        CONST_VTBL struct ICrossfireServerVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define ICrossfireServer_QueryInterface(This,riid,ppvObject)	\
    ( (This)->lpVtbl -> QueryInterface(This,riid,ppvObject) ) 

#define ICrossfireServer_AddRef(This)	\
    ( (This)->lpVtbl -> AddRef(This) ) 

#define ICrossfireServer_Release(This)	\
    ( (This)->lpVtbl -> Release(This) ) 


#define ICrossfireServer_GetTypeInfoCount(This,pctinfo)	\
    ( (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo) ) 

#define ICrossfireServer_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    ( (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo) ) 

#define ICrossfireServer_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    ( (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId) ) 

#define ICrossfireServer_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    ( (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr) ) 


#define ICrossfireServer_contextCreated(This,processId,url)	\
    ( (This)->lpVtbl -> contextCreated(This,processId,url) ) 

#define ICrossfireServer_contextDestroyed(This,processId)	\
    ( (This)->lpVtbl -> contextDestroyed(This,processId) ) 

#define ICrossfireServer_contextLoaded(This,processId)	\
    ( (This)->lpVtbl -> contextLoaded(This,processId) ) 

#define ICrossfireServer_getPort(This,value)	\
    ( (This)->lpVtbl -> getPort(This,value) ) 

#define ICrossfireServer_getState(This,value)	\
    ( (This)->lpVtbl -> getState(This,value) ) 

#define ICrossfireServer_registerBrowser(This,processId,browser)	\
    ( (This)->lpVtbl -> registerBrowser(This,processId,browser) ) 

#define ICrossfireServer_removeBrowser(This,processId)	\
    ( (This)->lpVtbl -> removeBrowser(This,processId) ) 

#define ICrossfireServer_setCurrentContext(This,processId)	\
    ( (This)->lpVtbl -> setCurrentContext(This,processId) ) 

#define ICrossfireServer_start(This,port,debugPort)	\
    ( (This)->lpVtbl -> start(This,port,debugPort) ) 

#define ICrossfireServer_stop(This)	\
    ( (This)->lpVtbl -> stop(This) ) 

#endif /* COBJMACROS */


#endif 	/* C style interface */




#endif 	/* __ICrossfireServer_INTERFACE_DEFINED__ */


#ifndef __ICrossfireServerClass_INTERFACE_DEFINED__
#define __ICrossfireServerClass_INTERFACE_DEFINED__

/* interface ICrossfireServerClass */
/* [unique][helpstring][nonextensible][dual][oleautomation][uuid][object] */ 


EXTERN_C const IID IID_ICrossfireServerClass;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("F48260BB-C061-4410-9CE1-4C5C7602690E")
    ICrossfireServerClass : public IDispatch
    {
    public:
        virtual HRESULT STDMETHODCALLTYPE GetServer( 
            /* [retval][out] */ ICrossfireServer **_value) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE RemoveServer( 
            /* [in] */ unsigned long windowHandle) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct ICrossfireServerClassVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            ICrossfireServerClass * This,
            /* [in] */ REFIID riid,
            /* [annotation][iid_is][out] */ 
            __RPC__deref_out  void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            ICrossfireServerClass * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            ICrossfireServerClass * This);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfoCount )( 
            ICrossfireServerClass * This,
            /* [out] */ UINT *pctinfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfo )( 
            ICrossfireServerClass * This,
            /* [in] */ UINT iTInfo,
            /* [in] */ LCID lcid,
            /* [out] */ ITypeInfo **ppTInfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetIDsOfNames )( 
            ICrossfireServerClass * This,
            /* [in] */ REFIID riid,
            /* [size_is][in] */ LPOLESTR *rgszNames,
            /* [range][in] */ UINT cNames,
            /* [in] */ LCID lcid,
            /* [size_is][out] */ DISPID *rgDispId);
        
        /* [local] */ HRESULT ( STDMETHODCALLTYPE *Invoke )( 
            ICrossfireServerClass * This,
            /* [in] */ DISPID dispIdMember,
            /* [in] */ REFIID riid,
            /* [in] */ LCID lcid,
            /* [in] */ WORD wFlags,
            /* [out][in] */ DISPPARAMS *pDispParams,
            /* [out] */ VARIANT *pVarResult,
            /* [out] */ EXCEPINFO *pExcepInfo,
            /* [out] */ UINT *puArgErr);
        
        HRESULT ( STDMETHODCALLTYPE *GetServer )( 
            ICrossfireServerClass * This,
            /* [retval][out] */ ICrossfireServer **_value);
        
        HRESULT ( STDMETHODCALLTYPE *RemoveServer )( 
            ICrossfireServerClass * This,
            /* [in] */ unsigned long windowHandle);
        
        END_INTERFACE
    } ICrossfireServerClassVtbl;

    interface ICrossfireServerClass
    {
        CONST_VTBL struct ICrossfireServerClassVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define ICrossfireServerClass_QueryInterface(This,riid,ppvObject)	\
    ( (This)->lpVtbl -> QueryInterface(This,riid,ppvObject) ) 

#define ICrossfireServerClass_AddRef(This)	\
    ( (This)->lpVtbl -> AddRef(This) ) 

#define ICrossfireServerClass_Release(This)	\
    ( (This)->lpVtbl -> Release(This) ) 


#define ICrossfireServerClass_GetTypeInfoCount(This,pctinfo)	\
    ( (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo) ) 

#define ICrossfireServerClass_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    ( (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo) ) 

#define ICrossfireServerClass_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    ( (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId) ) 

#define ICrossfireServerClass_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    ( (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr) ) 


#define ICrossfireServerClass_GetServer(This,_value)	\
    ( (This)->lpVtbl -> GetServer(This,_value) ) 

#define ICrossfireServerClass_RemoveServer(This,windowHandle)	\
    ( (This)->lpVtbl -> RemoveServer(This,windowHandle) ) 

#endif /* COBJMACROS */


#endif 	/* C style interface */




#endif 	/* __ICrossfireServerClass_INTERFACE_DEFINED__ */


#ifndef __IExplorerBar_INTERFACE_DEFINED__
#define __IExplorerBar_INTERFACE_DEFINED__

/* interface IExplorerBar */
/* [unique][helpstring][nonextensible][dual][uuid][object] */ 


EXTERN_C const IID IID_IExplorerBar;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("72BA7A37-6D18-439D-8A42-5F6A4F2CD3C3")
    IExplorerBar : public IDispatch
    {
    public:
    };
    
#else 	/* C style interface */

    typedef struct IExplorerBarVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            IExplorerBar * This,
            /* [in] */ REFIID riid,
            /* [annotation][iid_is][out] */ 
            __RPC__deref_out  void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            IExplorerBar * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            IExplorerBar * This);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfoCount )( 
            IExplorerBar * This,
            /* [out] */ UINT *pctinfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetTypeInfo )( 
            IExplorerBar * This,
            /* [in] */ UINT iTInfo,
            /* [in] */ LCID lcid,
            /* [out] */ ITypeInfo **ppTInfo);
        
        HRESULT ( STDMETHODCALLTYPE *GetIDsOfNames )( 
            IExplorerBar * This,
            /* [in] */ REFIID riid,
            /* [size_is][in] */ LPOLESTR *rgszNames,
            /* [range][in] */ UINT cNames,
            /* [in] */ LCID lcid,
            /* [size_is][out] */ DISPID *rgDispId);
        
        /* [local] */ HRESULT ( STDMETHODCALLTYPE *Invoke )( 
            IExplorerBar * This,
            /* [in] */ DISPID dispIdMember,
            /* [in] */ REFIID riid,
            /* [in] */ LCID lcid,
            /* [in] */ WORD wFlags,
            /* [out][in] */ DISPPARAMS *pDispParams,
            /* [out] */ VARIANT *pVarResult,
            /* [out] */ EXCEPINFO *pExcepInfo,
            /* [out] */ UINT *puArgErr);
        
        END_INTERFACE
    } IExplorerBarVtbl;

    interface IExplorerBar
    {
        CONST_VTBL struct IExplorerBarVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define IExplorerBar_QueryInterface(This,riid,ppvObject)	\
    ( (This)->lpVtbl -> QueryInterface(This,riid,ppvObject) ) 

#define IExplorerBar_AddRef(This)	\
    ( (This)->lpVtbl -> AddRef(This) ) 

#define IExplorerBar_Release(This)	\
    ( (This)->lpVtbl -> Release(This) ) 


#define IExplorerBar_GetTypeInfoCount(This,pctinfo)	\
    ( (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo) ) 

#define IExplorerBar_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    ( (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo) ) 

#define IExplorerBar_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    ( (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId) ) 

#define IExplorerBar_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    ( (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr) ) 


#endif /* COBJMACROS */


#endif 	/* C style interface */




#endif 	/* __IExplorerBar_INTERFACE_DEFINED__ */



#ifndef __IECrossfireExtensionLib_LIBRARY_DEFINED__
#define __IECrossfireExtensionLib_LIBRARY_DEFINED__

/* library IECrossfireExtensionLib */
/* [helpstring][version][uuid] */ 


EXTERN_C const IID LIBID_IECrossfireExtensionLib;

EXTERN_C const CLSID CLSID_IECrossfireBHO;

#ifdef __cplusplus

class DECLSPEC_UUID("E8779887-5AF1-4071-B4D4-6135157F142C")
IECrossfireBHO;
#endif

EXTERN_C const CLSID CLSID_ExplorerBar;

#ifdef __cplusplus

class DECLSPEC_UUID("34EF57F8-9295-483E-B656-4EE154B0B3A5")
ExplorerBar;
#endif

EXTERN_C const CLSID CLSID_CrossfireServer;

#ifdef __cplusplus

class DECLSPEC_UUID("47836AF4-3E0C-4995-8029-FF931C5A43FC")
CrossfireServer;
#endif

EXTERN_C const CLSID CLSID_CrossfireServerClass;

#ifdef __cplusplus

class DECLSPEC_UUID("7C3C5D7A-AF4D-4F32-A3C9-462BFBAFDC25")
CrossfireServerClass;
#endif

EXTERN_C const CLSID CLSID_BrowserContext;

#ifdef __cplusplus

class DECLSPEC_UUID("2FA65B09-5063-45FA-91F9-50EB7F4AF2C6")
BrowserContext;
#endif
#endif /* __IECrossfireExtensionLib_LIBRARY_DEFINED__ */

/* Additional Prototypes for ALL interfaces */

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif


