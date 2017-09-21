run: build
	@echo Running...
	@java -cp bin JoinUp suffix read < smalldict.txt
.PHONY: run

build:
	@echo Starting build...
	@javac src/JoinUp.java
	@echo Finishing building.
.PHONY: build
