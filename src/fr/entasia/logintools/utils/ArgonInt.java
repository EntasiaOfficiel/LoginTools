package fr.entasia.logintools.utils;

import com.sun.jna.IntegerType;

public class ArgonInt extends IntegerType {

	public ArgonInt() {
		this(0);
	}

	public ArgonInt(int value){
		super(8, value);
	}
}
