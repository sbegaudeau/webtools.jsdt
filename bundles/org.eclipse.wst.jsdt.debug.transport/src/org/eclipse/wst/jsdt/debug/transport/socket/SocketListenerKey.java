package org.eclipse.wst.jsdt.debug.transport.socket;

import org.eclipse.wst.jsdt.debug.transport.ListenerKey;

/**
 * Key implementation for a {@link SocketTransportService}
 * 
 * @since 1.0
 */
public final class SocketListenerKey implements ListenerKey {

	private String address;

	/**
	 * Constructor
	 * @param address
	 */
	public SocketListenerKey(String address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.TransportService.ListenerKey#address()
	 */
	public String address() {
		return address;
	}
}