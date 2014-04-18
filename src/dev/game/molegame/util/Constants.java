package dev.game.molegame.util;

public interface Constants {
    /**
     * server url
     */
	public static String APP_SERVER_URL = "http://192.168.100.102:8888/ranking";

	public static final String PREFERENCE_MOLEGAME_BASE_INFO = "MOLEGAME_PREFERENCES";
	public static final String LEVEL = "ACCOMPLISHED_LEVEL";
	public static final String CURRENT_LEVEL = "CURRENT_PLAYING_LEVEL";
    /**
     * scores needed to qualify to next level
     */
	public static final int[] QUALIFICATION = {20,30,40,50,60,70,80,90,100};
    /**
     * total number of level
     */
	public static final int LEVELS = 9;
	public static final String PREFERENCE_KEY_SOUNDS = "dev.game.molegame.sounds";
	public static final String PREFERENCE_KEY_VIBRATE = "dev.game.molegame.vibrate";
	public static final String PREFERENCE_KEY_SHOWTIPS = "dev.game.molegame.showtips";

/*	public static final String PREFERENCE_MIXEDCOLOR_RANKING_INFO = "MIXED_COLOR_RANKING_INFOS";
	public static final String PREFERENCE_KEY_RANKING_UID = "org.void1898.mixedcolor.ranking.uid";
	public static final String PREFERENCE_KEY_RANKING_NAME = "org.void1898.mixedcolor.ranking.name";
	public static final String PREFERENCE_KEY_RANKING_RECORD = "org.void1898.mixedcolor.ranking.record";
	public static final String PREFERENCE_KEY_RANKING_DATE = "org.void1898.mixedcolor.ranking.date";
	public static final String PREFERENCE_KEY_RANKING_FLAG = "org.void1898.mixedcolor.ranking.flag";*/
}
