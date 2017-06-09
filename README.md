# Project
Final project for Minor Programmeren

## Proposal

### Problem
*What problem will be solved for the user?*  
If Bunq users want to explore their bankstatements to figure out where their money is going, they would have to:  
+ peruse their statements in textual form
OR
+ download and then upload their statement to a service such as AFAS Personal.  
This requires to much effort on the user's part to do on a daily basis.

### Features
*What features will be available to solve the problem?*

To solve the above problem, the app:  
+ **Easily imports bankstatements**  
This app will be able to open exported bankstatements directly from the Bunq app and categorize the transactions. The file type will be CSV, and the format will be "Date","Amount","Account","Counterparty","Name","Description".
(optional): integration with the [Bunq API](https://docs.bunq.com) to automically import bankstatements.
+ **Categorizes transactions**  
By default a portion of the transactions is automatically categorized using keywords in transaction descriptions
(optional): Uncategorized or wrongly categorized can be assigned to categories manually.
(optional): Custom categories accompanied by their respective filter can be created.
+ **Charts transactions in overviews**  
By plotting the distribution of transactions over the categories per month in a clickable donut chart. Clicking on portions of the chart produces a new donut chart of the subcategories of that category. Transactions are the lowest level and are displayed in a list.  
(optional): Users can switch between periods (e.g. months, weeks) to see previous distributions of transactions.  
(optional): Users can compare spending between periods (e.g. months, weeks).

### Sketches
*A visual sketch of what the application will look like for the user; if you envision the application to have multiple screens, sketch these all out separately.*

![sketches](/doc/sketches.jpg)

### Data sources
*What data sets and data sources will you need, how you will get the data into the right form for your app?*

**CSV Bankstatement**  
The Bunq App exports bankstatements in the [UK CSV format](https://en.wikipedia.org/wiki/Comma-separated_values#Example) which will be used as data source for this app. This will be processed and sorted into a database.

### App decomposed
*What separate parts of the application can be defined (decomposing the problem) and how do they work together?*

+ **Importer**
Handles the importing of a CSV bankstatement.
+ **Categorizer**
Parses data input file, categorizes the entries, and sorts them into a database.
+ **DB Controller**
Takes care of databse related activity.
+ **Chart builder**
Builds a chart using the data from the DB
+ **Activities**
Takes care of activity specific methods, flow, etcetera. Charts live here.
+ **Navigator** (optional)
Handles activity independent navigation within the app.

### External components
*Which external components (online APIs, libraries etc.) you will need to make certain features possible (name and URL link).*

+ To retrieve data, CSV files formatted as a Bunq export CSV, can be used.
+ The [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) library is used for the charts within the app

### Risks
*Technical problems or limitations that could arise during development and what possibilities you have to overcome these.*
+ MPAndroidChart can have insufficient functionality
+ SQLite isn't suitable for storing the information. Firebase might be an alternative.
+ Integration with Bunq API is difficult and buggy. This can be circumvented by manually exporting statements from the Bunq app.

### Review of similar apps
Grip is one of the few Dutch apps that solves more or less the same problem. It presents overviews using donutcharts, barcharts and linecharts. For the sake of simplicity, only the donutcharts will be used in this app for now.

### MVP defined
*Which parts of the application define the minimum viable product (MVP) and which parts may be optional to implement.*
When a part is optional, it is marked as such.
