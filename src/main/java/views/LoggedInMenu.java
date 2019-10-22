package views;

import dao.AccountDao;
import dao.UserDao;
import models.Account;
import models.AccountType;
import models.User;
import utils.InputValidation;
import utils.ScannerUtil;

public class LoggedInMenu implements View {
	private User currentUser;
	private UserDao userDao = new UserDao();
	private AccountDao accountDao = new AccountDao();

	public LoggedInMenu(User currentUser) {
		super();
		this.currentUser = currentUser;
	}

	public void printMenu() {
		System.out.printf("\nHi, %s!\n", currentUser.getUsername());
		System.out.println("Choose from one of the options below:");
		System.out.println(
				"1) Create a checking storage\n" + "2) Create a savings storage\n" + "3) View/Edit your storages\n"
						+ "4) Update your personal info\n" + "5) Delete your user account\n" + "0) Log out");
	}

	public void printUpdateMenu() {
		System.out.println("What info would you like to change?");
		System.out.println("Choose from one of the options below:");
		System.out.println("1) My username\n" +
							"2) My password\n" +
							"3) Both my username and password\n" +
							"0) I've changed my mind, take me back");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(5);

		switch (selection) {
		case 1:
			createAccount(AccountType.CHECKING);
			return this;
		case 2:
			createAccount(AccountType.SAVINGS);
			return this;
		case 3:
			return new AccountMenu(currentUser);
		case 4:
			updateUser();
			return this;
		case 5:
			return deleteUser();
		case 0:
			System.out.println("Logging out...");
			return new MainMenu();
		default:
			return this;
		}
	}

	private void createAccount(AccountType at) {
		// Loop until title input is valid
		while (true) {
			String title = Prompt.promptTitle();
			if (InputValidation.isValidTitle(title)) {
				Account newAcc = new Account(title, at);
				boolean created = accountDao.createAccount(newAcc);
				if (created) {
					System.out.println("\nNew maple storage has been created!\n");
				}
				return;
			}
		}
	}

	private void updateUser() {
		printUpdateMenu();

		int selection = ScannerUtil.getInput(3);

		switch (selection) {
		case 1: {
			while (true) {
				String username = Prompt.promptUsername();
				
				// Check if username is valid
				if (InputValidation.isValidUsername(username)) {
					int update = userDao.updateUser(currentUser, username, currentUser.getPassword());
					if (update > 0) {
						currentUser.setUsername(username);
					}
					break;
				}
			}
			break;
		}
		case 2: {
			while (true) {
				String password = Prompt.promptPassword();
				
				// Check if password is valid
				if (InputValidation.isValidPassword(password)) {
					userDao.updateUser(currentUser, currentUser.getUsername(), password);
					break;
				}
			}
			break;
		}
		case 3: {
			String username = "";
			String password = "";

			// Loop until username input is valid
			while (true) {
				username = Prompt.promptUsername();
				if (InputValidation.isValidUsername(username)) {
					int update = userDao.updateUser(currentUser, username, password);
					if (update > 0) {
						currentUser.setUsername(username);
					}
					break;
				}
			}

			// Loop until password input is valid
			while (true) {
				password = Prompt.promptPassword();
				if (InputValidation.isValidPassword(password)) {
					break;
				}
			}

			userDao.updateUser(currentUser, username, password);
		}
		default:
			break;
		}
	}

	private View deleteUser() {
		String answer = Prompt.promptConfirmDelete();

		switch (answer.toLowerCase().charAt(0)) {
		case 'y':
			userDao.deleteUser(currentUser.getId());
			System.out.printf("User %s has been deleted\n", currentUser.getUsername());
			System.out.println("Returning to main menu");
			return new MainMenu();
		case 'n':
		default:
			return this;
		}
	}
}