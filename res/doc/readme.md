# Sysml2sec Project 

## Project structure

The contents of all the files indicated in the project structure schematic

  ├── Sysml2sec.jar
  ├── bin
  │     └── fr
  │          └── inria
  │                 ├── XmlBase.class
  │                 ├── XmlSource.class
  │                 ├── XmlBase.class
  │                 └── XmlSource.class
  ├── makefile
  ├── res
  │     └── doc
  │            └── readme.md
  ├── src
  │     └── fr
  │          └── inria
  │                 ├── XmlBase.java
  │                 ├── XmlSource.java
  │                 ├── XmlBase.java
  │                 └── XmlSource.java
  │
  ├── lib
  │     └── *.jar
  │

##Makefile 
Use makefile to operate project source. there are some parametres as below

### make new: 
new project, create src, bin, res dirs.

### make build: 
build entire project

### make clean: 
clear classes generated.

### make rebuild: 
rebuild project.
  
### make run: 
run your app.
  
### make jar: 
package your project into a executable jar.


##Application parametres
ex: Sysml2sec <source XML filename> <target XML filename> <rule template filename>

You gonna give the source xml filename at least. The target filename and template filename will use the default value if you haven't given.

