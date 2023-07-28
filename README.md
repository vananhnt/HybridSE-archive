# HybridSE
**HybridSE** combines Spf and **Corana/API** for analyzing APK-style Java with ARM native, and further external calls in Linux.
## Artifact summary
In previous work [Vu, 2019], a dynamic symbolic execution tool for ARM Cortex-M - CORANA was preliminarily built by extracting ARM formal semantics from natural language descriptions.

HybridSE consider performing symbolic execution across heterogeneous platforms. Java can include shared native libraries as .so files, an object library in the ELF format. For ARM native code in Java, we combines two SE tools - Symbolic Pathfinder and CORANA/API. Symbolic Pathfinder are used to analyse Java class file. We prepare a custom listener for Symbolic Pathfiner which invoke CORANA/API at the point of ARM native code. 

- The tool HybridSE and its dependencies
	* Prerequisite tools are provided in the sub folder /lib (Capstone and Z3)
	* The experimentation results are recoded in experiments/hybridSE
	* Scripts to run angr are in experiments/hybridSE
	* Source code of CORANA/API (for ARM), and jpf-core + jpf-sym (for Java)
- License.txt

## How to run
We have prepared a Ubuntu 22.4 VM (VM user: vbox, Password: 1234) with the project already pre-imported into Eclipse.
Open Eclipse, the project are pre-imported in ~/eclipse-workspace/HybridSE/

To run an example project, please navigate to  *~/eclipse-workspace/HybridSE/experiments/hybridSE/**"example_name"**/src/MainActivity.jpf*
(For example: *~/eclipse-workspace/HybridSE/experiments/hybridSE/defcon/src/MainActivity.jpf*)
- In Package Manager in Eclipse, Find and Right click into MainActivify.jpf 
-  Choose Run As -> Run Configurations ...
- In Run Configurations window, choose: run-JPF-symbc

Please refer to VM_Link for more details.

Note that, the project is already built. To re-build the project, run ant build command in Terminal (since Eclipse 4.23 does not support Ant build with Java 8):
```
cd ~/eclipse-workspace/HybridSE/jpf-core
ant clean build
cd ../jpf-symbc
ant clean build
```

## Manual Installation
A script for installation is prepared, please run:
```
sudo bash install.sh
```
After the installation process, please import HybridSE into Eclipse, then HybridSE can be run as described in (How to run). 

The belows explain how to manually install the components we used in HybridSE.  
### Prerequisite
- Java Open JDK 8 (currently SPF only run on Java 8)  and Ant 1.10.12
```
sudo apt update -y; 
sudo apt install openjdk-8-jdk
sudo apt install ant 
```
- Use the provided source code or 
```
git clone https://github.com/vananhnt/HybridSE.git
git submodule update --init --recursive
```
### Installation of SPF
jpf-core (Java Pathfinder) and jpf-symbc (Symbolic Pathfinder) are used to analyse Java class file. 

Please use the modified jpf-core and jpf-symbc.

Import them in Eclipse as 2 Java projects.

Create a .jpf dir in your home directory and create in it a file called "site.properties" with the following content:
```
jpf-core = ${user.home}/.../path-to-jpf-core-folder/jpf-core

jpf-symbc = ${user.home}/.../path-to-jpf-core-folder/jpf-symbc

extensions={jpf-symbc}
```

### Installation of CORANA/API

#### Binutils
* Install binutlis:
`sudo apt-get update -y; sudo apt-get install binutils-arm-none-eabi`
* Or from the prepared binutlis package: 
`cd dependencies; sudo dpkg -i binutils-arm-none-eabi_2.27-9.deb`
#### Java and File 
**Corana** is written entirely in Java. Thus, make sure that you already installed Java (version 1.8+). In addition, **Corana** use `file` command to check the format of an input binary file. In fact, `file` is already installed by default in Linux/MacOS, but for Windows, please install it first.

#### Capstone Engine
**Corana** utilizes Capstone as a single-step disassembler engine. It is worth noting that, we use the older version cloned by TRANScurity instead of the latest one since the maven library used in **Corana** is not compatible with newer releases. Please follow the installation below:
	* Clone the Github repository: https://github.com/TRANScurity/capstone (or using the Capstone repository included in `/denpendencies`)
	* Build: `cd capstone; ./make.sh; sudo ./make.sh install`
    
#### Z3 Solver
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
#### MongoDB
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
#### Build Corana
We provide a pre-built **Corana** as a `.jar` file. However, you can still re-build it by simply creating a new artifact from sources. After successfully building, make sure that `corana.jar` is successfully generated.

Import CORANA-API by adding the pre-built jar corana.jar as external library of jpf-symbc projects. 
### Combine SPF and CORANA/API
The procedure to analyse a sample is the same as running SPF.
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
Selecting a run configuration from the "Run" menu in Eclipse. In particular you should select: "run-JPF-symbc" to run Symbolic PathFinder on your example. 

## Contact
Anh V. Vu - Project maintainer - [Email](mailto:anhvvcs@gmail.com)  
Anh V. Nguyen - [Email](anhnv@jaist.ac.jp)

## License
This project is licensed under the [MIT License](http://www.opensource.org/licenses/mit-license.php).

## Acknowledgments

We thank JAIST for financially supporting our project and thank [Jan Willem Janssen](https://www.lxtreme.nl/) for his useful library for effectively parsing ELF binary file: https://github.com/jawi/java-binutils

**[Vu, 2019]** Vu, A., Ogawa, M.: Formal semantics extraction from natural language specifica-tions for arm. In: FM. pp. 465–483. LNCS (09 2019)
(https://github.com/anhvvcs/corana)
