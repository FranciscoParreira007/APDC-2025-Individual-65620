package pt.unl.fct.di.apdc.firstwebapp.util;

import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.enums.AccountProfile;
import pt.unl.fct.di.apdc.firstwebapp.resources.LoginResource;

import java.util.logging.Logger;

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

	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());


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
			LOG.warning("Error: Username cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(password)) {
			isValid = false;
			LOG.warning("Error: Password cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(email)) {
			isValid = false;
			LOG.warning("Error: Email cannot be empty or blank.");
		}
		if (!email.contains("@")) {
			isValid = false;
			LOG.warning("Error: Email must contain '@'.");
		}
		if (!nonEmptyOrBlankField(name)) {
			isValid = false;
			LOG.warning("Error: Name cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(phoneNum)) {
			isValid = false;
			LOG.warning("Error: Phone number cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(confirmation)) {
			isValid = false;
			LOG.warning("Error: The password Confirmation cannot be empty or blank.");
		}
		if (!nonEmptyOrBlankField(accountProfile)) {
			isValid = false;
			LOG.warning("Error: Account Profile cannot be empty or blank.");
		}
		if(!accountProfile.equals(AccountProfile.PUBLIC.getDescription()) && !accountProfile.equals(AccountProfile.PRIVATE.getDescription())){
			isValid = false;
			LOG.warning("Error: Account Profile must be either " + AccountProfile.PUBLIC + " or " + AccountProfile.PRIVATE + ".");
		}
		if (!isPasswordValid(password)) {
			isValid = false;
			LOG.warning("Error: Password does not meet the requirements to be accepted.");
		}
		if (!password.equals(confirmation)) {
			isValid = false;
			LOG.warning("Error: Password and Confirmation do not match.");
		}

		return isValid;
	}

	private boolean isPasswordValid(String password) {
		String passwordNeedsRegex = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}";
		return password.matches(passwordNeedsRegex);
	}
}
