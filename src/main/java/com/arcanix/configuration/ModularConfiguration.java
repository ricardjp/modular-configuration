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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.yaml.snakeyaml.Yaml;

import com.arcanix.convert.ConversionException;
import com.arcanix.convert.Converters;
import com.arcanix.introspection.BeanUtils;
import com.arcanix.introspection.Property;
import com.arcanix.introspection.PropertyException;
import com.arcanix.introspection.wrapper.BeanWrapper;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class ModularConfiguration {

	private Map<String, Class<?>> moduleDefinitions = new HashMap<String, Class<?>>();
	private Map<Class<?>, Object> configurationModulesByType = new HashMap<Class<?>, Object>();
	
	private final Converters converters;
	
	public ModularConfiguration(final Class<?>... moduleClasses) {
		this(Converters.getDefaultConverters(), moduleClasses);
	}
	
	public ModularConfiguration(final Converters converters, final Class<?>... moduleClasses) {
		this.converters = converters;
		for (Class<?> moduleClass : moduleClasses) {
			String name = moduleClass.getAnnotation(Configuration.class).value();
			this.moduleDefinitions.put(name, moduleClass);
		}
	}
	
	public void load(final String resource) {
		load(ModularConfiguration.class.getClassLoader().getResourceAsStream(resource));
	}
	
	public void load(final File file) throws FileNotFoundException {
		load(new FileInputStream(file));
	}
	
	@SuppressWarnings("unchecked")
	public void load(final InputStream inputStream) {
		Map<String, Map<String, ?>> values = (Map<String, Map<String, ?>>) new Yaml().load(inputStream);
		
		BeanUtils beanUtils = new BeanUtils(this.converters);
		for (Map.Entry<String, Map<String, ?>> entriesByModule : values.entrySet()) {
			String moduleName = entriesByModule.getKey();
			Class<?> moduleClass = this.moduleDefinitions.get(moduleName);
			if (moduleClass == null) {
				throw new InvalidConfigurationException("Module is not defined for " + moduleName);
			}
			try {
				BeanWrapper beanWrapper = new BeanWrapper(null, moduleClass, this.converters);
				Object module = beanWrapper.getResult();
				
				PropertyListBuilder builder = new PropertyListBuilder(entriesByModule.getValue());
				
				for (Property property : builder.getNestedProperties()) {
					try {
						beanUtils.setNestedProperty(module, property);
					} catch (PropertyException e) {
						throw new InvalidConfigurationException("Property " + property + " does not exist", e);
					}
				}
				
				// validate configuration with jsr 303, at this point im not sure how it handles
				// data within collections
				// TODO tests to make sure @Valid cover all cases
				ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
				Validator validator = validatorFactory.getValidator();
				Set<ConstraintViolation<Object>> constraintViolations = validator.validate(module);
				if (!constraintViolations.isEmpty()) {
					
					// TODO better format for output
					throw new InvalidConfigurationException(moduleName, constraintViolations);
				}
				
				this.configurationModulesByType.put(moduleClass, module);
				
			} catch (ConversionException | PropertyException e) {
				throw new ConfigurationException(e);
			}
		}
	}
		
	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> moduleClass) {
		return (T) this.configurationModulesByType.get(moduleClass);
	}
	
}
