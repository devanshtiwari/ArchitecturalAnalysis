# ArchitecturalAnalysis
Architectural Analysis of C Projects

This tool consists of two parts, ie. contains two main methods.
* Dependency CSV Generation
* Metrics Generation from Dependency CSV

### Dependencies

This tool uses the output of the following open source libaries 
for the processing of C files to generate Dependency CSV.
* GNU cflow [Cflow Documentation](https://www.gnu.org/software/cflow)
* Universal ctags [Ctags](https://ctags.io/)

Make sure commands `cflow` and `ctags` are running on your command line 
before proceeding to run this java tool.

Other dependencies are Java libraries used in the Java project. These
libraries are already included in the iml file. This proejct is worked
in JetBrains Intellij.
* org.jgrapht:jgrapht-core:1.2.0
* guru.nidi:graphviz-java:0.7.0
* org.jgrapht:jgrapht-io:1.2.0
* org.json:json:20180813
* org.apache.commons:commons-math3:3.6.1

### 1. Dependency CSV Generation

Since user interaction is not complete yet, changes to run the script
are required to be made at code level. 
To generate dependency CSV, go to `mainApp.App.java` and set the variable 
``projectDir`` to the directory where the project soruces are kept.

Please note that it can have multiple projects. Each subfolder in ``projectDir``
is considered as single project.
Hence if a directory contains 5 subfolders, all of them are treated as single 
projects.

After this, run the App and wait till the completion of the script.
The output will be saved in a newly generated directory named, ``CSV``.
The CSV directory will contain, 
* Dependencies

    | File1  | Function1 | File2 | Function2 |

    It shows Function1 of File1 calls Function2 of File2.

* FileInfo

    | File  | Headers |

    It contains all the header files included by a file.

* FunctionInfo

    | File Name | Function Name | Parameters | Count |

    It contains the parameters names as well as how many parameters each method have.

* HeaderIncludeInfo

    | Header File Location | Header Name | Included By | Count |

    For each header file, it includes all the files which include that header.

Each subfolder will have a csv named after the projects that were inside the  ``projectDir``.


### 2. Metrics Generation

The second part is the metric generation using the generated CSV from the previous step.
For this step, head to `mainApp.MetricsApp`. Manually set the CSV directory to the variable `CSVdir`
in the main method. Now simply run the `MetricsApp`, and it will generate metrics and create a new folder named
`CSVMetrics`. The structure is defined as follows,
* AllProjects.csv

    This CSV File contains the project level metrics for each project in a single file. 

* Other metrics which are evaluated per node, ie. module level, file level and function level are saved in
subfolders named `ModuleNode`, `FileNode` and `FunctionNode`.

##### Generating Dependency Graphs Images [To be improved]

To do this, head to `metrics.DataOrganiser` and search for `evaluateDependency` method.
Call to ```graph.setDependencyCSVData``` needs to have true parameter instead of 
false to generate dependency graph. Please note that it is computationally expensive to 
generate graphs, so if there are large number of projects, it is better not to generate graphs for all 
the projects.
The module and file graphs are generated in png format in the same directory.
