# make assemble assembly-file=src/Test.txt memory-image=out.txt verbose=true

verbose="false"

compile:
	rm -rf build
	mkdir build
	javac -d build src/*/*.java

assemble:
	java -classpath build assembler.Assembler $(assembly-file) $(memory-image) $(verbose)

simulator:
	java -classpath build simulator.SimulatorController

reader:
	java -classpath build reader.Reader