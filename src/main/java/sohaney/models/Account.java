package sohaney.models;

/**
 * Represents a bank account in the banking site.
 */
public class Account {

	/**
	 * The unique identifier of the account.
	 */
	private final int acc_id;

	/**
	 * The user-defined name of the account.
	 */
	private final String name;
	
    /**
     * The total balance of the account.
     */
    private final double balance;

    // /**
    //  * Flag indicating whether the account is disabled.
    //  */
    // private final boolean isDisabled;
	// CURRENTLY, WE DO NOT HAVE THE DISABLING OF ACCOUNTS IMPLEMENTED. WE MIGHT DO SO IN THE FUTURE.

    /**
     * Constructs a Account with specified details.
     *
	 * @param acc_id         the unique identifier of the account
	 * @param name           the user-defined name of the account
     * @param balance        the total balance of the account
     */
    public Account(int acc_id, String name, double balance) {
        this.acc_id = acc_id;
		this.name = name;
		this.balance = balance;
    }

	/**
     * Returns the unique identifier of the account.
     *
     * @return the unique identifier of the account
     */
    public int getAccId() {
        return acc_id;
    }

	/**
	 * Returns the account's user-defined name.
	 *
	 * @return the user-defined name of the account
	 */
	public String getName() {
		return name;
	}

    /**
     * Returns the total balance of the account.
     *
     * @return the total balance of the account.
     */
    public double getBalance() {
        return balance;
    }

}
