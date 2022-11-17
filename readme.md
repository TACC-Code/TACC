
# TACC
TACC(Token and AST-based Code Clone Detector) is an effective code clone detection tool that can scale to big code.

The overall process of TACC can be shown in the figure below. 
![Overview of TACC](/IMG/flow.png "Overview of TACC")
## Requirements
- JDK 8+
- Txl (Installation address:`https://www.txl.ca/`)

## Install & Usage
- Clone this repository (`git clone https://github.com/TACC-Code/TACC`)
- Move into TACC's directory (`cd TACC`) 
- Run TACC (`java -jar TACC.jar [options]` or `javac Main.java && java Main [options]`)
- Get two result files
  - allFunctionMap.csv, the format is 
  `[function_index, file_path, start_line, end_line]`.
  - Clone detect results of different groups are stored separately and named by group number, the format is `
  [function_index_A, function_index_B]`

## Options
|Name|Description|Default|
|:--:|:--|:--:|
|`-input`|Input source directory. You must specify the target dir.|None|
|`-N`|N for N-Lines.|`3`|
|`-partition`|The number of partitions.|`10`|
|`-output`|Output file directory.|`current directory`|
|`-thread`|The number of threads used for parallel execution (both the *Preprocess* and *Clone detection* phases)|10|
|`-fs`|N-line seed filter score.|`0.1`|
|`-tvs`|Token based verify score.|`0.5`|
|`-nnavs`|Not need ast verify score.|`0.7`|
|`-atvs`|AST two verify score.|`0.65`|
|`-afvs`|AST four verify score.|`0.85`|



