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

import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class InvalidConfigurationException extends ConfigurationException {

	private static final long serialVersionUID = 0L;

	public InvalidConfigurationException() {
		super();
	}
	
	public InvalidConfigurationException(final String message) {
		super(message);
	}
	
	public InvalidConfigurationException(final Throwable cause) {
		super(cause);
	}
	
	public InvalidConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public InvalidConfigurationException(final String moduleName, final Set<ConstraintViolation<Object>> constraintViolations) {
		super(buildMessage(moduleName, constraintViolations));
	}
	
	private static String buildMessage(final String moduleName, final Set<ConstraintViolation<Object>> constraintViolations) {
		StringBuilder builder = new StringBuilder();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			builder.append(moduleName).append(".");
			builder.append(constraintViolation.getPropertyPath()).append(" ");
			builder.append(constraintViolation.getMessage()).append("\n");
		}
		return builder.toString();
	}
	
}
