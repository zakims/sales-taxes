## Problem 1: SALES TAXES

Basic sales tax is applicable at a rate of 10% on all goods, except books, food, and medical products that are exempt. Import duty is an additional sales tax applicable on all imported goods at a rate of 5%, with no exemptions. When I purchase items I receive a receipt which lists the name of all the items and their price (including tax), finishing with the total cost of the items,
and the total amounts of sales taxes paid.  The rounding rules for sales tax are that for a tax rate of n%, a shelf price of p contains (np/100 rounded up to the nearest 0.05) amount of sales tax.

Write an application that prints out the receipt details for these shopping baskets...

### INPUT:
Input 1:
> 1 book at 12.49  
> 1 music CD at 14.99  
> 1 chocolate bar at 0.85

Input 2:
> 1 imported box of chocolates at 10.00  
> 1 imported bottle of perfume at 47.50  

Input 3:
> 1 imported bottle of perfume at 27.99  
> 1 bottle of perfume at 18.99  
> 1 packet of headache pills at 9.75  
> 1 box of imported chocolates at 11.25

### OUTPUT:
Output 1:
> 1 book: 12.49  
> 1 music CD: 16.49  
> 1 chocolate bar: 0.85  
> Sales Taxes: 1.50  
> Total: 29.83  

Output 2:
> 1 imported box of chocolates: 10.50  
> 1 imported bottle of perfume: 54.65  
> Sales Taxes: 7.65  
> Total: 65.15

Output 3:
> 1 imported bottle of perfume: 32.19  
> 1 bottle of perfume: 20.89  
> 1 packet of headache pills: 9.75  
> 1 imported box of chocolates: 11.85  
> Sales Taxes: 6.70  
> Total: 74.68

## How to use:

The repository is an Eclipse Maven project. 

1. Download Maven and JDK 16, unzip, setup JAVA_HOME and MAVEN_HOME environment variables and add them to your path (if needed checkout this [step-by-step guide](https://mkyong.com/maven/how-to-install-maven-in-windows/) from mkyong
2. CLone the repo to your local PC
3. Open CMD prompt or git bash inside your local repo and type:
`mvn package`
This will compile, test and package the application into target/salestaxes-0.0.1-SNAPSHOT.jar
4. Run the application and pass the input file path as a command line argument
`java -jar target/salestaxes-0.0.1-SNAPSHOT.jar input1.txt`


## Assumptions:

* Each line in the input file follows the format below in this exact order:
    1.	Line starts with integer value representing the ordered **quantity**
    2.	If the product is imported, the keyword **imported** is added. It is allowed to switch position between **imported** keyword and the sales **unit** (in 3)
    3.	If the product has a special sales **unit** (e.g., kg, bottle, packet), it must be followed by **of** keyword. It is allowed to switch position between sales **unit** and **imported** keyword (in 2)
    4.	Product **name** comes immediately before the **price**. It can be composed of several words, but cannot contain any of the keywords: *of*, *at*, *imported* 
    5.	Line ends with the net **price** of a single unit of the product preceded by **at** keyword

* The application has two files packaged into the final jar. [food-products.txt](src/main/resources/food-products.txt) : lists the names of all food products (one name per line) which is used to identify food products.
[medical-products.txt](src/main/resources/medical-products.txt): lists the names of all medical products (one name per line) which is used to identify medical products. Feel free to change the content of those two files and repeat the build as specified above.

* Product type inference from product name: 
    1. If name contains “book” : it is of type **Book**
    2. If name is listed in the food products file: it is of type **Food**
    3. If name is listed in the medical products file: it is of type **Medicine**
    4.	 If none of the above: it is other type which has a non-zero (0.05) basic tax rate

* No Discounts: Shelf price is equal to gross price (e.g. the paid price including all sales taxes)
