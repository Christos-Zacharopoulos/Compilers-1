all: compile

compile:
	jflex scanner.flex
	java -jar ../../java-cup-11b.jar -interface -parser Parser parser.cup
	javac -cp ../../java-cup-11b-runtime.jar *.java


execute:
	@java -cp ../../java-cup-11b-runtime.jar:. Main

clean:
	rm -f *.class Parser.java Scanner.java sym.java

test:
	make destroy
	make
	touch Output.java
	@java -cp ../../java-cup-11b-runtime.jar:. Main < test.txt > Output.java
	cat Output.java
	javac Output.java
	java Output
	make destroy

destroy:
	rm -f *.class Parser.java Scanner.java sym.java Output.java
