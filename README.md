ORM - Hibernate

Database - MySQL (8.0.21)

###Installation/Configuration:

1. Installation for MySQL on OSX/Windows:

    `https://dev.mysql.com/doc/mysql-osx-excerpt/5.7/en/osx-installation-pkg.html`

2. Installation of MySQL Workbench on OSX/Windows:

    `https://dev.mysql.com/downloads/workbench/`

3. MySQL configuration (please use this password while installing MySQL):

   `Hostname: localhost, Port: 3306, Username: root, Password: 12345678 (or the one given by you during installation in #1)`
   
4. MySQL schema:
    
   Create new schema `milestone4` in MySQL Workbench (Right click in Left Pane near `sys`, and select `Create new schema`)
   
5. MySQL scripts (before launching Fx Application):

   Go to `src/main/resources/scripts` -> Run `CREATE_TABLES.sql` and then Run `CONSOLIDATED_INSERTS.txt` in *serial order*        

6. JDK Used - 1.8

   *JavaFx comes along till JDK 1.8, if you use advanced versions, you need to install JavaFx separately*    


###Common Issues

1. OSX - `mysql-8.0.21-macos10.15-x86_64.pkg” can’t be opened because Apple cannot check it for malicious software.`

    If you see above error while installing MySQL on OSX, go to System Preferences > Security & Privacy > General > Click *Open Anyway*

2. OSX - `MySQL is installed but is not starting.`

    Use MySQL Preference Pane (`https://dev.mysql.com/doc/mysql-osx-excerpt/5.7/en/osx-installation-prefpane.html`) to Start MySQL server.
