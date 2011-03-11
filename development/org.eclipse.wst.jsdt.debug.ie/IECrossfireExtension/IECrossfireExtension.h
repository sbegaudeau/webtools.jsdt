

/* this ALWAYS GENERATED file contains the definitions for the interfaces */


 /* File created by MIDL compiler version 6.00.0366 */
/* at Fri Mar 11 10:39:44 2011
 */
/* Compiler settings for .\IECrossfireExtension.idl:
    Oicf, W1, Zp8, env=Win32 (32b run)
    protocol : dce , ms_ext, c_ext
    error checks: allocation ref bounds_check enum stub_data 
    VC __declspec() decoration level: 
         __declspec(uuid()), __declspec(selectany), __declspec(novtable)
         DECLSPEC_UUID(), MIDL_INTERFACE()
*/
//@@MIDL_FILE_HEADING(  )

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


#ifndef __ICrossfireServer_FWD_DEFINED__
#define __ICrossfireServer_FWD_DEFINED__
typedef interface ICrossfireServer ICrossfireServer;
#endif 	/* __ICrossfireServer_FWD_DEFINED__ */


#ifndef __ICrossfireServerClass_FWD_DEFINED__
#define __ICrossfireServerClass_FWD_DEFINED__
typedef interface ICrossfireServerClass ICrossfireServerClass;
#endif 	/* __ICrossfireServerClass_FWD_DEFINED__ */


#ifndef __IECrossfireBHO_FWD_DEFINED__
#define __IECrossfireBHO_FWD_DEFINED__

#ifdef __cplusplus
typedef class IECrossfireBHO IECrossfireBHO;
#else
typedef struct IECrossfireBHO IECrossfireBHO;
#endif /* __cplusplus */

#endif 	/* __IECrossfireBHO_FWD_DEFINED__ */


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


/* header files for imported files */
#include "oaidl.h"
#include "ocidl.h"

#ifdef __cplusplus
extern "C"{
#endif 

void * __RPC_USER MIDL_user_allocate(size_t);
void __RPC_USER MIDL_user_free( void * ); 

#ifndef __IIECrossfireBHO_INTERFACE_DEFINED__
#define __IIECrossfireBHO_INTERFACE_DEFINED__

/* interface IIECrossfireBHO */
/* [unique][helpstring][nonextensible][dual][uuid][object] */ 


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
            /* [iid_is][out] */ void **ppvObject);
        
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
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define IIECrossfireBHO_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define IIECrossfireBHO_Release(This)	\
    (This)->lpVtbl -> Release(This)


#endif /* COBJMACROS */


#endif 	/* C style interface */




#endif 	/* __IIECrossfireBHO_INTERFACE_DEFINED__ */


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
            /* [in] */ DWORD threadId,
            /* [string][in] */ OLECHAR *href) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE contextDestroyed( 
            /* [in] */ DWORD threadId) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE contextLoaded( 
            /* [in] */ DWORD threadId) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE isActive( 
            /* [out] */ boolean *value) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE registerContext( 
            /* [in] */ DWORD threadId,
            /* [string][in] */ OLECHAR *href) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE setCurrentContext( 
            /* [in] */ DWORD threadId) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE start( 
            /* [in] */ unsigned int port) = 0;
        
        virtual HRESULT STDMETHODCALLTYPE stop( void) = 0;
        
    };
    
#else 	/* C style interface */

    typedef struct ICrossfireServerVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            ICrossfireServer * This,
            /* [in] */ REFIID riid,
            /* [iid_is][out] */ void **ppvObject);
        
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
            /* [in] */ UINT cNames,
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
            /* [in] */ DWORD threadId,
            /* [string][in] */ OLECHAR *href);
        
        HRESULT ( STDMETHODCALLTYPE *contextDestroyed )( 
            ICrossfireServer * This,
            /* [in] */ DWORD threadId);
        
        HRESULT ( STDMETHODCALLTYPE *contextLoaded )( 
            ICrossfireServer * This,
            /* [in] */ DWORD threadId);
        
        HRESULT ( STDMETHODCALLTYPE *isActive )( 
            ICrossfireServer * This,
            /* [out] */ boolean *value);
        
        HRESULT ( STDMETHODCALLTYPE *registerContext )( 
            ICrossfireServer * This,
            /* [in] */ DWORD threadId,
            /* [string][in] */ OLECHAR *href);
        
        HRESULT ( STDMETHODCALLTYPE *setCurrentContext )( 
            ICrossfireServer * This,
            /* [in] */ DWORD threadId);
        
        HRESULT ( STDMETHODCALLTYPE *start )( 
            ICrossfireServer * This,
            /* [in] */ unsigned int port);
        
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
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define ICrossfireServer_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define ICrossfireServer_Release(This)	\
    (This)->lpVtbl -> Release(This)


#define ICrossfireServer_GetTypeInfoCount(This,pctinfo)	\
    (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo)

#define ICrossfireServer_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo)

#define ICrossfireServer_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)

#define ICrossfireServer_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)


#define ICrossfireServer_contextCreated(This,threadId,href)	\
    (This)->lpVtbl -> contextCreated(This,threadId,href)

#define ICrossfireServer_contextDestroyed(This,threadId)	\
    (This)->lpVtbl -> contextDestroyed(This,threadId)

#define ICrossfireServer_contextLoaded(This,threadId)	\
    (This)->lpVtbl -> contextLoaded(This,threadId)

#define ICrossfireServer_isActive(This,value)	\
    (This)->lpVtbl -> isActive(This,value)

#define ICrossfireServer_registerContext(This,threadId,href)	\
    (This)->lpVtbl -> registerContext(This,threadId,href)

#define ICrossfireServer_setCurrentContext(This,threadId)	\
    (This)->lpVtbl -> setCurrentContext(This,threadId)

#define ICrossfireServer_start(This,port)	\
    (This)->lpVtbl -> start(This,port)

#define ICrossfireServer_stop(This)	\
    (This)->lpVtbl -> stop(This)

#endif /* COBJMACROS */


#endif 	/* C style interface */



HRESULT STDMETHODCALLTYPE ICrossfireServer_contextCreated_Proxy( 
    ICrossfireServer * This,
    /* [in] */ DWORD threadId,
    /* [string][in] */ OLECHAR *href);


void __RPC_STUB ICrossfireServer_contextCreated_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServer_contextDestroyed_Proxy( 
    ICrossfireServer * This,
    /* [in] */ DWORD threadId);


void __RPC_STUB ICrossfireServer_contextDestroyed_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServer_contextLoaded_Proxy( 
    ICrossfireServer * This,
    /* [in] */ DWORD threadId);


void __RPC_STUB ICrossfireServer_contextLoaded_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServer_isActive_Proxy( 
    ICrossfireServer * This,
    /* [out] */ boolean *value);


void __RPC_STUB ICrossfireServer_isActive_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServer_registerContext_Proxy( 
    ICrossfireServer * This,
    /* [in] */ DWORD threadId,
    /* [string][in] */ OLECHAR *href);


void __RPC_STUB ICrossfireServer_registerContext_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServer_setCurrentContext_Proxy( 
    ICrossfireServer * This,
    /* [in] */ DWORD threadId);


void __RPC_STUB ICrossfireServer_setCurrentContext_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServer_start_Proxy( 
    ICrossfireServer * This,
    /* [in] */ unsigned int port);


void __RPC_STUB ICrossfireServer_start_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServer_stop_Proxy( 
    ICrossfireServer * This);


void __RPC_STUB ICrossfireServer_stop_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);



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
            /* [in] */ unsigned long windowHandle,
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
            /* [iid_is][out] */ void **ppvObject);
        
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
            /* [in] */ UINT cNames,
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
            /* [in] */ unsigned long windowHandle,
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
    (This)->lpVtbl -> QueryInterface(This,riid,ppvObject)

#define ICrossfireServerClass_AddRef(This)	\
    (This)->lpVtbl -> AddRef(This)

#define ICrossfireServerClass_Release(This)	\
    (This)->lpVtbl -> Release(This)


#define ICrossfireServerClass_GetTypeInfoCount(This,pctinfo)	\
    (This)->lpVtbl -> GetTypeInfoCount(This,pctinfo)

#define ICrossfireServerClass_GetTypeInfo(This,iTInfo,lcid,ppTInfo)	\
    (This)->lpVtbl -> GetTypeInfo(This,iTInfo,lcid,ppTInfo)

#define ICrossfireServerClass_GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)	\
    (This)->lpVtbl -> GetIDsOfNames(This,riid,rgszNames,cNames,lcid,rgDispId)

#define ICrossfireServerClass_Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)	\
    (This)->lpVtbl -> Invoke(This,dispIdMember,riid,lcid,wFlags,pDispParams,pVarResult,pExcepInfo,puArgErr)


#define ICrossfireServerClass_GetServer(This,windowHandle,_value)	\
    (This)->lpVtbl -> GetServer(This,windowHandle,_value)

#define ICrossfireServerClass_RemoveServer(This,windowHandle)	\
    (This)->lpVtbl -> RemoveServer(This,windowHandle)

#endif /* COBJMACROS */


#endif 	/* C style interface */



HRESULT STDMETHODCALLTYPE ICrossfireServerClass_GetServer_Proxy( 
    ICrossfireServerClass * This,
    /* [in] */ unsigned long windowHandle,
    /* [retval][out] */ ICrossfireServer **_value);


void __RPC_STUB ICrossfireServerClass_GetServer_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);


HRESULT STDMETHODCALLTYPE ICrossfireServerClass_RemoveServer_Proxy( 
    ICrossfireServerClass * This,
    /* [in] */ unsigned long windowHandle);


void __RPC_STUB ICrossfireServerClass_RemoveServer_Stub(
    IRpcStubBuffer *This,
    IRpcChannelBuffer *_pRpcChannelBuffer,
    PRPC_MESSAGE _pRpcMessage,
    DWORD *_pdwStubPhase);



#endif 	/* __ICrossfireServerClass_INTERFACE_DEFINED__ */



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
#endif /* __IECrossfireExtensionLib_LIBRARY_DEFINED__ */

/* Additional Prototypes for ALL interfaces */

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif


