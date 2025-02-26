# Personal File System (PFS) - JavaFX Application Development

## 1. Introduction

I developed a JavaFX application that used the ADT Tree as an Abstract Data Type, following object-oriented principles and incorporating software design patterns.  
The application simulated the management of a file system, named **Personal File System (PFS)**, enabling basic operations such as creation, editing, copying, deletion, and navigation between files and directories, with data persistence.  

The project was delivered in a single phase, as specified in **Section 5 – Deliverables**.  
A template repository was provided through Git Classroom after the team registered on the platform. The registration process followed the instructions made available on Moodle.

## 2. Problem Definition

> "A file system is a set of data structures, interfaces, abstractions, and APIs that work together to manage any type of file on any type of storage device, in a consistent manner."

In the developed application, the **Personal File System (PFS)** consisted of a set of directories and files:

- **Files** – Terminal elements of the structure.
- **Directories** – Elements that could contain other directories, files, or be empty.

### File System Structure

The PFS support structure allowed users to obtain information about stored directories and files, their exact location within the system, and their specific characteristics.  
It was intuitive to model this information using a **tree structure**, where:

- **Directories** could contain other directories and/or files.
- **Text files** could either be simple `.txt` files or tabular data stored as **comma-separated values (.csv)**.

## 3. Requirements

This section outlines the expected functionalities of the application and relevant implementation details.

### 3.1 Data Model

Each group designed the specific **data model** solution for the problem statement.  
The PFS data had to be stored persistently to ensure that all changes made by users were maintained between sessions. This included:

- Information about files and directories within the PFS.
- The relationships between different files and directories.
- The content of the files.

#### File Content Storage

The method of storing file content was left to the group’s discretion. Two possible approaches were:

1. Storing content as an attribute within the class representing a file.
2. Storing content in a physical file while maintaining its relative path as an attribute within the class.  
   - This option had implications for **data persistence**.

### 3.2 Graphical User Interface (GUI)

The GUI was implemented using **JavaFX** and included the following essential components:

1. **TreeView** – A visual representation of the PFS structure.
2. **Details Panel** – Displayed information when a directory or file was selected, including:
   - Creation date and time.
   - Last modification date and time.
   - Number of registered modifications.
   - **For files only**:
     - `locked` attribute.
     - File size in bytes.
     - Importance level (integer between 0 and 4).
3. **Editor Panel** – Displayed and allowed editing of `.txt` or `.csv` file contents.
4. **Toolbar** – Provided buttons to execute all directory and file operations.

### 3.3 Functionalities

#### 3.3.1 Directory Operations

The following directory operations were implemented:

- **Create** – Created an empty directory.
- **Move** – Moved a directory to another position in the tree.
- **Rename** – Changed the directory's name.
- **Delete** – Removed the directory along with all its contents.
- **Flatten** – Moved all files from subdirectories to the current directory and deleted the subdirectories.  
  - If name conflicts occurred, files were renamed with suffixes (`_1`, `_2`, `_3`, etc.).

#### 3.3.2 File Operations

- **Create** – Created a new file (default state: unlocked).
- **Protect** – Changed the file state to locked, requiring a password.
- **Move** – Moved a file to another position in the tree.
- **Rename** – Changed the file name.
- **Delete** – Removed the file.
- **View** – Displayed the file content (required password if locked).
- **Edit** – Allowed content modification (required password if locked).
- **Concatenate** – Created a new file with merged content from selected files.

#### 3.3.3 General PFS Operations

Beyond file and directory operations, the following functionalities were implemented:

- **Search Functionality**:
  - **By Name** – Checked if the input matched any file or directory name.
  - **By Content** – Checked if the input existed within any file’s content.

  A **single search form** was implemented, displaying two output panels for:
  - Matching directory/file names.
  - Matching file content.

- **Freeing Space**:
  - By **modification date** (removing older files first).
  - By **importance level** (removing less important files first).

- **Backup & Restore**:
  - The ability to save the current state of the **PFS** and restore it based on saved states.

### 3.3.4 PFS Metrics Calculation

The application provided a **pie chart** visualization of storage usage per directory, mapped to the directory selected in the GUI.

### 3.4 Implementation Details

Key implementation principles followed:

- **Separation of Concerns**:
  - Clear distinction between **data model, business logic, and GUI**.
  - Proper use of **packages** and **software design patterns**.

- **MVC (Model-View-Controller)**:
  - The GUI only handled **user input and result display**.
  - Business logic was handled separately.

- **Atomic Functionality**:
  - Each functionality had clearly defined **inputs and outputs**.
  - Standalone methods handled logic, ensuring clear separation from the UI.

- **Executable JAR**:
  - The final version could be launched via **double-click** on the generated `.jar` file.

- **Best Practices**:
  - No console-based implementations were accepted.
  - Proper application of **software design patterns** and **object-oriented principles**.
  - Minimization of **code smells** through **refactoring**.

- **Unit Testing**:
  - All classes implementing atomic operations and metrics calculations had unit tests.

## 4. Documentation 

### 4.1 Documentation

All code was documented using **Javadoc**.  
The documentation was generated in **HTML format** and delivered along with the project (`javadoc` directory).

