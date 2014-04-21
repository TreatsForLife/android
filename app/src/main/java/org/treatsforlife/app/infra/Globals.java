package org.treatsforlife.app.infra;

public interface Globals {
    //User
    public static final String USER_PREFS_NAME = "org.treatsforlife.app.UserPrefs";
    public static final String ID_KEY = "tfl_user_id";
    public static final String FULLNAME_KEY = "tfl_user_full_name";

    //API
    public static final String API_BASE_URL = "http://tfl.bchmn.com/";
    public static final String API_PET_URL = API_BASE_URL + "pet/";

    //Extras
    public static final String EXTRA_PET_ID = "TFL_PET_ID";
}
