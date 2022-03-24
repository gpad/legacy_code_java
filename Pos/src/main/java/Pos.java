//#include "stdafx.h"
//#include "Pos.h"

//Logger * Log::_logger = nullptr;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


//		#pragma once
//
//		#include <sstream>
//#include <vector>
//#include <Map>
//#include "tables.h"

class Db
{
	static void execute_sql(String sql) {
		// show message box
		throw new RuntimeException("not implemented yet!");
	}
};

class User
{
	private List<String> _roles = new ArrayList<>();
	private int _id;

	User(int id, String name, List<String> roles)
		{
_roles = roles;
}
//	User(const User&) = default;
//	User(User&&) = default;
//	User &operator=(const User &) = default;
//	User &operator=(User &&) = default;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (_id != user._id) return false;
		return _roles.equals(user._roles);
	}

	@Override
	public int hashCode() {
		int result = _roles.hashCode();
		result = 31 * result + _id;
		return result;
	}

	int id() { return _id; }

	List<String> roles() { return _roles; }
};

class Security {
	static User findUser(String name) {
		throw new RuntimeException("Not implementd");
	}
};

enum  PaymentType { cash, CC, debitCard };

class PaymentService {

	List<PaymentType> get_availabel_payment_types() {
		throw new RuntimeException("Read From DB Avilabel payment Types");
	}

	void pay(PaymentType paymentType) {
		throw new RuntimeException("Try to meake a real payment!!!");
	}
};

interface Logger
{
	void write(String s);
};

class Log {
	private static Logger _logger;
	static void Init(Logger logger) { _logger = logger; }
	static void debug(String str) {
		if (_logger == null) {
			throw new RuntimeException("Logger is not initialized!");
		}
		_logger.write(String.format("[DEBUG] %s", str));
	}
};

class CartItem{
		String descr;
		int price;
		int qty;

	public CartItem(String descr, int price, int qty) {

		this.descr = descr;
		this.price = price;
		this.qty = qty;
	}
};

class Tab
{
	private List<CartItem> _items = new ArrayList<>();
	private int _id;
	private User _user;

	Tab( User user, int id)
	{
		_user = user;
	_id = id;
		if (user.id() == -1) {
			throw new RuntimeException("User should be valid");
		}
	}

	void addItem(String descr, int single_price, int qty) {
		_items.add(new CartItem(descr, single_price, qty));
		var sql = String.format("INSERT INTO CART_ITEMS (tab_id, user_id, descr, value, qty) VALUES (%d, %d, %s, %d, %d)", _id,_user.id(),descr,single_price,qty);
		Db.execute_sql(sql);
	}

	List<CartItem> getItems() {	return _items; }
};

class Pos
{
	Map<Integer, Tab> _tabs;
	Tab _selectedTab = null;
	int _selectedTabId = -1;
	User _currentUser = new User(-1, "", new ArrayList<>());
	PaymentService _payment_service;


	private void ShowError(String str) {
		// show message box
		throw new RuntimeException("Show the dialog box with error " + str);
	}

	private boolean AskConfirmation(String msg) {
		// show message box
		throw new RuntimeException("Block and ask to user to: " + msg);
	}

	private void showMessage(String message) {
		// show message box
		throw new RuntimeException("Show Dialog Box to user for " + message);
	}

	private PaymentType AskUserPayment(List<PaymentType> paymentTypes)
	{
		throw new RuntimeException("Show dialog to select Payment Type");
	}

Pos(PaymentService payment_service)
{
	_payment_service = payment_service;
}



	void AddTab(int id) {
		Log.debug(String.format("Try to add tab %d", id));
		if (Tables.getInstance().isValid(id)) {
			ShowError("Table is not VALID");
		}

		AtomicBoolean alreadyPresent = new AtomicBoolean(false);
//		for (var pair : _tabs) {
//			if (pair.first == id) {
//				if (AskConfirmation("Tab already present do you want to select it?")) {
//					alreadyPresent = true;
//					_selectedTab = &pair.second;
//					_selectedTabId = pair.first;
//				}
//			}
//		}
		_tabs.forEach((k, v) -> {
			if (k == id){
				if (AskConfirmation("Tab already present do you want to select it?")) {
					alreadyPresent.set(true);
					_selectedTab = v;
					_selectedTabId = k;
				}

			}
		});

		Tables.getInstance().assignTable(id);

		if (!alreadyPresent.get()){
			Log.debug("Insert new TAB!!!");
			_tabs.put(id, new Tab(_currentUser, id));
			Db.execute_sql(String.format("INSERT TABS (ID) VALUES(%d)", id));
		}
	}

	void selectTab(int id) {
		_selectedTabId = id;
		_selectedTab = _tabs.get(id);
	}

	void pay(){
		Log.debug("Start payment ...");
		int tot = 0;
		for (var i : _selectedTab.getItems()) {
			tot += i.price;
		}

		var payment_types = this._payment_service.get_availabel_payment_types();
		var selectedpaymetType = this.AskUserPayment(payment_types);
		this._payment_service.pay(selectedpaymetType);
		Log.debug("Payment completed for table " + String.valueOf(this._selectedTabId));
	}

	void show_subtotal(){
		int tot = 0;
		for (var i : _selectedTab.getItems()) {
			tot += i.price;
		}
		this.showMessage(String.format("subtotal is %d", tot));
	}

	void ChangeUser(String username){
		try {
			_currentUser = Security.findUser(username);
		}
		catch (RuntimeException e) {
			this.ShowError("Unable to find new user");
		}
	}

	void addItem(String descr, int single_price, int qty) {
		boolean isEnabled = false;
		for (var role : _currentUser.roles()) {
			if (role == "waiter") {
				isEnabled = true;
			}
		}
		if (!isEnabled) return;

		this._selectedTab.addItem(descr, single_price, qty);
	}
};

