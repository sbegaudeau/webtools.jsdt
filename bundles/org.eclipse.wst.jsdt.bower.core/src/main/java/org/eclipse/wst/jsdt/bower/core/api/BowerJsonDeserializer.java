/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     sbegaudeau - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.bower.core.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author sbegaudeau
 */
public class BowerJsonDeserializer implements JsonDeserializer<BowerJson> {

	/**
	 * The name.
	 */
	private static final String NAME = "name"; //$NON-NLS-1$

	/**
	 * The description.
	 */
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * Ther version.
	 */
	private static final String VERSION = "version"; //$NON-NLS-1$

	/**
	 * The homepage.
	 */
	private static final String HOMEPAGE = "homepage"; //$NON-NLS-1$

	/**
	 * Main files.
	 */
	private static final String MAIN = "main"; //$NON-NLS-1$

	/**
	 * Private.
	 */
	private static final String PRIVATE = "private"; //$NON-NLS-1$

	/**
	 * Licenses.
	 */
	private static final String LICENSES = "licenses"; //$NON-NLS-1$

	/**
	 * Ignore.
	 */
	private static final String IGNORE = "ignore"; //$NON-NLS-1$

	/**
	 * Authors.
	 */
	private static final String AUTHORS = "authors"; //$NON-NLS-1$

	/**
	 * Dependencies.
	 */
	private static final String DEPENDENCIES = "dependencies"; //$NON-NLS-1$

	/**
	 * DevDependencies.
	 */
	private static final String DEV_DEPENDENCIES = "devDependencies"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public BowerJson deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		BowerJson bowerJson = new BowerJson();
		if (json instanceof JsonObject) {
			JsonObject jsonObject = (JsonObject)json;

			// Basic properties: string
			bowerJson.setName(this.getAsString(jsonObject, NAME));
			bowerJson.setDescription(this.getAsString(jsonObject, DESCRIPTION));
			bowerJson.setVersion(this.getAsString(jsonObject, VERSION));
			bowerJson.setHomepage(this.getAsString(jsonObject, HOMEPAGE));

			// Boolean
			bowerJson.setPrivate(this.getAsBoolean(jsonObject, PRIVATE));

			// Main: string or array of strings
			bowerJson.setMain(this.getMain(jsonObject));

			// List of string
			bowerJson.setLicenses(this.getAsStringList(jsonObject, LICENSES));
			bowerJson.setIgnore(this.getAsStringList(jsonObject, IGNORE));
			bowerJson.setAuthors(this.getAsStringList(jsonObject, AUTHORS));

			// Objects
			bowerJson.setDependencies(this.getAsStringMap(jsonObject, DEPENDENCIES));
			bowerJson.setDevDependencies(this.getAsStringMap(jsonObject, DEV_DEPENDENCIES));
		}
		return bowerJson;
	}

	/**
	 * Returns the value of the property with the given name in the given json object as a string.
	 *
	 * @param jsonObject
	 *            The json object
	 * @param propertyName
	 *            The name of the property
	 * @return The value of the property
	 */
	private String getAsString(JsonObject jsonObject, String propertyName) {
		String propertyValue = null;
		JsonElement jsonElement = jsonObject.get(propertyName);
		if (jsonElement != null && jsonElement.isJsonPrimitive()) {
			propertyValue = jsonElement.getAsString();
		}
		return propertyValue;
	}

	/**
	 * Returns the value of the property with the given name in the given json object as a boolean.
	 *
	 * @param jsonObject
	 *            The json object
	 * @param propertyName
	 *            The name of the property
	 * @return The value of the property
	 */
	private boolean getAsBoolean(JsonObject jsonObject, String propertyName) {
		boolean propertyValue = false;
		JsonElement jsonElement = jsonObject.get(propertyName);
		if (jsonElement != null && jsonElement.isJsonPrimitive()) {
			propertyValue = jsonElement.getAsBoolean();
		}
		return propertyValue;
	}

	/**
	 * Returns the value of the property main in the given json object as a string or a list of strings.
	 *
	 * @param jsonObject
	 *            The json object
	 * @return The value of the property main
	 */
	private List<String> getMain(JsonObject jsonObject) {
		List<String> main = new ArrayList<String>();
		JsonElement mainJsonElement = jsonObject.get(MAIN);
		if (mainJsonElement != null) {
			if (mainJsonElement.isJsonArray()) {
				JsonArray mainJsonArray = mainJsonElement.getAsJsonArray();
				for (JsonElement jsonElement : mainJsonArray) {
					if (jsonElement.isJsonPrimitive()) {
						main.add(jsonElement.getAsString());
					}
				}
			} else if (mainJsonElement.isJsonPrimitive()) {
				main.add(mainJsonElement.getAsString());
			}
		}
		return main;
	}

	/**
	 * Returns the value of the property with the given name in the given json object as a list of strings.
	 *
	 * @param jsonObject
	 *            The json object
	 * @param propertyName
	 *            The name of the property
	 * @return The value of the property
	 */
	private List<String> getAsStringList(JsonObject jsonObject, String propertyName) {
		List<String> propertyValue = new ArrayList<String>();
		JsonElement jsonElement = jsonObject.get(propertyName);
		if (jsonElement != null && jsonElement.isJsonArray()) {
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (JsonElement jsonChildElement : jsonArray) {
				if (jsonChildElement.isJsonPrimitive()) {
					propertyValue.add(jsonChildElement.getAsString());
				}
			}
		}
		return propertyValue;
	}

	/**
	 * Returns the value of the property with the given name in the given json object as a map of strings.
	 *
	 * @param jsonObject
	 *            The json object
	 * @param propertyName
	 *            The name of the property
	 * @return The value of the property
	 */
	private Map<String, String> getAsStringMap(JsonObject jsonObject, String propertyName) {
		Map<String, String> propertyValue = new HashMap<String, String>();
		JsonElement jsonElement = jsonObject.get(propertyName);
		if (jsonElement != null && jsonElement.isJsonObject()) {
			JsonObject childJsonObject = jsonElement.getAsJsonObject();
			Set<Entry<String, JsonElement>> entries = childJsonObject.entrySet();
			for (Entry<String, JsonElement> entry : entries) {
				if (entry.getValue().isJsonPrimitive()) {
					propertyValue.put(entry.getKey(), entry.getValue().getAsString());
				}
			}
		}
		return propertyValue;
	}

}
