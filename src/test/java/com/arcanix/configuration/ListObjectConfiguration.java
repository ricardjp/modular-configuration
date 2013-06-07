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

import java.util.List;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
@Configuration("configuration")
public final class ListObjectConfiguration {

	private List<SimpleMock> properties;
	
	public List<SimpleMock> getProperties() {
		return this.properties;
	}
	
	public void setProperties(final List<SimpleMock> properties) {
		this.properties = properties;
	}
	
}
