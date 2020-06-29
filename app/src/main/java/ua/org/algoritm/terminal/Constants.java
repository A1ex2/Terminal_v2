package ua.org.algoritm.terminal;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.truiton.foregroundservice.action.main";
        public static String PREV_CANCEL = "com.truiton.foregroundservice.action.cancel";
//        public static String PREV_ACTION = "com.truiton.foregroundservice.action.prev";
//        public static String PLAY_ACTION = "com.truiton.foregroundservice.action.play";
//        public static String NEXT_ACTION = "com.truiton.foregroundservice.action.next";
        public static String STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground";
        public static String STARTFOREGROUND_ACTION_ALL = "com.truiton.foregroundservice.action.startforegroundALL";
        public static String STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}