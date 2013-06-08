modular-configuration
=====================

Java library which converts multi-module YAML configuration files to validated Java objects

# Usage

Create a YAML file containing some properties.
Example:
```yaml
mymodule:
  version: 1.0
  directory: /to/my/path
  
```

Create a configuration module. A configuration module is a Java bean which will hold your configuration parameters.
It must be annotated with @Configuration. This annotation holds only one value, the name of the top element of your YAML configuration that represents your module.
Example:
```java
@Configuration("mymodule")
public class MyModule {

	private String version;
	private String directory;
	
	public String getVersion() {
	  return this.version;
	}
	
	public void setVersion(String version) {
	  this.version = version;
	}
	
	public String getDirectory() [
	  return this.directory;
	}
	
	public void setDirectory(String directory) {
	  this.directory = directory;
	}
}
```
Create a ModularConfiguration instance. You must specify which modules to map to this instance. Then you must load configuration values from a YAML configuration file.
Example:
```java
Configuration conf = new ModularConfiguration(MyModule.class);
conf.load("myconfiguration.yml");
```

Your configuration bean should now be populated and ready to use. To get a populated instance of your module, simply request it from the ModularConfiguration instance previously created.
Example:
```java
MyModule myModule = conf.get(MyModule.class);
String version = myModule.getVersion();
String directory = myModule.getDirectory();
```