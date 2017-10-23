# make assemble assembly-file=src/Test.txt memory-image=out.txt

verbose="false"

compile:
	rm -rf build
	mkdir build
	javac -d build src/*/*.java

assemble:
	java -classpath build assembler.Assembler $(assembly-file) $(memory-image) $(verbose)

simulator:
	java -classpath build simulator.SimulatorController $(verbose)

reader:
	java -classpath build reader.Reader

javadoc:
	rm -rf docs
	mkdir docs
	javadoc -d docs/ src/*/*.java