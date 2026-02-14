# CSL 7110: Assignment 1 - MapReduce and Apache Spark

**Student Name:** Arjun Baidya  
**Roll Number:** M25CSA006  
**Course:** CSL 7110 (Machine Learning with Big Data)  
**Repository:** [https://github.com/arjunbaidya/ML_with_BigData](https://github.com/arjunbaidya/ML_with_BigData)

---

## 📌 Project Overview

This repository contains the implementation and analysis for **Assignment 1**, which explores big data processing techniques using **Hadoop MapReduce** and **Apache Spark**. The project is divided into two main sections:
1.  **MapReduce:** A low-level Java implementation of the WordCount algorithm to analyze HDFS split size overhead.
2.  **Apache Spark:** A PySpark notebook for processing unstructured text, including metadata extraction, TF-IDF document similarity, and author influence network analysis.


## 🛠️ Part 1: Hadoop MapReduce

### Implementation Details
* **Algorithm:** WordCount (counts the frequency of each word in a text corpus).
* **Language:** Java.
* **Framework:** Apache Hadoop (HDFS & MapReduce).

### Key Experiment: Impact of Input Split Size
We analyzed the performance difference when processing the same dataset with different input split sizes to understand MapReduce overhead.

| Split Size | Execution Time | Observation |
|------------|----------------|-------------|
| **100 KB** | ~12,182 ms     | **High Overhead:** The system spent significant time managing the lifecycle of many tiny map tasks (JVM startup/cleanup) rather than processing data. |
| **64 MB** | ~592 ms        | **Optimized:** Larger splits reduced the number of map tasks, significantly lowering overhead and improving execution time. |

### How to Run
1.  **Compile the Code:**
    ```bash
    javac -classpath `hadoop classpath` -d . WordCount.java
    jar -cvf wordcount.jar *.class
    ```
2.  **Run on Hadoop:**
    ```bash
    hadoop jar wordcount.jar WordCount /input_path /output_path
    ```
3.  **View Output:**
    ```bash
    hadoop fs -cat /output_path/part-r-00000
    ```

---

## ⚡ Part 2: Apache Spark (PySpark)

### Environment
* **Platform:** Google Colab (Chosen for higher RAM availability compared to local WSL execution).
* **Language:** Python (PySpark).

### Tasks & Methodology

#### 1. Metadata Extraction
* **Goal:** Extract structured fields (`Title`, `Release Date`, `Language`, `Encoding`) from unstructured Project Gutenberg eBook files.
* **Approach:** Used custom **Regular Expressions (Regex)** to parse inconsistent file headers.
* **Challenges:** Handled multi-line titles, varying date formats, and missing metadata fields.

#### 2. TF-IDF & Document Similarity
* **Goal:** Identify books with similar content.
* **Approach:**
    * Computed **TF-IDF (Term Frequency-Inverse Document Frequency)** vectors to weight word importance.
    * Calculated **Cosine Similarity** between vectors to measure semantic similarity independent of book length.
* **Result:** Successfully ranked books most similar to a target document based on content distribution.

#### 3. Author Influence Network
* **Goal:** Model an influence network between authors based on publication timelines.
* **Approach:**
    * Represented data as a **Spark DataFrame**.
    * Performed a self-join with a **5-year time window** (assuming authors publishing in similar periods may influence each other).
    * Calculated graph metrics: **In-Degree** (Most Influenced) and **Out-Degree** (Most Influential).

---

## 📜 References
* **Dataset:** Project Gutenberg eBooks.
* **Documentation:** Apache Hadoop & Spark Official Documentation.
* **Tutorials:** [1] https://medium.com/@abhikdey06/apache-hadoop-3-3-6-installation-on-ubuntu-22-04-14516bceec85
[2] https://github.com/coder2j/pyspark-tutorial/tree/main

---
*Submitted by Arjun Baidya (M25CSA006)*
