all: test

test:
	javac -d bin -sourcepath ./src ./src/*

exe:
	cd ./bin; java Main
