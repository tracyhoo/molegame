package dev.game.molegame.util;

import java.text.DecimalFormat;

public class Util {

	private static DecimalFormat formatter;

	public static DecimalFormat getDecimalFormatter() {
		if (formatter == null) {
			formatter = new DecimalFormat("##.####");
		}
		return formatter;
	}
}
