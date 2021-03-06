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
public final class TestConfigurationParser {
	
	@Test
	public void testSimpleStringProperty() {
		ModularConfiguration parser = new ModularConfiguration(SimpleStringPropertyConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("simple.string.yml"));
		SimpleStringPropertyConfiguration result = parser.get(SimpleStringPropertyConfiguration.class);
		assertEquals("foo bar", result.getProperty());
	}
	
	@Test
	public void testSimpleConvertedProperty() {
		ModularConfiguration parser = new ModularConfiguration(SimpleConvertedPropertyConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("simple.converted.yml"));
		SimpleConvertedPropertyConfiguration result = parser.get(SimpleConvertedPropertyConfiguration.class);
		assertEquals(Integer.valueOf(7), result.getProperty());
	}
	
	@Test
	public void testSimpleObject() {
		ModularConfiguration parser = new ModularConfiguration(SimpleObjectConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("simple.object.yml"));
		SimpleObjectConfiguration result = parser.get(SimpleObjectConfiguration.class);
		assertEquals("foo bar", result.getObject().getProperty());
		assertEquals(Integer.valueOf(17), result.getObject().getNumber());
	}
	
	@Test
	public void testListOfStringProperty() {
		ModularConfiguration parser = new ModularConfiguration(ListStringConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("list.string.yml"));
		ListStringConfiguration result = parser.get(ListStringConfiguration.class);
		assertTrue(result.getProperties().size() == 3);
		assertEquals("property 1", result.getProperties().get(0));
		assertEquals("property 2", result.getProperties().get(1));
		assertEquals("property 3", result.getProperties().get(2));
	}
	
	@Test
	public void testListOfObjectsProperty() {
		ModularConfiguration parser = new ModularConfiguration(ListObjectConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("list.object.yml"));
		ListObjectConfiguration result = parser.get(ListObjectConfiguration.class);
		assertTrue(result.getProperties().size() == 3);
		
		assertEquals("property 1", result.getProperties().get(0).getProperty());
		assertEquals("property 2", result.getProperties().get(1).getProperty());
		assertEquals("property 3", result.getProperties().get(2).getProperty());
		
		assertEquals(Integer.valueOf(8), result.getProperties().get(0).getNumber());
		assertEquals(Integer.valueOf(12), result.getProperties().get(1).getNumber());
		assertEquals(Integer.valueOf(3), result.getProperties().get(2).getNumber());
	}
	
	@Test
	public void testMapOfStringProperty() {
		ModularConfiguration parser = new ModularConfiguration(MapStringConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("map.string.yml"));
		MapStringConfiguration result = parser.get(MapStringConfiguration.class);
		assertTrue(result.getProperties().size() == 2);
		assertEquals("foo bar", result.getProperties().get("name"));
		assertEquals("123 Main Street", result.getProperties().get("address"));
	}
	
	@Test
	public void testMapOfObjectProperty() {
		ModularConfiguration parser = new ModularConfiguration(MapObjectConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("map.object.yml"));
		MapObjectConfiguration result = parser.get(MapObjectConfiguration.class);
		assertTrue(result.getProperties().size() == 2);
		assertEquals("First property", result.getProperties().get("first").getProperty());
		assertEquals(Integer.valueOf(1), result.getProperties().get("first").getNumber());
		assertEquals("Second property", result.getProperties().get("second").getProperty());
		assertEquals(Integer.valueOf(2), result.getProperties().get("second").getNumber());
	}
	
	@Test
	public void testMapOfStringListProperty() {
		ModularConfiguration parser = new ModularConfiguration(MapOfListStringConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("map.complex.string.yml"));
		MapOfListStringConfiguration result = parser.get(MapOfListStringConfiguration.class);
		assertTrue(result.getProperties().size() == 2);
		assertEquals("foo", result.getProperties().get("first").get(0));
		assertEquals("bar", result.getProperties().get("first").get(1));
		assertEquals("john", result.getProperties().get("second").get(0));
		assertEquals("smith", result.getProperties().get("second").get(1));
	}
	
	@Test
	public void testListofMapStringProperty() {
		ModularConfiguration parser = new ModularConfiguration(ListofMapStringConfiguration.class);
		parser.load(TestConfigurationParser.class.getClassLoader().getResourceAsStream("list.map.string.yml"));
		ListofMapStringConfiguration result = parser.get(ListofMapStringConfiguration.class);
		assertTrue(result.getProperties().size() == 3);
		assertEquals("1", result.getProperties().get(0).get("id"));
		assertEquals("3", result.getProperties().get(1).get("id"));
		assertEquals("18", result.getProperties().get(2).get("id"));
	}
	
}
