# Vulnerability Analysis
## A1:2017 Injection
### Description
Injection refers to a security risk that occurs when user input is directly used in a programs code, without validation. This can cause issues when the user maliciously tries to acces information that is not supposed to be accessed.

An example of this is SQL injection, where an attacker can submit an SQl query instead of the required information that can inflict problems to the database, if not validated.
### Risk
With this vulnerability the application is at risk of malicious attacks that can heavily impact the workings of the application.

Adding authentication to this application could limit the user base to a trusted group, but would not fully guard the vulnerability.
### Counter-measures
Counter measures against these types of attacks are prepared statements. These statements take the user input and change them to an SQL statement. With this action the statement can be verified before being sent to a database.

Using Hibernate without making changes to the kind of queries it sents to the database also solves this problem.

## A2:2017 Broken Authentication
### Description
Broken Authentication entails that the authentication that users have to go through could be breached if the accounts are not secured properly. This could happen when account credentials are leaked, or by other means like brute-force.  
### Risk
Broken Authentication can be a serious risk to private information, or even application functions when it comes to admin accounts. However since this application does not have a registration system this will not be a vulnerability.  
### Counter-measures
Preventing this vulnerability means implementing multi-factor authentication, promoting the use of strong passwords by having weak-password checks, not using default credentials, along with many other prevention measures.
## A9:2017 Using Components with Known Vulnerabilities
### Description
When using existing frameworks and tools in a project, these dependances can have vulnerabilities in thier code that are already known to the public. They might be solved in a newer version, or another dependency offers the same solution but without the vulnerability.
### Risk
Because these components have known vulnerabilities, attackers can know where to try to attack, and when using these dependencies, the application is at risk.

Adding authentication does not solve the complete issue here, since some vulnerabilities might be outside the authentication proces.
### Counter-measures
In order the prevent this, it is important to know which dependencies you are using and to keep them up to date. There are tools that check all of your dependencies and refer to online databases to see if there are vulnerabilities in them.

