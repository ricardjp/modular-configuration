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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class TestModularConfiguration {
	
	@Test
	public void testMultipleModules() {
		ModularConfiguration modularConfiguration = new ModularConfiguration(
				EmployeesConfiguration.class, GeoLocationConfiguration.class);
		
		modularConfiguration.load("modular.multiple.yml");
		
		GeoLocationConfiguration geo = modularConfiguration.get(GeoLocationConfiguration.class);
		EmployeesConfiguration employees = modularConfiguration.get(EmployeesConfiguration.class);
	
		assertTrue(employees.getDirectors().size() == 2);
		assertEquals("John Smith", employees.getDirectors().get(0).getName());
		assertEquals(Integer.valueOf(47), employees.getDirectors().get(0).getAge());
		assertEquals("John Doe", employees.getDirectors().get(1).getName());
		assertEquals(Integer.valueOf(38), employees.getDirectors().get(1).getAge());
		
		assertTrue(employees.getEngineers().size() == 2);
		assertEquals("Jack Smith", employees.getEngineers().get(0).getName());
		assertEquals(Integer.valueOf(34), employees.getEngineers().get(0).getAge());
		assertEquals("Jane Doe", employees.getEngineers().get(1).getName());
		assertEquals(Integer.valueOf(28), employees.getEngineers().get(1).getAge());
		
		assertEquals(Double.valueOf(45.505174), geo.getPark().getLatitude());
		assertEquals(Double.valueOf(-73.590202), geo.getPark().getLongitude());
		assertEquals(Double.valueOf(45.460853), geo.getIsland().getLatitude());
		assertEquals(Double.valueOf(-73.547974), geo.getIsland().getLongitude());
	}
	
}
