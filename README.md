# Code ETR (Extract Translate Replace) Quick Start Guide




# About this guide

This guide provides the quick start actions required to use Code ETR.


# About Code ETR

Code ETR is a SQL translation tool for Java and Perl applications. It is used to **extract** SQL from Java and Perl applications, **translate** it to an alternative SQL dialect, and **replace** the original SQL with the translated equivalents. Supported dialects are:



*   **Source dialects:** Oracle Database
*   **Target dialects:** PostgreSQL


#


# Supported functionality

Code ETR currently supports the following functionality:



1. Scan a code set and produce summary and detailed reports:
    1. Java files (`.java`) and Perl files (`.perl`) are scanned for SQL keywords that are subsequently executed in statements with JDBC and Perl DBI  keywords, respectfully;
    2. Resource type files are scanned for SQL keywords. Java property files (`.properties`), yaml files (`.yaml`)  and SQL files (`.sql`) are supported.
2. Rerun reports for a scanned code set:
    3. Reports can be regenerated up to the point before the translated SQL is loaded (i.e. anytime before code\_etr is run with the `--load` option).
3. List all scanned code sets.
4. Extract the SQL statements from Java and Perl to be translated with CompilerWorks:
    4. Each SQL statement is extracted to a file;
    5. The extracted file can be run through CompilerWorks for translation.
5. Translate the SQL extracted from Java and Perl through CompilerWorks (only available to authorized Google personnel).
6. Load the translated SQL statements into the repository database:
    6. CompilerWorks output sql can be loaded into the Code ETR repository database;
    7. SQL statements with CompilerWorks errors during translation are not loaded;
    8. A comparison report can be generated to view the extracted and translated SQL statements.
7. Replace the original SQL statements in the Java and Perl with translated equivalents:
    9. The original SQL statements are replaced with translated SQL in a copy of the code set's source Java and Perl files.


# Prerequisites

The code\_etr program can run on any platform that supports python 3.10.9+.  A UNIX shell script is included that provides checks for the prerequisites.  The following will need to be installed on the machine for code\_etr to be executed:



*   python 3.10.9+ with sqlite module
*   sqlparse 0.6.2+ (`pip install sqlparse`)
*   jproperties 2.1.1+ (`pip install jproperties`)
*   PyYAML 6.0+ (`pip install pyyaml`)


# Installation

After receiving the code\_etr package, execute the following commands:


```
mkdir code_etr
cd code_etr
tar xpf code_etr_{platform}.tar.bz2
```



# Execution


## Usage

To see the range of available options, use the `--help` argument as follows:


```
usage: code_etr.py [-h] [--name NAME] [--input-path INPUT_PATH] [--extract-path EXTRACT_PATH] [--translate-path TRANSLATE_PATH] [--output-path OUTPUT_PATH]
                   [--reports-path REPORTS_PATH] [--logs-path LOGS_PATH] [--cw-home CW_HOME]
                   {list,scan,report,redo-sql,extract,translate,load,compare,replace}

Extract, Translate, and Replace code from a source format to a target format

positional arguments:
  {list,scan,report,redo-sql,extract,translate,load,compare,replace}
                        Action to be taken on the code path

options:
  -h, --help            show this help message and exit
  --name NAME           A name for the set of code contained in the path
  --input-path INPUT_PATH
                        Path to be scanned for the code
  --extract-path EXTRACT_PATH
                        Path to write the extracted sql statements from the code. Defaults to set_files/[set name]/extracted
  --translate-path TRANSLATE_PATH
                        Path to read the translated sql statements. Defaults to set_files/[set name]/translated
  --output-path OUTPUT_PATH
                        Path to write the code with the replacement sql statements. Defaults to set_files/[set name]/output
  --reports-path REPORTS_PATH
                        Path to write reports. Defaults to set_files/[set name]/reports
  --logs-path LOGS_PATH
                        Path to write logs. Defaults to set_files/[set name]/logs
  --cw-home CW_HOME     Compilerworks home
```



## Actions

There are several actions that can be performed, by passing the action as the first positional argument to the `code_etr` shell script. Actions fall into 2 categories: Code flow actions and reporting actions.


### Code Flow Actions

Code flow actions progress the code set through the code\_etr phases until you have code to run on the target platform. Any action can be used and as long as the necessary parameters are also specified for pre-cursor actions, then all each action in the flow up to the one specified will be executed. For example, on a new code set, extract could be the action, as long as the `--input-path` parameter was specified. In that case, the scan and extract actions would be done in a single execution of code\_etr.


<table>
  <tr>
   <td style="background-color: #000099"><strong>Action</strong>
   </td>
   <td style="background-color: #000099"><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td style="background-color: null">scan
   </td>
   <td style="background-color: null">Scan a code set and produce several reports
   </td>
  </tr>
  <tr>
   <td style="background-color: null">extract
   </td>
   <td style="background-color: null">Extract the SQL statements from the Code ETR repository and write them to individual files, so they can be transformed by Compilerworks
   </td>
  </tr>
  <tr>
   <td style="background-color: null">translate
   </td>
   <td style="background-color: null">Run extracted SQL statements through Compilerworks
   </td>
  </tr>
  <tr>
   <td style="background-color: null">load
   </td>
   <td style="background-color: null">After running the SQL through CompilerWorks, load the statements into the Code ETR repository and outputs the comparison report
   </td>
  </tr>
  <tr>
   <td style="background-color: null">replace
   </td>
   <td style="background-color: null">Replace the SQL in Java/Perl files with the translated SQL and output to a new Java/Perl file.
   </td>
  </tr>
</table>



#### Scan

To scan for SQL statements in a Java or Perl application and generate associated reports, use the `scan` option. The `--name` option associates a unique name with the Java or Perl code set and the path to the Java or Perl source path must also be provided using the `--input-path` parameter.


#### Extract

SQL statements are extracted from the Java or Perl source code set with the extract action. This is required to prepare for subsequent translation of the SQL. The extracted SQL statements are stored in files in the `--extract-path` location.


#### Translate

Authorized Google personnel can obtain CompilerWorks and after installing it, code\_etr can be used to run CompilerWorks to translate the extracted SQL. The `--name` option associates a unique name with the Java or Perl code set and the path to the CompilerWorks installation path must also be provided using the `--cw-home` parameter. The  `--extract-path` location and  `--translate-path` location can also be specified.


#### Load

Translated SQL is loaded into the Code ETR repository database with the `--load `action. The `--name` option associates a unique name with the Java or Perl code set and must be provided. The `--translate-path` location can also be specified.


#### Replace

A copy of the Java or Perl source with translated SQL statements is created for the code set with the `replace` action. The `--name` option associates a unique name with the Java or Perl code set and must be provided. An output path for the final code set can be specified.


### Reporting Actions

Reporting actions will allow you to get copies of reports or list code sets that are in process.


<table>
  <tr>
   <td style="background-color: #000099"><strong>Action</strong>
   </td>
   <td style="background-color: #000099"><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td style="background-color: null">report
   </td>
   <td style="background-color: null">Re-run the reports from a scan (without having to re-scan the code) and can be run between the scan and translate phases.
   </td>
  </tr>
  <tr>
   <td style="background-color: null">list
   </td>
   <td style="background-color: null">List out the code sets that have been scanned
   </td>
  </tr>
  <tr>
   <td style="background-color: null">compare
   </td>
   <td style="background-color: null">Produce an HTML report<strong> </strong>to view the before/after SQL of a code set. The compare report cannot be run until a code set scanned, extracted, translated and loaded
   </td>
  </tr>
</table>



#### Report

Use the `report` action to reproduce the reports for a named code set without re-scanning the Java or Perl source.


#### List

A list of all Java and Perl code sets (name and path) can be produced with the `list` action, as follows:


#### Compare

A comparison report of the original and translated SQL statements for a named code set is generated with the `compare` option.


## Output

Various reports are generated by the `scan` and `report` actions and one report is generated by the `load` and `compare` actions:


<table>
  <tr>
   <td style="background-color: #000099"><strong>Report</strong>
   </td>
   <td style="background-color: #000099"><strong>Description</strong>
   </td>
   <td style="background-color: #000099"><strong>Action</strong>
   </td>
  </tr>
  <tr>
   <td style="background-color: null">Summary
   </td>
   <td style="background-color: null">A summary report containing counts of keywords found in the source code.
   </td>
   <td style="background-color: null">scan/report
   </td>
  </tr>
  <tr>
   <td style="background-color: null">Detail HTML and  \
Tab-Delimited
   </td>
   <td style="background-color: null">A detailed report of SQL keywords executed with a JDBC keyword (function) or Perl DB keyword (function).
   </td>
   <td style="background-color: null">scan/report
   </td>
  </tr>
  <tr>
   <td style="background-color: null">SQL Keyword Audit
   </td>
   <td style="background-color: null">A tab-delimited audit report of all statements containing a SQL keyword.
   </td>
   <td style="background-color: null">scan/report
   </td>
  </tr>
  <tr>
   <td style="background-color: null">Java DB Keyword Audit
   </td>
   <td style="background-color: null">A tab-delimited audit report of all statements containing a JDBC keyword.
   </td>
   <td style="background-color: null">scan/report
   </td>
  </tr>
  <tr>
   <td style="background-color: null">Perl DB Keyword Audit
   </td>
   <td style="background-color: null">A tab-delimited audit report of all statements containing a Perl DB keyword.
   </td>
   <td style="background-color: null">scan/report
   </td>
  </tr>
  <tr>
   <td style="background-color: null">Resource Reports
   </td>
   <td style="background-color: null">1 report for each resource file type scanned (properties, yaml, SQL) of all statements containing a SQL keyword.
   </td>
   <td style="background-color: null">scan/report
   </td>
  </tr>
  <tr>
   <td style="background-color: null">Comparison Report
   </td>
   <td style="background-color: null">A report comparing extracted and translated SQL.
   </td>
   <td style="background-color: null">load/compare
   </td>
  </tr>
</table>


