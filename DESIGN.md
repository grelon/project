# Design Doc

+ Diagram of modules
![diagram](/doc/diagram.jpg)

+ Advanced sketches of UI
See current sketches in README.md
+ List of API's and frameworks
MPAndroidCharts
+ List of Datasources
The data will come from imported CSV files. A file will have the following format:
>"Date","Amount","Account","Counterparty","Name","Description"
"2017-05-06","20,00","NL01BANK0123456789","NL01BANK0123456789","J. Doe","Topup account"
+ List of DB tables and fields
    * accounts
        - account_id
        - account_number
        - name
    * categories
        - category_id
        - account_id
        - name
    * transactions
        - transaction_id
        - category_id
        - account_id
        - date
        - amount
        - counterparty
        - name
        - description