# Corana-X
**Corana** is an on-going project providing a Dynamic Symbolic Execution tool for ARM Cortex-M. It takes an ARM binary file as the input and outputs its precise Control Flow Graph (CFG) under the presence of obfuscations like indirect jumps. **Corana/API** extends the DSE tool CORANA for ARM by adding API stubs for Linux system functions. **CORANA-X** combines Spf and **Corana/API** for analyzing APK-style Java with ARM native, and further external calls in Linux.
## Artifact summary
In previous work [Vu, 2019], a dynamic symbolic execution tool for ARM Cortex-M - CORANA was preliminarily built by extracting ARM formal semantics from natural language descriptions. 

CORANA-X consider performing symbolic execution across heterogeneous platforms. Java can include shared native libraries as .so files, an object library in the ELF format. For ARM native code in Java, we combines two SE tools - Symbolic Pathfinder and CORANA/API. Symbolic Pathfinder are used to analyse Java class file. We prepare a custom listener for Symbolic Pathfiner which invoke CORANA/API at the point of ARM native code. 

- The tool CORANA-X and its dependencies
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

# CORANA/API

From this point, we describe the installation of CORANA/API.
The API stub is an underapproximation of a system function call by spawning its execution in the real Linux environment. 
CORANA/API inputs an ARM binary file and outputs its CFG. The CFG is represented as `.dot` file, thus you can arbitrarily further plot it in any graphic or data structure format as you want.

## Docker installation for CORANA/API
Assuming you have Docker installed, simply run (required internet download):
```
docker build -t corana .
docker run -it corana
```
After that, start mongodb and run the pre-built application using:
```
java -Xss16m -Xmx10240m -jar corana_api.jar -execute $f $Mx
```
The malware samples are in corana/sample, for example, to run Corana to retreive the trace of Mirai.32caff, we use:
```
java -Xss16m -Xmx10240m -jar corana_api.jar -execute samples/32caff26a4dfa373cd0ed869544a30b7\$ M0
```
More details on how to run Corana are presented in the later part. Corana will output the execution trace and a graph .dot file of the binary input. <br />
**NOTE** If encounter MongoSocketOpenException error, please start the service by:
` service mongodb start ` or `mongod --dbpath /var/lib/mongo --logpath /var/log/mongodb/mongod.log --fork`
## Manual Installation for CORANA/API

**Important note:** This installation is for Linux/MacOS only. For Windows, please take a look at the detailed instruction of each individual component.

### Binutils
* Install binutlis:
`sudo apt-get update -y; sudo apt-get install binutils-arm-none-eabi`
* Or from the prepared binutlis package: 
`cd dependencies; sudo dpkg -i binutils-arm-none-eabi_2.27-9.deb`
### Java and File 
**Corana** is written entirely in Java. Thus, make sure that you already installed Java (version 1.8+). In addition, **Corana** use `file` command to check the format of an input binary file. In fact, `file` is already installed by default in Linux/MacOS, but for Windows, please install it first.

### Capstone Engine
**Corana** utilizes Capstone as a single-step disassembler engine. It is worth noting that, we use the older version cloned by TRANScurity instead of the latest one since the maven library used in **Corana** is not compatible with newer releases. Please follow the installation below:
	* Clone the Github repository: https://github.com/TRANScurity/capstone (or using the Capstone repository included in `/denpendencies`)
	* Build: `cd capstone; ./make.sh; sudo ./make.sh install`
    
### Z3 Solver
**Corana** uses Z3 as a back-end SMT Solver to check the satisfiability of path constraints. Z3 can be installed either from source code or command line.
	* Using command line: `sudo apt-get update -y; sudo apt-get install -y z3`
	* From source code:  Please clone Z3 repository https://github.com/Z3Prover/z3 (or using the Z3 repository included in `/denpendencies`) and follow its instruction.
```
	cd z3
	python3 scripts/mk_make.py
	cd build
	make
	sudo make install
```

### MongoDB
* Install MongoDB: 
- Using MongoDB version in `/dependencies`:
```
	cd dependencies
	sudo cp mongodb-linux-x86_64-3.0.4/bin/* /usr/local/bin/
	sudo mkdir -p /var/lib/mongo
	sudo mkdir -p /var/log/mongodb
	sudo chown `whoami` /var/lib/mongo
	sudo chown `whoami` /var/log/mongodb
	sudo mkdir -p /data/db 
```
Then to run MongoDB, run the mongod process (might need sudo):
```
mongod --dbpath /var/lib/mongo --logpath /var/log/mongodb/mongod.log --fork
```
- The second option is to download from MongoDB site:
```
	wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | apt-key add -
	echo "deb http://repo.mongodb.org/apt/debian stretch/mongodb-org/4.4 main" | tee /etc/apt/sources.list.d/mongodb-org-4.4.list
	sudo apt-get install -y mongodb
	sudo service mongodb start
```

### Build Corana
We provide a pre-built **Corana** as a `.jar` file. However, you can still re-build it by simply creating a new artifact from sources. After successfully building, make sure that `corana.jar` is successfully generated.

## Execution 
**Corana** inputs an ARM binary file and outputs its CFG. The CFG is represented as `.dot` file, thus you can arbitrarily further plot it in any graphic or data structure format as you want. Use this command to execute **Corana**:

     java -Xss16m -Xmx10240m -jar corana_api.jar -execute /path/to/input/file

where

 - `-Xss`: the maximum memory allocated for stack size. We recommend to set it around 16MB or larger.
 - `-Xmx`: the maximum memory allocated for the execution. We recommend to set it as much as possible since the dynamic symbolic execution consumes a lot of memory.
 - `/path/to/input/file`: the path to the ARM binary file for analyzing.
 
 For example:
```
java -Xss16m -Xmx10240m -jar corana_api.jar -execute samples/32caff26a4dfa373cd0ed869544a30b7\$ M0
```

If you want to specify an ARM variation, please append the variation name  (M0, M0_Plus, M3, M4, M7, M33) to the end of the command above. Otherwise, **Corana** runs with the general ARM configurations.

     java -Xss16m -Xmx10240m -jar corana_api.jar -execute /path/to/input/file M7

## Contact
Anh V. Vu - Project maintainer - [Email](mailto:anhvvcs@gmail.com)  
Anh V. Nguyen - [Email](anhnv@jaist.ac.jp)

## License
This project is licensed under the [MIT License](http://www.opensource.org/licenses/mit-license.php).

## Acknowledgments

We thank JAIST for financially supporting our project and thank [Jan Willem Janssen](https://www.lxtreme.nl/) for his useful library for effectively parsing ELF binary file: https://github.com/jawi/java-binutils

**[Vu, 2019]** Vu, A., Ogawa, M.: Formal semantics extraction from natural language specifica-tions for arm. In: FM. pp. 465–483. LNCS (09 2019)

