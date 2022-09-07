
# TACC
TACC is a......

## Requirements
- JDK 8+
- Txl

## Install & Usage
- Clone this repository (`git clone https://github.com/TACC-Code/TACC`)
- Move into TACC's directory (`cd TACC`) 
- Run TACC (`java -jar TACC.jar [options]`)
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

