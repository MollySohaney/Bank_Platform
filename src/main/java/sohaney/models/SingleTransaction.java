package sohaney.models;

/**
 * Represents a deposit or withdrawal in the banking site.
 */
public class SingleTransaction {

	/**
     * The unique identifier of the transaction.
     */
    private final int tr_id;

    /**
     * The amount transferred.
     */
    private final double amount;
    
    /**
     * The date on which the transaction took place.
     */
    private final String date;
    
    /**
     * The time at which the transaction took place.
     */
    private final String time;
	
	/**
	 * The ID of the account that initiated the transaction.
	 */
	private final int acc_id;
	
	/**
	 * The ID of the user that initiated the transaction.
	 */
	private final int user_id;
	
	/**
	 * A boolean representing whether this was a deposit or a withdrawal.
	 */
	private final boolean isDeposit;

    /**
     * Constructs a Transaction with specified details.
     *
     * @param tr_id          the unique identifier of the transaction
     * @param amount         the amount transferred
     * @param date           the date of the transaction
     * @param time           the time of the transaction
     * @param acc_id         the ID of the account that initiated the transaction
	 * @param user_id        the ID of the user that initiated the transaction
	 * @param isDeposit      a boolean representing whether this was a deposit or a withdrawal
     */
    public SingleTransaction(int tr_id, double amount, String date, String time, int acc_id, int user_id, boolean isDeposit) {
        this.tr_id = tr_id;
		this.amount = amount;
		this.date = date;
		this.time = time;
		this.acc_id = acc_id;
		this.user_id = user_id;
		this.isDeposit = isDeposit;
    }

    /**
     * Returns the unique identifier of the transaction.
     *
     * @return the unique identifier of the transaction
     */
    public int getTransactionId() {
        return tr_id;
    }

    /**
     * Returns the amount transferred.
     *
     * @return the amount transferred
     */
    public double getAmountTransferred() {
        return amount;
    }

    /**
     * Returns the date of the transaction.
     *
     * @return the date of the transaction
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the time of the transaction.
     *
     * @return the time of the transaction
     */
    public String getTime() {
        return time;
    }
	
	/**
     * Returns the date and time of the transaction.
     *
     * @return the date and time of the transaction
     */
	public String getDateAndTime() {
		return date + " at " + time;
	}
	
	/**
     * Returns the ID of the account that initiated the transaction.
     *
     * @return the ID of the account that initiated the transaction
     */
	public int getAccount() {
		return acc_id;
	}
	
	/**
     * Returns the ID of the user that initiated the transaction.
     *
     * @return the ID of the user that initiated the transaction
     */
	public int getUser() {
		return user_id;
	}
	
	/**
	 * Returns true if this was a deposit, false if it was a withdrawal.
	 *
	 * @return true if this was a deposit, false if it was a withdrawal
	 */
	public boolean isDeposit() {
		return isDeposit;
	}

}
