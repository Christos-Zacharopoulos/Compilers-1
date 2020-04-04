all: compile

compile:
	javac Calculator.java

execute:
	@java Calculator

clean:
	rm -f *.class

test:
	make clean
	make
	make execute