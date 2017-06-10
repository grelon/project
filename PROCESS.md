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