# Corana/API

**Corana** is an on-going project providing a Dynamic Symbolic Execution tool for ARM Cortex-M. It takes an ARM binary file as the input and outputs its precise Control Flow Graph (CFG) under the presence of obfuscations like indirect jumps. Since it is currently a preliminary version and still being regularly improved, bugs may occur. The number of supported instruction is also limited. **Corana/API** extends the DSE tool CORANA for ARM by adding API stubs for Linux system functions.

## Artifact summary
In previous work [Vu, 2019], a dynamic symbolic execution tool for ARM Cortex-M - CORANA was preliminarily built by extracting ARM formal semantics from natural language descriptions. 

However, the DSE tool encounters problems when the binary contains library function calls and system calls. Our paper proposed an automatic generation of Linux API Stub from the C function interface, which extends the DSE tool CORANA for ARM. By 1659 collected API descriptions, we are able to produce 1129 API Stubs and 267 structure definition classes. The API stub is an underapproximation of a system function call by spawning its execution in the real Linux environment.

To demonstrate the ability of CORANA/API, we produce the execution traces of some malware IoT samples using the tool. The result shows that CORANA/API is able to trace real-world IoT malware samples and is resilient against several   obfuscation techniques. This artifact contains the tool **Corana/API** – DSE tool for ARM binaries with Linux API Stub support and related documentation, including:

- The tool and its dependencies
	*  Prerequisite tools are provided in the sub folder `/dependencies` (Capstone, Z3, mongodb, binutils)
	* The experimentation result of the paper is produced by a simple script `run_corana.sh` that run CORANA/API through all the malware samples. Some samples might take long time to finish.
	* Source code and the experiment samples are in `/src` and `/samples`
	* A pre-run result of Mirai by CORANA/API is in `/example_trace/32caff-fulltrace.out`
- A `Dockerfile` is also provided for easy installation (require internet connection).
- README.md
	* Contains step-by-step instructions on how to run the tool CORANA/API
- License.txt

CORANA/API inputs an ARM binary file and outputs its CFG. The CFG is represented as `.dot` file, thus you can arbitrarily further plot it in any graphic or data structure format as you want.

## Docker installation
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
## Manual Installation

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

## Experiments
* To generate all the trace of malware samples in the experiments, run the prepared script by: 
	`sudo chmod +x run_corana.sh; ./run_corana.sh`
The detailed output traces of the malware samples will be put in /output folder.
The .dot files which are contains the CFG are also generated and can be found in /samples.
* The scripts to run Angr are in `resources/scripts`

## Limitations and future works
### Function pointer 
A function pointer points to code, not data. We do not know how many cells needed to be copied into the emulated memory. Moreover, as the emulated memory in Java and actual memory of the system is not the same, the return address of the function pointer in JNA will point to a location in the actual system memory. Therefore, a method needed to be built to handle memory allocation and function fointer.
### Loop invariant generation
In the current implementation, we have to set an upper bound on the number of loop unrollings. Loop invariant can be used to handle loop. Dealing with loops is one of the main difficulties in symbolic execution, especially in the binary case.  Due to the lack of syntactic structure, “what is a loop” is not clear.  Automatically constructing inductive loop invariants is a classical problem in program analyses (e.g., Farkas’ Lemma , Craig interpolation), however, they mainly focus on high-level languages whose syntax of a loop statement is clearly defined.  The goal is to propose a loop invariant generation method targeting binary executables of typical loop structures in IoT malware (e.g., ARM Cortex-M-based malware). 
### Control flow graph construction
Currently, CORANA is able to generate execution traces of the analyzed binary samples. However, to correctly construct a CFG from the trace, we have to investigate how to define a model for the CFG, especially in the presence of typical obfuscations (e.g., self-modification).

## Contact
Anh V. Vu - Project maintainer - [Email](mailto:anhvvcs@gmail.com)  
Anh V. Nguyen - [Email](anhnv@jaist.ac.jp)

## License
This project is licensed under the [MIT License](http://www.opensource.org/licenses/mit-license.php).

## Acknowledgments

We thank JAIST for financially supporting our project and thank [Jan Willem Janssen](https://www.lxtreme.nl/) for his useful library for effectively parsing ELF binary file: https://github.com/jawi/java-binutils

**[Vu, 2019]** Vu, A., Ogawa, M.: Formal semantics extraction from natural language specifica-tions for arm. In: FM. pp. 465–483. LNCS (09 2019)

