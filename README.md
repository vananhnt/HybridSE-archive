# HybridSE
**Corana** is an on-going project providing a Dynamic Symbolic Execution tool for ARM Cortex-M. It takes an ARM binary file as the input and outputs its precise Control Flow Graph (CFG) under the presence of obfuscations like indirect jumps. **Corana/API** extends the DSE tool CORANA for ARM by adding API stubs for Linux system functions. **HybridSE** combines Spf and **Corana/API** for analyzing APK-style Java with ARM native, and further external calls in Linux.
## Artifact summary
In previous work [Vu, 2019], a dynamic symbolic execution tool for ARM Cortex-M - CORANA was preliminarily built by extracting ARM formal semantics from natural language descriptions (https://github.com/anhvvcs/corana). 

HybridSE consider performing symbolic execution across heterogeneous platforms. Java can include shared native libraries as .so files, an object library in the ELF format. For ARM native code in Java, we combines two SE tools - Symbolic Pathfinder and CORANA/API. Symbolic Pathfinder are used to analyse Java class file. We prepare a custom listener for Symbolic Pathfiner which invoke CORANA/API at the point of ARM native code. 

- The tool HybridSE and its dependencies
	* The custome listener for Symbolic Pathfinder are in /spf-interfaces
	* Prerequisite tools for CORANA are provided in the sub folder `/dependencies` (Capstone, Z3, mongodb, binutils)
	* Source code are in `/src` 
- A `Dockerfile` is also provided for easy installation of CORANA/API (require internet connection).
- README.md
- License.txt

## Installation
### Installation of SPF
jpf-core (Java Pathfinder) and jpf-symbc (Symbolic Pathfinder) are used to analyse Java class file. 

Please download jpf-core from here: https://github.com/yannicnoller/jpf-core/tree/0f2f2901cd0ae9833145c38fee57be03da90a64f or here https://github.com/corinus/jpf-core

And jpf-symbc from here: https://github.com/SymbolicPathFinder/jpf-symbc

Import them in Eclipse as 2 Java projects.

Create a .jpf dir in your home directory and create in it a file called "site.properties" with the following content:
```
jpf-core = ${user.home}/.../path-to-jpf-core-folder/jpf-core

jpf-symbc = ${user.home}/.../path-to-jpf-core-folder/jpf-symbc

extensions={jpf-symbc}
```
### Combine SPF and CORANA/API
Import CORANA-API by adding the pre-built jar /spf-interfaces/corana-x.jar as external library of jpf-symbc projects. 
Add NativeListener.java into jpf-symbc project and create a .jpf file for the Java project with the template:
```
@using=jpf-symbc

target= #classname
classpath=#path to class
native_classpath=#path to native library
sourcepath=#path to source

symbolic.method = #method name
symbolic.dp=choco


#search.properties=\
#gov.nasa.jpf.vm.NoUncaughtExceptionsProperty

############################### Listener part #############################
listener = #path to NativeListener (provided in /spf-interfaces/NativeListener.java)
```
Selecting a run configuration from the "Run" menu in Eclipse. In particular you should select: "run-JPF-symbc" to run Symbolic PathFinder on your example. Example of a Java project containing ARM native library are provided in /spf-interfaces.

CORANA-API can also be built manually from https://github.com/vananhnt/corana. 

## Contact
Anh V. Vu - Project maintainer - [Email](mailto:anhvvcs@gmail.com)  
Anh V. Nguyen - [Email](anhnv@jaist.ac.jp)

## License
This project is licensed under the [MIT License](http://www.opensource.org/licenses/mit-license.php).

## Acknowledgments

We thank JAIST for financially supporting our project and thank [Jan Willem Janssen](https://www.lxtreme.nl/) for his useful library for effectively parsing ELF binary file: https://github.com/jawi/java-binutils

**[Vu, 2019]** Vu, A., Ogawa, M.: Formal semantics extraction from natural language specifica-tions for arm. In: FM. pp. 465–483. LNCS (09 2019)
(https://github.com/anhvvcs/corana)

