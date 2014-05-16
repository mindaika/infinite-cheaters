# Makefile for X86 code generator
#
JFLAGS = -g
JC = javac
JCC = javacc

.SUFFIXES: .jj .java .class

.PRECIOUS: %.java

.jj.java:
	$(JCC) $*.jj

.java.class:
	$(JC) $(JFLAGS) $*.java

x86gen: IR.class irParser.class X86Gen.class Assignment.class X86.class Liveness.class

irParser.java: irParser.jj
	$(JCC) irParser.jj

clean:	
	rm *.class irParser*.java 
