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

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
@Configuration("geolocation")
public final class GeoLocationConfiguration {

	private Place park;
	private Place island;
	
	public Place getPark() {
		return this.park;
	}
	
	public void setPark(final Place park) {
		this.park = park;
	}
	
	public Place getIsland() {
		return this.island;
	}
	
	public void setIsland(final Place island) {
		this.island = island;
	}
	
}
