package fr.entasia.logintools.utils;

import com.sun.jna.NativeLong;

import java.security.SecureRandom;
import java.util.Base64;

public class Crypto {

	public final static SecureRandom rd = new SecureRandom();


	protected final static ArgonInt ITERATION = new ArgonInt(6);
	protected final static ArgonInt MEMORY = new ArgonInt(40164);
	protected final static ArgonInt PARALLELISM = new ArgonInt(25);

	protected final static ArgonInt SAlT_LEN = new ArgonInt(19);
	protected final static ArgonInt HASH_LEN = new ArgonInt(35);
	protected final static ArgonInt FINAL_LEN = ArgonLib.inst.argon2_encodedlen(ITERATION, MEMORY, PARALLELISM,
			SAlT_LEN, HASH_LEN, new NativeLong(2));

	public static byte[] genSalt() {
		byte[] b = new byte[SAlT_LEN.intValue()];
		rd.nextBytes(b);
		return b;
	}

	public static String hashPassword(String pass) throws SecurityException {

		byte[] hash = new byte[FINAL_LEN.intValue()];
		byte[] salt = genSalt();

		int truc = ArgonLib.inst.argon2id_hash_encoded(
				ITERATION, MEMORY, PARALLELISM,
				pass.getBytes(), new ArgonInt(pass.length()), salt, SAlT_LEN,
				HASH_LEN, hash, FINAL_LEN);

		if(truc!=0)throw new SecurityException("unsuccessful password hash");
		else return new String(hash);
	}

	public static boolean comparePassword(String hash, String pass) {
		return ArgonLib.inst.argon2id_verify((hash+(char)0).getBytes(), pass.getBytes(), new ArgonInt(pass.length()))==0;
	}


	public static String getfromBDD(String base){

		String[] a = base.split("\\$");
		StringBuilder sb = new StringBuilder("$argon2id$v=");
		sb.append(Byte.parseByte(a[1].substring(0, 2))).append("$m=").append(Crypto.MEMORY).append(",t=").append(Crypto.ITERATION).append(",p=").append(Crypto.PARALLELISM).append("$");
		sb.append(a[1].substring(2)).append("$").append(a[0]);
		return sb.toString();
	}


	public static String genBDD(String hash) {
		String[] a = hash.split("\\$");
		return (a[5].substring(0, a[5].length() - 1)) + "$" + Byte.parseByte(a[2].substring(2)) + a[4];

	}

	public static void main(String[] fz){

		String hashed = Crypto.hashPassword("bonjour");
		System.out.println(hashed);
		String n = genBDD(hashed);
		System.out.println(n);
//		String[] parts = hashed.split("\\$");
//		System.out.println(parts.length);
//
//		byte[] pass = parts[5].getBytes();
//		byte[] salt = parts[4].getBytes();
//		byte version = Byte.parseByte(parts[2].substring(2));
//
//		byte[] fina = new byte[35+19+1];
//
//		int f = 0;
//		int t = 0;
//		for(int i=16;i<35;i++){ // fin du mdp
//			fina[f] = pass[i];
//			t++;
//			f++;
//		}
//
//
//		t = 0;
//		for(int i=0;i<4;i++){ // debut du salt
//			fina[f] = pass[i];
//			t++;
//			f++;
//		}
//
//
//		t = 0;
//		for(int i=4;i<19;i++){ // fin du salt
//			fina[f] = pass[i];
//			t++;
//			f++;
//		}
//
//		fina[f] = version;
//		f++;
//
//
//		t = 0;
//		for(int i=0;i<16;i++){ // début du mdp
//			fina[f] = pass[i];
//			t++;
//			f++;
//		}
//		String newhash = Base64.getEncoder().encodeToString(fina);
//		System.out.println(newhash);
	}
}
