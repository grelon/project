# Final report

## Description
This app gives an overview of what you spend your money on if you are a Bunq customer. Categorize new transactions once and after that, they are categorized automatically. Simply share a CSV (semi-colon seperated) of your transaction statement with this app and you're good to go.  

![screenshot1](/doc/screenshot1.png)  

## Technical design
### Overview
+ The main screen is occupied by the ChartActivity, which charts categories in a donut chart. A user is sent here when launching the app, when importing new transactions, or by using back navigation.
+ When a stubcategory (i.e. has no subcategories) is clicked, the user is sent to a list with all transactions in that category (TransactionListActivity) which uses the TransactionRecyclerAdapter to render the transactions. 
+ After clicking a transaction a user is sent to the details of that transaction (SingleTransactionActivity).
    * Here the user can change the category of the transaction.  
  
+ The DB package is occupied by any classes related to database management.
+ The Helpers package is occupied by any helper classes (excluding those related to the database).
+ The ModelClasses package contains the Account, Category and Transaction classes.  

### Details
#### ChartActivity.java
+ This is the controller for the main screen.
+ On creation it first checks if it's intent contains a CSV file.
    * If it does it passes the intent to the CsvImportHelper, that further handles the import.
    * It also informs the user if there where new transactions.
+ If the intent contains a Category object, ChartActivity calls on the ChartHelper to turn that into data to build a chart.
+ Otherwise it creates a chart using all the categories.
+ Lastly, if there is no data available, it informs the user about that.

+ When the user selects a category on the chart it calls on the ChartHelper to handle that event.
+ When back navigation is triggered, ChartActivity first checks if there is a parentcategory to render. Only if there isn't the app is exited.
  
#### CsvImportHelper.java
+ This file handles the incoming CSV from the intent and turns it into a list of transaction objects.
+ While doing so it also sets up a new account if the data from the CSV indicates it contains transactions from a new account.
    * To set up the default categories for this account, the CategoryHelper comes to the rescue.
+ The transaction data is also formatted.
+ Lastly, the transaction is checked for duplicity.

#### CategoryHelper.java
+ This helper's main task is to categorize any given transactions into their respective categories.
    * To do this, the normalized Levenshtein score is used.
    * First two transaction descriptions are formatted for the comparison.
    * Then they are compared using the algorithm.
    * If the score is above 0.4 (threshold found by trial and error), they are considered similar and therefor probably belong in the same category.
    * Thus, the new transaction is writing to the databse with the same category id as the existing transaction.
    * Otherwise it lands in 'uncategorized'.

+ The second task this helper class has, is setting up the default categories for a new account.

#### DBManager.java
+ If there is no existing database when this class is called, it asks DBHelper to create one.
+ Afterwards this class handles the database manipulation for an existing database using a set of CRUD methods for each table.
+ The CRUD methods for the Accounts table are rather basic since support for multiple accounts is not yet build into the app for this version.
+ The CRUD methods for the Categories table and simple, except for the read method.
    * readCategories() is complicated because that helps the rest of the app with quickly getting all the data it needs from the database.
        - It reads all categories from the table and creates a list of Category objects out of them.
        - Then it returns either the root categories or a specified category, filled with all its subcategories.
        - The categories are not populated with their transactions when readCategories() returns. This is done when Category.updateTransactions() is called. First this prevents the infinite loop of when trying to create a Category with Transactions, that in turn need the Category's data to be created. Second, it keeps the Category object lightweight until it actually needs its list of Transactions.
    * updateCategory() does exactly what you would expect. Clean and simple.
    * deleteCategory() first categorizes the category's transactions to 'uncategorized' and then deletes the given category.
+ The CRUD methods for Transactions are rather straightforward as well, except for the read method.
    * readTransactions() either returns a list of all Transactions in the database, or just those that belong to the specified category.

#### DBHelper.java
+ Contains all constants and methods needed to create a database. Pretty standard really.

#### ChartHelper.java
+ Helps ChartActivity with building it's chart.
+ It transforms an array of categories into a usable PieData object, for ChartActivity to use when setting up the chart for the root categories. This is done by setupPieData().
+ rebuildPieData() rebuilds the PieData object created by setupPieData() when a category is clicked. But only if the clicked category has subcategories. Otherwise it calls the TransactionListActivity into life.
    * The design of the MPAndroidChart API encourages using the same PieData object.
    * Therefore the PieData object is cleared and filled with data from the selected Category object.
+ Lastly, it prettifies the Legend a bit.

#### TransactionListActivity.java
+ This controls the screen that displays the transactions of a category. 
+ The list is rendered by a recyclerview that uses the TransactionListAdapter.

#### TransactionListAdapter.java
+ This class takes the Transactions it is given and adapts it for a clickable RecyclerView. 
+ When a transaction is clicked it calls the SingleTransactionActivity.

#### SingleTransactionListActivity.java
+ Shows the details of a single transaction.
+ Also provides the possibility to change the category of the transaction.
  
## Challenges and changes
+ The first challenge was being able to 'read' the CSV file from the Bunq app. Figuring out what type of file (and metadata) they where sharing EXACTLY proved quite difficult. Especially since they didn't completely adhere to the best practices Google prescribes. After figuring this out I configured the app to only accept this type of file, which means it doesn't accept regular CSV files. In my opinion a good thing.
+ Another challenge was the fact that the charts api didn't provide a onClick listener for pie slices. It only had a onValueHighlighted listener. By tweaking around a bit functions as a onClick listener (i.e. you don't see any highlighting), but is still a onValueHighlighted listener.
+ One of the hardest challenges was the fact that to create a full donutchart from the categories, they would need to be mutually exclusive. In other words, a transaction can only be in one category at a time to properly draw a chart. However, it was preferable for users to have the freedom to decide in what category a certain transaction should go. Therefor I wanted to implement user created filters.  
This turned out to be difficult since users could create contradicting filters.
Thus, after exploring several solutions, I settled on using string similarity algorithms to categorize new transactions on behalf of the user. Currently this compares two transaction, but ideally you would want to abstract a category profile, or profiles, to compare against. This didn't fit into the timeframe of this course however.
+ After a while (week 3) I figured out that the best way to organize categories was through a tree like structure, where categories contain transactions. This would also be a promising infrastructure for future versions that could employ a large amount of subcategories (should the user want to specify its transaction to that level). Up until this time I had been reading transactions directly from the transactions table, instead of using Category objects as an intermediate. Therefore I rewrote the read methods for transactions and categories in DBManager. Only this week did I realize it also didn't make sense to have three rootcategories (expenses, income, uncategorized) instead of one (called root for example). This would really streamline the tree structure. However, I didn't have the time to implement this.