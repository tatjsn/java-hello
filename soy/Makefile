.PHONY: install-soy-templates install-soy-core clean all
SOY_FILES := $(wildcard *.soy)

all: install-soy-templates install-soy-core

install-soy-templates: soy.jar
	rm -rf ../repo/com/example/soy
	../mvnw deploy:deploy-file -e -Durl=file://$(shell cd ..; pwd)/repo/ -Dfile=$^ -DgroupId=com.example -DartifactId=soy -Dpackaging=jar -Dversion=1.0-SNAPSHOT

install-soy-core: soy-lib.jar
	rm -rf ../repo/com/example/soy-lib
	../mvnw deploy:deploy-file -e -Durl=file://$(shell cd ..; pwd)/repo/ -Dfile=$^ -DgroupId=com.example -DartifactId=soy-lib -Dpackaging=jar -Dversion=1.0-SNAPSHOT

soy.jar: SoyToJbcSrcCompiler.jar $(SOY_FILES)
	java -jar SoyToJbcSrcCompiler.jar --output $@ --srcs $(shell echo $^ | sed -e 's/SoyToJbcSrcCompiler\.jar //' | sed -e 's/ /,/g')

SoyToJbcSrcCompiler.jar:
	curl "https://repo1.maven.org/maven2/com/google/template/soy/2020-08-24/soy-2020-08-24-SoyToJbcSrcCompiler.jar" -o $@

soy-lib.jar:
	curl "https://repo1.maven.org/maven2/com/google/template/soy/2020-08-24/soy-2020-08-24.jar" -o $@
