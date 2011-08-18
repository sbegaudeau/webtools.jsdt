/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.transport;

import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.internal.crossfire.Tracing;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFNullValue;
import org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi.CFUndefinedValue;

/**
 * Class for reading / writing JSON objects
 * <br><br>
 * Crossfire has the following types:
 * <ul>
 * 	<li>object</li>
 * 	<li>function</li>
 * 	<li>boolean</li>
 * 	<li>number</li>
 * 	<li>string</li>
 * 	<li>undefined</li>
 * 	<li>ref</li>
 * </ul> 
 * @since 1.0
 */
public final class JSON {

	static boolean TRACE = false;
	
	/**
	 * Standard line feed / control feed terminus for Crossfire packets
	 */
	public static final String LINE_FEED = "\r\n"; //$NON-NLS-1$
	/**
	 * The default <code>Content-Length:</code> preamble
	 */
	public static final String CONTENT_LENGTH = "Content-Length:"; //$NON-NLS-1$
	/**
	 * Enables / Disables tracing in the all of the JSDI implementations
	 * 
	 * @param trace
	 */
	public static void setTracing(boolean trace) {
		TRACE = trace;
	}
	
	/**
	 * Constructor
	 * 
	 * No instantiation
	 */
	private JSON() {}
	
	/**
	 * Writes the given key / value pair to the buffer in the form: <code>"key":["]value["]</code>
	 * 
	 * @param key
	 * @param value
	 * @param buffer
	 */
	public static void writeKeyValue(String key, Object value, StringBuffer buffer) {
		writeString(key, buffer);
		buffer.append(':');
		writeValue(value, buffer);
	}
	
	/**
	 * Writes out the given value to the buffer. <br><br>
	 * Values are written out as:
	 * <ul>
	 * 	<li>Boolean / Number: <code>value.toString()</code></li>
	 * 	<li>String: <code>"value"</code></li>
	 * 	<li>null: <code>null</code>
	 * 	<li>Collection: <code>[{@link #writeValue(Object, StringBuffer)},...]</code></li>
	 * 	<li>Map: <code>{"key":{@link #writeValue(Object, StringBuffer)},...}</code></li>
	 * </ul>
	 * 
	 * @param value
	 * @param buffer
	 */
	public static void writeValue(Object value, StringBuffer buffer) {
		if (value == null) {
			buffer.append(CFNullValue.NULL);
		}
		else if (value instanceof Boolean || value instanceof Number) {
			buffer.append(value.toString());
		}
		else if (value instanceof String) {
			writeString((String) value, buffer);
		}
		else if(value instanceof Collection) {
			writeArray((Collection) value, buffer);
		}
		else if(value instanceof Map) {
			writeObject((Map) value, buffer);
		}
	}
	
	/**
	 * Writes the given {@link String} into the given {@link StringBuffer} properly escaping
	 * all control characters
	 * 
	 * @param string
	 * @param buffer
	 */
	public static void writeString(String string, StringBuffer buffer) {
		buffer.append('"');
		int length = string.length();
		for (int i = 0; i < length; i++) {
			char c = string.charAt(i);
			switch (c) {
				case '"' :
				case '\\' :
				case '/' : {
					buffer.append('\\');
					buffer.append(c);
					break;
				}
				case '\b' : {
					buffer.append("\\b"); //$NON-NLS-1$
					break;
				}
				case '\f' : {
					buffer.append("\\f"); //$NON-NLS-1$
					break;
				}
				case '\n' : {
					buffer.append("\\n"); //$NON-NLS-1$
					break;
				}
				case '\r' : {
					buffer.append("\\r"); //$NON-NLS-1$
					break;
				}
				case '\t' : {
					buffer.append("\\t"); //$NON-NLS-1$
					break;
				}
				default :
					if (Character.isISOControl(c)) {
						buffer.append("\\u"); //$NON-NLS-1$
						String hexString = Integer.toHexString(c);
						for (int j = hexString.length(); j < 4; j++) {
							buffer.append('0');
						}
						buffer.append(hexString);
					} else {
						buffer.append(c);
					}
			}
		}
		buffer.append('"');
	}

	/**
	 * Writes the given collection into an array string of the form: <code>[{@link #writeValue(Object, StringBuffer)},...]</code>
	 * 
	 * @param collection
	 * @param buffer
	 */
	static void writeArray(Collection collection, StringBuffer buffer) {
		buffer.append('[');
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			writeValue(iterator.next(), buffer);
			if(iterator.hasNext()) {
				buffer.append(',');
			}
		}
		buffer.append(']');
	}
	
	/**
	 * Writes an object mapping to the given buffer in the form: <code>{"key":{@link #writeValue(Object, StringBuffer)},...}</code>
	 * 
	 * @param map
	 * @param buffer
	 */
	public static void writeObject(Map map, StringBuffer buffer) {
		buffer.append('{');
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			writeString(key, buffer);
			buffer.append(':');
			writeValue(map.get(key), buffer);
			if(iterator.hasNext()) {
				buffer.append(',');
			}
		}
		buffer.append('}');
	}
	
	/**
	 * Writes the <code>Content-Length:N</code> preamble to the head of the given buffer
	 * 
	 * @param buffer
	 * @param length
	 */
	public static void writeContentLength(StringBuffer buffer, int length) {
		StringBuffer buff = new StringBuffer(18);
		buff.append(CONTENT_LENGTH).append(length).append(LINE_FEED).append(LINE_FEED);
		buffer.insert(0, buff.toString());
	}
	
	/**
	 * Serializes the given {@link CFPacket} to a {@link String}
	 * 
	 * @param packet the packet to serialize
	 * 
	 * @return the serialized {@link String}, never <code>null</code>
	 */
	public static String serialize(CFPacket packet) {
		Object json = packet.toJSON();
		StringBuffer buffer = new StringBuffer();
		writeValue(json, buffer);
		int length = buffer.length();
		writeContentLength(buffer, length);
		buffer.append(LINE_FEED);
		if(TRACE) {
			Tracing.writeString("SERIALIZE: " + packet.getType() +" packet as "+buffer.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return buffer.toString();
	}
	
	/**
	 * Reads and returns a new object from the given JSON {@link String}. This method
	 * will throw an {@link IllegalStateException} if parsing fails.
	 * 
	 * @param jsonString
	 * @return the object, never <code>null</code>
	 */
	public static Object read(String jsonString) {
		return parse(new StringCharacterIterator(jsonString));
	}
	
	/**
	 * Reads and returns a new object form the given {@link CharacterIterator} that corresponds to
	 * a properly formatted JSON string. This method will throw an {@link IllegalStateException} if
	 * parsing fails.
	 * 
	 * @param it the {@link CharacterIterator} to parse
	 * @return the object, never <code>null</code>
	 */
	public static Object parse(CharacterIterator it) {
		parseWhitespace(it);
		Object result = parseValue(it);
		parseWhitespace(it);
		if (it.current() != CharacterIterator.DONE) {
			throw error("should be done", it); //$NON-NLS-1$
		}
		return result;
	}
	
	/**
	 * Creates an {@link IllegalStateException} for the given message and iterator
	 * 
	 * @param message the message for the exception
	 * @param it the {@link CharacterIterator} to parse
	 * 
	 * @return a new {@link IllegalStateException} 
	 */
	private static RuntimeException error(String message, CharacterIterator it) {
		return new IllegalStateException("[" + it.getIndex() + "] " + message); //$NON-NLS-1$//$NON-NLS-2$
	}
	
	/**
	 * Chews up whitespace from the iterator
	 * 
	 * @param it the {@link CharacterIterator} to parse
	 */
	private static void parseWhitespace(CharacterIterator it) {
		char c = it.current();
		while (Character.isWhitespace(c)) {
			c = it.next();
		}
	}
	
	/**
	 * Parses the {@link Object} from the {@link CharacterIterator}. This method 
	 * delegates to the proper parsing method depending on the current iterator context.
	 * This method will throw an {@link IllegalStateException} if parsing fails.
	 * 
	 * @param it the {@link CharacterIterator} to parse
	 * 
	 * @return the new object, never <code>null</code>
	 * @see #parseString(CharacterIterator)
	 * @see #parseNumber(CharacterIterator)
	 * @see #parseArray(CharacterIterator)
	 * @see #parseObject(CharacterIterator)
	 */
	private static Object parseValue(CharacterIterator it) {
		switch (it.current()) {
			case '{' : {
				return parseObject(it);
			}
			case '[' : {
				return parseArray(it);
			}
			case '"' : {
				return parseString(it);
			}
			case '-' :
			case '0' :
			case '1' :
			case '2' :
			case '3' :
			case '4' :
			case '5' :
			case '6' :
			case '7' :
			case '8' :
			case '9' : {
				return parseNumber(it);
			}
			case 't' : {
				parseText(Boolean.TRUE.toString(), it);
				return Boolean.TRUE;
			}
			case 'f' : {
				parseText(Boolean.FALSE.toString(), it);
				return Boolean.FALSE;
			}
			case 'n' : {
				parseText(CFNullValue.NULL, it);
				return null;
			}
			case 'u': {
				parseText(CFUndefinedValue.UNDEFINED, it);
				return null;
			}
		}
		throw error("Bad JSON starting character '" + it.current() + "'", it); //$NON-NLS-1$ //$NON-NLS-2$;
	}
	
	/**
	 * Parses the JSON string from the {@link CharacterIterator}
	 * 
	 * @param it the {@link CharacterIterator} to parse
	 * @return the JSON {@link String}, never <code>null</code>
	 */
	private static String parseString(CharacterIterator it) {
		char c = it.next();
		if (c == '"') {
			it.next();
			return ""; //$NON-NLS-1$
		}
		StringBuffer buffer = new StringBuffer();
		while (c != CharacterIterator.DONE && c != '"') {
			if (Character.isISOControl(c)) {
				//ignore it and continue
				c = it.next();
				continue;
				//throw error("illegal ISO control character: '" + Integer.toHexString(c) + "'", it); //$NON-NLS-1$ //$NON-NLS-2$);
			}
			if (c == '\\') {
				c = it.next();
				switch (c) {
					case '"' :
					case '\\' :
					case '/' : {
						buffer.append(c);
						break;
					}
					case 'b' : {
						buffer.append('\b');
						break;
					}
					case 'f' : {
						buffer.append('\f');
						break;
					}
					case 'n' : {
						buffer.append('\n');
						break;
					}
					case 'r' : {
						buffer.append('\r');
						break;
					}
					case 't' : {
						buffer.append('\t');
						break;
					}
					case 'u' : {
						StringBuffer unicode = new StringBuffer(4);
						for (int i = 0; i < 4; i++) {
							unicode.append(it.next());
						}
						try {
							buffer.append((char) Integer.parseInt(unicode.toString(), 16));
						} catch (NumberFormatException e) {
							throw error("expected a unicode hex number but was '" + unicode.toString() + "'", it); //$NON-NLS-1$ //$NON-NLS-2$););
						}
						break;
					}
					default : {
						throw error("illegal escape character '" + c + "'", it); //$NON-NLS-1$ //$NON-NLS-2$););
					}
				}
			} else {
				buffer.append(c);
			}
			c = it.next();
		}
		c = it.next();
		return buffer.toString();
	}
	
	/**
	 * Parses an {@link Map} object from the iterator or throws an
	 * {@link IllegalStateException} if parsing fails.
	 * 
	 * @param it the {@link CharacterIterator} to parse
	 * @return a new {@link Map} object, never <code>null</code>
	 */
	private static Map parseObject(CharacterIterator it) {
		it.next();
		parseWhitespace(it);
		if (it.current() == '}') {
			it.next();
			return Collections.EMPTY_MAP;
		}

		Map map = new HashMap();
		while (it.current() != CharacterIterator.DONE) {
			if (it.current() != '"') {
				throw error("expected a string start '\"' but was '" + it.current() + "'", it); //$NON-NLS-1$ //$NON-NLS-2$
			}
			String key = parseString(it);
			if (map.containsKey(key)) {
				throw error("' already defined" + "key '" + key, it); //$NON-NLS-1$ //$NON-NLS-2$
			}
			parseWhitespace(it);
			if (it.current() != ':') {
				throw error("expected a pair separator ':' but was '" + it.current() + "'", it); //$NON-NLS-1$ //$NON-NLS-2$
			}
			it.next();
			parseWhitespace(it);
			Object value = parseValue(it);
			map.put(key, value);
			parseWhitespace(it);
			if (it.current() == ',') {
				it.next();
				parseWhitespace(it);
				continue;
			}
			if (it.current() != '}') {
				throw error("expected an object close '}' but was '" + it.current() + "'", it); //$NON-NLS-1$ //$NON-NLS-2$
			}
			break;
		}
		it.next();
		return map;
	}
	
	/**
	 * Parses an {@link ArrayList} from the given iterator or throws an
	 * {@link IllegalStateException} if parsing fails
	 * 
	 * @param it the {@link CharacterIterator} to parse
	 * @return a new {@link ArrayList} object never <code>null</code>
	 */
	private static List parseArray(CharacterIterator it) {
		it.next();
		parseWhitespace(it);
		if (it.current() == ']') {
			it.next();
			return Collections.EMPTY_LIST;
		}

		List list = new ArrayList();
		while (it.current() != CharacterIterator.DONE) {
			Object value = parseValue(it);
			list.add(value);
			parseWhitespace(it);
			if (it.current() == ',') {
				it.next();
				parseWhitespace(it);
				continue;
			}
			if (it.current() != ']') {
				throw error("expected an array close ']' but was '" + it.current() + "'", it); //$NON-NLS-1$ //$NON-NLS-2$
			}
			break;
		}
		it.next();
		return list;
	}

	/**
	 * @param string
	 * @param it
	 */
	private static void parseText(String string, CharacterIterator it) {
		int length = string.length();
		char c = it.current();
		for (int i = 0; i < length; i++) {
			if (c != string.charAt(i)) {
				throw error("expected to parse '" + string + "' but character " + (i + 1) + " was '" + c + "'", it); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$;
			}
			c = it.next();
		}
	}

	/**
	 * Parses a {@link Number} object from the given {@link CharacterIterator}
	 * 
	 * @param it
	 * @return a new {@link Number}, never <code>null</code>
	 */
	private static Object parseNumber(CharacterIterator it) {
		StringBuffer buffer = new StringBuffer();
		char c = it.current();
		while (Character.isDigit(c) || c == '-' || c == '+' || c == '.' || c == 'e' || c == 'E') {
			buffer.append(c);
			c = it.next();
		}
		try {
			return new BigDecimal(buffer.toString());
		} catch (NumberFormatException e) {
			throw error("expected a number but was '" + buffer.toString() + "'", it); //$NON-NLS-1$ //$NON-NLS-2$;
		}
	}
}
