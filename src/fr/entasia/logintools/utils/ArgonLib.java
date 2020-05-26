package fr.entasia.logintools.utils;

import com.sun.jna.IntegerType;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface ArgonLib extends Library {

	ArgonLib inst = Native.loadLibrary("argon2", ArgonLib.class);

	int argon2i_hash_encoded(ArgonInt t_cost, ArgonInt m_cost, ArgonInt parallelism, byte[] pwd, ArgonInt pwdlen, byte[] salt, ArgonInt saltlen, ArgonInt hashlen, byte[] encoded, ArgonInt encodedlen);

	int argon2id_hash_encoded(ArgonInt t_cost, ArgonInt m_cost, ArgonInt parallelism, byte[] pwd, ArgonInt pwdlen, byte[] salt, ArgonInt saltlen, ArgonInt hashlen, byte[] encoded, ArgonInt encodedlen);

	int argon2d_hash_encoded(ArgonInt t_cost, ArgonInt m_cost, ArgonInt parallelism, byte[] pwd, ArgonInt pwdlen, byte[] salt, ArgonInt saltlen, ArgonInt hashlen, byte[] encoded, ArgonInt encodedlen);

	int argon2i_hash_raw(ArgonInt t_cost, ArgonInt m_cost, ArgonInt parallelism, byte[] pwd, ArgonInt pwdlen, byte[] salt, ArgonInt saltlen, byte[] hash, ArgonInt hashlen);

	int argon2id_hash_raw(ArgonInt t_cost, ArgonInt m_cost, ArgonInt parallelism, byte[] pwd, ArgonInt pwdlen, byte[] salt, ArgonInt saltlen, byte[] hash, ArgonInt hashlen);

	int argon2d_hash_raw(ArgonInt t_cost, ArgonInt m_cost, ArgonInt parallelism, byte[] pwd, ArgonInt pwdlen, byte[] salt, ArgonInt saltlen, byte[] hash, ArgonInt hashlen);

	int argon2i_verify(byte[] encoded, byte[] pwd, ArgonInt pwdlen);

	int argon2d_verify(byte[] encoded, byte[] pwd, ArgonInt pwdlen);

	int argon2id_verify(byte[] encoded, byte[] pwd, ArgonInt pwdlen);

	ArgonInt argon2_encodedlen(ArgonInt t_cost, ArgonInt m_cost, ArgonInt parallelism, ArgonInt saltlen, ArgonInt hashlen, IntegerType type);

	String argon2_error_message(int error_code);
}

