.PHONY: install
SOY_FILES := $(wildcard *.soy)
install: soy.jar
	cd ..; ./mvnw deploy:deploy-file -e -Durl=file://$(shell cd ..;pwd)/repo/ -Dfile=./soy/$^ -DgroupId=com.example -DartifactId=soy -Dpackaging=jar -Dversion=1.0-SNAPSHOT
soy.jar: $(SOY_FILES)
	java -jar SoyToJbcSrcCompiler.jar --output $@ --srcs $(shell echo $^ | sed -e 's/ /,/')
