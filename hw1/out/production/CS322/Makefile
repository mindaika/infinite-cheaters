# Makefile for CS322 Homework 2.
#
JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

ast:	ast/Ast.class

astpsr: ast/astParser.class

ir:	ir/IR.class

irgen: 	astpsr ir IRGen.class

all:	irgen

clean:
	'rm' ast/*.class ir/*.class *.class


