# A general java project makefile
# Author: Zhao Hui(hui.zhao@inria.fr)
# create: 2017-08-23
# update: 2017-08-24
# version: 0.1

# 设置你要生成的jar包的文件名
# Set the file name of your jar package:
JAR_PKG = Sysml2sec.jar
# 设置你的项目的入口点
# Set your entry point of your java app:
ENTRY_POINT = fr.inria.Sysml2sec
# If you want package related files, pls put them into res directory and set RES_DIR= yes
RES_DIR = yes

# Source file
SOURCE_FILES =\
src/fr/inria/XmlBase.java \
src/fr/inria/XmlSource.java\
src/fr/inria/XmlTransform.java\
src/fr/inria/Sysml2sec.java \

CLASS_FILES =lib/commons-beanutils-1.9.3.jar:lib/commons-collections-3.2.jar:lib/commons-lang-2.6.jar:lib/commons-logging-1.2.jar:lib/dom4j-2.0.1.jar:lib/ezmorph-1.0.6.jar:lib/fastjson-1.2.7.jar:lib/jackson-annotations-2.8.0.jar:lib/jaxen-1.1.6.jar:lib/json-lib-2.4-jdk15.jar:lib/json.jar:lib/xom-1.2.5.jar

# 设置你的java编译器
# Set your java compiler here:
JAVAC = javac
# Set encoding
JFLAGS = -encoding UTF-8



vpath %.class bin
vpath %.java src

# show help message by default
Default:
	@echo "make new: new project, create src, bin, res dirs."
	@echo "make build: build project."
	@echo "make clean: clear classes generated."
	@echo "make rebuild: rebuild project."
	@echo "make run: run your app."
	@echo "make jar: package your project into a executable jar."

build: 

	$(JAVAC) -cp bin -d bin $(JFLAGS) -classpath ${CLASS_FILES} $(SOURCE_FILES)$<

buildAll: build jar

rebuild: clean build


.PHONY: new clean run jar

new:
ifeq ($(RES_DIR),yes)
	mkdir -pv src bin res
else
	mkdir -pv src bin
endif

clean:
	rm -frv bin/*

cleanAll: clean
	rm $(JAR_PKG)

run:
	java -cp bin $(ENTRY_POINT)

jar:
ifeq ($(RES_DIR),yes)
	jar cvfm $(JAR_PKG) MANIFEST.MF  -C bin . res 
else
	jar cvfm $(JAR_PKG) MANIFEST.MF -C bin .
endif