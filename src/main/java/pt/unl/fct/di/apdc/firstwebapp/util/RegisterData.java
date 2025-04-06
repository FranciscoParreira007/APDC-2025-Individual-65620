package pt.unl.fct.di.apdc.firstwebapp.util;

import pt.unl.fct.di.apdc.firstwebapp.enums.AccountProfile;

public class RegisterData {

	public String username;
	public String password;
	public String confirmation;
	public String email;
	public String name;
	public String phoneNum;
	public String accountProfile;
	public String ccNumber;
	public String role;
	public String nif;
	public String workPlace;
	public String workFunction;
	public String address;
	public String nifWorkPlace;
	public String accountStatus;


	public RegisterData(){}

	public RegisterData(String email, String username, String name, String phoneNum, String password, String confirmation, String accountProfile,
	String ccNumber, String role, String nif, String workPlace, String workFunction, String address, String nifWorkPlace, String accountStatus) {
		this.username = username;
		this.password = password;
		this.confirmation = confirmation;
		this.email = email;
		this.name = name;
		this.phoneNum = phoneNum;
		this.accountProfile = accountProfile;
		this.ccNumber = ccNumber;
		this.role = role;
		this.nif = nif;
		this.workPlace = workPlace;
		this.workFunction = workFunction;
		this.address = address;
		this.nifWorkPlace = nifWorkPlace;
		this.accountStatus = accountStatus;
	}

	public RegisterData(String username, String password, String confirmation, String email, String name) {
		this.username = username;
		this.password = password;
		this.confirmation = confirmation;
		this.email = email;
		this.name = name;
	}

	private boolean nonEmptyOrBlankField(String field) {
		return field != null && !field.isBlank();
	}

	public boolean validRegistration() {

		boolean isValid = true;

		if (!nonEmptyOrBlankField(username)) {
			isValid = false;
			System.out.println("Error: Username cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(password)) {
			isValid = false;
			System.out.println("Error: Password cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(email)) {
			isValid = false;
			System.out.println("Error: Email cannot be empty or blank.");
		}
		if (!email.contains("@")) {
			isValid = false;
			System.out.println("Error: Email must contain '@'.");
		}
		if (!nonEmptyOrBlankField(name)) {
			isValid = false;
			System.out.println("Error: Name cannot be empty or empty.");
		}
		if (!nonEmptyOrBlankField(phoneNum)) {
			isValid = false;
			System.out.println("Error: Phone number cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(confirmation)) {
			isValid = false;
			System.out.println("Error: The password Confirmation cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(accountProfile)) {
			isValid = false;
			System.out.println("Error: Account Profile cannot be empty or blank.");
		}
		if(!AccountProfile.PUBLIC.name().equals(accountProfile) && !AccountProfile.PRIVATE.name().equals(accountProfile)){
			isValid = false;
			System.out.println("Error: Account Profile must be either " + AccountProfile.PUBLIC + " or " + AccountProfile.PRIVATE + ".");
		}
		if (!isPasswordValid(password)) {
			isValid = false;
			System.out.println("Error: Password does not meet the requirements to be accepted.");
		}
		if (!password.equals(confirmation)) {
			isValid = false;
			System.out.println("Error: Password and Confirmation do not match.");
		}

		return isValid;
	}

	private boolean isPasswordValid(String password) {
		String passwordNeedsRegex = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}";
		return password.matches(passwordNeedsRegex);
	}
}
