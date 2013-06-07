/**
 * Copyright (C) 2013 Jean-Philippe Ricard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcanix.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.arcanix.convert.Converters;
import com.arcanix.introspection.Property;
import com.arcanix.introspection.PropertyResolver;
import com.arcanix.introspection.util.ReflectionUtils;
import com.arcanix.introspection.wrapper.BeanWrapper;
import com.arcanix.introspection.wrapper.PropertyWrapper;
import com.arcanix.introspection.wrapper.PropertyWrapperFactory;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PropertyListBuilder {

	private final PropertyResolver propertyResolver = new PropertyResolver();
	
	private final Converters converters;
	private final BeanWrapper beanWrapper;
	
	private List<Property> properties = new ArrayList<>();
	
	public PropertyListBuilder(
			final Converters converters,
			final BeanWrapper beanWrapper,
			final Map<String, ?> values) {
		
		this.converters = converters;
		this.beanWrapper = beanWrapper;
		convertToNestedProperties(values);
	}
	
	public List<Property> getNestedProperties() {
		return this.properties;
	}
	
	private void convertToNestedProperties(final Map<String, ?> values) {
		for (Map.Entry<String, ?> entry : values.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			
			if (PropertyWrapperFactory.isWrapperType(value.getClass())) {
				buildNestedProperties(this.beanWrapper, key, value);
			} else {
				addProperty(key, value);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void buildNestedProperties(
			final PropertyWrapper propertyWrapper,
			final String expression,
			final Object values) {
		
		Property property = this.propertyResolver.resolve(expression);
		PropertyWrapper wrapper = PropertyWrapperFactory.getPropertyWrapper(
				propertyWrapper.getPropertyType(property), this.converters);
		
		if (values instanceof Map) {
			Map<String, ?> mappedValues = (Map<String, ?>) values;
			Class<?> targetClass = ReflectionUtils.getClass(propertyWrapper.getPropertyType(property));

			boolean targetIsBean = true;
			if (targetClass.isAssignableFrom(Map.class)) {
				targetIsBean = false;
			}
			
			for (Map.Entry<String, ?> entry : mappedValues.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				
				if (value instanceof Map) {
					if (targetIsBean) {
						buildNestedProperties(wrapper, appendProperty(expression, key), value);
					} else {
						buildNestedProperties(wrapper, appendKey(expression, key), value);
					}
				} else if (value instanceof List) {
					List<?> indexedValues = (List<?>) value;
					for (Object listValue : indexedValues) {
						if (value instanceof Map) {
							buildNestedProperties(wrapper, appendKey(expression, key), listValue);
						} else {
							addProperty(appendKey(expression, key), listValue);
						}
					}
				} else {
					if (targetIsBean) {
						addProperty(appendProperty(expression, key), value);
					} else {
						addProperty(appendKey(expression, key), value);
					}
				}
			}
		} else if (values instanceof List) {
			List<?> indexedValues = (List<?>) values;
			int index = 0;
			for (Object value : indexedValues) {
				if (value instanceof Map) {
					buildNestedProperties(wrapper, appendIndex(expression, index), value);
				} else {
					addProperty(appendIndex(expression, index), value);
				}
				index++;
			}
		} else {
			throw new InvalidConfigurationException("Unknown object type. This may indicate a bug in the library.");
		}
	}
	
	private void addProperty(final String expression, final Object value) {
		this.properties.add(this.propertyResolver.resolveNestedProperty(expression, value.toString()));
	}
	
	private String appendIndex(final String expression, final int index) {
		return expression + Property.INDEXED_START + index + Property.INDEXED_END;
	}
	
	private String appendKey(final String expression, final String key) {
		return expression + Property.MAPPED_START + key + Property.MAPPED_END;
	}
	
	private String appendProperty(final String expression, final String propertyName) {
		return expression + Property.NESTED + propertyName;
	}
	
}
