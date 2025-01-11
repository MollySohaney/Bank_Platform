# 4370_final_project
## Running the project
* To run this project, from the root directory (which contains pom.xml), run

<code>mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8081'</code>

* Then navigate to "localhost:8081" in a chrome browser

## Contributions
* Michael McLoughlin - Performed BCNF/3NF on relations; created most .mustache files, StatementController, HomeController, most CSS/styling, ddl.sql

* Matt Zohoranacky - Created most Controllers, most Services, most Models; linked most .mustache files to appropriate Controllers

* Kaushal Patel - Created github repo, data.sql, security.txt

* Molly Sohaney - Created ER model, queries.txt; converted ER model to relations; implemented StatementController functionality

* Collective Efforts - error checking, deciding how certain features should function, deciding on what to create, recording our demo

## Database

* Name In MySQL - cs4370_final_project

* Username - root

* Password - mysqlpass

* Full URL Used - jdbc:mysql://localhost:33306/cs4370_final_project

## Username | Password Pairs

* johndoe | passwordone (John Doe's user account)

* janedoe | jane'sbank (Jane Doe's user account)

* easybank | easypass (Joshua Smith's user account)

## Assumptions
* Users will only create and modify accounts via the site, not remove (the user may be required to physically come to the bank to get one removed)

* Users will have to physically come to the bank to dispute or take action against a transaction

* Users have given permission for their own names and their accounts' names to be shown, as well as permission for others to transfer money into their accounts

* Users will only want to transfer money to accounts within our system (not any other banks' accounts), otherwise they will withdraw money to use
