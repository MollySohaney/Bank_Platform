package sohaney.models;

/**
 * Represents a transaction in the banking site.
 */
public class DoubleTransaction {

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
	private final int init_acc;
	
	/**
	 * The ID of the account that initiated the transaction.
	 */
	private final int dest_acc;
	
	/**
	 * The ID of the user that initiated the transaction.
	 */
	private final int user_id;
	
	/**
	 * The type of transaction that took place (used for listing transactions).
	 */
	private final String type;

    /**
     * Constructs a Transaction with specified details.
     *
     * @param tr_id          the unique identifier of the transaction
     * @param amount         the amount transferred
     * @param date           the date of the transaction
     * @param time           the time of the transaction
     * @param acc_id1        the ID of the account that initiated the transaction
     * @param acc_id1        the ID of the receiver account
	 * @param user_id        the ID of the user that initiated the transaction
	 * @param type           the type of transaction that took place
     */
    public DoubleTransaction(int tr_id, double amount, String date, String time, int acc_id1, int acc_id2, int user_id, String type) {
        this.tr_id = tr_id;
		this.amount = amount;
		this.date = date;
		this.time = time;
		this.init_acc = acc_id1;
		this.dest_acc = acc_id2;
		this.user_id = user_id;
		this.type = type;
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
	public int getSender() {
		return init_acc;
	}
	
	/**
     * Returns the ID of the account that received the transfer.
     *
     * @return the ID of the account that received the transfer
     */
	public int getReceiver() {
		return dest_acc;
	}
	
	/**
     * Returns the ID of the user that initiated the transaction.
     *
     * @return the ID of the user that initiated the transaction
     */
	public int getUser() {
		return user_id;
	}

	public String getType() {
		return type;
	}

}
