This folder contains my submisison of the implementation for the scenario provided by Akanksha.
There are 2 folders
1. Executeables - contains the jar files
2. Source code - contains the source code of the implementation

-------------------
Problem Statement provided

Create an ATM Machine that should be able to take care of following use cases:

Must Haves:

Ability to capture Card and PIN details and authenticate the user
Ability to load multiple denominations nodes into the system
Ability to withdraw multiple denominations nodes into the system
Ability to check account balance
User should not be able to withdraw more than 40 notes at a time
User Authentication should be done before any activities can be performed
For any user activities, Banking api's needs to be invoked on the Banking server
-------------------
My understanding of the above problem statement

Application must contain two modes -
1. Admin mode
   Ability to load multiple denomination notes into the system
2. User mode
   Ability to enter card number,PIN and authenticate self
   Upon succesful authentication
   Ability to check account balance
   User should be able to withdraw notes of multiple denominations
   User is limited to withdraw a max of 40 notes at a time

Banking API for user activities (separate module)
   Ability to authenticate user with card details and PIN
   Get account balance for user
   Withdraw money from account
   (preload a set of users and account balance into the Banking API)
====================

Implementation details

I have created two separate modules for the provided scenario
1. Banking server - 
	This is a spring boot REST application providing apis for all banking operations as required.
	Implementation provides following endpoints
	- GetAccount; input - cardNumber,cardPIN
	- GetBalanceForAccount; input - accountId (returned in GetAccountOperation)
	- UpdateAccountBalance; input - accountId, balanceAmount (used by ATM to update balance after withdrawal) 
	* In the interest of time I have not used a db for persistence instead I am storing data in Spring bean
	* On application startup 3 user details are loaded
	
	card number - card PIN - balance
	--------------------------------
	1111		  1212		 20000
	2222		  2323		 10000
	3333		  3434		 5000	

	For validation these inputs must be provided

2. Banking client - ATM machine
	This is a spring shell application provides a console interface for performing ATM operations
	ATM machine has been implemented as an application containing two modes 

	- admin-mode
		- This mode is for performing administrative activities on the ATM, currently adding cash of multiple denominations has been implemented
		- Implementation supports adding of any denomination to the ATM
		- Validates that the amount and denomination added match
		- Displays available total cash in machine with denomination details after successful cash upload
		- Supports adding cash multiple times - however because the implementation does not contain db persistence the upload is valid only as long as the application is running (beans are being to store values during the application execution)


	- user-mode
		- This mode is for performing user activities on the ATM
		- user must first login by providing valid cardNumber and cardPin details (banking server api is used for validation)
		- Post login user can perform following activities

		- Check balance in account 
			- upon selection of this operation the banking api is invoked to retrieve the balance for the logged in user
			- on success the balance amount is displayed on the screen

		- Withdraw amount from account
			- upon selection of this operation user is prompted to enter the amount to withdraw
			- application validates that the logged in user has sufficient balance in the account
			- application validates that the atm machine has sufficient balance loaded
			- determines the denomination to dispense based on the CashWithdrawalStrategy configured in the application (currently one implementation is provided - LeastNoteswithdrawal, to dispense least possible notes from the atm for the requested amount it uses a greedy approach to determine the denominations)
			- after the denomination is determined and validated that the number of notes is less than 40 (rule in scenario), the account balance is updated using the banking server api to update the balanceAmount
			- the amount and denomination details of the cash dispensed by the ATM is displayed on the screen


Steps to execute the application and demonstrate all specified scenarios is provided in the steps_to_execute.txt file available in the Executeables folder, please follow the same.

Thanks,
Praveen 

