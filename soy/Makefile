SOY_FILES := $(wildcard *.soy)
soy2.jar: $(SOY_FILES)
	echo java -jarSoyToJbcSrcCompiler.jar --output $@ --srcs $(shell echo $^ | sed -e 's/ /,/')
