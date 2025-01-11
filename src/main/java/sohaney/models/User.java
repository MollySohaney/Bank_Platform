package sohaney.models;

/**
 * Represents a user of the banking site.
 */
public class User {

    /**
     * Unique identifier for the user.
     */
    private final int userId;

    /**
     * First name of the user.
     */
    private final String firstName;

    /**
     * Last name of the user.
     */
    private final String lastName;
	
	/**
	 * Total number of accounts owned by the user.
	 */
	 private final int numAccounts;

    /**
     * Constructs a User with specified details under the assumption that the user has zero accounts.
     *
     * @param userId           the unique identifier of the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     */
    public User(int userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
		this.numAccounts = 0;
    }
	
	/**
     * Constructs a User with specified details.
     *
     * @param userId           the unique identifier of the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
	 * @param numAccounts      the number of accounts owned by the user
     */
    public User(int userId, String firstName, String lastName, int numAccounts) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
		this.numAccounts = numAccounts;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

	/**
	 * Returns the number of accounts owned by the user.
	 *
	 * @return the number of accounts owned by the user
	 */
	public int getNumAccounts() {
		return numAccounts;
	}
}
