package sohaney.models;

/**
 * Represents a bank account in the banking site.
 */
public class ExtAccount {

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

	/**
	 * The name of the account's owner.
	 */
	private final String owner_name;

    /**
     * Constructs a Account with specified details.
     *
	 * @param acc_id         the unique identifier of the account
	 * @param name           the user-defined name of the account
     * @param balance        the total balance of the account
	 * @param owner_name     the name of the owner of the account
     */
    public ExtAccount(int acc_id, String name, double balance, String owner_name) {
        this.acc_id = acc_id;
		this.name = name;
		this.balance = balance;
		this.owner_name = owner_name;
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

	/**
	 * Returns the name of the account's owner.
	 *
	 * @return the name of the account's owner
	 */
	public String getOwnerName() {
		return owner_name;
	}

}
