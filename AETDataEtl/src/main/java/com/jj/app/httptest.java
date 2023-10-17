package com.jj.app;

import com.jj.utils.HttpUtils;

public class httptest {
    public static void main(String[] args) {
        String data ="{\"debug\":1,\"appid\":\"debug-appid\",\"data\":{\"#account_id\":\"509642829\",\"#time\":\"2023-10-09 13:27:59.640\",\"#uuid\":\"566f9fdf-5bdf-1120-2914-3470b36753af\",\"#event_name\":\"AET6\",\"#type\":\"track\",\"properties\":{\"PageID\":\"100001\",\"user_etime_z\":1696832340704,\"#data_source\":\"Restful_API\",\"user_stime_z\":1696829279640,\"RealMac\":\"14:D8:D3:79:BE:DA\",\"CurViewName\":\"\",\"AppID\":0,\"appname\":\"jjmatch\",\"rcvtime\":\"1696832399\",\"session_etime_z\":1696832340704,\"user_stime\":\"2023-10-09 13:27:59\",\"GameID\":1,\"DeviceModel\":\"iphone\",\"session_stime_z\":1696829255962,\"session_stime\":\"2023-10-09 13:27:35\",\"StartTime\":1696829259385,\"iscompress\":\"0\",\"MID\":\"jjmiosiphonec666c50c26a8791f6077dded141f1dd3239e5a7d\",\"SDKVer\":6057,\"AppVer\":51604,\"ServerTime\":1696829259000,\"ChannelID\":60000,\"version\":\"3\",\"Mac\":\"14:D8:D3:79:BE:DA\",\"Nettype\":\"\",\"user_etime\":\"2023-10-09 14:19:00\",\"RefPageID\":\"start\",\"page_stime\":\"2023-10-09 14:19:42\",\"SysVer\":\"12.4\",\"behavior_xtr_opm_cnt\":\"\",\"SessionID\":\"6f16d62dacdd8fe16e72999881874a45\",\"UserType\":2,\"actionName\":\"\",\"Operator\":\"CMCC\",\"PkgVer\":6057,\"Processor\":\"ARM64\",\"page_etime_z\":1696832383985,\"behavior_xtr_isfree\":\"\",\"behavior_xtr_levelid\":\"\",\"behavior_xtr_enough\":\"false\",\"IMSI\":\"nil\",\"ScreenSize\":\"1125x2436\",\"page_etime\":\"2023-10-09 14:19:43\",\"page_stime_z\":1696832382525,\"PkgID\":1,\"ICCID\":\"nil\",\"WidgetResult\":\"\",\"SiteID\":\"\",\"RLTime\":3423,\"OS\":\"ios\",\"session_etime\":\"2023-10-09 14:19:00\",\"IMEI\":\"9bd4ceb42efb212c2144184312560e\",\"ostype\":\"ios\",\"TrackVer\":3,\"WidgetName\":\"\",\"PhoneSize\":\"\",\"behavior_xtr_paymoney\":\"\",\"UserId\":509642829,\"CPromoter\":60000,\"md5\":\"cec9e5d797ff8628e8710c95443a0004\"}}}";
        String url = "http://10.11.199.162:80/sync_json";
        HttpUtils.sendPostJsonBody(url,data);
    }
}
