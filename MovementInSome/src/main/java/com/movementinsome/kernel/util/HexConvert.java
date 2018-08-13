package com.movementinsome.kernel.util;

public class HexConvert {

	// 十六进制转二进制
	public static String HToB(String a) {
		String b = Integer.toBinaryString(Integer.valueOf(toD(a, 16)));
		return b;
	}

	// 二进制转十六进制
	public static String BToH(String a) {
		// 将二进制转为十进制再从十进制转为十六进制
		String b = Integer.toHexString(Integer.valueOf(toD(a, 2)));
		return b;
	}

	// 任意进制数转为十进制数
	public static String toD(String a, int b) {
		int r = 0;
		for (int i = 0; i < a.length(); i++) {
			r = (int) (r + formatting(a.substring(i, i + 1))
					* Math.pow(b, a.length() - i - 1));
		}
		return String.valueOf(r);
	}

	// 将十六进制中的字母转为对应的数字
	public static int formatting(String a) {
		int i = 0;
		for (int u = 0; u < 10; u++) {
			if (a.equals(String.valueOf(u))) {
				i = u;
			}
		}
		if (a.equals("a")) {
			i = 10;
		}
		if (a.equals("b")) {
			i = 11;
		}
		if (a.equals("c")) {
			i = 12;
		}
		if (a.equals("d")) {
			i = 13;
		}
		if (a.equals("e")) {
			i = 14;
		}
		if (a.equals("f")) {
			i = 15;
		}
		return i;
	}

	// 将十进制中的数字转为十六进制对应的字母
	public static String formattingH(int a) {
		String i = String.valueOf(a);
		switch (a) {
		case 10:
			i = "a";
			break;
		case 11:
			i = "b";
			break;
		case 12:
			i = "c";
			break;
		case 13:
			i = "d";
			break;
		case 14:
			i = "e";
			break;
		case 15:
			i = "f";
			break;
		}

		return i;
	}
}
