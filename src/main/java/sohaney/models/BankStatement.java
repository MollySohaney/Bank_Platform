package sohaney.models;

/**
 * Represents an account's bank statement in the banking site.
 */
public class BankStatement {

	/**
     * The unique identifier of the statement.
     */
    private final int st_id;
	
	/**
	 * The ID of the associated account.
	 */
	private final int acc_id;
	
	/**
	 * The user-specified name of the associated account.
	 */
	private final String name;

    /**
     * The total change in balance in the requested timeframe.
     */
    private final double amount;
    
    /**
     * The initial date to query to generate the statement.
     */
    private final String start_date;
    
    /**
     * The end date to query to generate the statement.
     */
    private final String end_date;

    /**
     * Constructs a BankStatement with specified details.
     *
     * @param st_id          the unique identifier of the statement
	 * @param acc_id         the unique identifier of the associated account
	 * @param name           the name of the associated account
     * @param amount         the total change in the account balance during this time
     * @param start_date     the initial date to query
     * @param end_date       the final date to query
     */
    public BankStatement(int st_id, int acc_id, String name, double amount, String start_date, String end_date) {
        this.st_id = st_id;
		this.acc_id = acc_id;
		this.name = name;
		this.amount = amount;
		this.start_date = start_date;
		this.end_date = end_date;
    }

    /**
     * Returns the unique identifier of the statement.
     *
     * @return the unique identifier of the statement
     */
    public int getStatementId() {
        return st_id;
    }
	
	/**
     * Returns the unique identifier of the associated account.
     *
     * @return the unique identifier of the associated account
     */
    public int getAccountId() {
        return acc_id;
    }
	
	/**
	 * Returns the user-specified name of the associated account.
	 *
	 * @return the name of the associated account
	 */
	public String getAccountName() {
		return name;
	}

    /**
     * Returns the total change in balance during the requested timeframe.
     *
     * @return the total change in balance
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the initial date requested.
     *
     * @return the initial date requested
     */
    public String getStartDate() {
        return start_date;
    }

    /**
     * Returns the final date requested.
     *
     * @return the final date requested
     */
    public String getEndDate() {
        return end_date;
    }

}
