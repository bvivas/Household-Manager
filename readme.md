# Household-Manager

Android application for managing the economy of a household. Developed for the Bachelor's Thesis (TFG) at EPS-UAM.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white) 	![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

- [Installation](#installation)
- [Usage](#usage)


## Installation
To test the app, it will be necessary to have Android Studio installed, and configure the emulator.

To open the project, start Android Studio and select *File -> Open Project*. The file to open is `App`.

### Create virtual device on Android Studio
To create a virtual device in the emulator:
1. Start the device manager (also named as AVD).
2. Select *Create device*.
3. In the installer wizard, select a device (the app has been tested on a Pixel 3a XL) with a x86 system image and a level 30 API.
4. Enter a name for the virtual device.

Once the device is created, start it and enable the developer options going to *Settings -> About phone (or about device)* and tap several times on the *Build number* option.

### Run the app
1. Build the project with *Build -> Make Project*.
2. Start the app with *Run*.

While using the application, the database will be accessible in the *App inspection* tab.  

## Usage
Once the app is started, tap on the logo and you will be prompted to the *Home Screen*.  

There are five main sections:  
- **Home:** all transactions will be shown here, from newest to oldest. Each transaction will show its name, the amount of money spent, the account used, the category it belongs to, the date, and an optional note. You can filter transactions by expenses, incomes or all together. Also, tapping on a transaction will display the *Edit Transaction Screen*, where you can edit any field of the transaction, hence the accounts budget and category stats will be recalculated.
- **Stats:** this screen will display a pie chart showing the proportion each category has been used, for expense and income categories. It will also display a bar chart showing the balance between them.
- **New Transaction:** you can make a new transaction here. You have to enter a name, an amount of money, the account to extract the money from or insert into, the category it belongs to and an optional note.
- **Planning:** this section shows a calendar which will (may) be implemented in the future with amazing features!
- **Settings:** in this last section you can check your accounts and categories, being able to add new ones and delete them.

## Demo
### Small tour

First look at each of the five sections.  
  
<p align="center">
  <img src="https://github.com/bvivas/Household-Manager/blob/master/media/show_app.gif" width="350">
</p>

### Filters and stats

See how the filters and stats work.  
  
<p align="center">
  <img src="https://github.com/bvivas/Household-Manager/blob/master/media/filter_stats.gif" width="350">
</p>

### New transaction

Add a new transaction, check the account budget and later edit the transaction.  
  
<p align="center">
  <img src="https://github.com/bvivas/Household-Manager/blob/master/media/transaction.gif" width="350">
</p>
