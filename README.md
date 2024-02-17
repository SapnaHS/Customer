# Description 
Design and implement REST APIs to:
- List customer accounts and theirbalances;
- Deposit money to account;
- Withdraw money from account;
- Transfer money between two accounts.
# Business requirements
Account types:
- Regular:
- All operations are allowed.
- Savings:
- You can’t withdraw money;
- You can’t transfer money from this account.
Money transfer types:
- Between own accounts:
- Amount up to 100000 EUR.
- To another customer’s account:
- Amount up to 15000 EUR.
Access:
- Customer should only be allowed to see his own accounts;
- Customer can only withdraw money from his own account;
- Customer can only deposit money to his own account;
- Customer can only transfer money from his own account; 
- Transfer can be done to any existing account.
