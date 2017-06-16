# 7-6
Current design:
![sketches](/doc/sketches.jpg)

Current model:
![diagram](/doc/diagram.jpg)

**Insights**
+ Selecting an exported statement in the Bunq app opens a dialog that asks how the user wants to *share* not *open* the file. Figure out how to get this app on that list and properly receive the csv file.
    * Solved: sharing in android means sharing between apps. [More info here](https://developer.android.com/training/sharing/receive.html)
+ When statements from multiple accounts are added, there needs to be a way to switch between them. Maybe create an activity for this? (optional)

# 8-6
**Insights**
+ The chart library doesnt have a onclicklistener for pieces of the pie. At the moment a onChartValueSelectListener is used, but this is not ideal.
+ Still to decide: what screen is the main screen?

# 10-6
**Insights**
+ Categorizer needs to be part of MVP. Host of extra activities needed for this.
+ Best way to implement app is by using a main activity that hosts fragments containing everything that is linked to in the navigation drawer. These fragments can incidentally link to activities (e.g. add, edit, etc.)
+ Categories need to be mutually exclusive to create the charts. Also, categorization can only be swift with filters. Thus well thought out mutually exclusive filter functionality is key.

# 12-6
**Insights**  
+ CSV needs to be semicolon seperated instead of comma seperated (i.e. European format) to make sure decimal numbers are parsed properly.

# 13-6
+ Note to self: don't underestimate setting up databases.

# 16-6
Spoils of the week:  
+ CSV's from Bunq app are be processed and written to DB
+ Income/Expenses and Categories chart are build with DB data
+ Categorization is currently done by checking for hardcoded strings
+ When no subcategories are present, user is send from chart to transactionlist

**Insights**  
+ Do NOT use floats for representation of currency. Floating point imprecision catches up earlier than you would expect.
+ Recyclerviews are more difficult to implement than ListViews, but it's worth it for long lists.
+ PieEntries can be passed Objects (such as category objects) which can come in handy.
+ A lot of unnecessary data is currently read from DB during queries. Figure out a way to make flexible CRUD methods (i.e. with where clauses and the like). This doesn't have priority since there the runtime of the current DB queries isn't noticable yet.

