# Day 2
Current design:
![sketches](/doc/sketches.jpg)

Current model:
![diagram](/doc/diagram.jpg)

## Insights
+ Selecting an exported statement in the Bunq app opens a dialog that asks how the user wants to *share* not *open* the file. Figure out how to get this app on that list and properly receive the csv file.
    * Solved: sharing in android means sharing between apps. [More info here](https://developer.android.com/training/sharing/receive.html)
+ When statements from multiple accounts are added, there needs to be a way to switch between them. Maybe create an activity for this? (optional)