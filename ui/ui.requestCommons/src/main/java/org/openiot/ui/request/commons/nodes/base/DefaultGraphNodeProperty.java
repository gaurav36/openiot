/**
 *    Copyright (c) 2011-2014, OpenIoT
 *   
 *    This file is part of OpenIoT.
 *
 *    OpenIoT is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, version 3 of the License.
 *
 *    OpenIoT is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with OpenIoT.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Contact: OpenIoT mailto: info@openiot.eu
 */

package org.openiot.ui.request.commons.nodes.base;

import java.io.Serializable;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openiot.ui.request.commons.logging.LoggerService;
import org.openiot.ui.request.commons.nodes.enums.PropertyType;
import org.openiot.ui.request.commons.nodes.interfaces.GraphNodeProperty;

/**
 * 
 * @author Achilleas Anagnostopoulos (aanag) email: aanag@sensap.eu
 */
public class DefaultGraphNodeProperty implements GraphNodeProperty, Serializable {
	private static final long serialVersionUID = 1L;

	private PropertyType type;
	private String name;
	private String variableName;
	private Class<?> javaType;
	private boolean isRequired;
	private boolean isVariable;
	private String[] allowedValues;

	public DefaultGraphNodeProperty() {
	}

	public PropertyType getType() {
		return type;
	}

	public void setType(PropertyType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public Class<?> getJavaType() {
		return this.javaType;
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

	public String[] getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(String[] allowedValues) {
		this.allowedValues = allowedValues;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean required) {
		this.isRequired = required;
	}

	public GraphNodeProperty getCopy() {
		DefaultGraphNodeProperty copy = new DefaultGraphNodeProperty();
		copy.setAllowedValues(allowedValues);
		copy.setJavaType(javaType);
		copy.setName(name);
		copy.setRequired(isRequired);
		copy.setType(type);

		return copy;
	}

	public JSONObject toJSON() {
		JSONObject spec = new JSONObject();
		try {
			spec.put("class", this.getClass().getCanonicalName());
			spec.put("type", getType().toString());
			spec.put("name", getName());
			spec.put("variableName", getVariableName());
			spec.put("javaClass", javaType.getCanonicalName());
			spec.put("isRequired", isRequired());
			spec.put("isVariable", isVariable());
			if (allowedValues != null) {
				JSONArray values = new JSONArray();
				for (String value : allowedValues) {
					values.put(value);
				}
				spec.put("allowedValues", values);
			}
		} catch (JSONException ex) {
			LoggerService.log(ex);
		}
		return spec;
	}

	public void importJSON(JSONObject spec) throws JSONException {
		setType(PropertyType.valueOf(spec.getString("type")));
		setName(spec.getString("name"));
		setVariableName(spec.optString("variableName"));
		try {
			setJavaType(Class.forName(spec.getString("javaClass")));
		} catch (Throwable ex) {
			throw new JSONException("Unsupported java class type value '" + spec.getString("javaClass") + "'");
		}
		setRequired(spec.getBoolean("isRequired"));
		setVariable(spec.getBoolean("isVariable"));
		if (spec.has("allowedValues")) {
			JSONArray values = spec.getJSONArray("allowedValues");
			allowedValues = new String[values.length()];
			for (int index = 0; index < values.length(); index++) {
				allowedValues[index] = values.getString(index);
			}
		}
	}

	public boolean isVariable() {
		return isVariable;
	}

	public void setVariable(boolean isVariable) {
		this.isVariable = isVariable;
	}

	@Override
	public String toString() {
		return "[type: " + getType() + ", name: " + getName() + ", javaType: " + getJavaType() + ", required: " + isRequired() + ", variable: " + isVariable + ", varName: " + variableName + "]";
	}
}
