# JavStock V1.0 - Java OOP Project

## 1. Description
<p align="justify">
JavStock is a project developed in Java using the principles of object-oriented programming. This first version offers the basic functionality required for a warehouse. It includes features relating to the creation and management of articles, packages, pallets and racks. <br>
Future versions will introduce new visual and practical features.
</p>

## 2. How to run it

### Prerequisites
* The project uses openjdk-25 - Oracle OpenJDK 25.0.2

### Running program in an IDE
* 1\. Clone the repository on your IDE and open it
* 2\. Run the file `com.logistics.Interface.java`

## 3. Main features

* A complete overview of the current warehouse stock
* Creating and deleting Articles, Packages, Pallets and Racks
* Management of pallet transfers between storage locations
* Package collection for orders
* Data uploads and automatic data updates with CSV files

## 4. Main classes

The project is organized into four java packages :

### User interface
* Interface : Initialises the data and handles user interactions in the console

### Models
* `Stockable` (Interface) : Defines a basic element that can be stored in the warehouse
  * `Article` (Abstract Class) : Defines the basis for a catalogue article
    * `ArticleStandard` (Class) : Represents a standard article, with no fragility restrictions
    * `ArticleFragile` (Class) : Represents a fragile article, with a fragility level between 1 and 5
  * `PackageArticle` (Class) : Represents a package containing a specific article and a quantity
  * `Pallet` (Class) : Represents a standard pallet with a packing list  
* `StorageLocation` (Abstract Class) : Defines the basis of a storage area
  * `StorageLocationPicking` (Class) : Represents the picking zones at level 0
  * `StorageLocationReserve` (Class) : Represents the reserve zones
* `StorageLocationRack` (Class) : Represents the storage racks in the warehouse

### Exception
Several custom exceptions relating to security and constraints

### Services

* `LogisticsService` : Business logic for managing implemented features 
* `CsvService :` Managing loading and saving data

## 5. Technical decisions

* 1\. `Map<T>` : Using Map<> allow to store the elements in the warehouse so that they can be easily retrieved and manipulated using their keys, which correspond to their identifiers 

* 2\. `Automatic save after action` : As CSV files are the only way to preserve data in the event of the interface crashing, it is essential to save the work after every change to ensure no data is lost

* 3\. `Optional<> and replacement search` : When a package is picked for an order during the picking process, if the pallet becomes empty, it is automatically detached from the storage location and the article reference is returned inside an `Optional` to trigger a replacement search. If the pallet is not empty, an empty `Optional` is returned. There are therefore two possible return scenarios that justify this use

## 6. Inheritance

### Article

The `ArticleStandard` and `ArticleFragile` classes both inherit from the abstract parent class Article. They therefore inherit the properties of an article

### StorageLocation

The `StorageLocationPicking` and `StorageLocationReserve` classes, as with the various items, inherit the common properties relating to storage locations

## 7. Polymorphism

Polymorphism is used primarily for managing locations. The locations are stored in a `Map<>` of type StorageLocation, which is used for a variety of operations

## 8. Abstract class & interface 

* Abstract Classes `Article` and `StorageLocation` are used to define shared attributes and constructors, thereby avoiding the duplication of code
* Interface `Stockable` is used to share common methods (getters for ID, weight and volume)

## 9. Custom exceptions

Several custom exceptions have been created to provide a better understanding of what is being thrown :

* Exceeded or Empty exception : `EmptyException` , `CapacityExceededException`, `WeightExceededException`

* Not found exception : `ArticleNotFoundException`, `PackageNotFoundException`, `PalletNotFoundException`, `RackNotFoundException`

* Security moving exception : `NotAPickingException`

* CSV exception : `SavingCsvException`

## 10. Loading data

At startup on the interface, the data is initialized with CSV files in `src/main/resources` with a strict order (`articles -> pallets -> packages -> locations`). This order must be followed because the packages need existing Article and Pallet instances to be created, and the locations also require the Pallet instances

## 11. SOLID principles and structure

### Project structure

```
JavStock/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── logistics/
│   │   │           ├── exception/
│   │   │           ├── model/
│   │   │           ├── service/
│   │   │           └── Interface.java
│   │   └── resources/
│   │ 
│   └── test/
│       └── java/
│           └── com/
│               └── logistics/
│                   ├── model/
│                   └── service/
├── .gitignore
├── pom.xml
└── README.md
```


### SOLID

* Single Responsibility: Application of the single responsibility principle by dividing implementations with different responsibilities into separate packages or files (`Model`, `Service`, `Test`, `Interface`)

* Polymorphism: Use of abstract classes (`Article` and `StorageLocation`) to enable the addition and manipulation of elements whilst retaining the desired behavior and business logic.

## 12. How to run the tests

The project uses JUnit 5 for several unit tests covering workflows and rule exceptions.

### In IntelliJ IDE
* 1\. Right-click on the `src/test/java` directory
* 2\. Select **Run 'All Tests'**
